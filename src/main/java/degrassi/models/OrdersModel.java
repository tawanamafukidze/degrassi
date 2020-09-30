package main.java.degrassi.models;

import main.java.degrassi.controllers.OrdersController;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrdersModel {
    private final String customerID;
    private String orderID;
    private String orderDate;
    private final ArrayList<ProductModel> orderedProducts;

    private double total;
    private final Connection db;

    public OrdersModel(Connection con, String customerID) {
        //create a new order
        db = con;
        this.customerID = customerID;
        orderedProducts = new ArrayList<>();
    }

    //create instance of a queried order
    public OrdersModel(
            Connection con, String customerID, String orderID, String orderDate, ArrayList<ProductModel> products,
            double sum
    ) {
        db = con;
        this.customerID = customerID;
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.orderedProducts = products;
        this.total = sum;
    }

    public boolean addNewOrder(ArrayList<CartItemModel> items) {
        orderDate = LocalDateTime.now().toString().replaceAll("T", " ");
        return (OrdersController.addNewOrder(this, items));
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<OrdersModel> queryOrders() {
        return OrdersController.queryOrders(db, customerID);
    }

    public static double calculateTotal(ArrayList<CartItemModel> items) {
        double total = 0.0;
        for (CartItemModel item : items) {
            total += (item.getProduct().getPrice() * item.getQuantity());
        }
        return total;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Connection DbConnection() {
        return db;
    }

    public ArrayList<ProductModel> getOrderedProducts() {
        return orderedProducts;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerID=" + customerID +
                ", orderID=" + orderID +
                ", orderDate='" + orderDate + '\'' +
                ", total=" + total +
                '}';
    }
}
