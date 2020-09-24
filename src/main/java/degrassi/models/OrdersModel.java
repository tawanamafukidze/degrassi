package main.java.degrassi.models;

import main.java.degrassi.controllers.OrdersController;
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

    private double sum;
    private final Connection db;
    private InvoiceModel invoice;

    private PreparedStatement pst;
    private ResultSet result;

    public OrdersModel(Connection con, String customerID) {
        //create a new order
        db = con;
        this.customerID = customerID;
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
        this.sum = sum;
        //invoice = new InvoiceModel(this);
    }

    public boolean addNewOrder(ArrayList<CartItemModel> items) {
        orderDate = LocalDateTime.now().toString().replaceAll("T", " ");
        if (OrdersController.addNewOrder(this, items)) {
            invoice = new InvoiceModel(
                    new OrdersModel(db, customerID, orderID, orderDate, orderedProducts, sum, "shipped")
            );
            return true;
        }
        return false;
    }

    public double getSum() {
        return sum;
    }

    public ArrayList<OrdersModel> queryOrders() {
        OrdersController.queryOrders(this);
        ArrayList<OrdersModel> queriedOrders = new ArrayList<>();
        try {
            pst = db.prepareStatement(
                    String.format(
                            "SELECT * FROM customer_orders WHERE customerID = '%s' ORDER BY orderID DESC", customerID
                    )
            );
            ResultSet orderIdQuery = pst.executeQuery();
            while (orderIdQuery.next()) {
                pst = db.prepareStatement(String.format(
                        "SELECT orderID, orderDate, productID, Title, Type, Price, Quantity FROM orders " +
                                "INNER JOIN games ON productID = gameID " +
                                "WHERE customerID = %s AND orderID = %s " +
                                "ORDER BY orderDate DESC",
                        customerID, orderIdQuery.getString("orderID")
                        )
                );
                //pst.setFetchDirection(ResultSet.FETCH_REVERSE);
                result = pst.executeQuery();
                orderedProducts = new ArrayList<>();
                double total = 0.0;
                while (result.next()) {
                    orderDate = result.getString("orderDate");
                    orderID = result.getString("orderID");
                    String productID = result.getString("productID");
                    String productTitle = result.getString("Title");
                    String productType = result.getString("Type");
                    int productQuantity = result.getInt("Quantity");
                    double productPrice = result.getDouble("Price");
                    ProductModel myProduct = new ProductModel(
                            productTitle, productType, productPrice, productQuantity, productID
                    );
                    orderedProducts.add(myProduct);
                    total += (productQuantity * productPrice);
                    status = getStatus(orderID);
                }
                queriedOrders.add(
                        new OrdersModel(
                                db, customerID, orderID, orderDate, orderedProducts, total, status
                        )
                );

            }
            return queriedOrders;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
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
                ", sum=" + sum +
                '}';
    }

    public static void main(String[] args) {
        Connection con = new MYSQLConnector().getDBConnection();
        ArrayList<CartItemModel> items = new ArrayList<>();
        items.add(new CartItemModel("3", 1, new ProductModel("Game", "Genre", 420.69, 10, "4")));
        items.add(new CartItemModel("4", 1, new ProductModel("Game2", "Genre", 420.69, 14, "5")));
        items.add(new CartItemModel("5", 1, new ProductModel("Game3", "Genre", 420.69, 14, "7")));
        items.add(new CartItemModel("6", 1, new ProductModel("Game4", "Genre", 420.69, 10, "1")));
        items.add(new CartItemModel("7", 1, new ProductModel("Game5", "Genre", 420.69, 10, "3")));
        items.add(new CartItemModel("8", 1, new ProductModel("Game6", "Genre", 420.69, 10, "8")));
        System.out.println(new OrdersModel(con, "5").addNewOrder(items));
    }
}
