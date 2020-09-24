package main.java.degrassi.models;

import main.java.degrassi.controllers.GameController;
import main.java.degrassi.controllers.ShoppingCartController;
import main.java.degrassi.mysql.MYSQLConnector;

import javax.swing.*;
import java.sql.Connection;
import java.util.ArrayList;

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
            for  (CartItemModel cartItem : cartItems) {
                //update quantity of product in cart and return, otherwise add product to cart
                if (cartItem.getProduct().getId().equals(item.getProduct().getId())) {
                    item.setQuantity(item.getQuantity()+cartItem.getQuantity());
                    updateCart(item);
                    return;
                }
            }
            ShoppingCartController.addToCart(db, item, id);
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
        int cartItemLength = cartItems.size();

        //adjust cart items' quantity is inventory is low or out of stock.
        for (CartItemModel item : cartItems) {
            if (!storeHasStock(item, item.getQuantity())) {
                info.append(item.getProduct().getProductTitle()).append("\n");
                ShoppingCartController.removeFromCart(db, item.getId());
            }
        }
        this.cartItems =  queryCart().cartItems; //fetch the new cart data from the database

        //inform user on items removed if queried cart size has decreased.
        if (cartItems.size() > cartItemLength) {
            JOptionPane.showMessageDialog(null,
                    info.toString(), "Cart Status", JOptionPane.INFORMATION_MESSAGE
            );
        }

        //if cart is empty, display an error message and return false
        if (cartItems.size() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Cannot proceed to checkout. Your cart seems to be empty.",
                    "Checkout Status",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        //place order, decrease inventory and then empty customer's cart
        if (new OrdersModel(db, customerID).addNewOrder(cartItems)) {
            for (CartItemModel item : cartItems) {
                int newStock = item.getProduct().getStock() - item.getQuantity();
                GameController.decreaseQuantity(db, newStock, item.getProduct().getId());
            }
            emptyCart();
            JOptionPane.showMessageDialog(null,
                    "Order has been placed.\n" +
                            "Current Status: processing."
            );
            return true;
        }
        return false;
    }

    public ShoppingCartModel queryCart() {
        return ShoppingCartController.queryCart(db, customerID);
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
