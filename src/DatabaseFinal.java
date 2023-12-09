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
    private static String password = "ColeyJ56!";
    
    
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
                    System.out.println("Please press 6 to Add Item to a customers Cart: ");
                    System.out.println("Please press 7 to Remove Item to a customers Cart: ");
                    System.out.println("Please press 8 to Update Quantity Of Item in a customers Cart: ");
                    
                    menuSelection = scan.nextInt();
                    
                    if( menuSelection > 0 && menuSelection < 9)
                        validOption = true;
                    else
                        System.out.println("Not a valid option.");
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
                    exit = true;
                    break;
                case 5: 
                   Store.menuOperation();
                   break;
                case 6: 
                   Has_In_List.addItemToCart();
                   break;
                case 7: 
                   Has_In_List.removeItemFromCart();
                   break;
                case 8: 
                   Has_In_List.updateQuantityOfItem();
                   break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (exit == false);
    }
}