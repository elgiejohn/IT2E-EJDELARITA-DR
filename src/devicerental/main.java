package devicerental;

import java.util.Scanner;
import implementation.history;

public class main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        costumer c = new costumer();
        device d = new device();
        rental r = new rental();
        config conf = new config();
        history h = new history();
        
        boolean isSelected = false;
        
        do{
            System.out.println("\nDevice Rental");
            System.out.println("1. Rental");
            System.out.println("2. Device");
            System.out.println("3. Costumer");
            System.out.println("4. View Rent History");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");
            int option = conf.validateInt();

            switch(option){
                case 1:
                    r.Rental();
                    break;
                case 2:
                    d.Device();
                    break;
                case 3:
                    c.Costumer();
                    break;
                case 4:
                    h.showRentHistory();
                    break;
                case 5:
                    System.out.print("Confirm exit? (yes/no): ");
                    String confirm = sc.next();
                    
                    if(confirm.contains("y"))
                        System.exit(0);
                    break;
                default:
                    System.out.println("Error, invalid option");
            }
        } while(!isSelected);
    }
}
