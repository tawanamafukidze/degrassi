import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private String customerID;
    private String orderID;
    private String orderDate;
    private String status;
    private ArrayList<Product> products;

    private double sum;
    private Connection db;
    private Invoice invoice;

    private PreparedStatement pst;
    private ResultSet result;

    public Order(Connection con, String customerID) {
        //create a new order
        db = con;
        this.customerID = customerID;
    }

    //create instance of a queried order
    public Order(
            Connection con, String customerID, String orderID, String orderDate, ArrayList<Product> products,
            double sum, String status
    ) {
        db = con;
        this.customerID = customerID;
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.status = status;
        this.products = products;
        this.sum = sum;
        invoice = new Invoice(this);
    }

    public boolean addNewOrder(ArrayList<Item> items) {
        orderDate = LocalDateTime.now().toString().replaceAll("T", " ");
        try {
            //create new customer order relationship
            pst = db.prepareStatement("INSERT INTO customer_orders(customerID, status) VALUES (?,?)");
            pst.setString(1, customerID);
            pst.setString(2, "processing");
            pst.executeUpdate();

            //fetch the id of the new order relationship
            pst = db.prepareStatement(
                    String.format("SELECT * FROM customer_orders WHERE customerID = '%s' " +
                            "ORDER BY orderID DESC LIMIT 1", customerID)
            );
            result = pst.executeQuery();
            while (result.next()) {
                orderID = result.getString("orderID");
            }
            System.out.println(orderID);
            //add items to orders table with the fetched orderID
            for (Item item : items) {
                pst = db.prepareStatement(
                        "INSERT INTO orders(orderID, customerID, productID, quantity, orderDate) " +
                                "VALUES (?,?,?,?,?)"
                );
                pst.setString(1, orderID);
                pst.setString(2, customerID);
                pst.setString(3, item.getProduct().getId());
                pst.setInt(4, item.getQuantity());
                pst.setString(5, orderDate);
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public double getSum() {
        return sum;
    }

    public ArrayList<Order> queryOrders() {
        ArrayList<Order> queriedOrders = new ArrayList<>();
        try {
            pst = db.prepareStatement(
                    String.format(
                            "SELECT * FROM customer_orders WHERE customerID = '%s' ORDER BY orderID DESC", customerID
                    )
            );
            ResultSet orderIdQuery = pst.executeQuery();
            while (orderIdQuery.next()) {
                pst = db.prepareStatement(String.format(
                        "SELECT orderID, orderDate, productID, Title, Type, Price, Quantity FROM orders " +
                                "INNER JOIN games ON productID = gameID " +
                                "WHERE customerID = %s AND orderID = %s " +
                                "ORDER BY orderDate DESC",
                        customerID, orderIdQuery.getString("orderID")
                        )
                );
                //pst.setFetchDirection(ResultSet.FETCH_REVERSE);
                result = pst.executeQuery();
                products = new ArrayList<>();
                double total = 0.0;
                while (result.next()) {
                    orderDate = result.getString("orderDate");
                    orderID = result.getString("orderID");
                    String productID = result.getString("productID");
                    String productTitle = result.getString("Title");
                    String productType = result.getString("Type");
                    int productQuantity = result.getInt("Quantity");
                    double productPrice = result.getDouble("Price");
                    Product myProduct = new Product(
                            productTitle, productType, productPrice, productQuantity, productID
                    );
                    products.add(myProduct);
                    total += (productQuantity * productPrice);
                    status = getStatus(orderID);
                }
                queriedOrders.add(
                        new Order(
                                db, customerID, orderID, orderDate, products, total, status
                        )
                );

            }
            return queriedOrders;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean cancelOrder() {
        try {
            if (status.equals("processing")) {
                pst = db.prepareStatement(String.format("UPDATE customer_orders" +
                        "SET status = 'cancelled'" +
                        " WHERE orderID = '%s'", this.orderID)
                );
                result = pst.executeQuery();

                JOptionPane.showMessageDialog(null,
                        "Your order has been cancelled.", "Order Status",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,
                "Can't cancel an order that is not longer being processed.", "Order Status",
                JOptionPane.ERROR_MESSAGE
        );
        return false;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public String getStatus(String id) {
        try {
            PreparedStatement pst = db.prepareStatement(
                    String.format("SELECT * FROM customer_orders WHERE orderID = '%s'", id)
            );
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                status = result.getString("status");
                return status;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerID=" + customerID +
                ", orderID=" + orderID +
                ", orderDate='" + orderDate + '\'' +
                ", sum=" + sum +
                '}';
    }

    public static void main(String[] args) {
        Connection con = new MYSQLConnection().getDBConnection();
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("3", 1, new Product("Game", "Genre", 420.69, 10, "4")));
        items.add(new Item("4", 1, new Product("Game2", "Genre", 420.69, 14, "5")));
        items.add(new Item("5", 1, new Product("Game3", "Genre", 420.69, 14, "7")));
        items.add(new Item("6", 1, new Product("Game4", "Genre", 420.69, 10, "1")));
        items.add(new Item("7", 1, new Product("Game5", "Genre", 420.69, 10, "3")));
        items.add(new Item("8", 1, new Product("Game6", "Genre", 420.69, 10, "8")));
        System.out.println(new Order(con, "5").addNewOrder(items));
    }
}
