import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.sql.Connection;

public class MainFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Connection con = new MYSQLConnection().getDBConnection();
				MainFrame frame = new MainFrame(con);
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	/**
	 * Create the frame.
	 */
	private final Connection con;
	public MainFrame(Connection con) {
		this.con = con;
		getWidgets();
		setVisible(true);
	}

	private void getWidgets() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 479, 301);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(e -> {
			new ClientMainFrame(con);
			dispose();
		});
		btnNewButton_1.setBackground(Color.LIGHT_GRAY);
		btnNewButton_1.setIcon(new ImageIcon("img\\client.png"));
		btnNewButton_1.setBounds(276, 174, 143, 38);
		contentPane.add(btnNewButton_1);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(e -> {
			new EmployeeMainFrame(con);
			dispose();
		});
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setBackground(new Color(0, 0, 0));
		btnNewButton.setIcon(new ImageIcon("img\\admin.png"));
		btnNewButton.setBounds(55, 174, 132, 38);
		contentPane.add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("img\\heading3.png"));
		lblNewLabel_1.setBounds(50, 24, 382, 63);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("img\\resize-1598019257675522578ps4wall.png"));
		lblNewLabel.setBounds(0, 0, 468, 261);
		contentPane.add(lblNewLabel);
	}

}
