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
        
        do{
            System.out.println("------Welcome to the shopping cart Application!-----");
            int menuSelection;
            boolean validOption = false;
            do{
                    System.out.println("Please Enter 1 to Add A New Customer: ");
                    System.out.println("Please Enter 2 to View Items in a customers cart: ");
                    System.out.println("Please enter 3 to Get Total Cost of a customers cart:");
                    System.out.println("Please press 4 to Exit the Application: ");
                    System.out.println("Please press 5 to see the Store menu: ");
                    menuSelection = scan.nextInt();
                    
                    if( menuSelection == 1 || menuSelection == 2 || menuSelection == 3 || menuSelection == 4 || menuSelection == 5 || menuSelection == 6)
                        validOption = true;
                    else
                        System.out.println("Not a valid option.");
            } while (validOption == false);
            
            Customer customer = new Customer();
            int customerID;
            switch (menuSelection) {
                case 1:
                    customer.addCustomer();
                    break;
                case 2:
                    System.out.println("Please enter customer ID: ");
                    customerID = scan.nextInt();
                    customer.getItemsInCart(customerID);
                    break;
                case 3:
                    System.out.println("Please enter customer ID: ");
                    customerID = scan.nextInt();
                    customer.totalCostOfCart(customerID);
                    break;
                case 4:
                    exit = true;
                    break;
                case 5: 
                   Store.menuOperation();
                   break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (exit == false);
    }
}
   
            
//            String getCustomerInfo = "Select * FROM customer";
//            
//            ResultSet rs = statement.executeQuery(getCustomerInfo);
//            while(rs.next()){
//                System.out.println("First Name: " + rs.getString(2));
//                System.out.println("Last Name: " + rs.getString(3));
//                System.out.println("Phone Number: " + rs.getString(4));
//                System.out.println("Email: " + rs.getString(5));
//                System.out.println("Address: " + rs.getString(6));
//                System.out.println("------------------------------\n");
