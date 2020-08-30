import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Invoice {
    private String id;
    private String issueDate;
    private double sum;

    public Invoice(Order order) {
        issueDate = LocalDateTime.now().toString().replaceAll("T", " ");
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
        System.out.println(n.toString());
    }
}
