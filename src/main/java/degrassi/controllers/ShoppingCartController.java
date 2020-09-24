package main.java.degrassi.controllers;

import main.java.degrassi.models.CartItemModel;
import main.java.degrassi.models.ProductModel;
import main.java.degrassi.models.ShoppingCartModel;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShoppingCartController {
    public static void addNewCart(Connection db, String customerID) {
        String query = "INSERT INTO karts(CustomerID) VALUES (?)";
        try {
            PreparedStatement pst = db.prepareStatement(query);
            pst.setString(1, customerID);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ShoppingCartModel queryCart(Connection con, String customerID) {
        ArrayList<CartItemModel> cartItems = new ArrayList<>();
        String cartID = queryCartID(con, customerID);

        String query = String.format(
                "SELECT DISTINCT karts.KartID, CustomerID, kartline.id, kartline.gameID, kartline.Quantity,\n" +
                        "games.Title, games.Type, games.Price, games.Stock\n" +
                        "FROM karts\n" +
                        "INNER JOIN kartline\n" +
                        "ON karts.kartID = kartline.KartID AND karts.CustomerID = '%s'\n" +
                        "INNER JOIN games\n" +
                        "ON kartline.gameID = games.gameID", customerID);
        try {
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String itemID = rs.getString("id");
                int itemQuantity = rs.getInt("Quantity");
                String gameID = rs.getString("gameID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("Stock");

                CartItemModel item = new CartItemModel(
                        itemID, itemQuantity, new ProductModel(title, type, price, stock, gameID)
                );
                cartItems.add(item);
            }
            return new ShoppingCartModel(con, customerID, cartID, cartItems);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void addToCart(Connection db, CartItemModel item, String kartID) {
        try {
            PreparedStatement pst = db.prepareStatement(
                    "INSERT INTO kartline(gameID, KartID, Quantity) Values(?,?,?)"
            );
            pst.setString(1, item.getProduct().getId());
            pst.setString(2, kartID);
            pst.setInt(3, item.getQuantity());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Item has been added to your cart.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void updateCart(Connection con, String cartID, CartItemModel item) {
        boolean itemFound = false;
        try {
            PreparedStatement pst = con.prepareStatement(
                    String.format("SELECT quantity FROM kartline WHERE kartID = %s AND gameID = %s",
                            cartID, item.getProduct().getId())
            );
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                itemFound = true;
                con.prepareStatement(String.format("UPDATE kartline " +
                                "SET quantity = %d " +
                                "WHERE kartID = %s AND gameID = %s",
                        item.getQuantity(), cartID, item.getProduct().getId())
                ).executeUpdate();
            }

            if (!itemFound) {
                try {
                    PreparedStatement insertPst = con.prepareStatement(
                            "INSERT INTO kartline(gameID, kartID, quantity) VALUES(?,?,?)"
                    );
                    insertPst.setString(1, item.getProduct().getId());
                    insertPst.setString(2, cartID);
                    insertPst.setInt(3, item.getQuantity());
                    insertPst.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void removeFromCart(Connection con, String itemID) {
        String query = String.format("DELETE FROM kartline WHERE id = '%s'", itemID);
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void emptyCart(Connection con, String id) {
        String query = String.format("DELETE FROM kartline where kartID = '%s'", id);
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String queryCartID(Connection db, String customerID) {
        String query = String.format(
                "SELECT * FROM karts WHERE customerID = %S", customerID);
        try {
            PreparedStatement pst = db.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("kartID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static boolean customerHasCart(Connection con, String customerID) {
        String query = String.format("SELECT * FROM karts WHERE customerID = '%s'", customerID);
        try {
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
