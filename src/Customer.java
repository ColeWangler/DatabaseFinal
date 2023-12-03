
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import java.sql.*;
import java.text.DecimalFormat;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Cole Wangler
 */
public class Customer {
    private static String url = "jdbc:postgresql://localhost:5432/ShoppingCart";
    private static String user = "postgres";
    private static String password = "Archer54";  
    
    
    public static void addCustomer() {

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement statement = conn.createStatement();

            Scanner scan = new Scanner(System.in);

            System.out.println("Please enter the first name: ");
            String firstName = scan.nextLine();
            System.out.println("Please enter the last name: ");
            String lastName = scan.nextLine();
            System.out.println("Please enter the phone number: ");
            String phoneNumber = scan.nextLine();
            System.out.println("Please enter the email: ");
            String email = scan.nextLine();
            System.out.println("Please enter the address: ");
            String address = scan.nextLine();

            String insertIntoCustomer = "INSERT INTO customer (customer_first_name, customer_last_name, customer_phone_number, customer_email, current_address) "
                    + "Values ('Peter', 'Mulark', '1-320-987-9898', 'customer4@email.com', '2340 23rd St Fargo')";
//                    + "VALUES ('?','?','?','?','?')";
//
//            PreparedStatement pstmt = conn.prepareStatement(insertIntoCustomer);
//            pstmt.setString(1, firstName);
//            pstmt.setString(2, lastName);
//            pstmt.setString(3, phoneNumber);
//            pstmt.setString(4, email);
//            pstmt.setString(5, address);

            int res = statement.executeUpdate(insertIntoCustomer);

            System.out.println(res + " records inserted");

        } catch (ClassNotFoundException e) {
            System.out.println("Cannot Load Driver");
        } catch (SQLException sqle) {
            System.out.println(sqle.toString());
        }
    }
    
    public static void getItemsInCart(int x){
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement statement = conn.createStatement();

            DecimalFormat moneyFormat = new DecimalFormat("$0.00");

            String getItemsInCart = "Select item_quantity, item_name, description, cost  From items "
                    + "Join has_in_list ON has_in_list.item_id = items.item_id "
                    + "Where has_in_list.customer_id = " + x;
            ResultSet rs = statement.executeQuery(getItemsInCart);

            int itemNumber = 1;

            while (rs.next()) {

                System.out.println("#" + itemNumber + " Item On List");

                System.out.println("Quantity: " + rs.getInt(1));
                System.out.println("Item Name: " + rs.getString(2));
                System.out.println("Item Description: " + rs.getString(3));
                System.out.println("Item Cost: " + moneyFormat.format(rs.getDouble(4)));
                System.out.println("----------------------\n");

                itemNumber++;

            }
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot Load Driver");
        } catch (SQLException sqle) {
            System.out.println(sqle.toString());
        }
    }
    
    public static void totalCostOfCart(int x) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement statement = conn.createStatement();

            String getTotalCostInCart = "Select item_quantity, cost, amount_off  From items "
                    + "Join has_in_list ON has_in_list.item_id = items.item_id "
                    + "Join discounts ON discounts.item_id = items.item_id "
                    + " Where has_in_list.customer_id = " + x;
            ResultSet rs = statement.executeQuery(getTotalCostInCart);

            DecimalFormat moneyFormat = new DecimalFormat("$0.00");

            double totalCost = 0;
            while (rs.next()) {
                totalCost += rs.getInt(1) * (rs.getDouble(2) - rs.getDouble(3));
            }
            System.out.println("Your Total Cost: " + moneyFormat.format(totalCost));
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot Load Driver");
        } catch (SQLException sqle) {
            System.out.println(sqle.toString());
        }
    }
}
