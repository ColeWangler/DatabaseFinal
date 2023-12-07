import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author andrew.horner
 * @version 2023.12.02
 */
public class Store {
    
    //Instance variables
    private  int store_id;
    private  String store_name;
    private  Address store_address;
    private  int addresss_id;
    private  int store_address_id;
    
    //Class util
    private static Scanner scan = new Scanner(System.in);
    private static String url = "jdbc:postgresql://localhost:5432/ShoppingCart";
    private static String username = "postgres";
    private static String password = "Archer54";
    
    private static void printStoreMenu(){
        
        System.out.println("========================================\n"); //divider
        
        
        System.out.println("Aa) View All Stores");
        System.out.println("Bb) Add a Store");
        System.out.println("Cc) Delete a Store");
        System.out.println("Dd) Update a Store Record");
        System.out.println("Qq) Exit Store Menu");
        System.out.print("\nPlease enter an option: ");
        
        
    }
    
    public static void menuOperation(){
        boolean returnToMenu = true;
        
        do{
            printStoreMenu();
            String input = scan.nextLine();
            
            switch(input){
                case "A", "a" : 
                    //functionality;
                    break;
                case"B", "b" : 
                    System.out.println("B was chosen");
                    break;
                case "C", "c" : 
                    //functionality;
                    break;
                case"D", "d" :  
                    //functionality;
                    break;
                case"Q", "q" : 
                    returnToMenu = false; 
                    System.out.println("================================================\n");
                    break;
                default :  
                    System.out.println("Invalid Option. Please try again.\n");
                    break;
            }
            
        }while(returnToMenu);
    }
    
    private void viewStores(){
        
        System.out.print("Enter the name of a store or type All to view all stores: ");
        String store = scan.nextLine();
        
        
        try{
            String query = "";
            PreparedStatement pstmt;
            Class.forName("org.postgresql.Driver");
            
            Connection conn = DriverManager.getConnection(url, username, password);
            
            if(store.trim().equalsIgnoreCase("all") || store.trim().equals("")){
                query = "SELECT * FROM stores join store_locations "
                    + "on stores.store_id = store_locations.store_id";
                pstmt = conn.prepareStatement(query);
                printStores(pstmt);
                
                return;
            }
            
            query = "SELECT * FROM stores join store_locations "
                    + "on stores.store_id = store_locations.store_id"
                    + " where lower(store_name) like '?'";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, store);
            printStores(pstmt);
        }
        catch(ClassNotFoundException cnfe){
            System.out.println(cnfe.getMessage());
        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
        
    }
    
    private void addStore(){
        
    }
    
    private void updateStore(){
        
    }
    
    private void printStores(PreparedStatement query){
        
    }
    
}
