package main.java.degrassi.models;

import main.java.degrassi.controllers.AddressController;

import javax.swing.*;
import java.sql.Connection;

public class AddressModel {
    private String street;
    private String state;
    private String city;
    private String postCode;
    private final Connection db;
    private boolean validEntry = false;

    public AddressModel(Connection con, String street, String state, String city, String postCode) {
        db = con;
        if (FieldLengthInvalid(street, 1, 100)) {
            if (street.length() < 1) {
                JOptionPane.showMessageDialog(null,
                        "Provided an invalid Street. \n" +
                                " First Name must be at least 1 character long and not more than 100 characters long.");
                return;
            }
        }

        if (FieldLengthInvalid(city, 1, 100)) {
            JOptionPane.showMessageDialog(null,
                    "Provided an invalid City. \n" +
                            " First Name must be at least 1 character long and not more than 100 characters long.");
            return;
        }

        if (FieldLengthInvalid(state, 1, 100)) {
            JOptionPane.showMessageDialog(null,
                    "Provided an invalid State. \n" +
                            " First Name must be at least 1 character long and not more than 100 characters long.");
            return;
        }

        if (FieldLengthInvalid(postCode, 3, 5)) {
            JOptionPane.showMessageDialog(null,
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

    public AddressModel(Connection con) {
        db = con;
    }

    public AddressModel queryAddress(String id) {
        return AddressController.queryAddress(db, id);
    }

    public void db_commit(String customer_id) {
        if (FieldLengthInvalid(customer_id, 1, Integer.MAX_VALUE)) {
            JOptionPane.showMessageDialog(null,
                    "Internal Error: Address has no Customer ID associated with it."
            );
            return;
        }
        AddressController.commit(this, customer_id);
    }

    //checks if provided field is within the input limit and returns true, otherwise false.
    public boolean isValidEntry() {
        return validEntry;
    }

    public String getStreet() {
        if (street == null) return "N/A";
        return street;
    }

    public String getCity() {
        if (street == null) return  "N/A";
        return city;
    }

    public String getState() {
        if (street == null) return  "N/A";
        return state;
    }

    public String getPostCode() {
        if (postCode == null) return "N/A";
        return postCode;
    }

    public Connection getDb() {
        return db;
    }

    private boolean FieldLengthInvalid(String field, int minLength, int maxLength) {
        if (field == null) {
            return true;
        }
        int fieldLength = field.length();
        return fieldLength < minLength || fieldLength > maxLength;
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
