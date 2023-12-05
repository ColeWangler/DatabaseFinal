
import java.util.Scanner;

/**
 *
 * @author andrew.horner
 * @version 2023.12.02
 */
public class Store {
    
    private static int store_id;
    private static String store_name;
    private static Address store_address;
    private static int addresss_id;
    private static int store_address_id;
    
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
        Scanner scan = new Scanner(System.in);
        
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
}
