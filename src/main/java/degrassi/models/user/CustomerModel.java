package main.java.degrassi.models.user;

import main.java.degrassi.controllers.CustomerController;
import main.java.degrassi.controllers.OrdersController;
import main.java.degrassi.models.AddressModel;
import main.java.degrassi.models.CartItemModel;
import main.java.degrassi.models.OrdersModel;
import main.java.degrassi.models.ShoppingCartModel;

import java.sql.Connection;
import java.util.ArrayList;

public class CustomerModel extends Person {
    private AddressModel customerAddress;
    private ShoppingCartModel customerCart;
    private ArrayList<OrdersModel> customerOrders;

    //Creates a new user instance and verifies the information provided by the client before committing to the the DB
    public CustomerModel(Connection con, String firstName, String lastName, String email, String password, String phone) {
        super(con, firstName, lastName, email, password, phone);
    }

    //Fetch the required user from the database
    public CustomerModel(Connection con, String email, String password) {
        super(con, email, password);
        if (isValidEntry()) {
            login(email, password);
        }
    }

    //standard querying of user information
    public CustomerModel(Connection con) {
        super(con);
    }

    //addNewAddress to db and return the id of the newly added customer
    public String db_commit() {
        return CustomerController.addNewCustomer(this);
    }

    //User fetch upon correct login information
    public void login(String email, String password) {
        CustomerController.queryCustomer(this, email, password);
        String customerID = getId();
        customerAddress = new AddressModel(dbConnection()).queryAddress(customerID);
        customerCart = new ShoppingCartModel(dbConnection(), customerID).queryCart();
        //user has logged in
        setActive(true);
    }

    public static ArrayList<String> queryCustomerIDs(Connection con) {
        return CustomerController.queryCustomerIDs(con);
    }

    public ArrayList<CustomerModel> queryCustomers() {
        return CustomerController.queryCustomers(dbConnection());
    }

    //query the customer by the provided id
    public CustomerModel queryCustomerByID(String id) {
        return CustomerController.queryCustomerByID(dbConnection(), id);
    }

    public ShoppingCartModel getShoppingCart() {
        return customerCart.queryCart();
    }

    public boolean checkOut() {
        try {
            if (customerCart.checkOut()) {
                customerOrders = OrdersController.queryOrders(dbConnection(), getId());
                return true;
            }
        } catch (NullPointerException ignored) {
        }
        return false;
    }

    public void removeFromCart(String itemID) {
        customerCart.removeFromCart(itemID);
    }

    public void emptyCart() {
        customerCart.emptyCart();
    }

    public void updateCart(CartItemModel items) {
        customerCart.updateCart(items);
    }

    public void purchase() {
        customerCart.checkOut();
    }

    public void setCustomerAddress(AddressModel customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCustomerCart(ShoppingCartModel customerCart) {
        this.customerCart = customerCart;
    }

    public ArrayList<OrdersModel> getCustomerOrders() {
        return customerOrders;
    }

    public AddressModel getCustomerAddress() {
        try {
            return customerAddress;
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("isEmployee: %s \n", false)
                + customerAddress.toString();
    }
}