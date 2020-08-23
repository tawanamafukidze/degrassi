import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JTabbedPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import java.awt.Panel;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.*;
import com.mysql.jdbc.Driver;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
/*
public class AdminSearch extends JFrame {
	
	private JPanel contentPane;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminSearch frame = new AdminSearch();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Connection con;

	PreparedStatement pst;
	DefaultTableModel tbl;
	DefaultListModel dl;
	
	
	private JTable tblProducts;
	
	public void Connect() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				try {
					con = DriverManager.getConnection("jdbc:mysql://192.168.137.153/degrassi", "root", "Aaronstone07"); 
				} catch (SQLException ignored) {
					con = DriverManager.getConnection("jdbc:mysql://localhost/degrassi", "root", "Aaronstone07"); 
					//using an installed MYSQL connector
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Logger.getLogger(AdminSearch.class.getName()).isLoggable(Level.SEVERE);
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(AdminSearch.class.getName()).isLoggable(Level.SEVERE);
			e.printStackTrace();
		}
		System.out.println("Connection Successful");
	}
	
	
	
public void load() {
		
		int a;
		try {
			pst = con.prepareStatement("select * from games");
			
			ResultSet rs = pst.executeQuery();
			
			
			ResultSetMetaData rd = (ResultSetMetaData) rs.getMetaData();
			
			a = rd.getColumnCount();
			
			tbl = (DefaultTableModel)tblProducts.getModel();
			tbl.setRowCount(0);
			
			while (rs.next()) {
				Vector v2 = new Vector();
				
				for(int i = 1; i <= a; i++) {
					v2.add(rs.getInt("GameID"));
					v2.add(rs.getString("Title"));
					v2.add(rs.getString("Type"));
					v2.add(rs.getInt("Stock"));
					v2.add(rs.getDouble("Price"));
				}
				
				tbl.addRow(v2);
				
				
			}
			
			
		}catch (SQLException ex) {
			Logger.getLogger(AdminSearch.class.getName()).isLoggable(Level.SEVERE);
		}
	}

public void loadlist() {
	
	dl.addElement("Title");
	GameList.setModel(dl);
	
	
		
	
}
	public AdminSearch() {
	con = new MYSQLConnection().getConnection();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 707, 519);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel Search = new JPanel();
		tabbedPane.addTab("", new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\searchmall.png"), Search, null);
		tabbedPane.setBackgroundAt(0, Color.BLACK);
		Search.setLayout(null);
		
		JList GameList = new JList();
		GameList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
		});
		GameList.setBounds(116, 108, 435, 545);
		Search.add(GameList);
		
		JLabel degrassii = new JLabel("");
		degrassii.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\heading3.png"));
		degrassii.setBounds(147, 29, 368, 49);
		Search.add(degrassii);
		
		JLabel SearchLabel = new JLabel("");
		SearchLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\search.png"));
		SearchLabel.setBounds(32, 92, 85, 14);
		Search.add(SearchLabel);
		
		JFormattedTextField SearchBar = new JFormattedTextField();
		SearchBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		SearchBar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		SearchBar.setBounds(118, 89, 433, 20);
		Search.add(SearchBar);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\ps4 wall.png"));
		lblNewLabel_3.setBounds(-183, -39, 915, 634);
		Search.add(lblNewLabel_3);
		
		JPanel Update = new JPanel();
		tabbedPane.addTab("", new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\Update.png"), Update, null);
		tabbedPane.setBackgroundAt(1, Color.BLACK);
		Update.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(208, 105, 458, 326);
		Update.add(scrollPane);
		
		
		
		tblProducts = new JTable();
		
		
		tblProducts.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"ID", "Title", "Type", "Stock", "Price"
			}
		));
		scrollPane.setViewportView(tblProducts);
		
		JTextPane txtTitle = new JTextPane();
		txtTitle.setBounds(101, 105, 86, 14);
		Update.add(txtTitle);
		
		JTextPane txtStock = new JTextPane();
		txtStock.setBounds(101, 209, 86, 14);
		Update.add(txtStock);
		
		JTextPane txtType = new JTextPane();
		txtType.setBounds(101, 157, 86, 14);
		Update.add(txtType);
		
		JTextPane txtPrice = new JTextPane();
		txtPrice.setBounds(101, 261, 86, 14);
		Update.add(txtPrice);
		
		JLabel PriceLabel = new JLabel("");
		PriceLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\price.png"));
		PriceLabel.setBounds(21, 261, 46, 14);
		Update.add(PriceLabel);
		
		JLabel StockLabel = new JLabel("");
		StockLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\stock.png"));
		StockLabel.setBounds(21, 209, 56, 14);
		Update.add(StockLabel);
		
		JLabel TypeLabel = new JLabel("");
		TypeLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\type.png"));
		TypeLabel.setBounds(21, 157, 46, 14);
		Update.add(TypeLabel);
		
		JLabel TitleLabel = new JLabel("");
		TitleLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\title.png"));
		TitleLabel.setBounds(21, 105, 46, 14);
		Update.add(TitleLabel);
		
		JLabel degrassi = new JLabel("");
		degrassi.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\heading3.png"));
		degrassi.setBounds(172, 27, 371, 54);
		Update.add(degrassi);
		
		JButton addbtn = new JButton("add");
		addbtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				try {
					
					//int GameID = Integer.parseInt(txtGameID.getText());
					String Title = txtTitle.getText();
					String Type = txtType.getText();
					int Stock = Integer.parseInt(txtStock.getText());
				    Double Price = Double.parseDouble(txtPrice.getText());
					
					pst = con.prepareStatement("Insert into games( Title, Type, Stock, Price) Values(?,?,?,?)");
					//pst.setInt(1, GameID);
					pst.setString(1, Title);
					pst.setString(2, Type);
					pst.setInt(3, Stock);
					pst.setDouble(4, Price);
					pst.executeUpdate();
					
					//txtGameID.setText("");
					txtTitle.setText("");
					txtType.setText("");
					txtStock.setText("");
					txtPrice.setText("");
					txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
					
					dispose();
					AdminSearch refresh = new AdminSearch();
					refresh.setVisible(true);
					
				//}
				}catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
		});
		addbtn.setBounds(21, 359, 67, 23);
		Update.add(addbtn);
		
		JButton editbtn = new JButton("edit");
		editbtn.setBounds(101, 359, 77, 23);
		Update.add(editbtn);
		
		JLabel BackgroundUpdate = new JLabel("");
		BackgroundUpdate.setBounds(-290, 5, 1023, 543);
		BackgroundUpdate.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\ps4 wall.png"));
		Update.add(BackgroundUpdate);
		
		load();
		loadlist();
		}
			

				
	}
 */
