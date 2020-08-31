import javax.print.DocFlavor;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private final Connection db;
    private boolean isValidEntry = false;
    private boolean isActive = false;
    private boolean isEmployee;

    public Person(Connection con, String firstName, String lastName, String email, String password, String phone) {
        db = con; //get the mysql connection instance
        if (FieldLengthInvalid(firstName, 1, 30)) {
            JOptionPane.showMessageDialog(null,
                    "Provided an invalid first name. \n" +
                            " First Name must be at least 1 character long and not more than 30 characters long.");
            return;
        }

        if (FieldLengthInvalid(lastName, 1, 50)) {
            JOptionPane.showMessageDialog(null,
                    "Provided an invalid last name. \n" +
                            " First Name must be at least 1 character long and not more than 50 characters long.");
            return;
        }

        if (emailInvalid(email)) {
            JOptionPane.showMessageDialog(null, "Provided an invalid email address. \n");
            return;
        }

        if (!emailUnique(email)) {
            JOptionPane.showMessageDialog(null, "Email address provided is already in use.");
            return;
        }

        if (FieldLengthInvalid(password, 1, 120)) {
            JOptionPane.showMessageDialog(null,
                    "Password must be at least 1 character long.");
            return;
        }

        if (phoneNumberInvalid(phone)) {
            JOptionPane.showMessageDialog(null, "Provided an invalid phone number. \n" +
                    "Phone number cannot contain non digit characters.");
            return;
        }

        if (FieldLengthInvalid(phone, 10, 10)) {
            if (phone.length() < 10) {
                JOptionPane.showMessageDialog(null,
                        "Phone number provided is too short.");
            } else {
                JOptionPane.showMessageDialog(null, "Phone number provided is too long.");
            }
            return;
        }

        isValidEntry = true;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public Person(Connection con, String email, String password) {
        db = con;

        //check if email is correctly formatted
        if (emailInvalid(email)) {
            //Email is Not Valid.
            JOptionPane.showMessageDialog(null, "Provided an invalid email address. \n");
            return;
        }

        if (FieldLengthInvalid(password, 1, 120)) {
            JOptionPane.showMessageDialog(null,
                    "Password must be at least 1 character long.");
            return;
        }
        isValidEntry = true;
    }

    public Person(Connection con) {
        db = con;
    }

    //checks if provided field is within the input limit and returns true, otherwise false.
    private boolean FieldLengthInvalid(String field, int minLength, int maxLength) {
        int fieldLength = field.length();
        return fieldLength < minLength || fieldLength > maxLength;
    }

    private boolean emailInvalid(String email) {
        if (FieldLengthInvalid(email, 3, 60)) return true;
        return !email.matches("[\\w+.-]+@\\w+.[\\w]+");
    }

    private boolean emailUnique(String email) {
        try {
            String query = String.format("SELECT * FROM Customers WHERE EmailAddress = '%s'", email);
            PreparedStatement p = db.prepareCall(query);
            ResultSet result = p.executeQuery();
            if (result.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isValidEntry() {
        return isValidEntry;
    }

    private boolean phoneNumberInvalid(String phone) {
        if (phone == null) return true;
        try {
            Integer.parseInt(phone);
            return false;
        } catch (NumberFormatException ignored) {
            return true;
        }
    }

    protected Connection dbConnection() {
        return db;
    }

    public boolean checkPassword(String userPass) {
        return password.equals(userPass);
    }

    protected void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }

    protected void setPhone(String phone) {
        this.phone = phone;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    protected String getPassword() {
        return password;
    }

    protected String getEmail() {
        return email;
    }

    protected String getLastName() {
        return lastName;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected String getId() {
        return id;
    }

    protected String getFirstName() {
        return firstName;
    }

    protected void setActive(boolean b) {
        isActive = b;
    }

    protected boolean Active() {
        return isActive;
    }

    public boolean isEmployee() {
        return isEmployee;
    }

    public void setEmployee(boolean employee) {
        isEmployee = employee;
    }

    @Override
    public String toString() {
        return String.format("id: %s \n" +
                "firstName: %s \n" +
                "lastName: %s \n" +
                "email: %s \n" +
                "phone: %s \n" +
                "isActive: %s \n" +
                "isEmployee: %s \n", id, firstName, lastName, email, phone, isActive, isEmployee
        );
    }
}
