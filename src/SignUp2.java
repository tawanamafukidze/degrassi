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

public class SignUp2 extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUp2 frame = new SignUp2();
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
	public SignUp2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 491, 280);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ClientMainFrame c = new ClientMainFrame();
				c.setVisible(true);
				
			}
		});
		btnNewButton_1.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\nxt4.png"));
		btnNewButton_1.setBounds(331, 207, 79, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				SignUp s = new SignUp();
				s.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\backbtn2.png"));
		btnNewButton.setBounds(50, 207, 89, 23);
		contentPane.add(btnNewButton);
		
		JFormattedTextField txtStreet = new JFormattedTextField();
		txtStreet.setBounds(311, 141, 99, 20);
		contentPane.add(txtStreet);
		
		JFormattedTextField txtCity = new JFormattedTextField();
		txtCity.setBounds(311, 116, 99, 20);
		contentPane.add(txtCity);
		
		JFormattedTextField txtState = new JFormattedTextField();
		txtState.setBounds(311, 91, 99, 20);
		contentPane.add(txtState);
		
		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\Street.png"));
		lblNewLabel_4.setBounds(50, 147, 80, 14);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\city.png"));
		lblNewLabel_3.setBounds(50, 122, 46, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\heading3.png"));
		lblNewLabel_2.setBounds(50, 11, 360, 42);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\statecode2.png"));
		lblNewLabel_1.setBounds(50, 97, 117, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\Downloads\\resize-1598019257675522578ps4wall.png"));
		lblNewLabel.setBounds(0, -18, 475, 273);
		contentPane.add(lblNewLabel);
	}
}
