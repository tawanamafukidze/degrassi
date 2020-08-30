import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Address {
    private String street;
    private String state;
    private String city;
    private String postCode;
    private Connection db;
    private boolean validEntry = false;

    public Address(Connection con, String street, String state, String city, String postCode) {
        db = con;
        if (FieldLengthInvalid(street, 1, 100)) {
            if (street.length() < 1) {
                JOptionPane.showMessageDialog(new MainFrame(),
                        "Provided an invalid Street. \n" +
                                " First Name must be at least 1 character long and not more than 100 characters long.");
                return;
            }
        }

        if (FieldLengthInvalid(city, 1, 100)) {
            JOptionPane.showMessageDialog(new MainFrame(),
                    "Provided an invalid City. \n" +
                            " First Name must be at least 1 character long and not more than 100 characters long.");
            return;
        }

        if (FieldLengthInvalid(state, 1, 100)) {
            JOptionPane.showMessageDialog(new MainFrame(),
                    "Provided an invalid State. \n" +
                            " First Name must be at least 1 character long and not more than 100 characters long.");
            return;
        }

        if (FieldLengthInvalid(postCode, 3, 5)) {
            JOptionPane.showMessageDialog(new MainFrame(),
                    "Provided an invalid Postal Code. \n" +
                            " First Name must be at least 3 characters long and not more than 5 characters long.");
            return;
        }

        validEntry = true;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postCode = postCode;
    }

    public Address(Connection con) {
        db = con;
    }

    public Address queryAddress(String id) {
        String query = String.format("SELECT * FROM Address WHERE CustomerID = '%s'", id);
        try {
            PreparedStatement pst = db.prepareStatement(query);
            ResultSet result = pst.executeQuery();

            if (result.next()) {
                street = result.getString("Street");
                city = result.getString("City");
                state = result.getString("State");
                postCode = result.getString("PostalCode");
                return this;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void db_commit(String customer_id) {
        if (FieldLengthInvalid(customer_id, 1, Integer.MAX_VALUE)) {
            JOptionPane.showMessageDialog(new MainFrame(),
                    "Internal Error: Address has no Customer ID associated with it."
            );
            return;
        }

        try {
            PreparedStatement pst = db.prepareStatement(
                    "Insert into Address(State, City, PostalCode, Street, CustomerID) Values(?,?,?,?,?)"
            );
            pst.setString(1, state);
            pst.setString(2, city);
            pst.setString(3, postCode);
            pst.setString(4, street);
            pst.setString(5, customer_id);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //checks if provided field is within the input limit and returns true, otherwise false.
    private boolean FieldLengthInvalid(String field, int minLength, int maxLength) {
        if (field == null) {
            return true;
        }
        int fieldLength = field.length();
        return fieldLength < minLength || fieldLength > maxLength;
    }

    public boolean isValidEntry() {
        return validEntry;
    }

    @Override
    public String toString() {
        return String.format("ADDRESS \n" +
                "street: %s \n" +
                "city: %s \n" +
                "state: %s \n" +
                "postCode: %s", street, city, state, postCode);
    }
}
