import java.awt.EventQueue;
import java.sql.Connection;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class EmployeeMainFrame extends GUI {
    private JPasswordField txtPassword;
    private Connection con;

    //Create the frame.
    public EmployeeMainFrame(Connection con) {
        super();
        this.con = con;
        loadWidgets();
        setVisible(true);
    }

    @Override
    protected void loadWidgets() {
        JFormattedTextField txtEmailAddress = new JFormattedTextField();
        JLabel PasswordImage = new JLabel();
        JLabel EmailAddressImage = new JLabel();
        JButton backBtn = new JButton();
        JButton nextBtn = new JButton();
        txtPassword = new JPasswordField();
        txtPassword.setBounds(290, 193, 135, 20);

        PasswordImage.setIcon(new ImageIcon("img\\loginpass4.png"));
        PasswordImage.setBounds(50, 190, 177, 34);

        EmailAddressImage.setIcon(new ImageIcon("img\\emailaddylogin.png"));
        EmailAddressImage.setBounds(50, 149, 206, 43);
        txtEmailAddress.setBounds(290, 159, 135, 20);


        backBtn.addActionListener(e -> {
            dispose();
            new MainFrame();
        });
        backBtn.setIcon(new ImageIcon("img\\backbtn3.png"));
        backBtn.setBounds(48, 221, 75, 23);

        nextBtn.setIcon(new ImageIcon("img\\nxt4.png"));
        nextBtn.setBounds(339, 221, 86, 23);
        nextBtn.addActionListener(e -> {
            //get admin instance when correct credentials are supplied
            String email = txtEmailAddress.getText();
            String pass = String.valueOf(txtPassword.getPassword());

            Employee employee = new Employee(con, email, pass);
            if (employee.Active()) {
                new AdminSearch(employee);
                dispose();
            }
        });


        contentPane.add(txtEmailAddress);
        contentPane.add(txtPassword);
        contentPane.add(backBtn);
        contentPane.add(nextBtn);
        contentPane.add(headingLabel);
        contentPane.add(PasswordImage);
        contentPane.add(EmailAddressImage);
        contentPane.add(background); //Add background to pane
    }

    @Override
    protected void setConfig() {
        super.setConfig();
        setBounds(100, 100, 479, 300);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        headingLabel.setBounds(54, 35, 443, 43);
    }

    //Launch the application
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Connection myCon = new MYSQLConnection().getDBConnection();
                new EmployeeMainFrame(myCon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}

