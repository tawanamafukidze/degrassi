import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import java.sql.Connection;
import javax.swing.JPasswordField;

public class EmployeeMainFrame extends JFrame {
    private final JPasswordField txtPassword;

    //Launch the application
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new EmployeeMainFrame(
                        new MYSQLConnection().getDBConnection()
                ).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EmployeeMainFrame(Connection con) {
        /*
         * Create the frame.
         */

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 479, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton backBtn = new JButton();
        backBtn.addActionListener(e -> {
            dispose();
            new MainFrame(con);
        });

        JFormattedTextField txtAdminID = new JFormattedTextField();
        txtAdminID.setBounds(290, 159, 135, 20);
        contentPane.add(txtAdminID);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(290, 193, 135, 20);
        contentPane.add(txtPassword);

        backBtn.setIcon(new ImageIcon("img\\backbtn3.png"));
        backBtn.setBounds(48, 221, 75, 23);
        contentPane.add(backBtn);

        JButton nxtBtn = new JButton("");
        nxtBtn.setIcon(new ImageIcon("img\\nxt4.png"));
        nxtBtn.setBounds(339, 221, 86, 23);
        nxtBtn.addActionListener(e -> {
            //get admin instance when correct credentials are supplied
            String email = txtAdminID.getText();
            String pass = String.valueOf(txtPassword.getPassword());

            Employee employee = new Employee(con, email, pass);
            if (employee.Active()) {
                new AdminSearch(con, employee);
                dispose();
            }
        });
        contentPane.add(nxtBtn);

        JLabel degrassi = new JLabel("");
        degrassi.setIcon(new ImageIcon("img\\heading3.png"));
        degrassi.setBounds(54, 35, 443, 43);
        contentPane.add(degrassi);

        JLabel PasswordImage = new JLabel("");
        PasswordImage.setIcon(new ImageIcon("img\\loginpass4.png"));
        PasswordImage.setBounds(50, 190, 177, 34);
        contentPane.add(PasswordImage);

        JLabel EmailAddressImage = new JLabel("");
        EmailAddressImage.setIcon(new ImageIcon("img\\emailaddylogin.png"));
        EmailAddressImage.setBounds(50, 149, 206, 43);
        contentPane.add(EmailAddressImage);

        JLabel background = new JLabel("");
        background.setIcon(new ImageIcon("img\\resize-1598019257675522578ps4wall.png"));
        background.setBounds(0, 0, 463, 261);
        contentPane.add(background);
        setVisible(true);
    }
}

