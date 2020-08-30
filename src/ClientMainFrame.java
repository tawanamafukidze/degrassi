import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JPasswordField;
import javax.xml.stream.events.Characters;

public class ClientMainFrame extends JFrame {

    private JPanel contentPane;
    private JPasswordField txtPassword;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Connection con = new MYSQLConnection().getDBConnection();
                    ClientMainFrame frame = new ClientMainFrame(con);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ClientMainFrame(Connection con) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 484, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnNewButton_2 = new JButton("");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                SignUp s = new SignUp();
                s.setVisible(true);

            }
        });

        txtPassword = new JPasswordField();
        txtPassword.setBounds(306, 183, 135, 20);
        contentPane.add(txtPassword);
        btnNewButton_2.setIcon(new ImageIcon("img\\signup2.png"));
        btnNewButton_2.setBounds(197, 224, 103, 23);
        contentPane.add(btnNewButton_2);

        JFormattedTextField txtEmailAddress = new JFormattedTextField();
        txtEmailAddress.setBounds(306, 152, 135, 20);
        contentPane.add(txtEmailAddress);

        JButton btnNewButton_1 = new JButton("");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: LOGIN FUNCTIONALITY
                String email = txtEmailAddress.getText();
                String password = txtPassword.getText();

                Customer customer = new Customer(con, email, password);
                if (customer.Active()) {
                    System.out.println(customer.toString());
                    System.out.println(customer.getCustomerAddress());
                    System.out.println(customer.getShoppingCart().toString());
                }
            }
        });
        btnNewButton_1.setIcon(new ImageIcon("img\\nxt4.png"));
        btnNewButton_1.setBounds(362, 224, 79, 23);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton = new JButton("New button");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainFrame m = new MainFrame(con);
                m.setVisible(true);
            }
        });
        btnNewButton.setIcon(new ImageIcon("img\\backbtn3.png"));
        btnNewButton.setBounds(53, 224, 75, 23);
        contentPane.add(btnNewButton);

        JLabel lblNewLabel_1_1_1 = new JLabel("");
        lblNewLabel_1_1_1.setIcon(new ImageIcon("img\\loginpass4.png"));
        lblNewLabel_1_1_1.setBounds(53, 170, 153, 43);
        contentPane.add(lblNewLabel_1_1_1);

        JLabel lblNewLabel_1_1 = new JLabel("");
        lblNewLabel_1_1.setIcon(new ImageIcon("img\\emailaddylogin.png"));
        lblNewLabel_1_1.setBounds(53, 141, 206, 43);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon("img\\heading3.png"));
        lblNewLabel_1.setBounds(53, 22, 415, 45);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("img\\resize-1598019257675522578ps4wall.png"));
        lblNewLabel.setBounds(0, 0, 468, 261);
        contentPane.add(lblNewLabel);
    }
}
