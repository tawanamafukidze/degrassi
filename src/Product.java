import javax.swing.*;

public class Product {
    private String id;
    private String title;
    private String type;
    private double price;
    private int stock;

    public Product(String title, String type, double price, int stock, String id) {
        this.title = title;
        this.type = type;
        this.price = price;
        this.stock = stock;
        this.id = id;
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

    /*
    @Override
    public String toString() {
        return String.format("Product" +
                "id: %s \n" +
                "title: %s \n" +
                "type: %s \n" +
                "price: %s \n" +
                "stock: %s \n", id, title, type, price, stock);
    }
    */

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
