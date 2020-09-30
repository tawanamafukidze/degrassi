package main.java.degrassi.models.user;

import main.java.degrassi.controllers.EmployeeController;

import java.sql.Connection;

public class EmployeeModel extends Person {

    //Fetch the required user from the database
    public EmployeeModel(Connection con, String email, String password) {
        super(con, email, password);
        if (isValidEntry()) {
            EmployeeController.queryEmployee(this, email, password);
            if (getId() == null) return; //no admin with the provided details found
            setActive(true); //user has logged in
        }
    }

    @Override
    public String toString() {
        return super.toString() + String.format("isEmployee: %s", true);
    }
}
