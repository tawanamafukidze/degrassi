import javax.print.DocFlavor;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Invoice {
    private final String id;
    private final String issueDate;
    private final double sum;
    private final ArrayList<Product> products;

    public Invoice(Order order) {
        issueDate = LocalDateTime.now().toString().replaceAll("T", " ").substring(0, 19);
        products = order.getProducts();
        id = order.getOrderID();
        sum = order.getSum();
    }

    public String getId() {
        return id;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public double getSum() {
        return sum;
    }

    public String printInvoice() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("\n| %s | %s-20.20s| %f,2d|", id, issueDate, sum));
        for (Product product : products) {
            s.append(String.format("\n|  | %-20.20s|         |",
                    product.getId()+" "+product.getProductTitle()+" "+product.getPrice()
            ));
        }
        return s.toString();
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id='" + id + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", sum=" + sum +
                '}';
    }

    public static void main(String[] args) {
        Connection con = new MYSQLConnection().getDBConnection();
        ArrayList<Order> orderList = new Order(con, "6").queryOrders();
        Invoice n = new Invoice(orderList.get(0));
        System.out.println(n.printInvoice());
    }
}
