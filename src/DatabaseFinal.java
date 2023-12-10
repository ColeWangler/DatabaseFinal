/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.sql.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author Cole Wangler
 */
public class DatabaseFinal {

    private static String url = "jdbc:postgresql://localhost:5432/ShoppingCart";
    private static String user = "postgres";
    private static String password = "Archer54";
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Scanner scan = new Scanner(System.in);

        boolean exit = false;
        
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
        }catch (ClassNotFoundException e) { System.out.println("Cannot Load Driver");} 
         catch (SQLException sqle) {System.out.println(sqle.toString());}
        
        do{
            System.out.println("------Welcome to the shopping cart Application!-----");
            int menuSelection = -1;
            boolean validOption = false;
            do{
                    System.out.println("Please Enter 1 to Add A New Customer: ");
                    System.out.println("Please Enter 2 to View Items in a customers cart: ");
                    System.out.println("Please enter 3 to Get Total Cost of a customers cart:");
                    System.out.println("Please press 4 to Add Item to a customers Cart: ");
                    System.out.println("Please press 5 to Remove Item to a customers Cart: ");
                    System.out.println("Please press 6 to Update Quantity Of Item in a customers Cart: ");
                    System.out.println("Please press 7 to View Stores: ");
                    System.out.println("Please press 8 to View Store Items: ");
                    System.out.println("Please press 9 to Add a Store: ");
                    System.out.println("Please press 10 to Delete a Store: ");
                    System.out.println("Please press 11 to Edit a Store Inventory: ");
                    System.out.println("Please press 12 to Exit the Application: ");
                    System.out.print("\nEnter Option: ");
                    
                    if( scan.hasNextInt()){
                        validOption = true;
                        menuSelection = scan.nextInt();
                    }
                    else{
                        System.out.println("Not a valid option.");
                        String garbage = scan.nextLine();
                    }
            } while (validOption == false);
            
            int customerID;
            switch (menuSelection) {
                case 1:
                    Customer.addCustomer();
                    break;
                case 2:
                    Customer.getItemsInCart();
                    break;
                case 3:
                    Customer.totalCostOfCart();
                    break;
                case 4: 
                   Has_In_List.addItemToCart();
                   break;
                case 5: 
                   Has_In_List.removeItemFromCart();
                   break;
                case 6: 
                   Has_In_List.updateQuantityOfItem();
                   break;
                case 7:
                    Store.viewStores(conn);
                    break;
                case 8: 
                    Store.viewStoreItems(conn);
                    break;
                case 9:
                    Store.addStore(conn);
                    break;
                case 10:
                    Store.deleteStore(conn);
                    break;
                case 11:
                    Store.editInventory(conn);
                    break;
                case 12:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (exit == false);
    }
}