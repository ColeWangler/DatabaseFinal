import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author andrew.horner
 * @version 2023.12.02
 */
public class Store {
    private static Scanner scan = new Scanner(System.in);
    
    public static void viewStores(Connection conn){
        String store = readString("Enter the name of a store or type All to view all stores: ");
        try{
            PreparedStatement pstmt;
            if(store.trim().equalsIgnoreCase("all") || store.trim().equals("")){
                String query = "SELECT * FROM store join store_locations on store.store_id = store_locations.store_id";
                pstmt = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                printStoreLocations(pstmt);
                return;
            }
            pstmt = conn.prepareStatement("SELECT * FROM store join store_locations on store.store_id = store_locations.store_id where lower(store_name) like ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.setString(1, store.toLowerCase());
            printStoreLocations(pstmt);
        }
        catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
    }
    
    public static void viewStoreItems(Connection conn){
        try{
            String query = "SELECT st.store_name, i.item_name, i.cost, d.amount_off FROM store st join sells se on st.store_id = se.store_id " +
                           "join items i on se.item_id = i.item_id left join discounts d on st.store_id = d.store_id AND i.item_id = d.item_id " +
                           "WHERE st.store_id = ? order by st.store_name";
            PreparedStatement storeItems = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String storeName = readString("Enter store name to view items: ");
            if(!hasStore(storeName, conn)){
                System.out.println("Invalid Store Name.");
                return;
            }
            int storeID = getStoreID(storeName, conn);
            storeItems.setInt(1, storeID);
            printStoreItems(storeItems);
        }
        catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
    }
    
    public static void editInventory(Connection conn){
        try{
            int addOrDelete;
            do{addOrDelete = readInt("Press 1 to add item or 2 to delete item: ");}while(addOrDelete < 1 || addOrDelete > 2);
            String storeName = readString("Enter store name to edit items: ");
            if(!hasStore(storeName,conn)){
                System.out.println("Invalid Store Name.");
                return;
            }
            int storeID = getStoreID(storeName, conn);
            
            if(addOrDelete == 1){
                String addItem = readString("Enter name of item to add: ");
                int itemID = getItemID(addItem, conn);
                PreparedStatement insert = conn.prepareStatement("INSERT INTO sells (item_id, store_id) VALUES (?,?)");
                insert.setInt(1, itemID);
                insert.setInt(2,storeID);
                insert.executeUpdate();
                conn.close();
                return;
            }
            
            String deleteItem = readString("Enter name of item to delete: ");
            int itemID = getItemID(deleteItem, conn);
            PreparedStatement delete = conn.prepareStatement("DELETE FROM sells WHERE item_id = ? AND store_id = ?");
            delete.setInt(1, itemID);
            delete.setInt(2,storeID);
            delete.executeUpdate();
            conn.close();
        }
        catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
        catch(IllegalArgumentException iae){System.out.println(iae.getMessage());}
    }
    
    public static void addStore(Connection conn){
        try{
            PreparedStatement pStore = conn.prepareStatement("INSERT INTO store (store_name) VALUES (?)");
            PreparedStatement pLocation = conn.prepareStatement("INSERT INTO store_locations (postal_code, street, city, location_state, zip_code, country, store_id) VALUES (?, ?, ?, ?, ?, ?, ?)");
            //get user input
            String storeName = readString("Enter store name: ");            
            //insert new store if not already exists
            if(!hasStore(storeName,conn)){
                pStore.setString(1,storeName);
                pStore.executeUpdate();
            }
            int storeID = getStoreID(storeName, conn);
            //insert location
            setPreparedLocation(pLocation);
            pLocation.setInt(7, storeID);
            pLocation.executeUpdate();
        }
        catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
    }
    
    public static void deleteStore(Connection conn){
        int toDelete;
        do{toDelete = readInt("Press 1 to delete a location and 2 to delete a store: ");}while(toDelete < 1 || toDelete > 2);
        try{
            //Delete store location
            if(toDelete == 1){
                PreparedStatement pLocation = conn.prepareStatement("DELETE FROM store_locations WHERE postal_code = ? AND street = ? AND city = ? AND location_state = ? AND zip_code = ? AND country = ?");
                setPreparedLocation(pLocation);
                conn.close();
                return;
            }
        
            //delete store record(and associated records)
            PreparedStatement sellsStatement = conn.prepareStatement("DELETE FROM sells WHERE sells.store_id = ?");
            PreparedStatement discountStatement = conn.prepareStatement("DELETE FROM discounts WHERE discounts.store_id = ?");
            PreparedStatement locationStatement = conn.prepareStatement("DELETE FROM store_locations WHERE store_locations.store_id = ?");
            PreparedStatement storeStatement = conn.prepareStatement("DELETE FROM store WHERE store.store_id = ?");
            
            String storeName = readString("Enter name of store to delete: ");
            if(!hasStore(storeName, conn)){
                System.out.println("Invalid Store Name.");
                return;
            }
            int storeID = getStoreID(storeName, conn);
            
            //delete records from sells, discounts, store_locations, and store
            sellsStatement.setInt(1,storeID);
            discountStatement.setInt(1, storeID);
            locationStatement.setInt(1, storeID);
            storeStatement.setInt(1, storeID);
            
            sellsStatement.executeUpdate();
            discountStatement.executeUpdate();
            locationStatement.executeUpdate();
            storeStatement.executeUpdate();
        }catch(SQLException sqlex){System.out.println(sqlex.getMessage());}
    }
    
    private static void printStoreLocations(PreparedStatement query){
        try{
            ResultSet rs = query.executeQuery();
            if(!rs.next())throw new NullPointerException("Invalid Store Name.");
            rs.previous();

            String divider = "+--------------------------------+----------------------------------------------------------------------------------+";
            System.out.println(divider);
            System.out.printf("| %-30s | %-80s |\n", "Store Name", "Store Address");
            System.out.println(divider); while(rs.next()){
                String address = rs.getInt("postal_code") + " " + rs.getString("street")+ " " + rs.getString("city")+ " " + rs.getString("location_state")+ " " + rs.getInt("zip_code")+ " " + rs.getString("country");
                System.out.printf("| %-30s | %-80s |\n", rs.getString("store_name"), address);
                System.out.println(divider);
            }
        }catch(SQLException sqlex){sqlex.printStackTrace();}
         catch(NullPointerException npe){System.out.println(npe.getMessage());}
    }
    
    private static void printStoreItems(PreparedStatement query){
        try{
            ResultSet rs = query.executeQuery();
            if(!rs.next()) throw new NullPointerException("Invalid Store Name.");
            rs.previous();

            String divider = "+-----------------+----------------------+---------+--------------+";
            System.out.println(divider);
            System.out.printf("| %-15s | %-20s | %-7s | %-12s |\n", "Store", "Item", "Cost", "Amount Off");
            System.out.println(divider);
            while(rs.next()){
                System.out.printf("| %-15s | %-20s | $%6.2f | $%11.2f |\n", rs.getString("store_name"), rs.getString("item_name"), rs.getDouble("cost"), rs.getDouble("amount_off"));
                System.out.println(divider);
            }
        }catch(SQLException sqlex){sqlex.printStackTrace();}
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
        String returnStr = scan.nextLine().trim();
        while(returnStr.isBlank()){
            System.out.println("No input given. Try again.");
            System.out.print(prompt);
            returnStr = scan.nextLine().trim();
        }
        return returnStr;
    }
    
    public static boolean hasStore(String storeName, Connection conn){
        boolean hasStore = false;
         try{
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM store where lower(store_name like) ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.setString(1, storeName.toLowerCase());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) hasStore = true;
         }catch(SQLException sqlex){sqlex.printStackTrace();}
         return hasStore;
    }
    
    //This method should ONLY be used after checking storeName with hasStore(storeName)
    private static int getStoreID(String storeName, Connection conn){
        ResultSet rs;
        int storeID = -1;
        try{
            String idQuery = "SELECT store_id FROM store where lower(store_name) like ?";
            PreparedStatement idStatement = conn.prepareStatement(idQuery);
            idStatement.setString(1,storeName.toLowerCase());
            rs = idStatement.executeQuery();
            rs.next();
            storeID = rs.getInt("store_id");
        }catch(SQLException sqlex){sqlex.printStackTrace();}
        return storeID;
    }
    
    private static int getItemID(String itemName, Connection conn) throws IllegalArgumentException{
        ResultSet rs;
        int itemID = -1;
        try{
            String idQuery = "SELECT item_id FROM items where item_name = ?";
            PreparedStatement idStatement = conn.prepareStatement(idQuery);
            idStatement.setString(1,itemName);
            rs = idStatement.executeQuery();
            rs.next();
            itemID = rs.getInt("item_id");
        }
        catch(SQLException sqlex){}
        if(itemID == -1) throw new IllegalArgumentException("Item not found in item list.");
        return itemID;
    }
    
    private static void setPreparedLocation(PreparedStatement pLocation) throws SQLException{
        int postalCode = readInt("Enter postal code: ");
        String street = readString("Enter street: ");
        String city = readString("Enter city: ");
        String state = readString("Enter state: ");
        int zip = readInt("Enter zip code: ");
        String country = readString("Enter country: ");
        pLocation.setInt(1, postalCode);
        pLocation.setString(2, street);
        pLocation.setString(3, city);
        pLocation.setString(4, state);
        pLocation.setInt(5, zip);
        pLocation.setString(6,country);
    } 
}
