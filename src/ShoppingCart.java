import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Item> items;
    private String id;
    private final String customerID;
    private final Connection db;
    private PreparedStatement pst;
    private String query;

    public ShoppingCart(Connection connection, String customerID) {
        db = connection;
        this.customerID = customerID;
        items = new ArrayList<>();
        //create cart if not found
        if (!customerHasCart(customerID)) {
            query = "INSERT INTO karts(CustomerID) VALUES (?)";
            try {
                pst = db.prepareStatement(query);
                pst.setString(1, customerID);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            queryKartID();
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
                pst = db.prepareStatement("INSERT INTO kartline(gameID, KartID, Quantity) Values(?,?,?)");
                pst.setString(1, item.getProduct().getId());
                pst.setString(2, id);
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
            for (Item item : outOfStock) {
                query = String.format("DELETE FROM kartline WHERE kartID = '%s' AND gameID = '%s'",
                        id, item.getProduct().getId()
                );
                try {
                    PreparedStatement pst = db.prepareStatement(query);
                    pst.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                items.remove(item);
            }
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
                    "Order has been placed.\n" +
                            "Current Status: processing."
            );
        } else {
            JOptionPane.showMessageDialog(null,
                    "Could not place order. Please try again later.",
                    "Order Status", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public ShoppingCart queryCart() {
        items = new ArrayList<>();
        String query = String.format(
                "SELECT DISTINCT karts.KartID, CustomerID, kartline.id, kartline.gameID, kartline.Quantity,\n" +
                        "games.Title, games.Type, games.Price, games.Stock\n" +
                        "FROM karts\n" +
                        "INNER JOIN kartline\n" +
                        "ON karts.kartID = kartline.KartID AND karts.CustomerID = '%s'\n" +
                        "INNER JOIN games\n" +
                        "ON kartline.gameID = games.gameID", customerID);
        try {
            PreparedStatement pst = db.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                id = rs.getString("kartID");
                String itemID = rs.getString("id");
                int itemQuantity = rs.getInt("Quantity");
                String gameID = rs.getString("gameID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("Stock");

                Item item = new Item(
                        itemID, itemQuantity, new Product(title, type, price, stock, gameID)
                );
                items.add(item);
            }
            return this;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void updateCart(Item item) {
        boolean itemFound = false;
        try {
            pst = db.prepareStatement(
                    String.format("SELECT quantity FROM kartline WHERE kartID = %s AND gameID = %s"
                            , id, item.getProduct().getId())
            );
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                itemFound = true;
                //int currentQuantity = result.getInt("quantity");
                db.prepareStatement(String.format("UPDATE kartline " +
                                "SET quantity = %s " +
                                "WHERE kartID = %s AND gameID = %s",
                        item.getQuantity(), id, item.getProduct().getId())
                ).executeUpdate();
            }

            if (!itemFound) {
                try {
                    PreparedStatement insertPst = db.prepareStatement(
                            "INSERT INTO kartline(gameID, kartID, quantity) VALUES(?,?,?)"
                    );
                    insertPst.setString(1, item.getProduct().getId());
                    insertPst.setString(2, id);
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

    private void queryKartID() {
        String query = String.format(
                "SELECT * FROM karts WHERE customerID = %S", customerID);
        try {
            PreparedStatement pst = db.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                id = rs.getString("kartID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public ArrayList<Item> toArrayList() {
        return items;
    }

    @Override
    public String toString() {
        try {
            StringBuilder s = new StringBuilder();
            for (Item item : items) {
                s.append(item.toString()).append('\n');
            }
            return s.toString();
        } catch (NullPointerException ignored) {
        }
        return "";
    }

    public static void main(String[] args) {
        Connection con = new MYSQLConnection().getDBConnection();
        ShoppingCart myCart = new ShoppingCart(con, "6");
        System.out.println(myCart.queryCart());
        myCart.updateCart(
                new Item("5", 3, new Product("Fifa", "sport", 200.0, 3, "12"))
        );
        myCart.queryCart();
        myCart.checkOut();
    }
}
