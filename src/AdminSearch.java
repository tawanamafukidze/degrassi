import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
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
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class AdminSearch extends JFrame {
    private JTable tblProducts;
    private final Employee employee;
    private JTable Customers;

    Connection con;
    PreparedStatement pst;
    DefaultTableModel tbl;
    DefaultTableModel uTbl;
    DefaultListModel<String> searchList;


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Connection myCon = new MYSQLConnection().getDBConnection();
                new AdminSearch(
                        myCon,
                        new Employee(myCon, "vitae@at.ca", "non,")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadGames() {
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

    public void LoadUser() {
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

    private void BindData() {
        inputArray().forEach((game) -> searchList.addElement(game));
    }

    public ArrayList<String> inputArray() {
        // TODO Auto-generated method stub
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

    public AdminSearch(Connection con, Employee employee) {
        this.con = con;
        this.employee = employee;
        searchList = new DefaultListModel<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 707, 519);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        BindData();

        JPanel Update = new JPanel();
        tabbedPane.addTab("", new ImageIcon("img\\Stockkk.png"), Update, null);
        tabbedPane.setBackgroundAt(0, Color.BLACK);
        Update.setLayout(null);

        JFormattedTextField txtSearchStock = new JFormattedTextField();
        txtSearchStock.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                DefaultTableModel filteredTable = (DefaultTableModel) tblProducts.getModel();
                String search = "(?i)" + "^" + txtSearchStock.getText();
                TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(filteredTable);
                tblProducts.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter(search));
            }
        });

        JTextPane txtTitle = new JTextPane();
        txtTitle.setBounds(101, 132, 86, 23);
        Update.add(txtTitle);

        JTextPane txtPrice = new JTextPane();
        txtPrice.setBounds(101, 297, 86, 14);
        Update.add(txtPrice);

        JTextPane txtStock = new JTextPane();
        txtStock.setBounds(101, 245, 86, 14);
        Update.add(txtStock);

        JTextPane txtType = new JTextPane();
        txtType.setBounds(101, 184, 86, 23);
        Update.add(txtType);

        JButton addbtn = new JButton("add");


        addbtn.addActionListener(e -> {

            try {
                String Title = txtTitle.getText();
                String Type = txtType.getText();
                int Stock = Integer.parseInt(txtStock.getText());
                double Price = Double.parseDouble(txtPrice.getText());

                pst = con.prepareStatement("Insert into games( Title, Type, Stock, Price) Values(?,?,?,?)");

                pst.setString(1, Title);
                pst.setString(2, Type);
                pst.setInt(3, Stock);
                pst.setDouble(4, Price);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(Update, "New Game Added");

                loadGames();
                txtTitle.setText("");
                txtType.setText("");
                txtStock.setText("");
                txtPrice.setText("");
                txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });


        addbtn.setBounds(21, 359, 67, 23);
        Update.add(addbtn);
        txtSearchStock.setBounds(302, 105, 270, 23);
        Update.add(txtSearchStock);

        JLabel StockSeachLabel = new JLabel("New label");
        StockSeachLabel.setIcon(new ImageIcon("img\\search.png"));
        StockSeachLabel.setBounds(208, 104, 77, 23);
        Update.add(StockSeachLabel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(208, 138, 458, 173);
        Update.add(scrollPane);

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
        scrollPane.setViewportView(tblProducts);

        JLabel PriceLabel = new JLabel("");
        PriceLabel.setIcon(new ImageIcon("img\\price.png"));
        PriceLabel.setBounds(21, 297, 46, 14);
        Update.add(PriceLabel);

        JLabel StockLabel = new JLabel("");
        StockLabel.setIcon(new ImageIcon("img\\stock.png"));
        StockLabel.setBounds(21, 245, 56, 14);
        Update.add(StockLabel);

        JLabel TypeLabel = new JLabel("");
        TypeLabel.setIcon(new ImageIcon("img\\type.png"));
        TypeLabel.setBounds(21, 193, 46, 14);
        Update.add(TypeLabel);

        JLabel TitleLabel = new JLabel("");
        TitleLabel.setIcon(new ImageIcon("img\\title.png"));
        TitleLabel.setBounds(21, 141, 46, 14);
        Update.add(TitleLabel);

        JLabel degrassi = new JLabel("");
        degrassi.setIcon(new ImageIcon("img\\heading3.png"));
        degrassi.setBounds(170, 11, 371, 54);
        Update.add(degrassi);

        JButton editbtn = new JButton("edit");
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
                JOptionPane.showMessageDialog(Update, "Game Info Updated");

                txtTitle.setText("");
                txtType.setText("");
                txtStock.setText("");
                txtPrice.setText("");
                txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
                loadGames();
                addbtn.setEnabled(true);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        JButton deletebtn = new JButton("delete");
        deletebtn.addActionListener(e -> {

            tbl = (DefaultTableModel) tblProducts.getModel();
            int selected = tblProducts.getSelectedRow();

            int id = Integer.parseInt((tbl.getValueAt(selected, 0).toString()));


            try {
                pst = con.prepareStatement("delete from games where GameID = ?");


                pst.setInt(1, id);
                pst.executeUpdate();


                JOptionPane.showMessageDialog(Update, "Game Deleted");

                txtTitle.setText("");
                txtType.setText("");
                txtStock.setText("");
                txtPrice.setText("");
                txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
                loadGames();
                addbtn.setEnabled(true);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }


        });
        deletebtn.setBounds(196, 359, 89, 23);
        Update.add(deletebtn);

        editbtn.setBounds(101, 359, 77, 23);
        Update.add(editbtn);

        JButton cancelbtn = new JButton("cancel");
        cancelbtn.addActionListener(e -> {
            txtTitle.setText("");
            txtType.setText("");
            txtStock.setText("");
            txtPrice.setText("");
            txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
            addbtn.setEnabled(true);
        });
        cancelbtn.setBounds(302, 359, 89, 23);
        Update.add(cancelbtn);

        JLabel BackgroundUpdate = new JLabel("");
        BackgroundUpdate.setBounds(-290, 5, 1023, 543);
        BackgroundUpdate.setIcon(new ImageIcon("img\\ps4 wall.png"));
        Update.add(BackgroundUpdate);

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
                String search = "(?i)" + "^" + txtSearchCustomers.getText();
                TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(filteredTable);
                Customers.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter(search));
            }
        });
        txtSearchCustomers.setBounds(116, 111, 536, 20);
        CustomerInfo.add(txtSearchCustomers);

        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon("img\\search.png"));
        lblNewLabel_1.setBounds(28, 116, 91, 14);
        CustomerInfo.add(lblNewLabel_1);

        JLabel degrassiSearchCustomers = new JLabel("");
        degrassiSearchCustomers.setIcon(new ImageIcon("img\\heading3.png"));
        degrassiSearchCustomers.setBounds(128, 48, 414, 52);
        CustomerInfo.add(degrassiSearchCustomers);

        JLabel SearchCustomersBackground = new JLabel("New label");
        SearchCustomersBackground.setIcon(new ImageIcon("img\\ps4 wall.png"));
        SearchCustomersBackground.setBounds(-194, 23, 880, 442);
        CustomerInfo.add(SearchCustomersBackground);
        loadGames();
        LoadUser();
        setVisible(true);
    }
}
