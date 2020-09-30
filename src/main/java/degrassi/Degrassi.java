package main.java.degrassi;
import main.java.degrassi.views.MainFrame;
import main.java.degrassi.mysql.MYSQLConnector;

public class Degrassi {
    private static boolean debugMode = false;
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("1")) {
                debugMode = true;
            }
        }
        new MainFrame(
                new MYSQLConnector().getDBConnection()
        );
    }

    public static void debug(String message){
        if (!debugMode) return;
        System.out.println(message);
    }
}
