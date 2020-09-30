package test.java.degrassi.controllers;
import main.java.degrassi.controllers.AddressController;
import main.java.degrassi.models.AddressModel;
import main.java.degrassi.models.user.CustomerModel;
import main.java.degrassi.mysql.MYSQLConnector;
import org.junit.Assert;
import org.junit.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddressControllerTest {
    private final Connection con = new MYSQLConnector().getDBConnection();
    private AddressModel myAddress;
    private CustomerModel newCustomer;

    @Test
    public void addNewAddress() {
        PreparedStatement pst;
        try {
            pst = con.prepareStatement("DELETE FROM customers WHERE FirstName = 'JUnit'");
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        newCustomer = new CustomerModel(
                con, "JUnit", "Testing", "junit@test.com", "password", "0123456789"
        );
        String customerID = newCustomer.db_commit();
        myAddress = new AddressModel(con, "street", "state", "city", "00000");
        myAddress.db_commit(customerID);
        newCustomer = new CustomerModel(con,"junit@test.com","password");
        myAddress = myAddress.queryAddress(newCustomer.getId());
        Assert.assertEquals("street", myAddress.getStreet());
        Assert.assertEquals("city", myAddress.getCity());
        Assert.assertEquals("state", myAddress.getState());
        Assert.assertEquals("00000", myAddress.getPostCode());
    }

    @Test
    public void queryAddress() {
        newCustomer = new CustomerModel(con, "junit@test.com", "password");
        myAddress = AddressController.queryAddress(con, newCustomer.getId());
        if (myAddress != null) {
            Assert.assertEquals("street", myAddress.getStreet());
            Assert.assertEquals("city", myAddress.getCity());
            Assert.assertEquals("state", myAddress.getState());
            Assert.assertEquals("00000", myAddress.getPostCode());
        }

        PreparedStatement pst;
        try {
            pst = con.prepareStatement("DELETE FROM customers WHERE customerID = '"+newCustomer.getId()+"'");
            pst.executeUpdate();
            pst = con.prepareStatement("DELETE FROM address WHERE customerID = '"+newCustomer.getId()+"'");
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void queryTestAddress() {
        myAddress = AddressController.queryAddress(con, "110");
        if (myAddress != null) {
            Assert.assertEquals("Street", myAddress.getStreet());
            Assert.assertEquals("City", myAddress.getCity());
            Assert.assertEquals("State", myAddress.getState());
            Assert.assertEquals("00000", myAddress.getPostCode());
        }
    }
}
