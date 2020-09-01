import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MYSQLConnection {
    private Connection con;
    public MYSQLConnection() { //Using the method Connect() to connect to the database automatically without the need to use the method in every class
        connect();
    }

    public void connect() { //method created to connect to database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
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
                        null, "Error: Could Not Connect TO MYSQL DATABASE",
                        "Server getDBConnection", JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Dependency Error: MYSQL Connector (com.mysql.jdbc.Driver) Not Found!",
                    "Dependency Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        System.out.println("getDBConnection Successful");
    }

    public Connection getDBConnection() {  //used to return the value of the connection
        return con;
    }
}
