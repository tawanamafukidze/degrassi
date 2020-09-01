import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class AdminSearch extends GUI {
    private JTable tblProducts;
    private final Employee employee;
    private JTable Customers;
    private final Connection con;

    PreparedStatement pst;
    DefaultTableModel tbl;
    DefaultTableModel uTbl;
    DefaultListModel<String> searchList;
    private final JTabbedPane tabbedPane;

    public AdminSearch(Employee employee) {
        super();
        this.employee = employee;
        this.con = employee.getDBConnection();
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        loadWidgets();
        setVisible(true);
    }

    @Override
    protected void loadWidgets() {
        searchList = new DefaultListModel<>();
        BindData();
        stockPanelWidgets();
        customerPanelWidgets();
        queryUsers();
        queryGames();
    }

    private void stockPanelWidgets() {
        JButton cancelbtn = new JButton("cancel");
        JButton editbtn = new JButton("edit");
        JTextPane txtStock = new JTextPane();
        JTextPane txtPrice = new JTextPane();
        JLabel TypeLabel = new JLabel();
        JTextPane txtType = new JTextPane();
        JButton deletebtn = new JButton("delete");
        JLabel stockPanelHeading = new JLabel();
        JLabel StockLabel = new JLabel();
        JLabel PriceLabel = new JLabel();
        JButton addbtn = new JButton("add");
        JLabel TitleLabel = new JLabel();
        JPanel stockPanel = new JPanel();
        JLabel StockSeachLabel = new JLabel("New label");
        JScrollPane scrollPane = new JScrollPane();
        JFormattedTextField txtSearchStock = new JFormattedTextField();
        JTextPane txtTitle = new JTextPane();
        JLabel BackgroundUpdate = new JLabel();
        stockPanel.setLayout(null);


        tabbedPane.addTab("", new ImageIcon("img\\Stockkk.png"), stockPanel, null);
        tabbedPane.setBackgroundAt(0, Color.BLACK);

        txtSearchStock.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                DefaultTableModel filteredTable = (DefaultTableModel) tblProducts.getModel();
                String search = "(?i)^" + txtSearchStock.getText();
                TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(filteredTable);
                tblProducts.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter(search));
            }
        });

        txtTitle.setBounds(101, 132, 86, 23);
        stockPanel.add(txtTitle);

        txtPrice.setBounds(101, 297, 86, 14);
        stockPanel.add(txtPrice);

        txtStock.setBounds(101, 245, 86, 14);
        stockPanel.add(txtStock);

        txtType.setBounds(101, 184, 86, 23);
        stockPanel.add(txtType);

        addbtn.addActionListener(e -> {
            try {
                String messageDialog = "";
                String title = txtTitle.getText();
                String type = txtType.getText();
                int stock = -1;
                double price = -1;

                if (title.length() < 1) {
                    messageDialog = "Title Cannot Be Empty";
                } else if (type.length() < 1) {
                    messageDialog = "Type Cannot Be Empty";
                }

                if (messageDialog.length() == 0) {
                    try {
                        stock = Integer.parseInt(txtStock.getText());
                        if (stock < 1) {
                            messageDialog = "Stock value must be at least 1";
                        }
                    } catch (NumberFormatException ignored) {
                        messageDialog = "Invalid Stock Value Provided";
                    }
                }

                if (messageDialog.length() == 0) {
                    try {
                        price = Double.parseDouble(txtPrice.getText());
                        if (price < 0.0) {
                            messageDialog = "Price Cannot Be Less Than 0.0";
                        }
                    } catch (Exception ignored) {
                        messageDialog = "Invalid Price Value Provided";
                    }
                }

                if (messageDialog.length() > 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            messageDialog,
                            "Inventory Status",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                employee.addInventory(title, type, stock, price);
                JOptionPane.showMessageDialog(this, "New Game Added");
                queryGames();
                txtTitle.setText("");
                txtType.setText("");
                txtStock.setText("");
                txtPrice.setText("");
                txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Internal Error: Could Not Add Game To Inventory.",
                        "Inventory Status",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        addbtn.setBounds(21, 359, 67, 23);
        stockPanel.add(addbtn);


        txtSearchStock.setBounds(302, 105, 270, 23);
        stockPanel.add(txtSearchStock);

        StockSeachLabel.setIcon(new ImageIcon("img\\search.png"));
        StockSeachLabel.setBounds(208, 104, 77, 23);
        stockPanel.add(StockSeachLabel);

        scrollPane.setBounds(208, 138, 458, 173);
        stockPanel.add(scrollPane);

        tblProducts = new JTable();
        tblProducts.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                tbl = (DefaultTableModel) tblProducts.getModel();
                int selected = tblProducts.getSelectedRow();

                txtTitle.setText(tbl.getValueAt(selected, 1).toString());
                txtType.setText(tbl.getValueAt(selected, 2).toString());
                txtStock.setText(tbl.getValueAt(selected, 3).toString());
                txtPrice.setText(tbl.getValueAt(selected, 4).toString());
                addbtn.setEnabled(false);
            }
        });

        tblProducts.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Title", "Type", "Stock", "Price"}
        ) {
            final boolean[] columnEditables = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProducts.getColumnModel().getColumn(0).setPreferredWidth(29);
        tblProducts.getTableHeader().setReorderingAllowed(false);

        PriceLabel.setIcon(new ImageIcon("img\\price.png"));
        PriceLabel.setBounds(21, 297, 46, 14);
        stockPanel.add(PriceLabel);

        StockLabel.setIcon(new ImageIcon("img\\stock.png"));
        StockLabel.setBounds(21, 245, 56, 14);
        stockPanel.add(StockLabel);

        TypeLabel.setIcon(new ImageIcon("img\\type.png"));
        TypeLabel.setBounds(21, 193, 46, 14);
        stockPanel.add(TypeLabel);

        TitleLabel.setIcon(new ImageIcon("img\\title.png"));
        TitleLabel.setBounds(21, 141, 46, 14);
        stockPanel.add(TitleLabel);

        scrollPane.setViewportView(tblProducts);
        stockPanelHeading.setIcon(new ImageIcon("img\\heading3.png"));
        stockPanelHeading.setBounds(170, 11, 371, 54);
        stockPanel.add(stockPanelHeading);

        editbtn.addActionListener(e -> {
            tbl = (DefaultTableModel) tblProducts.getModel();
            int selected = tblProducts.getSelectedRow();

            int id = Integer.parseInt((tbl.getValueAt(selected, 0).toString()));
            String Title = txtTitle.getText();
            String Type = txtType.getText();
            int Stock = Integer.parseInt(txtStock.getText());
            double Price = Double.parseDouble(txtPrice.getText());

            try {
                pst = con.prepareStatement(
                        "UPDATE games set Title = ?, Type = ?, Stock = ?, Price = ? where GameID = ?"
                );

                pst.setString(1, Title);
                pst.setString(2, Type);
                pst.setInt(3, Stock);
                pst.setDouble(4, Price);
                pst.setInt(5, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Game Info Updated");

                txtTitle.setText("");
                txtType.setText("");
                txtStock.setText("");
                txtPrice.setText("");
                txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
                queryGames();
                addbtn.setEnabled(true);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        deletebtn.addActionListener(e -> {

            tbl = (DefaultTableModel) tblProducts.getModel();
            int selected = tblProducts.getSelectedRow();

            int id = Integer.parseInt((tbl.getValueAt(selected, 0).toString()));


            try {
                pst = con.prepareStatement("delete from games where GameID = ?");


                pst.setInt(1, id);
                pst.executeUpdate();


                JOptionPane.showMessageDialog(this, "Game Deleted");

                txtTitle.setText("");
                txtType.setText("");
                txtStock.setText("");
                txtPrice.setText("");
                txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
                queryGames();
                addbtn.setEnabled(true);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }


        });
        deletebtn.setBounds(196, 359, 89, 23);
        stockPanel.add(deletebtn);

        editbtn.setBounds(101, 359, 77, 23);
        stockPanel.add(editbtn);

        cancelbtn.addActionListener(e -> {
            txtTitle.setText("");
            txtType.setText("");
            txtStock.setText("");
            txtPrice.setText("");
            txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
            addbtn.setEnabled(true);
        });
        cancelbtn.setBounds(302, 359, 89, 23);
        stockPanel.add(cancelbtn);

        BackgroundUpdate.setBounds(-290, 5, 1023, 543);
        BackgroundUpdate.setIcon(new ImageIcon("img\\ps4 wall.png"));
        stockPanel.add(BackgroundUpdate);
    }

    public void queryUsers() {
        if (!employee.Active()) return;
        try {
            pst = con.prepareStatement("select * from customers");
            Customer customer;
            ResultSet rs = pst.executeQuery();
            uTbl = (DefaultTableModel) Customers.getModel();
            uTbl.setRowCount(0);

            while (rs.next()) {
                Vector<Object> customerVector = new Vector<>();
                String customer_id = rs.getString("customerID");
                customer = new Customer(con);
                customer.queryCustomer(employee, customer_id);
                try {
                    customerVector.add(customer.getId());
                    customerVector.add(customer.getFirstName());
                    customerVector.add(customer.getLastName());
                    customerVector.add(customer.getEmail());
                    customerVector.add(customer.getPhone());

                    String address = customer.getCustomerAddress().getStreet() + " " +
                            customer.getCustomerAddress().getCity() + " " + customer.getCustomerAddress().getState() + " " +
                            customer.getCustomerAddress().getPostCode();
                    customerVector.add(address);
                } catch (NullPointerException ignored) {
                }
                uTbl.addRow(customerVector);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminSearch.class.getName()).isLoggable(Level.SEVERE);
        }
    }

    public void queryGames() {
        if (!employee.Active()) return;
        try {
            pst = con.prepareStatement("select * from games");
            ResultSet rs = pst.executeQuery();
            tbl = (DefaultTableModel) tblProducts.getModel();
            tbl.setRowCount(0);

            while (rs.next()) {
                Vector<Object> gameVector = new Vector<>();
                gameVector.add(rs.getInt("GameID"));
                gameVector.add(rs.getString("Title"));
                gameVector.add(rs.getString("Type"));
                gameVector.add(rs.getInt("Stock"));
                gameVector.add(rs.getDouble("Price"));
                tbl.addRow(gameVector.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminSearch.class.getName()).isLoggable(Level.SEVERE);
        }
    }

    private void BindData() {
        queryGameTitles().forEach((game) -> searchList.addElement(game));
    }

    private void customerPanelWidgets() {
        JPanel CustomerInfo = new JPanel();
        tabbedPane.addTab("", new ImageIcon("img\\Customers.png"), CustomerInfo, null);
        tabbedPane.setBackgroundAt(1, Color.BLACK);
        CustomerInfo.setLayout(null);
        JScrollPane scrollPane_3 = new JScrollPane();
        scrollPane_3.setBounds(28, 142, 624, 289);
        CustomerInfo.add(scrollPane_3);
        Customers = new JTable();
        Customers.setModel(new DefaultTableModel(
                                   new Object[][]{
                                           {null, null, null, "", null, null},
                                   },
                                   new String[]{
                                           "ID", "FirstName", "Surname", "EmailAddress", "Phone Number", "Address"
                                   }
                           ) {
                               @Override
                               public boolean isCellEditable(int row, int column) {
                                   return false;
                               }
                           }
        );
        Customers.getColumnModel().getColumn(0).setPreferredWidth(27);
        Customers.getColumnModel().getColumn(1).setPreferredWidth(97);
        Customers.getColumnModel().getColumn(2).setPreferredWidth(97);
        Customers.getColumnModel().getColumn(3).setPreferredWidth(100);
        Customers.getColumnModel().getColumn(4).setPreferredWidth(91);
        Customers.getColumnModel().getColumn(5).setPreferredWidth(210);
        Customers.getTableHeader().setReorderingAllowed(false);
        scrollPane_3.setViewportView(Customers);

        JFormattedTextField txtSearchCustomers = new JFormattedTextField();
        txtSearchCustomers.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                DefaultTableModel filteredTable = (DefaultTableModel) Customers.getModel();
                String search = "(?i)^" + txtSearchCustomers.getText();
                TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(filteredTable);
                Customers.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter(search));
            }
        });
        txtSearchCustomers.setBounds(116, 111, 536, 20);
        CustomerInfo.add(txtSearchCustomers);

        JLabel lblNewLabel_1 = new JLabel();
        lblNewLabel_1.setIcon(new ImageIcon("img\\search.png"));
        lblNewLabel_1.setBounds(28, 116, 91, 14);
        CustomerInfo.add(lblNewLabel_1);

        JLabel degrassiSearchCustomers = new JLabel();
        degrassiSearchCustomers.setIcon(new ImageIcon("img\\heading3.png"));
        degrassiSearchCustomers.setBounds(128, 48, 414, 52);
        CustomerInfo.add(degrassiSearchCustomers);

        JLabel SearchCustomersBackground = new JLabel("New label");
        SearchCustomersBackground.setIcon(new ImageIcon("img\\ps4 wall.png"));
        SearchCustomersBackground.setBounds(-194, 23, 880, 442);
        CustomerInfo.add(SearchCustomersBackground);
    }

    public ArrayList<String> queryGameTitles() {
        if (!employee.Active()) return null;
        try {
            pst = con.prepareStatement("Select * from games");
            ResultSet rs = pst.executeQuery();
            ArrayList<String> arr = new ArrayList<>();
            while (rs.next()) {
                String title = rs.getString("Title");
                arr.add(title);
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setConfig() {
        super.setConfig();
        setBounds(100, 100, 707, 519);
        background.setBounds(-290, 5, 1023, 543);
        headingLabel.setBounds(128, 48, 414, 52);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Connection myCon = new MYSQLConnection().getDBConnection();
                new AdminSearch(
                        new Employee(myCon, "vitae@at.ca", "non,")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
