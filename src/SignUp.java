import sun.plugin.dom.html.HTMLBodyElement;

import java.awt.EventQueue;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SignUp extends GUI {
    private JPasswordField txtPassword;
    private JPasswordField txtReEnterPassword;
    private final Connection con;
    private JFormattedTextField txtFirstName;
    private JFormattedTextField txtSurname;
    private JFormattedTextField txtPhoneNumber;
    private JFormattedTextField txtEmailAddress;
    private JFormattedTextField txtPostalCode;
    private JFormattedTextField txtStreet;
    private JFormattedTextField txtCity;
    private JFormattedTextField txtState;


    //Create the frame.

    public SignUp(Connection con) {
        super();
        this.con = con;
        loadWidgets();
        setVisible(true);
    }
    @Override
    protected void loadWidgets() {
        JLabel FirstNameLabel = new JLabel();
        JLabel SurnameLabel = new JLabel();
        JLabel PhoneNumberLabel = new JLabel();
        JLabel EmailAddressLabel = new JLabel();
        JLabel PasswordLabel = new JLabel();
        JLabel reEnterPasswordLabel = new JLabel();
        JLabel StreetLabel = new JLabel();
        JLabel CityLabel = new JLabel();
        JLabel StateCodeLabel = new JLabel("New label");
        JLabel PostalCodeLabel = new JLabel();

        txtFirstName = new JFormattedTextField();
        txtSurname = new JFormattedTextField();
        txtPhoneNumber = new JFormattedTextField();
        txtEmailAddress = new JFormattedTextField();
        txtPassword = new JPasswordField();
        txtReEnterPassword = new JPasswordField();
        txtPostalCode = new JFormattedTextField();
        txtStreet = new JFormattedTextField();
        txtCity = new JFormattedTextField();
        txtState = new JFormattedTextField();
        JButton clientFrameBtn = new JButton();
        JButton nxtBtn = new JButton();


        clientFrameBtn.addActionListener(e -> {
            dispose();
            new ClientMainFrame(con);
        });
        clientFrameBtn.setIcon(new ImageIcon("img\\backbtn2.png"));
        clientFrameBtn.setBounds(28, 521, 89, 23);

        txtPostalCode.setBounds(296, 469, 136, 22);
        txtStreet.setBounds(296, 334, 136, 22);
        txtCity.setBounds(296, 424, 136, 22);
        txtState.setBounds(296, 379, 136, 22);
        PostalCodeLabel.setBounds(28, 469, 102, 22);
        PostalCodeLabel.setIcon(new ImageIcon("img\\postalcode.png"));
        StreetLabel.setIcon(new ImageIcon("img\\Street.png"));
        StreetLabel.setBounds(28, 334, 79, 22);
        CityLabel.setIcon(new ImageIcon("img\\city.png"));
        CityLabel.setBounds(28, 379, 46, 22);
        StateCodeLabel.setIcon(new ImageIcon("img\\statecode2.png"));
        StateCodeLabel.setBounds(28, 424, 102, 22);

        FirstNameLabel.setBounds(28, 64, 112, 22);
        FirstNameLabel.setIcon(new ImageIcon("img\\firstname.png"));
        SurnameLabel.setBounds(28, 109, 79, 22);
        SurnameLabel.setIcon(new ImageIcon("img\\surname.png"));
        txtPhoneNumber.setBounds(296, 289, 136, 22);
        PhoneNumberLabel.setIcon(new ImageIcon("img\\phonenumber.png"));
        PhoneNumberLabel.setBounds(28, 289, 117, 22);
        EmailAddressLabel.setBounds(28, 154, 123, 22);
        EmailAddressLabel.setIcon(new ImageIcon("img\\addy.png"));
        PasswordLabel.setBounds(28, 199, 112, 22);
        PasswordLabel.setIcon(new ImageIcon("img\\passwordsign.png"));
        reEnterPasswordLabel.setBounds(28, 244, 161, 22);
        reEnterPasswordLabel.setIcon(new ImageIcon("img\\reenterpass.png"));

        txtFirstName.setBounds(296, 63, 136, 23);
        txtSurname.setBounds(296, 109, 136, 24);
        txtEmailAddress.setBounds(296, 156, 136, 22);
        txtPassword.setBounds(296, 201, 136, 21);
        txtReEnterPassword.setBounds(296, 245, 135, 21);

        nxtBtn.addActionListener(e -> registerCustomer());
        nxtBtn.setIcon(new ImageIcon("img\\nxt4.png"));
        nxtBtn.setBounds(353, 514, 79, 23);


        contentPane.add(txtStreet);
        contentPane.add(StreetLabel);
        contentPane.add(txtCity);
        contentPane.add(CityLabel);
        contentPane.add(txtState);
        contentPane.add(StateCodeLabel);
        contentPane.add(txtPostalCode);
        contentPane.add(PostalCodeLabel);

        contentPane.add(txtFirstName);
        contentPane.add(FirstNameLabel);
        contentPane.add(txtSurname);
        contentPane.add(SurnameLabel);
        contentPane.add(txtPhoneNumber);
        contentPane.add(PhoneNumberLabel);
        contentPane.add(txtEmailAddress);
        contentPane.add(EmailAddressLabel);
        contentPane.add(txtPassword);
        contentPane.add(PasswordLabel);
        contentPane.add(txtReEnterPassword);
        contentPane.add(reEnterPasswordLabel);
        contentPane.add(clientFrameBtn);
        contentPane.add(nxtBtn);
        contentPane.add(headingLabel);
        contentPane.add(background);
    }

    private void registerCustomer() {
        String FirstName = txtFirstName.getText();
        String Surname = txtSurname.getText();
        String EmailAddress = txtEmailAddress.getText();
        String Password = String.valueOf(txtPassword.getPassword());
        String RePassword = String.valueOf(txtReEnterPassword.getPassword());
        String PhoneNumber = txtPhoneNumber.getText();
        Customer newCustomer = new Customer(con, FirstName, Surname,
                EmailAddress, Password, PhoneNumber
        );

        String State = txtState.getText();
        String City = txtCity.getText();
        String PostalCode = txtPostalCode.getText();
        String Street = txtStreet.getText();

        if (newCustomer.isValidEntry()) {
            if (newCustomer.checkPassword(RePassword)) {
                //create address instance and associate user with address if validation passes
                Address newAddress = new Address(con, Street, City, State, PostalCode);

                if (newAddress.isValidEntry()) {
                    String customerID = newCustomer.db_commit();
                    //Don't Insert data into address table if no customerID was given (error during user creation)
                    if (customerID != null) {
                        newAddress.db_commit(customerID);
                        dispose();
                        new ClientMainFrame(con);
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Internal Error: User was not created.\n" +
                                        "Please try again...",
                                "Registration Status", JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Password entered does not match.",
                        "Registration Status",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }

    }

    @Override
    protected void setConfig() {
        super.setConfig();
        setBounds(100, 100, 592, 595);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        headingLabel.setIcon(new ImageIcon("img\\heading3.png"));
        headingLabel.setBounds(49, 11, 376, 46);
        background.setIcon(new ImageIcon("img\\ps4 wall.png"));
        background.setBounds(-19, -21, 659, 600);
    }

    
    //launch the application.
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Connection myCon = new MYSQLConnection().getDBConnection();
                new SignUp(myCon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
