import java.awt.EventQueue;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import java.sql.Connection;
import javax.swing.JPasswordField;

public class ClientMainFrame extends GUI {
    private Connection con;

    public ClientMainFrame(Connection con) {
        super();
        this.con = con;
        loadWidgets();
        setVisible(true);
    }


    @Override
    protected void loadWidgets() {
        JPasswordField txtPassword = new JPasswordField();
        JFormattedTextField txtEmailAddress = new JFormattedTextField();
        JButton signUpBtn = new JButton("");
        JButton loginBtn = new JButton("");
        JButton backBtn = new JButton("");
        JLabel passwordLabel = new JLabel("");
        JLabel emailLabel = new JLabel("");


        loginBtn.addActionListener(e -> {
            //LOGIN USER
            String email = txtEmailAddress.getText();
            String password = String.valueOf(txtPassword.getPassword());

            Customer customer = new Customer(con, email, password);
            if (customer.Active()) {
                new ClientFunctions(customer);
                dispose();
            }
        });

        passwordLabel.setIcon(new ImageIcon("img\\loginpass4.png"));
        passwordLabel.setBounds(53, 170, 153, 43);

        txtPassword.setBounds(306, 183, 135, 20);
        txtPassword.addKeyListener(myKeyAdapter.getMyKeyAdapter(loginBtn));

        emailLabel.setIcon(new ImageIcon("img\\emailaddylogin.png"));
        emailLabel.setBounds(53, 141, 206, 43);

        txtEmailAddress.setBounds(306, 152, 135, 20);
        txtEmailAddress.addKeyListener(myKeyAdapter.getMyKeyAdapter(loginBtn));

        signUpBtn.addActionListener(e -> {
            dispose();
            new SignUp(con);
        });
        signUpBtn.setIcon(new ImageIcon("img\\signup2.png"));
        signUpBtn.setBounds(197, 224, 103, 23);

        loginBtn.setIcon(new ImageIcon("img\\nxt4.png"));
        loginBtn.setBounds(362, 224, 79, 23);

        backBtn.addActionListener(e -> {
            dispose();
            new MainFrame();
        });
        backBtn.setIcon(new ImageIcon("img\\backbtn3.png"));
        backBtn.setBounds(53, 224, 75, 23);


        contentPane.add(passwordLabel);
        contentPane.add(txtPassword);
        contentPane.add(emailLabel);
        contentPane.add(txtEmailAddress);
        contentPane.add(signUpBtn);
        contentPane.add(loginBtn);
        contentPane.add(backBtn);
        contentPane.add(headingLabel);
        contentPane.add(background);
    }


    @Override
    protected void setConfig() {
        super.setConfig();
        setBounds(100, 100, 484, 300);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        background.setBounds(0, 0, 468, 261);
        headingLabel.setBounds(53, 22, 415, 45);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Connection con = new MYSQLConnection().getDBConnection();
                new ClientMainFrame(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
