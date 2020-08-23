import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer extends Person {
    private Address customerAddress;

    //Creates a new user instance and verifies the information provided by the client before committing to the the DB
    public Customer(Connection con, String firstName, String lastName, String email, String password, String phone) {
        super(con, firstName, lastName, email, password, phone);
        setEmployee(false);
    }

    //Fetch the required user from the database
    public Customer(Connection con, String email, String password) {
        super(con, email, password);
        setEmployee(false);
        if (isValidEntry()) {
            queryCustomer(email, password);
        }
    }


    public String db_commit() {
        try {
            PreparedStatement
                    pst = dbConnection().prepareStatement("Insert into Customers(FirstName, Surname, EmailAddress,Password,PhoneNumber) Values(?,?,?,?,?)");
            pst.setString(1, getFirstName());
            pst.setString(2, getLastName());
            pst.setString(3, getEmail());
            pst.setString(4, getPassword());
            pst.setString(5, getPhone());
            pst.executeUpdate();

            String query = String.format("SELECT * FROM Customers Where EmailAddress = '%s'", getEmail());
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

    public void queryCustomer(String email, String password) {
        String query = String.format(
                "SELECT * FROM Customers WHERE EmailAddress = '%s' and Password = '%s'", email, password
        );
        try {
            PreparedStatement pst = dbConnection().prepareStatement(query);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                //store to customer instance
                String firstName = result.getString("FirstName");
                String lastName = result.getString("Surname");
                String phone = result.getString("PhoneNumber");
                setId(result.getString("CustomerID"));
                setFirstName(firstName);
                setLastName(lastName);
                setEmail(email);
                setPassword(password);
                setPhone(phone);
                String id = getId();
                customerAddress = new Address(dbConnection()).queryAddress(id);
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

    public void emptyCart() {
        //TODO: Empty cart
    }

    //public void updateCart(Item[] items) {
    //    for (Item item : items) {
    //        //TODO: add to items table
    //    }
    //}

    public void purchase() {
        //TODO: Empty cart
    }

    public String getCustomerAddress() {
        try {
            return customerAddress.toString();
        } catch (NullPointerException ignored) { }
        return "";
    }

    public static void main(String[] args) {
    }
}