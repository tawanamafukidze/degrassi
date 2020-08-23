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

	
	/**
	 * Create the frame.
	 */
	PreparedStatement pst;
	Connection con;
	
	public SignUp() {
		
		con = new DatabaseConnection().getConnection();
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 592, 665);
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
		
		JFormattedTextField txtPostalCode = new JFormattedTextField();
		txtPostalCode.setBounds(296, 469, 136, 22);
		contentPane.add(txtPostalCode);
		
		JFormattedTextField txtStreet = new JFormattedTextField();
		txtStreet.setBounds(296, 424, 136, 22);
		contentPane.add(txtStreet);
		
		JFormattedTextField txtCity = new JFormattedTextField();
		txtCity.setBounds(296, 379, 136, 22);
		contentPane.add(txtCity);
		
		JFormattedTextField txtState = new JFormattedTextField();
		txtState.setBounds(296, 334, 136, 22);
		contentPane.add(txtState);
		
		JLabel PostalCodeLabel = new JLabel("");
		PostalCodeLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\postalcode.png"));
		PostalCodeLabel.setBounds(28, 469, 102, 22);
		contentPane.add(PostalCodeLabel);
		
		JLabel StreetLabel = new JLabel("");
		StreetLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\Street.png"));
		StreetLabel.setBounds(28, 424, 79, 22);
		contentPane.add(StreetLabel);
		
		JLabel CityLabel = new JLabel("");
		CityLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\city.png"));
		CityLabel.setBounds(28, 379, 46, 22);
		contentPane.add(CityLabel);
		
		JLabel StateCodeLabel = new JLabel("New label");
		StateCodeLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\statecode2.png"));
		StateCodeLabel.setBounds(28, 334, 102, 22);
		contentPane.add(StateCodeLabel);
		
		JFormattedTextField txtPhoneNumber = new JFormattedTextField();
		txtPhoneNumber.setBounds(296, 289, 136, 22);
		contentPane.add(txtPhoneNumber);
		
		JLabel PhoneNumberLabel = new JLabel("");
		PhoneNumberLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\phonenumber.png"));
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
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\backbtn2.png"));
		btnNewButton.setBounds(28, 521, 89, 23);
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
					
				//	if (!Password.equals(RePassword)) {
						
				//		JOptionPane.showMessageDialog(txtPassword,"Passwords do not match");
					//}
				//else {
					pst = con.prepareStatement("Insert into customers(FirstName, Surname, EmailAddress,Password,PhoneNumber) Values(?,?,?,?,?)");
					pst.setString(1, FirstName);
					pst.setString(2, Surname);
					pst.setString(3, EmailAddress);
					pst.setString(4, Password);
					pst.setString(5, PhoneNumber);
					pst.executeUpdate();
					
					String State = txtState.getText();
					String City = txtCity.getText();
					String PostalCode = txtPostalCode.getText();
					String Street = txtStreet.getText();
					
					
					pst = con.prepareStatement("Insert into address(State, City, PostalCode, Street) Values(?,?,?,?)");
					pst.setString(1, State);
					pst.setString(2, City);
					pst.setString(3, PostalCode);
					pst.setString(4, Street);
					pst.executeUpdate();
					
					txtFirstName.setText("");
					txtSurname.setText("");
					txtEmailAddress.setText("");
					txtPhoneNumber.setText("");
					txtPassword.setText("");
					
					txtState.setText("");
					txtCity.setText("");
					txtStreet.setText("");
					txtPostalCode.setText("");
					txtEmailAddress.requestFocus(); //Returns Cursor to Initial TextField "Email Address" at the top of the window
					
					dispose();
					ClientMainFrame c =  new ClientMainFrame();
					c.setVisible(true);
					
				//}
				}catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			
					
				
				
			}
		});
		realnxtbtn.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\nxt4.png"));
		realnxtbtn.setBounds(353, 514, 79, 23);
		contentPane.add(realnxtbtn);
		
		JLabel degrassi = new JLabel("");
		degrassi.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\heading3.png"));
		degrassi.setBounds(49, 11, 376, 46);
		contentPane.add(degrassi);
		
		JLabel ReEnterLabel = new JLabel("");
		ReEnterLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\reenterpass.png"));
		ReEnterLabel.setBounds(28, 244, 161, 22);
		contentPane.add(ReEnterLabel);
		
		JLabel PasswordLabel = new JLabel("");
		PasswordLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\passwordsign.png"));
		PasswordLabel.setBounds(28, 199, 112, 22);
		contentPane.add(PasswordLabel);
		
		JLabel SurnameLabel = new JLabel("");
		SurnameLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\surname.png"));
		SurnameLabel.setBounds(28, 154, 79, 22);
		contentPane.add(SurnameLabel);
		
		JLabel FirstNameLabel = new JLabel("");
		FirstNameLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\firstname.png"));
		FirstNameLabel.setBounds(28, 109, 112, 22);
		contentPane.add(FirstNameLabel);
		
		JLabel EmailAddressLabel = new JLabel("");
		EmailAddressLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\addy.png"));
		EmailAddressLabel.setBounds(28, 64, 123, 22);
		contentPane.add(EmailAddressLabel);
		
		JLabel nextbtn = new JLabel("");
		nextbtn.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\degrassi_repo\\DEGRASSI\\img\\ps4 wall.png"));
		nextbtn.setBounds(-19, -21, 659, 600);
		contentPane.add(nextbtn);
	}
}
