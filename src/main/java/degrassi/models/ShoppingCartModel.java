package main.java.degrassi.models;

import main.java.degrassi.controllers.GameController;
import main.java.degrassi.controllers.ShoppingCartController;
import main.java.degrassi.mysql.MYSQLConnector;

import javax.swing.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class ShoppingCartModel {
    private ArrayList<CartItemModel> cartItems;
    private String id;
    private final String customerID;
    private final Connection db;

    public ShoppingCartModel(Connection connection, String customerID) {
        db = connection;
        this.customerID = customerID;
        cartItems = new ArrayList<>();
        //create cart if not found
        if (!customerHasCart(customerID)) {
            ShoppingCartController.addNewCart(db, customerID);
        } else {
            queryKartID();
        }
    }

    public ShoppingCartModel(Connection connection, String customerID, String cartID, ArrayList<CartItemModel> cartItems) {
        db = connection;
        this.customerID = customerID;
        id = cartID;
        this.cartItems = cartItems;
    }

    private boolean customerHasCart(String customerID) {
        return ShoppingCartController.customerHasCart(db, customerID);
    }

    public void emptyCart() {
        ShoppingCartController.emptyCart(db, id);
    }

    public void addToCart(CartItemModel item) {
        //check if inventory has stock on item, else remove from cart and inform the user of insufficient stock
        if (storeHasStock(item, item.getQuantity())) {
            //check if product had previously been added to the cart
            for (CartItemModel cartItem : cartItems) {
                //update quantity of product in cart and return, otherwise add product to cart
                if (cartItem.getProduct().getId().equals(item.getProduct().getId())) {
                    if (storeHasStock(item, item.getQuantity() + cartItem.getQuantity())) {
                        item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                        updateCart(item);
                    } else {
                        cartItems.remove(item);
                        JOptionPane.showMessageDialog(null,
                                "Insufficient Stock: Failed to add " + item.getProduct().getProductTitle()
                                        + " to your cart.\n" + "Please review the requested quantity.",
                                "Cart Status", JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }
            }
            ShoppingCartController.addToCart(db, item, id);
            cartItems = queryCart().cartItems;
        } else {
            cartItems.remove(item);
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

    public boolean checkOut() {
        StringBuilder info = new StringBuilder("" +
                "The following item(s) have been removed from your cart due to insufficient stock:\n"
        );
        int initialCartSize = cartItems.size();

        //adjust cart items' quantity if inventory is low or out of stock.
        for (CartItemModel item : cartItems) {
            if (!storeHasStock(item, item.getQuantity())) {
                info.append(item.getProduct().getProductTitle()).append("\n");
                ShoppingCartController.removeFromCart(db, item.getId());
            }
        }
        //inform user on items removed if queried cart size has decreased.
        //gets size new cart instance and compares it to the initial cart size
        if (queryCart().cartItems.size() != initialCartSize) {
            JOptionPane.showMessageDialog(null,
                    info.toString(), "Cart Status", JOptionPane.INFORMATION_MESSAGE
            );
            queryCart(); //fetch the new cart data from the database
        }

        //if cart is empty, display an error message and return false
        if (cartItems.size() == 0) {
            return false;
        }

        //place order, decrease inventory and then empty customer's cart
        if (new OrdersModel(db, customerID).addNewOrder(cartItems)) {
            for (CartItemModel item : cartItems) {
                int newStock = item.getProduct().getStock() - item.getQuantity();
                GameController.decreaseQuantity(db, newStock, item.getProduct().getId());
            }
            emptyCart();
            return true;
        }
        return false;
    }

    public ShoppingCartModel queryCart() {
        ArrayList<CartItemModel> queriedCart = Objects.requireNonNull(
                ShoppingCartController.queryCart(db, customerID)
        ).cartItems;
        if (queriedCart.size() == 0) {
            cartItems.clear();
        } else {
            cartItems = queriedCart;
        }
        return this;
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
        return cartItems;
    }

    @Override
    public String toString() {
        try {
            StringBuilder s = new StringBuilder();
            for (CartItemModel item : cartItems) {
                s.append(item.toString()).append('\n');
            }
            return s.toString();
        } catch (NullPointerException ignored) {
        }
        return "";
    }

    public String getCustomerID() {
        return customerID;
    }

    public ArrayList<CartItemModel> getCartItems() {
        return cartItems;
    }

    public Connection DbConnection() {
        return db;
    }

    public void setID(String cartID) {
        id = cartID;
    }
}
