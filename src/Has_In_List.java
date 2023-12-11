import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author Cole Wangler
 */
public class Has_In_List {
        //Database Connection Information
    private static String url = "jdbc:postgresql://localhost:5432/ShoppingCart";
    private static String user = "postgres";
    private static String password = "Archer54";  
    
    public static void addItemToCart(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            Scanner scan = new Scanner(System.in);

            //Getting the user information
            System.out.println("Please enter the Customer ID: ");
            int customer_id = scan.nextInt();
            System.out.println("Please enter the Item ID: ");
            int item_id = scan.nextInt();
            System.out.println("Please enter the quantity of item: ");
            int quantity = scan.nextInt();
            String insertIntoList = "INSERT INTO has_in_list (customer_id, item_id, item_quantity) Values (?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(insertIntoList);
            pstmt.setInt(1, customer_id);
            pstmt.setInt(2, item_id);
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();

        } catch (ClassNotFoundException e) {
            System.out.println("Cannot Load Driver");
        } catch (SQLException sqle) {
            System.out.println(sqle.toString());
        }
    }
    
    public static void removeItemFromCart(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            Scanner scan = new Scanner(System.in);

            //Getting the user information
            System.out.println("Please enter the Customer ID: ");
            int customer_id = scan.nextInt();
            System.out.println("Please enter the Item ID: ");
            int item_id = scan.nextInt();
            String deleteFromList = "Delete From has_in_list where customer_id = ? AND item_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(deleteFromList);
            pstmt.setInt(1, customer_id);
            pstmt.setInt(2, item_id);

            pstmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot Load Driver");
        } catch (SQLException sqle) {
            System.out.println(sqle.toString());
        }
    }
    
    public static void updateQuantityOfItem(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            Scanner scan = new Scanner(System.in);

            //Getting the user information
            System.out.println("Please enter the Customer ID: ");
            int customer_id = scan.nextInt();
            System.out.println("Please enter the Item ID: ");
            int item_id = scan.nextInt();
            System.out.println("Please enter quantity: ");
            int quantity = scan.nextInt();
            String updateQuantity = "Update has_in_list set item_quantity = ? where customer_id = ? AND item_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(updateQuantity);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, customer_id);
            pstmt.setInt(3, item_id);

            pstmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot Load Driver");
        } catch (SQLException sqle) {
            System.out.println(sqle.toString());
        }
    }
}
