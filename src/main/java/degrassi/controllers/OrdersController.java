package main.java.degrassi.controllers;

import main.java.degrassi.models.CartItemModel;
import main.java.degrassi.models.OrdersModel;
import main.java.degrassi.models.ProductModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersController {
    private OrdersController() {
    }

    public static boolean addNewOrder(OrdersModel order, ArrayList<CartItemModel> items) {
        PreparedStatement pst;
        //create new customer order relationship
        try {
            pst = order.DbConnection()
                    .prepareStatement("INSERT INTO customer_orders(customerID, status, total) VALUES (?,?,?)")
            ;
            pst.setString(1, order.getCustomerID());
            pst.setString(2, "shipped");
            double total = 0.0;
            for (CartItemModel item : items) {
                total += (item.getProduct().getPrice() * item.getQuantity());
            }
            pst.setDouble(3, total);
            pst.executeUpdate();

            //fetch the id of the new order relationship
            pst = order.DbConnection().prepareStatement(
                    String.format("SELECT * FROM customer_orders WHERE customerID = '%s' " +
                            "ORDER BY orderID DESC LIMIT 1", order.getCustomerID())
            );
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                order.setOrderID(result.getString("orderID"));
            }
            System.out.println(order.getOrderID());
            //add items to orders table with the fetched orderID
            for (CartItemModel item : items) {
                pst = order.DbConnection().prepareStatement(
                        "INSERT INTO orders(orderID, customerID, productID, quantity, orderDate) " +
                                "VALUES (?,?,?,?,?)"
                );
                pst.setString(1, order.getOrderID());
                pst.setString(2, order.getCustomerID());
                pst.setString(3, item.getProduct().getId());
                pst.setInt(4, item.getQuantity());
                pst.setString(5, order.getOrderDate());
                pst.executeUpdate();
                order.getOrderedProducts().add(item.getProduct());
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static ArrayList<OrdersModel> queryOrders(OrdersModel order) {
        ArrayList<OrdersModel> queriedOrders = new ArrayList<>();
        try {
            PreparedStatement pst = order.DbConnection().prepareStatement(
                    String.format(
                            "SELECT * FROM customer_orders WHERE customerID = '%s' ORDER BY orderID DESC",
                            order.getCustomerID()
                    )
            );
            ResultSet orderIdQuery = pst.executeQuery();
            while (orderIdQuery.next()) {
                pst = order.DbConnection().prepareStatement(String.format(
                        "SELECT orders.orderID, orderDate, productID, Title, Type, Price, Quantity, total " +
                                "FROM orders "+
                                "INNER JOIN games ON productID = gameID " +
                                "INNER JOIN customer_orders ON customer_orders.orderID = orders.orderID " +
                                "WHERE customerID = %s AND orderID = %s " +
                                "ORDER BY orderDate DESC",
                        order.getCustomerID(), orderIdQuery.getString("orderID"))
                );
                ResultSet result = pst.executeQuery();
                ArrayList<OrdersModel> orderedProducts = new ArrayList<>();
                double total = -1.0;
                while (result.next()) {
                    String orderDate = result.getString("orderDate");
                    String orderID = result.getString("orderID");
                    String productID = result.getString("productID");
                    String productTitle = result.getString("Title");
                    String productType = result.getString("Type");
                    int productQuantity = result.getInt("Quantity");
                    double productPrice = result.getDouble("Price");

                    if (total == -1.0) {
                        total = result.getDouble("total");
                    }
                    ProductModel orderedProduct = new ProductModel(
                            productTitle, productType, productPrice, productQuantity, productID
                    );
                    order.getOrderedProducts().add(orderedProduct);
                }
                queriedOrders.add(
                        new OrdersModel(
                                order.DbConnection(), order.getCustomerID(), order.getOrderID(),
                                orderDate, order.getOrderedProducts(), total,
                                order.getStatus(order.getOrderID())
                        )
                );
            }
            return queriedOrders;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static String getStatus(Connection con, String id) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    String.format("SELECT * FROM customer_orders WHERE orderID = '%s'", id)
            );
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                return result.getString("status");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
