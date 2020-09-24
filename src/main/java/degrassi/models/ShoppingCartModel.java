package main.java.degrassi.models;

import main.java.degrassi.controllers.GameController;
import main.java.degrassi.controllers.ShoppingCartController;
import main.java.degrassi.mysql.MYSQLConnector;

import javax.swing.*;
import java.sql.Connection;
import java.util.ArrayList;

public class ShoppingCartModel {
    private final ArrayList<CartItemModel> items;
    private String id;
    private final String customerID;
    private final Connection db;

    public ShoppingCartModel(Connection connection, String customerID) {
        db = connection;
        this.customerID = customerID;
        items = new ArrayList<>();
        //create cart if not found
        if (!customerHasCart(customerID)) {
            ShoppingCartController.addNewCart(db, customerID);
        } else {
            queryKartID();
        }
    }

    private boolean customerHasCart(String customerID) {
        return ShoppingCartController.customerHasCart(db, customerID);
    }

    public void emptyCart() {
        ShoppingCartController.emptyCart(db, id);
    }

    public void addToCart(CartItemModel item) {
        if (storeHasStock(item, item.getQuantity())) {
            ShoppingCartController.addToCart(db, item, id);
        } else {
            items.remove(item);
            JOptionPane.showMessageDialog(null,
                    "Insufficient Stock: Failed to add " + item.getProduct().getProductTitle()
                            + " to your cart.\n" + "Please review the requested quantity.",
                    "Cart Status", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void removeFromCart(String itemID) {
        ShoppingCartController.removeFromCart(db, itemID);
    }

    public boolean storeHasStock(CartItemModel item, int quantity) {
        return GameController.hasStock(db, item, quantity);
    }

    public void checkOut() {
        ArrayList<CartItemModel> outOfStock = new ArrayList<>();
        StringBuilder info = new StringBuilder("" +
                "The following item(s) have been removed from your cart due to insufficient stock:\n"
        );

        for (CartItemModel item : items) {
            if (storeHasStock(item, item.getQuantity())) {
                int newStock = item.getProduct().getStock() - item.getQuantity();
                GameController.decreaseQuantity(db, newStock, item.getProduct().getId());
            } else {
                info.append(item.getProduct().getProductTitle()).append("\n");
                outOfStock.add(item);
            }
        }

        if (outOfStock.size() > 0) {
            JOptionPane.showMessageDialog(null,
                    info.toString(), "Cart Status", JOptionPane.INFORMATION_MESSAGE
            );
            for (CartItemModel item : outOfStock) {
                /*
                String query = String.format("DELETE FROM kartline WHERE kartID = '%s' AND gameID = '%s'",
                        id, item.getProduct().getId()
                );
                try {
                    PreparedStatement pst = db.prepareStatement(query);
                    pst.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                */
                ShoppingCartController.removeFromCart(db, item.getId());
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
        OrdersModel myOrder = new OrdersModel(db, customerID);
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

    public ShoppingCartModel queryCart() {
        return ShoppingCartController.queryCart(this);
    }

    public void updateCart(CartItemModel item) {
        ShoppingCartController.updateCart(db, id, item);
    }

    private void queryKartID() {
        id = ShoppingCartController.queryCartID(db, customerID);
    }

    public String getId() {
        return id;
    }

    public ArrayList<CartItemModel> toArrayList() {
        return items;
    }

    @Override
    public String toString() {
        try {
            StringBuilder s = new StringBuilder();
            for (CartItemModel item : items) {
                s.append(item.toString()).append('\n');
            }
            return s.toString();
        } catch (NullPointerException ignored) {
        }
        return "";
    }

    public static void main(String[] args) {
        Connection con = new MYSQLConnector().getDBConnection();
        ShoppingCartModel myCart = new ShoppingCartModel(con, "6");
        System.out.println(myCart.queryCart());
        myCart.updateCart(
                new CartItemModel("5", 3, new ProductModel("Fifa", "sport", 200.0, 3, "12"))
        );
        myCart.queryCart();
        myCart.checkOut();
    }

    public String getCustomerID() {
        return customerID;
    }

    public ArrayList<CartItemModel> getItems() {
        return items;
    }

    public Connection DbConnection() {
        return db;
    }

    public void setID(String cartID) {
        id = cartID;
    }
}
