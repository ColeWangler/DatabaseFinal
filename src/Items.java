import java.sql.*;
import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 *
 * @author jacobgerhart
 * @version 2023.12.10
 */
public class Items {

    private static String url = "jdbc:postgresql://localhost:5432/ShoppingCart";
    private static String user = "postgres";
    private static String password = "Archer54";

    public static void addItem() {
        try (Connection conn = establishConnection();
             ) {

            Scanner scan = new Scanner(System.in);
            // Getting item information...
            System.out.print("Please enter the item name: ");
            String itemName = scan.nextLine();
            System.out.print("Please enter the item description: ");
            String description = scan.nextLine();
            System.out.print("Please enter the item cost: ");
            String doubleLine = scan.nextLine();
            double cost = Double.parseDouble(doubleLine);
            

            String insertIntoItems = "INSERT INTO items (item_name, description, cost) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertIntoItems)) {
                pstmt.setString(1, itemName);
                pstmt.setString(2, description);
                pstmt.setDouble(3, cost);

                pstmt.executeUpdate();
            }

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.toString());
        }
    }

    public static void deleteItem(Connection conn){
        try{
            String itemName = readString("Enter name of item to delete: ");
            int itemID = getItemID(itemName, conn);
            PreparedStatement itemStatement = conn.prepareStatement("DELETE FROM items WHERE items.item_id = ?");
            itemStatement.setInt(1, itemID);
            itemStatement.executeUpdate();
        }catch(SQLException sqlex){sqlex.printStackTrace();}
        catch(IllegalArgumentException iae){System.out.println(iae.getMessage());}

    }

    private static Connection establishConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, password);
    }
    
    private static int getItemID(String itemName, Connection conn) throws IllegalArgumentException{
        ResultSet rs;
        int itemID = -1;
        try{
            String idQuery = "SELECT item_id FROM items where lower(item_name) like  ?";
            PreparedStatement idStatement = conn.prepareStatement(idQuery);
            idStatement.setString(1,itemName.toLowerCase());
            rs = idStatement.executeQuery();
            if(!rs.next()) throw new IllegalArgumentException("Item not found in item list.");
            itemID = rs.getInt("item_id");
        }
        catch(SQLException sqlex){}
        if(itemID == -1) throw new IllegalArgumentException("Item not found in item list.");
        return itemID;
    }
    
    private static String readString(String prompt){
        Scanner scan = new Scanner(System.in);
        System.out.print(prompt);
        String returnStr = scan.nextLine().trim();
        while(returnStr.isBlank()){
            System.out.println("No input given. Try again.");
            System.out.print(prompt);
            returnStr = scan.nextLine().trim();
        }
        return returnStr;
    }
}
