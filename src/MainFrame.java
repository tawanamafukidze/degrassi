import java.awt.EventQueue;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.sql.Connection;

public class MainFrame extends GUI {
	//Create the frame.
	private final Connection con = new MYSQLConnection().getDBConnection();
	public MainFrame() {
		super();
		setConfig();
		loadWidgets();
		setVisible(true);
	}

	protected void loadWidgets() {
		JButton adminBtn = new JButton("");
		JButton clientBtn = new JButton("");


		clientBtn.addActionListener(e -> {
			new ClientMainFrame(con);
			dispose();
		});
		clientBtn.setBackground(Color.LIGHT_GRAY);
		clientBtn.setIcon(new ImageIcon("img\\client.png"));
		clientBtn.setBounds(276, 174, 143, 38);

		adminBtn.addActionListener(e -> {
			new EmployeeMainFrame(con);
			dispose();
		});
		adminBtn.setForeground(Color.BLACK);
		adminBtn.setBackground(new Color(0, 0, 0));
		adminBtn.setIcon(new ImageIcon("img\\admin.png"));
		adminBtn.setBounds(55, 174, 132, 38);


		contentPane.add(clientBtn);
		contentPane.add(adminBtn);
		contentPane.add(headingLabel);
		contentPane.add(background);
	}

	@Override
	protected void setConfig() {
		super.setConfig();
		setBounds(100, 100, 479, 301);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		background.setBounds(0, 0, 468, 261);
		headingLabel.setBounds(50, 24, 382, 63);
	}

	//Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				MainFrame frame = new MainFrame();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
