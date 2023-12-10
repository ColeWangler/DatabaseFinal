import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 *
 * @author jacobgerhart
 * @version 2023.12.09
 */
public class Items {

    private static String url = "jdbc:postgresql://localhost:5432/ShoppingCart";
    private static String user = "postgres";
    private static String password = "Archer54";

    public static void addItem() {
        try (Connection conn = establishConnection();
             Scanner scan = new Scanner(System.in)) {

            // Getting item information...
            System.out.println("Please enter the item name: ");
            String itemName = scan.nextLine();
            System.out.println("Please enter the item description: ");
            String description = scan.nextLine();
            System.out.println("Please enter the item cost: ");
            double cost = scan.nextDouble();

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

    public static void viewCart() {
        try (Connection conn = establishConnection()) {
            DecimalFormat moneyFormat = new DecimalFormat("$0.00");

            String viewItems = "SELECT * FROM items";

            try (Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery(viewItems)) {

                while (rs.next()) {
                    System.out.println("Item ID: " + rs.getInt("item_id"));
                    System.out.println("Item Name: " + rs.getString("item_name"));
                    System.out.println("Description: " + rs.getString("description"));
                    System.out.println("Cost: " + moneyFormat.format(rs.getDouble("cost")));
                    System.out.println("----------------------\n");
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.toString());
        }
    }

    public static void deleteItem() {
        try (Connection conn = establishConnection();
             Scanner scan = new Scanner(System.in)) {

            System.out.println("Please enter the item ID to delete: ");
            int itemId = scan.nextInt();

            String deleteItem = "DELETE FROM items WHERE item_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(deleteItem)) {
                pstmt.setInt(1, itemId);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Item deleted successfully.");
                } else {
                    System.out.println("Item not found with ID: " + itemId);
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.toString());
        }
    }

    private static Connection establishConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}