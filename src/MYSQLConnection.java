import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MYSQLConnection {

	private Connection con;	
	
	public MYSQLConnection() { //Using the method Connect() to connect to the database automatically without the need to use the method in every class
		connect();
	}
	
	public void connect() { //method created to connect to database
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				try {
					con = DriverManager.getConnection(
							"jdbc:mysql://192.168.1.48/degrassi", "root", "Aaronstone07"
					);
				} catch (SQLException ignored) {
					con = DriverManager.getConnection(
							"jdbc:mysql://localhost/degrassi", "root", "Aaronstone07"
					);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(
						new MainFrame(con), "Server Error: Could Not Connect TO MYSQL DATABASE"
				);
			}
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(new MainFrame(con), "Dependency Error: MYSQL Connector Not Found!");
		}
		JOptionPane.showMessageDialog(new MainFrame(con), "Status: Connection Successful");
		System.out.println("Connection Successful");
	}
	
	public Connection getDBConnection(){  //used to return the value of the connection
		return con;
	}
}
