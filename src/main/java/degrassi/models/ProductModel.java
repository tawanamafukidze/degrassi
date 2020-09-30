package main.java.degrassi.models;

import main.java.degrassi.controllers.GameController;

import java.sql.Connection;
import java.util.ArrayList;

public class ProductModel {
    private final String id;
    private final String title;
    private final String type;
    private final double price;
    private final int stock;

    public ProductModel(String title, String type, double price, int stock, String id) {
        this.title = title;
        this.type = type;
        this.price = price;
        this.stock = stock;
        this.id = id;
    }

    public static ArrayList<ProductModel> queryProduct(Connection con) {
        return GameController.queryGames(con);
    }

    //add a new product to the inventory
    public static boolean addToInventory(Connection con, String title, String type, int stock, double price) {
        return GameController.addToInventory(con, title, type, stock, price);
    }

    //update game inventory when a purchase has been mad.
    public static boolean update(Connection con, String id, String title, String type, int stock, double price) {
        return GameController.updateGame(con, id, title, type, stock, price);
    }

    //remove product from inventory using the product ID
    public static boolean removeByID(Connection con, String id) {
        return GameController.removeGameByID(con, id);
    }

    public int getStock() {
        return stock;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getProductTitle() {
        return title;
    }

    public String getProductType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("Product" +
                "id: %s \n" +
                "title: %s \n" +
                "type: %s \n" +
                "price: %s \n" +
                "stock: %s \n", id, title, type, price, stock);
    }
}
