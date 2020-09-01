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

public class ClientFunctions extends GUI {

    private final Customer customer;
    private final Connection con;
    private JTable tblCustomers;
    private JTable tblProducts;
    private JTable tblKart;
    private int maxSpinnerValue;

    PreparedStatement pst;
    DefaultTableModel tbl;
    DefaultTableModel utbl;
    private JTabbedPane tabbedPane;
    private GridBagConstraints gbc_tabbedPane;


    //Create the frame.
    public ClientFunctions(Customer customer) {
        super();
        this.con = customer.getDBConnection();
        this.customer = customer;
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        gbc_tabbedPane = new GridBagConstraints();
        loadWidgets();
        setVisible(true);
    }

    @Override
    protected void loadWidgets() {
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);
        
        gbc_tabbedPane.fill = GridBagConstraints.BOTH;
        gbc_tabbedPane.gridx = 0;
        gbc_tabbedPane.gridy = 0;
        contentPane.add(tabbedPane, gbc_tabbedPane);

        shopPanelWidgets();
        profilePanelWidgets();
        checkoutPanelWidgets();

        loadGames();
        LoadUser();
        refreshCartTable();
    }

    private void shopPanelWidgets() {
        JPanel shopPanel = new JPanel();
        tabbedPane.addTab("", new ImageIcon("img\\shop.png"), shopPanel, null);
        tabbedPane.setBackgroundAt(0, Color.BLACK);
        shopPanel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(146, 110, 361, 295);
        shopPanel.add(scrollPane);

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
        shopPanel.add(txtSearchGames);

        JLabel searchLabel = new JLabel();
        searchLabel.setIcon(new ImageIcon("img\\search.png"));
        searchLabel.setBounds(56, 81, 76, 14);
        shopPanel.add(searchLabel);

        JLabel heading = new JLabel();
        heading.setIcon(new ImageIcon("img\\heading3.png"));
        heading.setBounds(146, 11, 385, 59);
        shopPanel.add(heading);

        JLabel quantitySpinnerLabel = new JLabel();
        quantitySpinnerLabel.setIcon(new ImageIcon("img\\quantity.png"));
        quantitySpinnerLabel.setBounds(146, 417, 76, 14);
        quantitySpinnerLabel.setVisible(false);
        shopPanel.add(quantitySpinnerLabel);

        maxSpinnerValue = 1;
        JSpinner quantitySpinner = new JSpinner();
        quantitySpinner.setModel(new SpinnerNumberModel(1, 1, maxSpinnerValue, 1));
        quantitySpinner.setBounds(293, 416, 30, 20);
        quantitySpinner.setVisible(false);
        shopPanel.add(quantitySpinner);

        tblProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (tblProducts.getSelectedRow() > 1) {
                    int selected = tblProducts.getSelectedRow();
                    maxSpinnerValue = Integer.parseInt(tbl.getValueAt(selected, 2).toString());

                    quantitySpinner.setModel(
                            new SpinnerNumberModel(1, 1, maxSpinnerValue, 1)
                    );
                    quantitySpinnerLabel.setVisible(true);
                    quantitySpinner.setVisible(true);
                }
            }
        });

        JButton addToKartBtn = new JButton("add to kart");
        addToKartBtn.setBounds(146, 442, 177, 23);
        shopPanel.add(addToKartBtn);
        addToKartBtn.addMouseListener(
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
                                new Item(
                                        "", (Integer) quantitySpinner.getValue(),
                                        new Product(game, type, price, stock, gameID)
                                )
                        );
                        refreshCartTable();
                    }
                }
        );

        JLabel background = new JLabel();
        background.setIcon(new ImageIcon("img\\ps4 wall.png"));
        background.setBounds(-190, -64, 909, 631);
        shopPanel.add(background);
    }

    private void profilePanelWidgets() {
        JPanel profilePanel = new JPanel();
        tabbedPane.addTab("", new ImageIcon("img\\profile.png"), profilePanel, null);
        tabbedPane.setForegroundAt(1, Color.BLACK);
        tabbedPane.setBackgroundAt(1, Color.BLACK);
        profilePanel.setLayout(null);

        JButton editBtn = new JButton("Edit");
        editBtn.setBounds(10, 432, 89, 23);
        profilePanel.add(editBtn);

        JFormattedTextField txtPostalCode = new JFormattedTextField();
        txtPostalCode.setBounds(146, 402, 122, 20);
        profilePanel.add(txtPostalCode);

        JLabel postalCodeLabel = new JLabel();
        postalCodeLabel.setIcon(new ImageIcon("img\\postalcode.png"));
        postalCodeLabel.setBounds(10, 407, 105, 14);
        profilePanel.add(postalCodeLabel);

        JFormattedTextField txtStreet = new JFormattedTextField();
        txtStreet.setBounds(143, 371, 122, 20);
        profilePanel.add(txtStreet);

        JLabel StreetLabel = new JLabel();
        StreetLabel.setIcon(new ImageIcon("img\\Street.png"));
        StreetLabel.setBounds(10, 371, 59, 14);
        profilePanel.add(StreetLabel);

        JFormattedTextField txtCity = new JFormattedTextField();
        txtCity.setBounds(143, 337, 122, 20);
        profilePanel.add(txtCity);

        JLabel cityLabel = new JLabel();
        cityLabel.setIcon(new ImageIcon("img\\city.png"));
        cityLabel.setBounds(10, 340, 46, 14);
        profilePanel.add(cityLabel);

        JFormattedTextField txtStateCode = new JFormattedTextField();
        txtStateCode.setBounds(143, 298, 122, 20);
        profilePanel.add(txtStateCode);

        JLabel StateLabel = new JLabel();
        StateLabel.setIcon(new ImageIcon("img\\statecode2.png"));
        StateLabel.setBounds(10, 304, 93, 14);
        profilePanel.add(StateLabel);

        JFormattedTextField txtPhoneNumber = new JFormattedTextField();
        txtPhoneNumber.setBounds(143, 264, 122, 20);
        profilePanel.add(txtPhoneNumber);

        JLabel PhoneNumberLabel = new JLabel();
        PhoneNumberLabel.setIcon(new ImageIcon("img\\phonenumber.png"));
        PhoneNumberLabel.setBounds(10, 264, 112, 14);
        profilePanel.add(PhoneNumberLabel);

        JFormattedTextField txtEmailAddress = new JFormattedTextField();
        txtEmailAddress.setBounds(143, 217, 122, 20);
        profilePanel.add(txtEmailAddress);

        JLabel EmailAddressLabel = new JLabel();
        EmailAddressLabel.setIcon(new ImageIcon("img\\emailupdate.png"));
        EmailAddressLabel.setBounds(10, 223, 112, 14);
        profilePanel.add(EmailAddressLabel);

        JFormattedTextField txtSurname = new JFormattedTextField();
        txtSurname.setBounds(143, 175, 122, 22);
        profilePanel.add(txtSurname);

        JLabel SurnameLabel = new JLabel();
        SurnameLabel.setIcon(new ImageIcon("img\\surname.png"));
        SurnameLabel.setBounds(10, 175, 93, 22);
        profilePanel.add(SurnameLabel);

        JTextPane txtFirstName = new JTextPane();
        txtFirstName.setBounds(143, 129, 122, 20);
        profilePanel.add(txtFirstName);

        JLabel FirstNameLabel = new JLabel();
        FirstNameLabel.setIcon(new ImageIcon("img\\firstname.png"));
        FirstNameLabel.setBounds(10, 129, 93, 22);
        profilePanel.add(FirstNameLabel);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(274, 129, 370, 43);
        profilePanel.add(scrollPane_1);

        tblCustomers = new JTable();
        tblCustomers.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                },
                new String[]{
                        "ID", "FirstName", "Surname", "Email Address", "Phone Number", "Address"
                }
        ));
        tblCustomers.getTableHeader().setReorderingAllowed(false);
        scrollPane_1.setViewportView(tblCustomers);

        JLabel heading = new JLabel();
        heading.setIcon(new ImageIcon("img\\heading3.png"));
        heading.setBounds(143, 11, 370, 63);
        profilePanel.add(heading);

        JLabel background = new JLabel();
        background.setIcon(new ImageIcon("img\\ps4 wall.png"));
        background.setBounds(-191, -25, 957, 570);
        profilePanel.add(background);
    }
    
    private void checkoutPanelWidgets() {
        JPanel checkoutPanel = new JPanel();
        tabbedPane.addTab("", new ImageIcon("img\\checkout.png"), checkoutPanel, null);
        tabbedPane.setForegroundAt(2, Color.BLACK);
        tabbedPane.setBackgroundAt(2, Color.BLACK);
        checkoutPanel.setLayout(null);

        JLabel heading = new JLabel();
        heading.setIcon(new ImageIcon("img\\heading3.png"));
        heading.setBounds(149, 26, 373, 53);
        checkoutPanel.add(heading);

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(85, 107, 523, 139);
        checkoutPanel.add(scrollPane_2);

        tblKart = new JTable();
        tblKart.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Title", "Quantity", "Price", ""
                }
        ));
        tblKart.getTableHeader().setReorderingAllowed(false);
        scrollPane_2.setViewportView(tblKart);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(85, 326, 89, 23);
        checkoutPanel.add(deleteBtn);
        deleteBtn.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);

                        int selected = tblKart.getSelectedRow();
                        int itemID = Integer.parseInt(
                                tblKart.getModel().getValueAt(selected, 3).toString()
                        );
                        System.out.println(itemID);
                        customer.removeFromCart(itemID);
                        refreshCartTable();
                    }
                }
        );

        JButton orderBtn = new JButton("Order");
        orderBtn.setBounds(205, 326, 89, 23);
        checkoutPanel.add(orderBtn);
        orderBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                long start = System.currentTimeMillis();
                customer.checkOut();
                while (System.currentTimeMillis() - start < 6000) {
                    System.out.print("");
                }

                JOptionPane.showMessageDialog(null,
                        "Your order has been shipped.\n"
                );
                refreshCartTable();
            }
        });

        JLabel background = new JLabel();
        background.setIcon(new ImageIcon("img\\ps4 wall.png"));
        background.setBounds(-190, 0, 844, 544);
        checkoutPanel.add(background);
    }

    public void LoadUser() {
        utbl = (DefaultTableModel) tblCustomers.getModel();
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

    private void refreshCartTable() {
        tblKart.setVisible(false);
        customer.getShoppingCart();
        ArrayList<Item> cartItems = customer.getShoppingCart().toArrayList();
        DefaultTableModel cartModel;
        cartModel = (DefaultTableModel) tblKart.getModel();
        cartModel.setRowCount(0);
        Vector<Object> itemDetails;
        for (Item item : cartItems) {
            itemDetails = new Vector<>();
            itemDetails.add(item.getProduct().getProductTitle());
            itemDetails.add(item.getQuantity());
            itemDetails.add(item.getProduct().getPrice());
            itemDetails.add(item.getId());
            cartModel.addRow(itemDetails);
        }
        tblKart.setVisible(true);
    }

    @Override
    protected void setConfig() {
        super.setConfig();
        setBounds(100, 100, 685, 594);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    //Launch the application.
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Connection con = new MYSQLConnection().getDBConnection();
                ClientFunctions frame = new ClientFunctions(
                        new Customer(con, "test@gmail.com", "pass")
                );
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
