package main.java.degrassi.models;

import main.java.degrassi.mysql.MYSQLConnector;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class InvoiceModel {
    private final String id;
    private final String issueDate;
    private final double sum;
    private final ArrayList<ProductModel> products;

    public InvoiceModel(OrdersModel order) {
        issueDate = LocalDateTime.now().toString().replaceAll("T", " ").substring(0, 19);
        products = order.getOrderedProducts();
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
        for (ProductModel product : products) {
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
        Connection con = new MYSQLConnector().getDBConnection();
        ArrayList<OrdersModel> orderList = new OrdersModel(con, "6").queryOrders();
        InvoiceModel n = new InvoiceModel(orderList.get(0));
        System.out.println(n.printInvoice());
    }
}
