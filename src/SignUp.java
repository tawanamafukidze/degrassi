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
		
		JFormattedTextField txtFirstName_1 = new JFormattedTextField();
		txtFirstName_1.setBounds(296, 248, 136, 20);
		contentPane.add(txtFirstName_1);
		
		JLabel lblNewLabel_5_1 = new JLabel("");
		lblNewLabel_5_1.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\phonenumber.png"));
		lblNewLabel_5_1.setBounds(27, 254, 117, 14);
		contentPane.add(lblNewLabel_5_1);
		
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
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\backbtn2.png"));
		btnNewButton.setBounds(27, 305, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton realnxtbtn = new JButton("");
		realnxtbtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				try {
					
					String uFirstName = txtFirstName.getText();
					String uSurname = txtSurname.getText();
					String uEmailAddress = txtEmailAddress.getText();
					String uPassword = txtPassword.getText();
					String uRePassword = txtRePassword.getText();
					
					if (!uPassword.equals(uRePassword)) {
						
						JOptionPane.showMessageDialog(txtPassword,"Passwords do not match");
					}
					else {
					pst = con.prepareStatement("Insert into users(FirstName, Surname, EmailAddress,Password) Values(?,?,?,?)");
					pst.setString(1, uFirstName);
					pst.setString(2, uSurname);
					pst.setString(3, uEmailAddress);
					pst.setString(4, uPassword);
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
		realnxtbtn.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\nxt4.png"));
		realnxtbtn.setBounds(352, 305, 79, 23);
		contentPane.add(realnxtbtn);
		
		JLabel lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\heading3.png"));
		lblNewLabel_6.setBounds(49, 11, 376, 46);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblNewLabel_5 = new JLabel("");
		lblNewLabel_5.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\reenterpass.png"));
		lblNewLabel_5.setBounds(27, 218, 161, 14);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\passwordsign.png"));
		lblNewLabel_4.setBounds(27, 186, 112, 14);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\surname.png"));
		lblNewLabel_3.setBounds(27, 106, 79, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\firstname.png"));
		lblNewLabel_2.setBounds(27, 68, 112, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\addy.png"));
		lblNewLabel_1.setBounds(27, 137, 123, 22);
		contentPane.add(lblNewLabel_1);
		
		JLabel nextbtn = new JLabel("");
		nextbtn.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\ps4 wall.png"));
		nextbtn.setBounds(10, -34, 467, 414);
		contentPane.add(nextbtn);
		
		JFormattedTextField txtEmailAddress_1 = new JFormattedTextField();
		txtEmailAddress_1.setBounds(296, 218, 136, 20);
		contentPane.add(txtEmailAddress_1);
		
		JFormattedTextField txtSurname_1 = new JFormattedTextField();
		txtSurname_1.setBounds(295, 218, 136, 20);
		contentPane.add(txtSurname_1);
	}
}
