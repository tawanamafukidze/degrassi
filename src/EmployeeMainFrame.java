import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
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

public class EmployeeMainFrame extends JFrame {

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
					EmployeeMainFrame frame = new EmployeeMainFrame(con);
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
	private Connection con;
	private PreparedStatement pst;

	public EmployeeMainFrame(Connection con) {
		this.con = con;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 479, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton backbtn = new JButton();
		backbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MainFrame m = new MainFrame(con);
				m.setVisible(true);
			}
		});

		JFormattedTextField txtAdminID = new JFormattedTextField();
		txtAdminID.setBounds(290, 159, 135, 20);
		contentPane.add(txtAdminID);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(290, 193, 135, 20);
		contentPane.add(txtPassword);
		backbtn.setIcon(new ImageIcon("img\\backbtn3.png"));
		backbtn.setBounds(48, 221, 75, 23);
		contentPane.add(backbtn);

		JButton nxtbtn = new JButton("");
		nxtbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: LOGIN
				String email = txtAdminID.getText();
				String pass = txtPassword.getText();
				Employee employee = new Employee(con, email, pass);

				if (employee.Active()) {
					System.out.println(employee.toString());
				}
			}
		});
		nxtbtn.setIcon(new ImageIcon("img\\nxt4.png"));
		nxtbtn.setBounds(339, 221, 86, 23);
		contentPane.add(nxtbtn);

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
	}
}

