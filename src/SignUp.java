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
			Class.forName("com.mysql.jdbc.Driver");
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost/degrassi", "root", "Aaronstone07");
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
		setBounds(100, 100, 479, 378);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ClientMainFrame c = new ClientMainFrame();
				c.setVisible(true);
			}
		});
		
		JFormattedTextField txtPhoneNumber = new JFormattedTextField();
		txtPhoneNumber.setBounds(296, 248, 136, 20);
		contentPane.add(txtPhoneNumber);
		
		JLabel PhoneNumberLabel = new JLabel("");
		PhoneNumberLabel.setIcon(new ImageIcon(""img\\phonenumber.png"));
		PhoneNumberLabel.setBounds(27, 254, 117, 14);
		contentPane.add(PhoneNumberLabel);
		
		txtRePassword = new JPasswordField();
		txtRePassword.setBounds(297, 218, 135, 20);
		contentPane.add(txtRePassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(297, 180, 136, 20);
		contentPane.add(txtPassword);
		
		JFormattedTextField txtEmailAddress = new JFormattedTextField();
		txtEmailAddress.setBounds(297, 137, 136, 20);
		contentPane.add(txtEmailAddress);
		
		JFormattedTextField txtSurname = new JFormattedTextField();
		txtSurname.setBounds(297, 100, 136, 20);
		contentPane.add(txtSurname);
		
		JFormattedTextField txtFirstName = new JFormattedTextField();
		txtFirstName.setBounds(296, 63, 136, 20);
		contentPane.add(txtFirstName);
		btnNewButton.setIcon(new ImageIcon(""img\\backbtn2.png"));
		btnNewButton.setBounds(27, 305, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton realnxtbtn = new JButton("");
		realnxtbtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				try {
					
					String FirstName = txtFirstName.getText();
					String Surname = txtSurname.getText();
					String EmailAddress = txtEmailAddress.getText();
					String Password = txtPassword.getText();
					String RePassword = txtRePassword.getText();
					String PhoneNumber = txtPhoneNumber.getText();
					
					if (!Password.equals(RePassword)) {
						
						JOptionPane.showMessageDialog(txtPassword,"Passwords do not match");
					}
				else {
					pst = con.prepareStatement("Insert into Customers(FirstName, Surname, EmailAddress,Password,PhoneNumber) Values(?,?,?,?)");
					pst.setString(1, FirstName);
					pst.setString(2, Surname);
					pst.setString(3, EmailAddress);
					pst.setString(4, Password);
					pst.setString(5, PhoneNumber);
					pst.executeUpdate();
					
					dispose();
					SignUp2 s2 = new SignUp2();
					s2.setVisible(true);
					
				}
				}catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			
					
				
				
			}
		});
		realnxtbtn.setIcon(new ImageIcon(""img\\nxt4.png"));
		realnxtbtn.setBounds(352, 305, 79, 23);
		contentPane.add(realnxtbtn);
		
		JLabel degrassi = new JLabel("");
		degrassi.setIcon(new ImageIcon(""img\\heading3.png"));
		degrassi.setBounds(49, 11, 376, 46);
		contentPane.add(degrassi);
		
		JLabel ReEnterLabel = new JLabel("");
		ReEnterLabel.setIcon(new ImageIcon(""img\\reenterpass.png"));
		ReEnterLabel.setBounds(27, 218, 161, 14);
		contentPane.add(ReEnterLabel);
		
		JLabel PasswordLabel = new JLabel("");
		PasswordLabel.setIcon(new ImageIcon(""img\\passwordsign.png"));
		PasswordLabel.setBounds(27, 186, 112, 14);
		contentPane.add(PasswordLabel);
		
		JLabel SurnameLabel = new JLabel("");
		SurnameLabel.setIcon(new ImageIcon(""img\\surname.png"));
		SurnameLabel.setBounds(27, 106, 79, 14);
		contentPane.add(SurnameLabel);
		
		JLabel FirstNameLabel = new JLabel("");
		FirstNameLabel.setIcon(new ImageIcon(""img\\firstname.png"));
		FirstNameLabel.setBounds(27, 68, 112, 14);
		contentPane.add(FirstNameLabel);
		
		JLabel EmailAddressLabel = new JLabel("");
		EmailAddressLabel.setIcon(new ImageIcon(""img\\addy.png"));
		EmailAddressLabel.setBounds(27, 137, 123, 22);
		contentPane.add(EmailAddressLabel);
		
		JLabel nextbtn = new JLabel("");
		nextbtn.setIcon(new ImageIcon(""img\\ps4 wall.png"));
		nextbtn.setBounds(10, -34, 467, 414);
		contentPane.add(nextbtn);
		
		JFormattedTextField txtReEnterPassword = new JFormattedTextField();
		txtReEnterPassword.setBounds(296, 218, 136, 20);
		contentPane.add(txtReEnterPassword);
	}
}
