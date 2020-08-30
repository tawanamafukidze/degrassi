
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JTabbedPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import java.awt.Panel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.mysql.jdbc.ResultSetMetaData;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.AbstractListModel;
import javax.swing.JScrollBar;

public class AdminSearch extends JFrame {

	Connection con = new DatabaseConnection().getConnection();
	PreparedStatement pst;

	private JPanel contentPane;
	private JTable tblProducts;

	DefaultTableModel tbl;
	DefaultTableModel utbl;
	DefaultListModel<String> searchList;

	String Arr[];
	int size = 0;
	private JTable Customers;

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

	public void loadGames() {

		try {
			int a;
			pst = con.prepareStatement("select * from games");

			ResultSet rs = pst.executeQuery();

			ResultSetMetaData rd = (ResultSetMetaData) rs.getMetaData();

			a = rd.getColumnCount();

			tbl = (DefaultTableModel) tblProducts.getModel();
			tbl.setRowCount(0);

			while (rs.next()) {
				Vector v2 = new Vector();
				v2.add(rs.getInt("GameID"));
				v2.add(rs.getString("Title"));
				v2.add(rs.getString("Type"));
				v2.add(rs.getString("Stock"));
				v2.add(rs.getString("Price"));
				System.out.println(v2.toString());
				tbl.addRow(v2.toArray());
			}
		} catch (SQLException ex) {
			Logger.getLogger(AdminSearch.class.getName()).isLoggable(Level.SEVERE);
		}
	}

	public void LoadUser() {

		try {
			int a;
			pst = con.prepareStatement("select * from customers");
			Customer customer;
			ResultSet rs = pst.executeQuery();

			ResultSetMetaData rd = (ResultSetMetaData) rs.getMetaData();

			a = rd.getColumnCount();
			utbl = (DefaultTableModel) Customers.getModel();
			utbl.setRowCount(0);

			while (rs.next()) {
				Vector v2 = new Vector();
				String customer_id = rs.getString("customerID");
				customer = new Customer(con);
				customer.queryCustomer(customer_id);
				
				try {
				
				v2.add(customer_id);
				v2.add(customer.getFirstName());
				v2.add(customer.getLastName());
				v2.add(customer.getEmail());
				v2.add(customer.getPhone());

				String address = customer.getCustomerAddress().getStreet() + " " +
						customer.getCustomerAddress().getCity() + " " + customer.getCustomerAddress().getState() + " " +
						customer.getCustomerAddress().getPostCode();
				v2.add(address);
				} catch (NullPointerException ignored) {}
		
				utbl.addRow(v2);
			}
		} catch (SQLException ex) {
			Logger.getLogger(AdminSearch.class.getName()).isLoggable(Level.SEVERE);
		}
	}

	private void BindData() {
		inputArray().stream().forEach((game) -> {
			System.out.println(game);
			searchList.addElement(game);
		});
	}

	public ArrayList<String> inputArray() {
		// TODO Auto-generated method stub
		try {

			pst = con.prepareStatement("Select * from games");
			ResultSet rs = pst.executeQuery();
			// System.out.println(rs.getString("Title"));

			ArrayList<String> arr = new ArrayList<>();
			while (rs.next()) {
				String title = rs.getString("Title");
				arr.add(title);
			}

			return arr;
		} catch (Exception e) {
			System.out.println("Data not found");
		}
		return null;
	}

	public AdminSearch() {
		
		
		
	
		searchList = new DefaultListModel<>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 707, 519);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		BindData();
		
		JPanel Update = new JPanel();
		tabbedPane.addTab("", new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\Stockkk.png"), Update, null);
		tabbedPane.setBackgroundAt(0, Color.BLACK);
		Update.setLayout(null);
		
		JFormattedTextField txtSearchStock = new JFormattedTextField();
		txtSearchStock.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				DefaultTableModel filteredTable = (DefaultTableModel)tblProducts.getModel();
				String search = "(?i)"+"^"+txtSearchStock.getText();
				System.out.println(search);
				TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(filteredTable);
				tblProducts.setRowSorter(tr);
				tr.setRowFilter(RowFilter.regexFilter(search));
				System.out.println(tr.getRowFilter());
				
			}
		});
		
		JTextPane txtTitle = new JTextPane();
		txtTitle.setBounds(101, 132, 86, 23);
		Update.add(txtTitle);
		
		JTextPane txtPrice = new JTextPane();
		txtPrice.setBounds(101, 297, 86, 14);
		Update.add(txtPrice);
		
		JTextPane txtStock = new JTextPane();
		txtStock.setBounds(101, 245, 86, 14);
		Update.add(txtStock);
		
		JTextPane txtType = new JTextPane();
		txtType.setBounds(101, 184, 86, 23);
		Update.add(txtType);
		
		JButton addbtn = new JButton("add");
		
		
		addbtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				try {
					
					
					String Title = txtTitle.getText();
					String Type = txtType.getText();
					int Stock = Integer.parseInt(txtStock.getText());
				    Double Price = Double.parseDouble(txtPrice.getText());
					
					pst = con.prepareStatement("Insert into games( Title, Type, Stock, Price) Values(?,?,?,?)");
					
					pst.setString(1, Title);
					pst.setString(2, Type);
					pst.setInt(3, Stock);
					pst.setDouble(4, Price);
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(Update, "New Game Added");
					
					loadGames();
					txtTitle.setText("");
					txtType.setText("");
					txtStock.setText("");
					txtPrice.setText("");
					txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
					
					
					
				//}
				}catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
		});
		
		
		addbtn.setBounds(21, 359, 67, 23);
		Update.add(addbtn);
		txtSearchStock.setBounds(302, 105, 270, 23);
		Update.add(txtSearchStock);
		
		JLabel StockSeachLabel = new JLabel("New label");
		StockSeachLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\search.png"));
		StockSeachLabel.setBounds(208, 104, 77, 23);
		Update.add(StockSeachLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(208, 138, 458, 173);
		Update.add(scrollPane);
		
		tblProducts = new JTable();
		tblProducts.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				tbl = (DefaultTableModel)tblProducts.getModel();
				int selected = tblProducts.getSelectedRow();
				
				int id = Integer.parseInt((tbl.getValueAt(selected, 0).toString()));
			    
			    txtTitle.setText(tbl.getValueAt(selected, 1).toString());
			    txtType.setText(tbl.getValueAt(selected, 2).toString());
			    txtStock.setText(tbl.getValueAt(selected, 3).toString());
			    txtPrice.setText(tbl.getValueAt(selected, 4).toString());
			    
			    addbtn.setEnabled(false);
			}
		});
		
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
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblProducts.getColumnModel().getColumn(0).setPreferredWidth(29);
		scrollPane.setViewportView(tblProducts);
		
		JLabel PriceLabel = new JLabel("");
		PriceLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\price.png"));
		PriceLabel.setBounds(21, 297, 46, 14);
		Update.add(PriceLabel);
		
		JLabel StockLabel = new JLabel("");
		StockLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\stock.png"));
		StockLabel.setBounds(21, 245, 56, 14);
		Update.add(StockLabel);
		
		JLabel TypeLabel = new JLabel("");
		TypeLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\type.png"));
		TypeLabel.setBounds(21, 193, 46, 14);
		Update.add(TypeLabel);
		
		JLabel TitleLabel = new JLabel("");
		TitleLabel.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\title.png"));
		TitleLabel.setBounds(21, 141, 46, 14);
		Update.add(TitleLabel);
		
		JLabel degrassi = new JLabel("");
		degrassi.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\heading3.png"));
		degrassi.setBounds(170, 11, 371, 54);
		Update.add(degrassi);
		
		JButton editbtn = new JButton("edit");
		editbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tbl = (DefaultTableModel)tblProducts.getModel();
				int selected = tblProducts.getSelectedRow();
			    
				int id = Integer.parseInt((tbl.getValueAt(selected, 0).toString()));
			    String Title = txtTitle.getText();
				String Type = txtType.getText();
				int Stock = Integer.parseInt(txtStock.getText());
			    Double Price = Double.parseDouble(txtPrice.getText());
				
				try {
					pst = con.prepareStatement("UPDATE games set Title = ?, Type = ?, Stock = ?, Price = ? where GameID = ?");
				
				
				pst.setString(1, Title);
				pst.setString(2, Type);
				pst.setInt(3, Stock);
				pst.setDouble(4, Price);
				pst.setInt(5, id);
				
				pst.executeUpdate();
				
				
				JOptionPane.showMessageDialog(Update, "Game Info Updated");
				
				txtTitle.setText("");
				txtType.setText("");
				txtStock.setText("");
				txtPrice.setText("");
				txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
				loadGames();
				addbtn.setEnabled(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
		});
		
		JButton deletebtn = new JButton("delete");
		deletebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				tbl = (DefaultTableModel)tblProducts.getModel();
				int selected = tblProducts.getSelectedRow();
			    
				int id = Integer.parseInt((tbl.getValueAt(selected, 0).toString()));
			    
			    
				
				try {
					pst = con.prepareStatement("delete from games where GameID = ?");
				
				
				
				pst.setInt(1, id);
				pst.executeUpdate();
				
				
				JOptionPane.showMessageDialog(Update, "Game Deleted");
				
				txtTitle.setText("");
				txtType.setText("");
				txtStock.setText("");
				txtPrice.setText("");
				txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
				loadGames();
				addbtn.setEnabled(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		deletebtn.setBounds(196, 359, 89, 23);
		Update.add(deletebtn);
				
		editbtn.setBounds(101, 359, 77, 23);
		Update.add(editbtn);
		
		JButton cancelbtn = new JButton("cancel");
		cancelbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				txtTitle.setText("");
				txtType.setText("");
				txtStock.setText("");
				txtPrice.setText("");
				txtTitle.requestFocus(); //Returns Cursor to Initial TextField "Title" at the top of the window
				
				addbtn.setEnabled(true);
				
				
				
				
			}
		});
		cancelbtn.setBounds(302, 359, 89, 23);
		Update.add(cancelbtn);
		
		JLabel BackgroundUpdate = new JLabel("");
		BackgroundUpdate.setBounds(-290, 5, 1023, 543);
		BackgroundUpdate.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\ps4 wall.png"));
		Update.add(BackgroundUpdate);
		
		JPanel CustomerInfo = new JPanel();
		tabbedPane.addTab("", new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\Customers.png"), CustomerInfo, null);
		tabbedPane.setBackgroundAt(1, Color.BLACK);
		CustomerInfo.setLayout(null);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(28, 142, 624, 289);
		CustomerInfo.add(scrollPane_3);
		
		Customers = new JTable();
		Customers.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, "", null, null},
			},
			new String[] {
				"ID", "FirstName", "Surname", "EmailAddress", "Phone Number", "Address"
			}
		));
		Customers.getColumnModel().getColumn(0).setPreferredWidth(27);
		Customers.getColumnModel().getColumn(1).setPreferredWidth(97);
		Customers.getColumnModel().getColumn(2).setPreferredWidth(97);
		Customers.getColumnModel().getColumn(3).setPreferredWidth(100);
		Customers.getColumnModel().getColumn(4).setPreferredWidth(91);
		Customers.getColumnModel().getColumn(5).setPreferredWidth(210);
		scrollPane_3.setViewportView(Customers);
		
		JFormattedTextField txtSearchCustomers = new JFormattedTextField();
		txtSearchCustomers.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				DefaultTableModel filteredTable = (DefaultTableModel)Customers.getModel();
				String search = "(?i)"+"^"+txtSearchCustomers.getText();
				System.out.println(search);
				TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(filteredTable);
				Customers.setRowSorter(tr);
				tr.setRowFilter(RowFilter.regexFilter(search));
				System.out.println(tr.getRowFilter());
				
				
				
			}
		});
		txtSearchCustomers.setBounds(116, 111, 536, 20);
		CustomerInfo.add(txtSearchCustomers);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\search.png"));
		lblNewLabel_1.setBounds(28, 116, 91, 14);
		CustomerInfo.add(lblNewLabel_1);
		
		JLabel degrassiSearchCustomers = new JLabel("");
		degrassiSearchCustomers.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\heading3.png"));
		degrassiSearchCustomers.setBounds(128, 48, 414, 52);
		CustomerInfo.add(degrassiSearchCustomers);
		
		JLabel SearchCustomersBackground = new JLabel("New label");
		SearchCustomersBackground.setIcon(new ImageIcon("C:\\Users\\tawan\\git\\DEGRASSI_repo\\DEGRASSI\\img\\ps4 wall.png"));
		SearchCustomersBackground.setBounds(-194, 23, 880, 442);
		CustomerInfo.add(SearchCustomersBackground);
		
		loadGames();
		LoadUser();
	}
}
