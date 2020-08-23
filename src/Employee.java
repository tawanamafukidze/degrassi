import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee extends Person {

    //Creates a new user instance and verifies the information provided by the client before committing to the the DB
    public Employee(Connection con, String firstName, String lastName, String email, String password, String phone) {
        super(con, firstName, lastName, email, password, phone);
        setEmployee(true);
    }

    //Fetch the required user from the database
    public Employee(Connection con, String email, String password) {
        super(con, email, password);
        setEmployee(true);
        if (isValidEntry()) {
            queryEmployee(email, password);
        }
    }

    public String db_commit() {
        try {
            PreparedStatement pst = dbConnection().prepareStatement(
                    "Insert into Admin(FirstName, Surname, EmailAddress,Password,PhoneNumber) Values(?,?,?,?,?)"
            );
            pst.setString(1, getFirstName());
            pst.setString(2, getLastName());
            pst.setString(3, getEmail());
            pst.setString(4, getPassword());
            pst.setString(5, getPhone());
            pst.executeUpdate();

            String query = String.format("SELECT * FROM Admin Where EmailAddress = '%s'", getEmail());
            pst = dbConnection().prepareStatement(query);
            ResultSet result = pst.executeQuery();

            if (result.next()) {
                return result.getString("CustomerID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void queryEmployee(String email, String password) {
        String query = String.format(
                "SELECT * FROM Admin WHERE EmailAddress = '%s' and Password = '%s'", email, password
        );
        try {
            PreparedStatement pst = dbConnection().prepareStatement(query);
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
            } else {
                JOptionPane.showMessageDialog(this,
                        "User Not Found: Please check the email and password provided."
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
