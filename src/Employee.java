import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee extends Person {

    //Creates a new user instance and verifies the information provided by the client before committing to the the DB
    //public Employee(getgetDBConnection con, String firstName, String lastName, String email, String password, String phone) {
    //    super(con, firstName, lastName, email, password, phone);
    //    setEmployee(true);
    //}

    //Fetch the required user from the database
    public Employee(Connection con, String email, String password) {
        super(con, email, password);
        setEmployee(true);
        if (isValidEntry()) {
            queryEmployee(email, password);
        }
    }

    public void queryEmployee(String email, String password) {
        String query = String.format(
                "SELECT * FROM Admin WHERE EmailAddress = '%s' and Password = '%s'", email, password
        );
        try {
            PreparedStatement pst = getDBConnection().prepareStatement(query);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                //store to customer instance
                String firstName = result.getString("FirstName");
                String lastName = result.getString("Surname");
                String phone = result.getString("PhoneNumber");
                setId(result.getString("AdminID"));
                setFirstName(firstName);
                setLastName(lastName);
                setEmail(email);
                setPassword(password);
                setPhone(phone);

                //user has logged in
                setActive(true);
            }
            //user not logged in
            if (!Active()) {
                JOptionPane.showMessageDialog(null,
                        "User Not Found: Please check the email and password provided."
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean addInventory(String title, String type, int stock, double Price) throws SQLException {
        PreparedStatement pst = getDBConnection().prepareStatement(
                "Insert into games( Title, Type, stock, Price) Values(?,?,?,?)"
        );
        pst.setString(1, title);
        pst.setString(2, type);
        pst.setInt(3, stock);
        pst.setDouble(4, Price);
        pst.executeUpdate();
        return true;
    }
}
