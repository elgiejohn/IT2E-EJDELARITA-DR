package devicerental;

import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        costumer c = new costumer();
        device d = new device();
        rental r = new rental();
        config conf = new config();
        
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
                    showRentHistory();
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
    
    public static void showRentHistory(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Costumer ID: ");
        int cid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT c_id FROM costumer WHERE c_id = ?", cid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            cid = sc.nextInt();
        }
        
        String rentalQuery = "SELECT rental.r_id, c.c_name, d.d_name, rental.r_due_date, r_rent_date, r_payment_status FROM rental "
                + "INNER JOIN costumer c ON rental.c_id = c.c_id "
                + "INNER JOIN device d ON rental.d_id = d.d_id "
                + "WHERE rental.c_id = ?";
        
        try{
            PreparedStatement findRow = conf.connectDB().prepareStatement(rentalQuery);
            findRow.setInt(1, cid);
            
            ResultSet result = findRow.executeQuery();
            
            String renterName = result.getString("c_name");
            int renterID = result.getInt("r_id");
            
            System.out.println("\nSelected ID: "+renterID);
            System.out.println("Renter Name: "+renterName);
            System.out.println("Rent history:");
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        
        String[] rentalHeaders = {"ID", "Costumer", "Device", "Due Date", "Date Rented", "Payment Status"};
        String[] rentalColumns = {"r_id", "c_name", "d_name", "r_due_date", "r_rent_date", "r_payment_status"};
        String[] whereValues = {String.valueOf(cid)};

        conf.viewRecords(rentalQuery, rentalHeaders, rentalColumns, whereValues);
    }
}
