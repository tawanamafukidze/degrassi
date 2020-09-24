package main.java.degrassi.controllers;

import main.java.degrassi.models.user.EmployeeModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeController {
    private EmployeeController() {
    }

    public static void queryEmployee(EmployeeModel employee, String email, String password) {
        String query = String.format(
                "SELECT * FROM Admin WHERE EmailAddress = '%s' and Password = '%s'", email, password
        );

        PreparedStatement pst;
        ResultSet result;
        try {
            pst = employee.dbConnection().prepareStatement(query);
            result = pst.executeQuery();
            if (result.next()) {
                //store to customer instance
                String firstName = result.getString("FirstName");
                String lastName = result.getString("Surname");
                String phone = result.getString("PhoneNumber");
                employee.setId(result.getString("AdminID"));
                employee.setFirstName(firstName);
                employee.setLastName(lastName);
                employee.setEmail(email);
                employee.setPassword(password);
                employee.setPhone(phone);
                //user has logged in
                employee.setActive(true);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
