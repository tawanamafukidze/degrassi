package test.java.degrassi.controllers;

import main.java.degrassi.controllers.CustomerController;
import main.java.degrassi.models.user.CustomerModel;
import main.java.degrassi.mysql.MYSQLConnector;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerControllerTest {
    private final Connection con = new MYSQLConnector().getDBConnection();

    @Test
    public void addNewCustomer() {
        CustomerModel myAccount = new CustomerModel(
                con, "JUnit", "Test", "junit@test.com", "password", "0123456789"
        );
        String id = myAccount.db_commit();
        myAccount = null; //clear instance for new instance
        myAccount = CustomerController.queryCustomerByID(con, id); //new instance
        Assert.assertNotNull(id);
        assert myAccount != null;
        Assert.assertEquals("JUnit", myAccount.getFirstName());

        PreparedStatement pst;
        try {
            pst = con.prepareStatement("DELETE FROM customers WHERE customerID = '" + id + "'");
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void queryCustomersAndIDs() {
        ArrayList<String> queriedIDs = new ArrayList<>();
        PreparedStatement pst;
        try {
            pst = con.prepareStatement("SELECT customerID FROM customers");
            ResultSet r = pst.executeQuery();
            while (r.next()) {
                queriedIDs.add(r.getString("customerID"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ArrayList<String> controllerIDs = CustomerController.queryCustomerIDs(con);
        assert controllerIDs != null;
        for (int i = 0; i < queriedIDs.size(); i++) {
            Assert.assertEquals(queriedIDs.get(i), controllerIDs.get(i));

            Assert.assertEquals(
                    CustomerController.queryCustomerByID(con, controllerIDs.get(i)).getFirstName(),
                    CustomerController.queryCustomerByID(con, queriedIDs.get(i)).getFirstName()
            );
        }


    }

    @Test
    public void queryCustomers() {
        Assert.assertNotNull(CustomerController.queryCustomers(con));
    }

}
