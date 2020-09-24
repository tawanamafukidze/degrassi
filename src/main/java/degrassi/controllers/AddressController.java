package main.java.degrassi.controllers;

import main.java.degrassi.models.AddressModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressController {
    private AddressController() {}

    public static void addNewAddress(AddressModel address, String customer_id) {
        try {
            PreparedStatement pst = address.getDb().prepareStatement(
                    "Insert into Address(State, City, PostalCode, Street, CustomerID) Values(?,?,?,?,?)"
            );
            pst.setString(1, address.getState());
            pst.setString(2, address.getCity());
            pst.setString(3, address.getPostCode());
            pst.setString(4, address.getStreet());
            pst.setString(5, customer_id);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static AddressModel queryAddress(Connection con, String id) {
        String query = String.format("SELECT * FROM Address WHERE CustomerID = '%s'", id);
        try {
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                String street = result.getString("Street");
                String city = result.getString("City");
                String state = result.getString("State");
                String postCode = result.getString("PostalCode");
                return new AddressModel(con, street, state, city, postCode);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
