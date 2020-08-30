import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Item> items;
    private int id;
    private String customerID;
    private Connection db;
    private PreparedStatement pst;
    private String query;

    public ShoppingCart(Connection connection, String customerID) {
        db = connection;
        this.customerID = customerID;
        items = new ArrayList<>();
        //create cart if not found
        if (!customerHasCart(customerID)) {
            query = String.format("INSERT INTO karts(CustomerID) VALUES (?)");
            try {
                pst = db.prepareStatement(query);
                pst.setString(1, customerID);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private boolean customerHasCart(String customerID) {
        query = String.format("SELECT * FROM karts WHERE customerID = '%s'", customerID);
        try {
            pst = db.prepareStatement(query);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void emptyCart() {
        query = String.format("DELETE FROM kartline where kartID = '%s'", id);
        try {
            pst = db.prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addToCart(Item item) {
        if (storeHasStock(item, item.getQuantity())) {
            try {
                pst = db.prepareStatement("INSERT INTO kartline(ProductID, KartID, Quantity) Values(?,?,?)");
                pst.setString(1, item.getProduct().getId());
                pst.setInt(2, id);
                pst.setInt(3, item.getQuantity());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Item has been added to your cart.");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            items.remove(item);
            JOptionPane.showMessageDialog(null,
                    "Insufficient Stock: Failed to add " + item.getProduct().getProductTitle()
                            + " to your cart.\n" + "Please review the requested quantity.",
                    "Cart Status", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void removeFromCart(int itemID) {
        query = String.format("DELETE FROM kartline WHERE id = '%s'", itemID);
        try {
            pst = db.prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean storeHasStock(Item item, int quantity) {
        query = String.format(
                "SELECT * FROM games WHERE gameID = '%s'", item.getProduct().getId()
        );
        try {
            pst = db.prepareStatement(query);
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

    public void checkOut() {
        ArrayList<Item> outOfStock = new ArrayList<>();
        StringBuilder info = new StringBuilder("" +
                "The following item(s) have been removed from your cart due to insufficient stock:\n"
        );
        for (Item item : items) {
            if (storeHasStock(item, item.getQuantity())) {
                int newStock = item.getProduct().getStock() - item.getQuantity();
                query = String.format(
                        "UPDATE games SET `Stock` = '%s' WHERE (`GameID` = '%s')", newStock, item.getProduct().getId()
                );

                try {
                    pst = db.prepareStatement(query);
                    pst.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                info.append(item.getProduct().getProductTitle()).append("\n");
                outOfStock.add(item);
            }
        }

        if (outOfStock.size() > 0) {
            JOptionPane.showMessageDialog(null,
                    info.toString(), "Cart Status", JOptionPane.INFORMATION_MESSAGE
            );
            for (Item item : outOfStock) { items.remove(item); }
        }

        if (items.size() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Cannot proceed to checkout. Your cart seems to be empty.",
                    "Checkout Status",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        Order myOrder = new Order(db, customerID);
        boolean orderPlaced = myOrder.addNewOrder(items);
        if (orderPlaced) {
            emptyCart();
            JOptionPane.showMessageDialog(null,
                    String.format("Order has been placed.\n" +
                    "Current Status: processing.")
            );
        } else {
            JOptionPane.showMessageDialog(null,
                    "Could not place order. Please try again later.",
                    "Order Status", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public ShoppingCart queryCart() {
        String query = String.format(
                "SELECT DISTINCT kart.KartID, kart.CustomerID, line.id, line.ProductID, line.Quantity, " +
                        "game.Title, game.Type, game.Price, game.Stock " +
                        "FROM karts kart inner join kartline line " +
                        "ON line.KartID = kart.KartID AND kart.CustomerID = '%s' " +
                        "inner join games game " +
                        "ON line.productID = game.gameID", customerID);
        try {
            PreparedStatement pst = db.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                id = rs.getInt("kartID");
                String itemID = rs.getString("id");
                int itemQuantity = rs.getInt("Quantity");
                String productID = rs.getString("productID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("Stock");

                Item item = new Item(
                        itemID, itemQuantity, new Product(title, type, price, stock, productID)
                );
                items.add(item);
            }
            return this;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        try {
            StringBuilder s = new StringBuilder();
            for (Item item : items) {
                s.append(item.toString()).append('\n');
            }
            return s.toString();
        } catch (NullPointerException ignored) {}
        return "";
    }

    public static void main(String[] args) {
        Connection con = new MYSQLConnection().getDBConnection();
        ShoppingCart myCart = new ShoppingCart(con, "6");
        System.out.println(myCart.queryCart());
        myCart.checkOut();
    }
}
