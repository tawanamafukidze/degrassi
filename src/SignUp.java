import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;

public class SignUp extends JFrame {

    private JPanel contentPane;
    private JPasswordField txtPassword;
    private JPasswordField txtRePassword;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SignUp frame = new SignUp();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Connection con;
    PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                con = DriverManager.getConnection("jdbc:mysql://192.168.1.48/degrassi", "root", "Aaronstone07");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Create the frame.
     */
    public SignUp() {

        connect();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 592, 665);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnNewButton = new JButton();
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                ClientMainFrame c = new ClientMainFrame();
                c.setVisible(true);
            }
        });

        JFormattedTextField txtPostalCode = new JFormattedTextField();
        txtPostalCode.setBounds(296, 469, 136, 22);
        contentPane.add(txtPostalCode);

        JFormattedTextField txtStreet = new JFormattedTextField();
        txtStreet.setBounds(296, 334, 136, 22);
        contentPane.add(txtStreet);

        JFormattedTextField txtCity = new JFormattedTextField();
        txtCity.setBounds(296, 424, 136, 22);
        contentPane.add(txtCity);

        JFormattedTextField txtState = new JFormattedTextField();
        txtState.setBounds(296, 379, 136, 22);
        contentPane.add(txtState);

        JLabel PostalCodeLabel = new JLabel();
        PostalCodeLabel.setIcon(new ImageIcon("img\\postalcode.png"));
        PostalCodeLabel.setBounds(28, 469, 102, 22);
        contentPane.add(PostalCodeLabel);

        JLabel StreetLabel = new JLabel();
        StreetLabel.setIcon(new ImageIcon("img\\Street.png"));
        StreetLabel.setBounds(28, 334, 79, 22);
        contentPane.add(StreetLabel);

        JLabel CityLabel = new JLabel();
        CityLabel.setIcon(new ImageIcon("img\\city.png"));
        CityLabel.setBounds(28, 379, 46, 22);
        contentPane.add(CityLabel);

        JLabel StateCodeLabel = new JLabel("New label");
        StateCodeLabel.setIcon(new ImageIcon("img\\statecode2.png"));
        StateCodeLabel.setBounds(28, 424, 102, 22);
        contentPane.add(StateCodeLabel);

        JFormattedTextField txtPhoneNumber = new JFormattedTextField();
        txtPhoneNumber.setBounds(296, 289, 136, 22);
        contentPane.add(txtPhoneNumber);

        JLabel PhoneNumberLabel = new JLabel();
        PhoneNumberLabel.setIcon(new ImageIcon("img\\phonenumber.png"));
        PhoneNumberLabel.setBounds(28, 289, 117, 22);
        contentPane.add(PhoneNumberLabel);

        txtRePassword = new JPasswordField();
        txtRePassword.setBounds(296, 245, 135, 21);
        contentPane.add(txtRePassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(296, 201, 136, 21);
        contentPane.add(txtPassword);

        JFormattedTextField txtEmailAddress = new JFormattedTextField();
        txtEmailAddress.setBounds(296, 156, 136, 22);
        contentPane.add(txtEmailAddress);

        JFormattedTextField txtSurname = new JFormattedTextField();
        txtSurname.setBounds(296, 109, 136, 24);
        contentPane.add(txtSurname);

        JFormattedTextField txtFirstName = new JFormattedTextField();
        txtFirstName.setBounds(296, 63, 136, 23);
        contentPane.add(txtFirstName);

        btnNewButton.setIcon(new ImageIcon("img\\backbtn2.png"));
        btnNewButton.setBounds(28, 521, 89, 23);
        contentPane.add(btnNewButton);

        JButton realnxtbtn = new JButton();
        realnxtbtn.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                String FirstName = txtFirstName.getText();
                String Surname = txtSurname.getText();
                String EmailAddress = txtEmailAddress.getText();
                String Password = txtPassword.getText();
                String RePassword = txtRePassword.getText();
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
                        Address newAddress = new Address(con, Street, City, State, PostalCode);

                        if (newAddress.isValidEntry()) {
                            String customerID = newCustomer.db_commit();
                            //Don't Insert data into address table if no customerID was given (error during user creation)
                            if (customerID != null) {
                                newAddress.db_commit(customerID);
                                dispose();
                                new ClientMainFrame(con).setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(
                                        contentPane, "Internal Error: User was not created. \n Please try again..."
                                );
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(txtPassword, "Password entered does not match.");
                    }
                }

            }
        });
        realnxtbtn.setIcon(new ImageIcon("img\\nxt4.png"));
        realnxtbtn.setBounds(353, 514, 79, 23);
        contentPane.add(realnxtbtn);

        JLabel degrassi = new JLabel();
        degrassi.setIcon(new ImageIcon("img\\heading3.png"));
        degrassi.setBounds(49, 11, 376, 46);
        contentPane.add(degrassi);

        JLabel ReEnterLabel = new JLabel();
        ReEnterLabel.setIcon(new ImageIcon("img\\reenterpass.png"));
        ReEnterLabel.setBounds(28, 244, 161, 22);
        contentPane.add(ReEnterLabel);

        JLabel PasswordLabel = new JLabel();
        PasswordLabel.setIcon(new ImageIcon("img\\passwordsign.png"));
        PasswordLabel.setBounds(28, 199, 112, 22);
        contentPane.add(PasswordLabel);

        JLabel SurnameLabel = new JLabel();
        SurnameLabel.setIcon(new ImageIcon("img\\surname.png"));
        SurnameLabel.setBounds(28, 109, 79, 22);
        contentPane.add(SurnameLabel);

        JLabel FirstNameLabel = new JLabel();
        FirstNameLabel.setIcon(new ImageIcon("img\\firstname.png"));
        FirstNameLabel.setBounds(28, 64, 112, 22);
        contentPane.add(FirstNameLabel);

        JLabel EmailAddressLabel = new JLabel();
        EmailAddressLabel.setIcon(new ImageIcon("img\\addy.png"));
        EmailAddressLabel.setBounds(28, 154, 123, 22);
        contentPane.add(EmailAddressLabel);

        JLabel nextbtn = new JLabel();
        nextbtn.setIcon(new ImageIcon("img\\ps4 wall.png"));
        nextbtn.setBounds(-19, -21, 659, 600);
        contentPane.add(nextbtn);
    }
}
