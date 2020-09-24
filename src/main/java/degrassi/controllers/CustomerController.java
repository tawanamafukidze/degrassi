package main.java.degrassi.controllers;

import javax.swing.*;

import main.java.degrassi.models.AddressModel;
import main.java.degrassi.models.ShoppingCartModel;
import main.java.degrassi.models.user.CustomerModel;
import main.java.degrassi.mysql.MYSQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerController {
    private CustomerController() {}

    public static String addNewCustomer(CustomerModel customer) {
        try {
            PreparedStatement pst = customer.dbConnection().prepareStatement(
                    "Insert into Customers(FirstName, Surname, EmailAddress,Password,PhoneNumber) " +
                            "Values(?,?,?,?,?)"
            );
            pst.setString(1, customer.getFirstName());
            pst.setString(2, customer.getLastName());
            pst.setString(3, customer.getEmail());
            pst.setString(4, customer.getPassword());
            pst.setString(5, customer.getPhone());
            pst.executeUpdate();

            String query = String.format("SELECT * FROM Customers Where EmailAddress = '%s'", customer.getEmail());
            pst = customer.dbConnection().prepareStatement(query);
            ResultSet result = pst.executeQuery();

            if (result.next()) {
                return result.getString("CustomerID");
            } else {return null;}
        } catch (SQLException e) {
            return null;
        }
    }

    public static ArrayList<String> queryCustomerIDs(Connection con) {
        PreparedStatement pst;
        ArrayList<String> queriedIDs;
        try {
            pst = con.prepareStatement("select customerID from customers");
            ResultSet result = pst.executeQuery();
            queriedIDs = new ArrayList<>();
            while (result.next()) {
                queriedIDs.add(result.getString("customerID"));
            }
            return queriedIDs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static void queryCustomer(CustomerModel customer, String email, String password) {
        String query = String.format(
                "SELECT * FROM Customers WHERE EmailAddress = '%s' and Password = '%s'", email, password
        );
        try {
            PreparedStatement pst = customer.dbConnection().prepareStatement(query);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                //store to customer instance
                String firstName = result.getString("FirstName");
                String lastName = result.getString("Surname");
                String phone = result.getString("PhoneNumber");
                customer.setId(result.getString("CustomerID"));
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setEmail(email);
                customer.setPassword(password);
                customer.setPhone(phone);
            } else {
                JOptionPane.showMessageDialog(null,
                        "User Not Found: Please check the email and password provided."
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static CustomerModel queryCustomerByID(Connection con, String id) {
        String query = String.format(
                "SELECT * FROM Customers WHERE customerID = '%s'", id
        );
        try {
            ResultSet result = con.prepareStatement(query).executeQuery();
            if (result.next()) {
                //store to customer instance
                String firstName = result.getString("FirstName");
                String lastName = result.getString("Surname");
                String phone = result.getString("PhoneNumber");
                String email = result.getString("EmailAddress");

                //Create a customer object with the queried information
                CustomerModel c = new CustomerModel(con);
                c.setId(result.getString("CustomerID"));
                c.setFirstName(firstName);
                c.setLastName(lastName);
                c.setEmail(email);
                c.setPhone(phone);
                return c;
            } else {
                JOptionPane.showMessageDialog(null,
                        "User Not Found: Please check the email and password provided."
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static ArrayList<CustomerModel> queryCustomers(Connection con) {
        PreparedStatement pst;
        ArrayList<CustomerModel> queriedCustomers;
        CustomerModel customer;
        try {
            pst = con.prepareStatement("select * from customers");
            ResultSet result = pst.executeQuery();
            queriedCustomers = new ArrayList<>();
            while (result.next()) {
                customer = new CustomerModel(con);
                String firstName = result.getString("FirstName");
                String lastName = result.getString("Surname");
                String phone = result.getString("PhoneNumber");
                String email = result.getString("EmailAddress");

                customer.setId(result.getString("CustomerID"));
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setEmail(email);
                customer.setPhone(phone);
                customer.setActive(true);
                AddressModel customerAddress = new AddressModel(con).queryAddress(customer.getId());
                if (customerAddress == null) {
                    customer.setCustomerAddress(
                            new AddressModel(con, "N/A", "   ", "   ", "   ")
                    );
                } else {
                    customer.setCustomerAddress(customerAddress);
                }
                ShoppingCartModel customerCart = new ShoppingCartModel(con, customer.getId()).queryCart();
                customer.setCustomerCart(customerCart);
                queriedCustomers.add(customer);
            }
            return queriedCustomers;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        ArrayList<CustomerModel> c = CustomerController.queryCustomers(new MYSQLConnector().getDBConnection());
        for (CustomerModel customer : c){
            System.out.println(customer.toString());
        }
    }
}
