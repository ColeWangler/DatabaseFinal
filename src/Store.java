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
        
        
        System.out.println("Aa) View Stores");
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
            
            switch(input.trim()){
                case "A", "a" : 
                    viewStores();
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
    
    private static void viewStores(){
        
        System.out.print("Enter the name of a store or type All to view all stores: ");
        String store = scan.nextLine();
        
        
        try{
            String query = "";
            PreparedStatement pstmt;
            Class.forName("org.postgresql.Driver");
            
            Connection conn = DriverManager.getConnection(url, username, password);
            
            if(store.trim().equalsIgnoreCase("all") || store.trim().equals("")){
                query = "SELECT * FROM store join store_locations "
                    + "on store.store_id = store_locations.store_id";
                pstmt = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                printStores(pstmt);
                
                return;
            }
            
            query = "SELECT * FROM store join store_locations "
                    + "on store.store_id = store_locations.store_id"
                    + " where lower(store_name) like ?";
            pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.setString(1, store);
            printStores(pstmt);
        }
        catch(ClassNotFoundException cnfe){
            System.out.println(cnfe.getMessage());
        }
        catch(SQLException sqlex){
            System.out.println(sqlex.getMessage());
        }
        
    }
    
    private static void addStore(){
        
    }
    
    private static void updateStore(){
        
    }
    
    private static void deleteStore(){
        
    }
    
    private static void printStores(PreparedStatement query){
        
        try{
        ResultSet rs = query.executeQuery();
        
        if(!rs.next())
            throw new NullPointerException("Invalid Store Name.");
        rs.previous();
        
        String divider = "+--------------------------------+----------------------------------------------------------------------------------+";
        System.out.println(divider);
        System.out.printf("| %-30s | %-80s |\n", "Store Name", "Store Address");
        System.out.println(divider);
        
        while(rs.next()){
            Address storeAddress = new Address(rs.getInt("postal_code"), rs.getString("street"), rs.getString("city"), rs.getString("location_state"), rs.getInt("zip_code"), rs.getString("country"));
            System.out.printf("| %-30s | %-80s |\n", rs.getString("store_name"), storeAddress.toString());
            System.out.println(divider);
        }
        
        }
        catch(SQLException sqlex){sqlex.printStackTrace();}
        catch(NullPointerException npe){System.out.println(npe.getMessage());}
    }
    
}
