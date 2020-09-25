package main.java.degrassi.models;

import main.java.degrassi.controllers.OrdersController;
import main.java.degrassi.controllers.ShoppingCartController;
import main.java.degrassi.mysql.MYSQLConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrdersModel {
    private final String customerID;
    private String orderID;
    private String orderDate;
    private String status;
    private ArrayList<ProductModel> orderedProducts;

    private double total;
    private final Connection db;
    private InvoiceModel invoice;

    private PreparedStatement pst;
    private ResultSet result;

    public OrdersModel(Connection con, String customerID) {
        //create a new order
        db = con;
        this.customerID = customerID;
        orderedProducts = new ArrayList<>();
    }

    //create instance of a queried order
    public OrdersModel(
            Connection con, String customerID, String orderID, String orderDate, ArrayList<ProductModel> products,
            double sum, String status
    ) {
        db = con;
        this.customerID = customerID;
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.status = status;
        this.orderedProducts = products;
        this.total = sum;
        //invoice = new InvoiceModel(this);
    }

    public boolean addNewOrder(ArrayList<CartItemModel> items) {
        orderDate = LocalDateTime.now().toString().replaceAll("T", " ");
        if (OrdersController.addNewOrder(this, items)) {
            invoice = new InvoiceModel(
                    new OrdersModel(db, customerID, orderID, orderDate, orderedProducts, total, "processing")
            );
            return true;
        }
        return false;
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<OrdersModel> queryOrders() {
        return OrdersController.queryOrders(db, customerID);
    }

    /*
    public boolean cancelOrder() {
        try {
            if (status.equals("processing")) {
                pst = db.prepareStatement(String.format("UPDATE customer_orders" +
                        "SET status = 'cancelled'" +
                        " WHERE orderID = '%s'", this.orderID)
                );
                result = pst.executeQuery();

                JOptionPane.showMessageDialog(null,
                        "Your order has been cancelled.", "Order Status",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,
                "Can't cancel an order that is not longer being processed.", "Order Status",
                JOptionPane.ERROR_MESSAGE
        );
        return false;
    }
     */

    public static double calculateTotal(ArrayList<CartItemModel> items) {
        double total = 0.0;
        for (CartItemModel item : items) {
            total += (item.getProduct().getPrice() * item.getQuantity());
        }
        return total;
    }

    public void setOrderedProducts(ArrayList<ProductModel> orderedProducts) {
        this.orderedProducts = orderedProducts;
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

    public String getStatus(String id) {
        return OrdersController.getStatus(db, id);
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
