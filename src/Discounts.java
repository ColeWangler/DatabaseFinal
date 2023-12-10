
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
/*

----> It is 1:30am and I have just realized that none of my code uses prepared statements. This likely has to be addressed. <----

*/
/**
 *
 * @author Reid
 */
public class Discounts {
    //Database Connection Information
    private static String url = "jdbc:postgresql://localhost:5432/ShoppingCart";
    private static String user = "postgres";
    private static String password = "ColeyJ56!";  
    private static Scanner scan = new Scanner(System.in);
    private static String garbage;
    
    public static void viewDiscounts(Connection connection){
        try{
            String select = "SELECT * FROM discounts INNER JOIN items ON discounts.item_id = items.item_id INNER JOIN store ON discounts.store_id = store.store_id;";
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(select);
            while(resultSet.next()){
                System.out.println("Store: " + resultSet.getString("store_name"));
                System.out.println("Item: " + resultSet.getString("item_name"));
                System.out.println("Base Price: " + resultSet.getDouble("cost"));
                System.out.println("Discount: " + resultSet.getDouble("amount_off"));
                System.out.println("Discounted Price: " + (resultSet.getDouble("cost") - resultSet.getDouble("amount_off")));
                System.out.println("Start Date:" + resultSet.getDate("start_date"));
                System.out.println("End Date: " + resultSet.getDate("end_date"));
                System.out.println("\n");
            }
        }
        catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
    }
    
    public static void addDiscount(Connection connection){
        try{
            //Get Store Name
            System.out.println("Enter Store Name: \n");
            String storeName = scan.next();
            //Get Store ID
            String select = "SELECT * FROM store WHERE name = '" + storeName + "';";
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(select);
            int storeID = resultSet.getInt("store_id");
            //Get Item Name
            System.out.println("Enter Item Name: \n");
            String itemName = scan.next();
            //Get Item ID
            select = "SELECT * FROM items WHERE name = '" + itemName + "';";
            selectStatement = connection.createStatement();
            resultSet = selectStatement.executeQuery(select);
            int itemID = resultSet.getInt("item_id");
            //Check that store sells item
            select = "SELECT count(*) FROM sells WHERE item_id = " + itemID + "AND store_id = " + storeID + ";";
            selectStatement = connection.createStatement();
            resultSet = selectStatement.executeQuery(select);
            if(resultSet.getInt("count") < 1)
                throw new SQLException("Store Does Not Sell Item");
            
            //Get Start Date
            boolean validInput= false;
            String startDate = "null";
            while(!validInput){
               System.out.println("Would you like to enter a start date? (Y/N)");
            String input = scan.next();
            switch(input){
                case "y":
                case "Y":
                case "Yes":
                case "yes":
                    System.out.println("What year did the sale begin?\n");
                    while(!scan.hasNextInt()){
                        System.out.println("Please enter an integer year.\n");
                        garbage = scan.next();
                    }
                    int startYear = scan.nextInt();
                    //Consider adding a switch statement that converts case insensitive month names to numbers i.e. january -> 1.
                    System.out.println("What month did the sale begin?\n");
                    while(!scan.hasNextInt()){
                        System.out.println("Please enter an integer month.\n");
                        garbage = scan.next();
                    }
                    int startMonth = scan.nextInt();
            
                    System.out.println("What day did the sale begin?\n");
                    while(!scan.hasNextInt()){
                        System.out.println("Please enter an integer day.\n");
                        garbage = scan.next();
                    }
                    int startDay = scan.nextInt();
                    startDate = startYear + "-" + startMonth + "-" + startDay;
                    validInput = true;
                    break;
                case "N":
                case "n":
                case "No":
                case "no":
                    startDate = "null";
                    validInput = true;
                    break;
                default:
                    System.out.println("Invalid Input");
                    break;
            } 
            }
            
            //Get End Date
            String endDate = "null";
            validInput= false;
            while(!validInput){
               System.out.println("Would you like to enter an end date? (Y/N)");
            String input = scan.next();
            switch(input){
                case "y":
                case "Y":
                case "Yes":
                case "yes":
                    System.out.println("What year did the sale end?\n");
                    while(!scan.hasNextInt()){
                        System.out.println("Please enter an integer year.\n");
                        garbage = scan.next();
                    }
                    int endYear = scan.nextInt();
                    //Consider adding a switch statement that converts case insensitive month names to numbers i.e. january -> 1.
                    System.out.println("What month did the sale end?\n");
                    while(!scan.hasNextInt()){
                        System.out.println("Please enter an integer month.\n");
                        garbage = scan.next();
                    }
                    int endMonth = scan.nextInt();
            
                    System.out.println("What day did the sale end?\n");
                    while(!scan.hasNextInt()){
                        System.out.println("Please enter an integer day.\n");
                        garbage = scan.next();
                    }
                    int endDay = scan.nextInt();
                    endDate = endYear + "-" + endMonth + "-" + endDay;
                    validInput = true;
                    break;
                case "N":
                case "n":
                case "No":
                case "no":
                    endDate = "null";
                    validInput = true;
                    break;
                default:
                    System.out.println("Invalid Input");
                    break;
            } 
            }
            //Get amount
            System.out.println("How much is the item disounted?\n$");
            while(!scan.hasNextDouble()){
                System.out.println("Please enter a decimal value");
                garbage = scan.next();
            }
            double reduction = scan.nextDouble();
            //Insert Record
            String insert = "INSERT INTO discounts VALUES (default, " + storeID + ", " + itemID + ", '" + startDate + "', " + endDate + ", " + reduction + ")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(insert);
        }
        
        catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
    }
    
    public static void removeDiscount(Connection connection, int id){
     try{
        String delete = "";
        System.out.println("How would you like to delete?\n"
                + "1) By id\n"
                + "2) By Name");
        boolean validInput = false;
        while(!validInput){
            String input = scan.next();
            switch(input){
                case "1":
                    System.out.println("Enter discount_ID\n");
                    while(!scan.hasNextInt()){
                        System.out.println("Please enter an integer\n");
                        garbage = scan.next();
                    }
                    int discountID = scan.nextInt();
                    delete = "DELETE FROM discounts WHERE discount_id = " + discountID + ";";
                    
                    validInput = true;
                    break;
                case "2":
                    System.out.println("Enter item name: \n");
                    String itemName = scan.next();
                    System.out.println("Enter store name: \n");
                    String storeName = scan.next();
                    
                    //Get Store ID
                    String select = "SELECT * FROM store WHERE name = '" + storeName + "';";
                    Statement selectStatement = connection.createStatement();
                    ResultSet resultSet = selectStatement.executeQuery(select);
                    int storeID = resultSet.getInt("store_id");
                    //Get Item ID
                    select = "SELECT * FROM items WHERE name = '" + itemName + "';";
                    selectStatement = connection.createStatement();
                    resultSet = selectStatement.executeQuery(select);
                    int itemID = resultSet.getInt("item_id");
                    
                    delete = "DELETE FROM discounts WHERE item_id = " + itemID + "AND" + "store_id = " + storeID + ";";
                    validInput = true;
                    break;
                default:
                    System.out.println("Please select either 1 or 2.");
                    break;
                
            }
        }
     }
     catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
    }
    
}
