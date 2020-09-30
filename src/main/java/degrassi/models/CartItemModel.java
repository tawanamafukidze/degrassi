package main.java.degrassi.models;

public class CartItemModel {
    private final String id;
    private int quantity;
    private final ProductModel product;

    public CartItemModel(String id, int quantity, ProductModel product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public void setQuantity(int i) {
        quantity = i;
    }

    public String getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductModel getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return "Cart Item{" +
                "id='" + id + '\'' +
                ", quantity=" + quantity +
                ", product=" + product.toString() +
                '}';
    }
}
