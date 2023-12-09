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
                    addStore();
                    break;
                case "C", "c" : 
                    deleteStore();
                    break;
                case"D", "d" :  
                    updateStore();
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
            
            conn.close();
        }
        catch(ClassNotFoundException cnfe){
            System.out.println(cnfe.getMessage());
        }
        catch(SQLException sqlex){
            System.out.println(sqlex.getMessage());
        }
        
    }
    
    private static void addStore(){
        try{
            String storeQuery = "INSERT INTO store (store_name) VALUES (?)";
            String locationQuery = "INSERT INTO store_locations (postal_code, street, city, location_state, zip_code, country, store_id)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            Class.forName("org.postgresql.Driver");
            
            Connection conn = DriverManager.getConnection(url, username, password);
            
            PreparedStatement pStore = conn.prepareStatement(storeQuery);
            PreparedStatement pLocation = conn.prepareStatement(locationQuery);

            //get user input
            String storeName = readString("Enter store name: ");
            int postalCode = readInt("Enter postal code: ");
            String street = readString("Enter street: ");
            String city = readString("Enter city: ");
            String state = readString("Enter state: ");
            int zip = readInt("Enter zip code: ");
            String country = readString("Enter country: ");
            
            //insert new store if not already exists
            if(hasStore(storeName)){
                pStore.setString(1,storeName);
                int storeCount = pStore.executeUpdate();
            }
            
            //get store's primary key
            String query = "SELECT * from store where store_name like ?";
            PreparedStatement pstat = conn.prepareStatement(query);
            pstat.setString(1,storeName);
            ResultSet rs = pstat.executeQuery();
            
            int storeID = -1; //initialize as invalid value
            while(rs.next()) 
                storeID = rs.getInt("store_id");
            
            //insert location
            pLocation.setInt(1, postalCode);
            pLocation.setString(2,street);
            pLocation.setString(3, city);
            pLocation.setString(4, state);
            pLocation.setInt(5, zip);
            pLocation.setString(6, country);
            pLocation.setInt(7, storeID);
            int locationCount = pLocation.executeUpdate();
            
            conn.close();
        }
        catch(ClassNotFoundException cnfe){System.out.println(cnfe.getMessage());}
        catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
    }
    
    private static void updateStore(){
        
    }
    
    private static void deleteStore(){
        int toDelete;
        do{
        toDelete = readInt("Press 1 to delete a location and 2 to delete a store: ");
        }while(toDelete < 1 || toDelete > 2);
        
        try{ 
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(url, username, password);
            
            //Delete store location
            if(toDelete == 1){
                viewStores();
                
                String locationQuery = "DELETE FROM store_locations WHERE postal_code = ? AND street = ? AND"
                        + " city = ? AND location_state = ? AND zip_code = ? AND country = ?";
                PreparedStatement pLocation = conn.prepareStatement(locationQuery);

                //get user input
                int postalCode = readInt("Enter postal code: ");
                String street = readString("Enter street: ");
                String city = readString("Enter city: ");
                String state = readString("Enter state: ");
                int zip = readInt("Enter zip code: ");
                String country = readString("Enter country: ");

                //delete location
                pLocation.setInt(1, postalCode);
                pLocation.setString(2,street);
                pLocation.setString(3, city);
                pLocation.setString(4, state);
                pLocation.setInt(5, zip);
                pLocation.setString(6, country);
                int locationCount = pLocation.executeUpdate();

                conn.close();
                return;
            }
        
            //delete store record(and associated records)
            PreparedStatement sellsStatement = conn.prepareStatement("DELETE FROM sells WHERE sells.store_id = ?");
            PreparedStatement discountStatement = conn.prepareStatement("DELETE FROM discounts WHERE discounts.store_id = ?");
            PreparedStatement locationStatement = conn.prepareStatement("DELETE FROM store_locations WHERE store_locations.store_id = ?");
            PreparedStatement storeStatement = conn.prepareStatement("DELETE FROM store WHERE store.store_id = ?");
            
            String storeName = readString("Enter name of store to delete: ");
            if(!hasStore(storeName)){
                System.out.println("Invalid Store Name.");
                return;
            }
            
            String idQuery = "SELECT store_id FROM store where store_name = ?";
            PreparedStatement idStatement = conn.prepareStatement(idQuery);
            idStatement.setString(1,storeName);
            ResultSet rs = idStatement.executeQuery();
            rs.next();
            int storeID = rs.getInt("store_id");
            
            //delete records from sells, discounts, store_locations, and store
            sellsStatement.setInt(1,storeID);
            discountStatement.setInt(1, storeID);
            locationStatement.setInt(1, storeID);
            storeStatement.setInt(1, storeID);
            
            sellsStatement.executeUpdate();
            discountStatement.executeUpdate();
            locationStatement.executeUpdate();
            storeStatement.executeUpdate();
            
            conn.close();
        
        }
        catch(ClassNotFoundException cnfe){System.out.println(cnfe.getMessage());}
        catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
        
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
            String address = rs.getInt("postal_code") + " " + rs.getString("street")+ " " + rs.getString("city")+ " " + rs.getString("location_state")+ " " + rs.getInt("zip_code")+ " " + rs.getString("country");
            System.out.printf("| %-30s | %-80s |\n", rs.getString("store_name"), address);
            System.out.println(divider);
        }
        
        }
        catch(SQLException sqlex){sqlex.printStackTrace();}
        catch(NullPointerException npe){System.out.println(npe.getMessage());}
    }
    
    private static int readInt(String prompt){
        int returnInt = -1;
        
        do{
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            Scanner scanLine = new Scanner(line);
            try{
                if(scanLine.hasNextInt()){
                    returnInt = Integer.parseInt(line);
                    break;
                }  
            }catch(NumberFormatException nfe){}
            System.out.println("Invalid input: must be an integer.");
        }while(true);
        
        return returnInt;  
    }
    
    private static String readString(String prompt){
        System.out.print(prompt);
        String returnStr = scan.nextLine();
        
        while(returnStr.isBlank()){
            System.out.println("No input given. Try again.");
            System.out.print(prompt);
            returnStr = scan.nextLine();
        }
        
        return returnStr;
    }
    
    public static boolean hasStore(String store){
        boolean hasStore = false;
        
         try{
            String query = "SELECT * FROM store where store_name like ?";
            PreparedStatement pstmt;
            Class.forName("org.postgresql.Driver");
            
            Connection conn = DriverManager.getConnection(url, username, password);
            
            pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.setString(1, store);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) hasStore = true;
            
            conn.close();
        }
        catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
         
         return hasStore;
    }
    
   public static void main(String[] args){
       deleteStore();
   }
    
}
