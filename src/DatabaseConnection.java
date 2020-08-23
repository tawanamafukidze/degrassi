import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {

	private Connection con;	
	
	public DatabaseConnection() { //Using the method Connect() to connect to the database automatically without the need to use the method in every class
		connect();
	}
	
	public void connect() { //method created to connect to database
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
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connection Successful");
	}
	
	public Connection getConnection(){  //used to return the value of the connection
		return con;
	}
}
