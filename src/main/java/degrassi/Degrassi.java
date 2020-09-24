package main.java.degrassi;
import main.java.degrassi.views.MainFrame;
import main.java.degrassi.mysql.MYSQLConnector;

public class Degrassi {
    public static void main(String[] args) {
        new MainFrame(
                new MYSQLConnector().getDBConnection()
        );
    }
}
