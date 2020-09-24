package main.java.degrassi.controllers;

import main.java.degrassi.models.CartItemModel;
import main.java.degrassi.models.ProductModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameController {
    private GameController() {}

    public static boolean addToInventory(Connection con, String title, String type, int stock, double price) {
        PreparedStatement pst;
        try {
            pst = con.prepareStatement("Insert into games( Title, Type, Stock, Price) Values(?,?,?,?)");
            pst.setString(1, title);
            pst.setString(2, type);
            pst.setInt(3, stock);
            pst.setDouble(4, price);
            pst.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static ArrayList<ProductModel> queryGames(Connection con) {
        ArrayList<ProductModel> queriedProducts;
        try {
            ResultSet result = con.prepareStatement("select * from games").executeQuery();
            queriedProducts = new ArrayList<>();
            while (result.next()) {
                String id = result.getString("gameID");
                String title = result.getString("title");
                String type = result.getString("type");
                int stock = result.getInt("stock");
                double price = result.getDouble("price");
                ProductModel product = new ProductModel(title, type, price, stock, id);
                queriedProducts.add(product);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return queriedProducts;
    }

    public static boolean updateGame(Connection con, String id, String title, String type, int stock, double price) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "UPDATE games set Title = ?, Type = ?, Stock = ?, Price = ? where GameID = ?"
            );
            pst.setString(1, title);
            pst.setString(2, type);
            pst.setInt(3, stock);
            pst.setDouble(4, price);
            pst.setString(5, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static boolean removeGameByID(Connection con, String id) {
        try {
            PreparedStatement pst = con.prepareStatement("delete from games where GameID = ?");
            pst.setString(1, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasStock(Connection db, CartItemModel item, int quantity) {
        String query = String.format(
                "SELECT * FROM games WHERE gameID = '%s'", item.getProduct().getId()
        );
        try {
            PreparedStatement pst = db.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            int returnedStock = 0;
            if (result.next()) {
                returnedStock = result.getInt("stock");
            }
            return returnedStock >= quantity;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void decreaseQuantity(Connection con, int newStock, String id) {
        String query = String.format(
                "UPDATE games SET `Stock` = '%s' WHERE (`GameID` = '%s')", newStock, id
        );

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
