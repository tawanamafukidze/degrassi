package main.java.degrassi.views.client;

import main.java.degrassi.models.CartItemModel;
import main.java.degrassi.models.ProductModel;
import main.java.degrassi.models.user.CustomerModel;
import main.java.degrassi.mysql.MYSQLConnector;
import main.java.degrassi.views.admin.AdminSearch;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ClientFunctions extends JFrame {

    private final CustomerModel customer;
    private JTable Customers;
    private JTable tblProducts;
    Connection con;

    PreparedStatement pst;
    DefaultTableModel tbl;
    DefaultTableModel utbl;
    private JTable tblKart;
    private int maxSpinnerValue;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Connection con = new MYSQLConnector().getDBConnection();
                ClientFunctions frame = new ClientFunctions(
                        con, new CustomerModel(con, "test@gmail.com", "pass")
                );
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void LoadUser() {
        utbl = (DefaultTableModel) Customers.getModel();
        utbl.setRowCount(0);

        Vector<String> v2 = new Vector<>();
        try {
            v2.add(customer.getId());
            v2.add(customer.getFirstName());
            v2.add(customer.getLastName());
            v2.add(customer.getEmail());
            v2.add(customer.getPhone());

            String address = customer.getCustomerAddress().getStreet() + " " +
                    customer.getCustomerAddress().getCity() + " " +
                    customer.getCustomerAddress().getState() + " " +
                    customer.getCustomerAddress().getPostCode();
            v2.add(address);
        } catch (NullPointerException ignored) {
        }
        utbl.addRow(v2);
    }

    public void loadGames() {
        try {
            pst = con.prepareStatement("select * from games");

            ResultSet rs = pst.executeQuery();

            tbl = (DefaultTableModel) tblProducts.getModel();
            tbl.setRowCount(0);

            while (rs.next()) {
                Vector<Object> gameVector = new Vector<>();
                gameVector.add(rs.getString("Title"));
                gameVector.add(rs.getString("Type"));
                gameVector.add(rs.getString("Stock"));
                gameVector.add(rs.getString("Price"));
                gameVector.add(rs.getInt("gameID"));
                tbl.addRow(gameVector.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminSearch.class.getName()).isLoggable(Level.SEVERE);
        }
    }


    /**
     * Create the frame.
     */
    public ClientFunctions(Connection con, CustomerModel customer) {
        this.con = con;
        this.customer = customer;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 685, 594);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        setResizable(false);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
        gbc_tabbedPane.fill = GridBagConstraints.BOTH;
        gbc_tabbedPane.gridx = 0;
        gbc_tabbedPane.gridy = 0;
        contentPane.add(tabbedPane, gbc_tabbedPane);

        JPanel panel = new JPanel();
        tabbedPane.addTab("", new ImageIcon("img\\shop.png"), panel, null);
        tabbedPane.setBackgroundAt(0, Color.BLACK);
        panel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(146, 110, 361, 295);
        panel.add(scrollPane);

        tblProducts = new JTable();
        tblProducts.setModel(
                new DefaultTableModel(
                        new Object[][]{},
                        new String[]{"Title", "Type", "Stock", "Price", ""}
                ) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                }
        );
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProducts.getTableHeader().setReorderingAllowed(false);
        tblProducts.removeColumn(tblProducts.getColumnModel().getColumn(4));
        scrollPane.setViewportView(tblProducts);


        JFormattedTextField txtSearchGames = new JFormattedTextField();
        txtSearchGames.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                DefaultTableModel filteredTable = (DefaultTableModel) tblProducts.getModel();
                String search = "(?i)" + "^" + txtSearchGames.getText();
                TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(filteredTable);
                tblProducts.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter(search));
            }
        });
        txtSearchGames.setBounds(146, 78, 361, 20);
        panel.add(txtSearchGames);

        JLabel lblNewLabel_2 = new JLabel("New label");
        lblNewLabel_2.setIcon(new ImageIcon("img\\search.png"));
        lblNewLabel_2.setBounds(56, 81, 76, 14);
        panel.add(lblNewLabel_2);

        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon("img\\heading3.png"));
        lblNewLabel_1.setBounds(146, 11, 385, 59);
        panel.add(lblNewLabel_1);

        JLabel quantitySpinnerLabel = new JLabel("");
        quantitySpinnerLabel.setIcon(new ImageIcon("img\\quantity.png"));
        quantitySpinnerLabel.setBounds(146, 417, 76, 14);
        quantitySpinnerLabel.setVisible(false);
        panel.add(quantitySpinnerLabel);

        maxSpinnerValue = 1;
        JSpinner quantitySpinner = new JSpinner();
        quantitySpinner.setModel(new SpinnerNumberModel(1, 1, maxSpinnerValue, 1));
        quantitySpinner.setBounds(293, 416, 30, 20);
        quantitySpinner.setVisible(false);
        panel.add(quantitySpinner);

        tblProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (tblProducts.getSelectedRow() > 1) {
                    int selected = tblProducts.getSelectedRow();
                    maxSpinnerValue = Integer.parseInt(tbl.getValueAt(selected, 2).toString());

                    try {
                        quantitySpinner.setModel(
                                new SpinnerNumberModel(1, 1, maxSpinnerValue, 1)
                        );
                        quantitySpinnerLabel.setVisible(true);
                        quantitySpinner.setVisible(true);
                    } catch (IllegalArgumentException ignored) {
                        quantitySpinner.setVisible(false);
                        quantitySpinnerLabel.setVisible(false);
                    }
                }
            }
        });


        JButton addtokart = new JButton("add to kart");
        addtokart.setBounds(146, 442, 177, 23);
        panel.add(addtokart);
        addtokart.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        int selected = tblProducts.getSelectedRow();

                        String game = tbl.getValueAt(selected, 0).toString();
                        String type = tbl.getValueAt(selected, 1).toString();
                        int stock = Integer.parseInt(tbl.getValueAt(selected, 2).toString());
                        double price = Double.parseDouble(tbl.getValueAt(selected, 3).toString());
                        String gameID = tblProducts.getModel().getValueAt(selected, 4).toString();
                        customer.getShoppingCart().addToCart(
                                new CartItemModel("", (Integer) quantitySpinner.getValue(),
                                        new ProductModel(game, type, price, stock, gameID))
                        );
                        getCartItems();
                    }
                }
        );

        JLabel BackgroundShop = new JLabel("New label");
        BackgroundShop.setIcon(new ImageIcon("img\\ps4 wall.png"));
        BackgroundShop.setBounds(-190, -64, 909, 631);
        panel.add(BackgroundShop);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("", new ImageIcon("img\\profile.png"), panel_1, null);
        tabbedPane.setForegroundAt(1, Color.BLACK);
        tabbedPane.setBackgroundAt(1, Color.BLACK);
        panel_1.setLayout(null);

        JButton btnNewButton = new JButton("Edit");
        btnNewButton.setBounds(10, 432, 89, 23);
        panel_1.add(btnNewButton);

        JFormattedTextField txtPostalCode = new JFormattedTextField();
        txtPostalCode.setBounds(146, 402, 122, 20);
        panel_1.add(txtPostalCode);

        JLabel lblNewLabel_3 = new JLabel("");
        lblNewLabel_3.setIcon(new ImageIcon("img\\postalcode.png"));
        lblNewLabel_3.setBounds(10, 407, 105, 14);
        panel_1.add(lblNewLabel_3);

        JFormattedTextField formattedTextField_2 = new JFormattedTextField();
        formattedTextField_2.setBounds(143, 371, 122, 20);
        panel_1.add(formattedTextField_2);

        JLabel StreetLabel = new JLabel("");
        StreetLabel.setIcon(new ImageIcon("img\\Street.png"));
        StreetLabel.setBounds(10, 371, 59, 14);
        panel_1.add(StreetLabel);

        JFormattedTextField formattedTextField_1 = new JFormattedTextField();
        formattedTextField_1.setBounds(143, 337, 122, 20);
        panel_1.add(formattedTextField_1);

        JLabel txtCity = new JLabel("New label");
        txtCity.setIcon(new ImageIcon("img\\city.png"));
        txtCity.setBounds(10, 340, 46, 14);
        panel_1.add(txtCity);

        JFormattedTextField txtStateCode = new JFormattedTextField();
        txtStateCode.setBounds(143, 298, 122, 20);
        panel_1.add(txtStateCode);

        JLabel StateLabel = new JLabel("");
        StateLabel.setIcon(new ImageIcon("img\\statecode2.png"));
        StateLabel.setBounds(10, 304, 93, 14);
        panel_1.add(StateLabel);

        JFormattedTextField txtPhoneNumber = new JFormattedTextField();
        txtPhoneNumber.setBounds(143, 264, 122, 20);
        panel_1.add(txtPhoneNumber);

        JLabel PhoneNumberLabel = new JLabel("");
        PhoneNumberLabel.setIcon(new ImageIcon("img\\phonenumber.png"));
        PhoneNumberLabel.setBounds(10, 264, 112, 14);
        panel_1.add(PhoneNumberLabel);

        JFormattedTextField txtEmailAddress = new JFormattedTextField();
        txtEmailAddress.setBounds(143, 217, 122, 20);
        panel_1.add(txtEmailAddress);

        JLabel EmailAddressLabel = new JLabel("");
        EmailAddressLabel.setIcon(new ImageIcon("img\\emailupdate.png"));
        EmailAddressLabel.setBounds(10, 223, 112, 14);
        panel_1.add(EmailAddressLabel);

        JFormattedTextField txtSurname = new JFormattedTextField();
        txtSurname.setBounds(143, 175, 122, 22);
        panel_1.add(txtSurname);

        JLabel SurnameLabel = new JLabel("");
        SurnameLabel.setIcon(new ImageIcon("img\\surname.png"));
        SurnameLabel.setBounds(10, 175, 93, 22);
        panel_1.add(SurnameLabel);

        JTextPane txtFirstName = new JTextPane();
        txtFirstName.setBounds(143, 129, 122, 20);
        panel_1.add(txtFirstName);

        JLabel FirstNameLabel = new JLabel("");
        FirstNameLabel.setIcon(new ImageIcon("img\\firstname.png"));
        FirstNameLabel.setBounds(10, 129, 93, 22);
        panel_1.add(FirstNameLabel);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(274, 129, 370, 43);
        panel_1.add(scrollPane_1);

        Customers = new JTable();
        Customers.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                },
                new String[]{
                        "ID", "FirstName", "Surname", "Email Address", "Phone Number", "Address"
                }
        ));
        Customers.getTableHeader().setReorderingAllowed(false);
        scrollPane_1.setViewportView(Customers);

        JLabel DegrassiMyInfo = new JLabel("");
        DegrassiMyInfo.setIcon(new ImageIcon("img\\heading3.png"));
        DegrassiMyInfo.setBounds(143, 11, 370, 63);
        panel_1.add(DegrassiMyInfo);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("img\\ps4 wall.png"));
        lblNewLabel.setBounds(-191, -25, 957, 570);
        panel_1.add(lblNewLabel);

        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("", new ImageIcon("img\\checkout.png"), panel_2, null);
        tabbedPane.setForegroundAt(2, Color.BLACK);
        tabbedPane.setBackgroundAt(2, Color.BLACK);
        panel_2.setLayout(null);

        JLabel kartDegrassi = new JLabel("");
        kartDegrassi.setIcon(new ImageIcon("img\\heading3.png"));
        kartDegrassi.setBounds(149, 26, 373, 53);
        panel_2.add(kartDegrassi);

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(85, 107, 523, 139);
        panel_2.add(scrollPane_2);

        tblKart = new JTable();
        tblKart.setModel(
                new DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                                "Title", "Quantity", "Price", ""
                        }
                )
        );
        tblKart.getTableHeader().setReorderingAllowed(false);
        scrollPane_2.setViewportView(tblKart);

        JButton deletebtn = new JButton("Delete");
        deletebtn.setBounds(85, 326, 89, 23);
        panel_2.add(deletebtn);
        deletebtn.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);

                        int selected = tblKart.getSelectedRow();
                        String itemID = tblKart.getModel().getValueAt(selected, 3).toString();
                        customer.removeFromCart(itemID);
                        getCartItems();
                    }
                }
        );

        JButton orderbtn = new JButton("Order");
        orderbtn.setBounds(205, 326, 89, 23);
        panel_2.add(orderbtn);
        orderbtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (customer.checkOut()) {
                    JOptionPane.showMessageDialog(panel_2,
                            "Order has been placed.\n" +
                                    "Current Status: processing."
                    );
                } else if (customer.getShoppingCart().getCartItems().size() == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot proceed to checkout. Your cart seems to be empty.",
                            "Checkout Status",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(panel_2,
                            "Could not place order. Please try again later.",
                            "Order Status", JOptionPane.ERROR_MESSAGE
                    );
                }
                loadGames();
                getCartItems();
            }
        });

        JLabel kartBackground = new JLabel("");
        kartBackground.setIcon(new ImageIcon("img\\ps4 wall.png"));
        kartBackground.setBounds(-190, 0, 844, 544);
        panel_2.add(kartBackground);

        LoadUser();
        loadGames();
        getCartItems();
    }

    private void getCartItems() {
        tblKart.setVisible(false);
        customer.getShoppingCart();
        ArrayList<CartItemModel> cartItems = customer.getShoppingCart().toArrayList();
        DefaultTableModel cartModel;
        cartModel = (DefaultTableModel) tblKart.getModel();
        cartModel.setRowCount(0);
        Vector<Object> itemDetails;
        for (CartItemModel item : cartItems) {
            itemDetails = new Vector<>();
            itemDetails.add(item.getProduct().getProductTitle());
            itemDetails.add(item.getQuantity());
            itemDetails.add(item.getProduct().getPrice());
            itemDetails.add(item.getId());
            cartModel.addRow(itemDetails);
        }
        tblKart.setVisible(true);
        setVisible(true);
    }
}
