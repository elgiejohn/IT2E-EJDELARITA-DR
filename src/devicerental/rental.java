package devicerental;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDate;

public class rental {
    public void Rental(){
        config conf = new config();
        
        boolean isSelected = false;
        
        do{
            viewRent();
            
            System.out.println("\nRental:");
            System.out.println("1. Add Rent");
            System.out.println("2. Edit Rent");
            System.out.println("3. Remove Rent");
            System.out.println("4. Select Rent");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");
            int option = conf.validateInt();

            switch(option){
                case 1:
                    addRent();
                    break;
                case 2:
                    editRent();
                    break;
                case 3:
                    removeRent();
                    break;
                case 4:
                    viewIndivReport();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default: 
                    System.out.println("Error, invalid option");
            }
        } while(!isSelected);
    }
    
    public void addRent(){
        LocalDate date = LocalDate.now();
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Select costumer id: ");
        int cid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT c_id FROM costumer WHERE c_id = ?", cid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            cid = conf.validateInt();
        }
        
        System.out.print("Enter device ID to rent: ");
        int did = conf.validateInt();
        
        while(conf.getSingleValue("SELECT d_id FROM device WHERE d_id = ?", did) == 0){
            System.out.print("ID doesn't exist, try again: ");
            did = conf.validateInt();
        }
        
        System.out.print("Enter Due Date (format: yyyy-mm-dd): ");
        sc.nextLine();
        String due_date = sc.nextLine();
        
        System.out.print("Enter Payment Status: ");
        String status = sc.nextLine();
        
        String sql = "INSERT INTO rental (c_id, d_id, r_due_date, r_rent_date, r_payment_status) VALUES (?, ?, ?, ?, ?)";

        conf.addRecord(sql, cid, did, due_date, date.toString(), status);
    }
    
    public void editRent(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter rent ID to edit: ");
        int rid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT r_id FROM rental WHERE r_id = ?", rid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            rid = conf.validateInt();
        }
        
        System.out.print("Enter New Due Date (format: yyyy-mm-dd): ");
        sc.nextLine();
        String newDue = sc.nextLine();
        
        System.out.print("Enter New Payment Status: ");
        String newStatus = sc.nextLine();
        
        String sql = "UPDATE rental SET r_due_date = ?, r_payment_status = ? WHERE r_id = ?";
        
        conf.updateRecord(sql, newDue, newStatus, rid);
    }
    
    public void removeRent(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter rent ID to remove: ");
        int rid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT r_id FROM rental WHERE r_id = ?", rid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            rid = conf.validateInt();
        }
        
        String sqlDelete = "DELETE FROM rental WHERE r_id = ?";
        
        conf.deleteRecord(sqlDelete, rid);
    }
    
    public void viewRent(){
        config conf = new config();
        
        String rentalQuery = "SELECT rental.r_id, c.c_name, d.d_name, rental.r_due_date, r_rent_date, r_payment_status FROM rental "
                + "INNER JOIN costumer c ON rental.c_id = c.c_id INNER JOIN device d ON rental.d_id = d.d_id";
        
        String[] rentalHeaders = {"ID", "Costumer", "Device", "Date Rented", "Due Date", "Payment Status"};
        String[] rentalColumns = {"r_id", "c_name", "d_name","r_rent_date", "r_due_date", "r_payment_status"};
        String[] whereValues = null;

        conf.viewRecords(rentalQuery, rentalHeaders, rentalColumns, whereValues);
    }
    
    public void viewIndivReport(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Rent ID: ");
        int rid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT r_id FROM rental WHERE r_id = ?", rid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            rid = conf.validateInt();
        }
        
        try{
            String rentalQuery = "SELECT rental.r_id, c.c_name, d.d_name, rental.r_due_date, r_rent_date, r_payment_status FROM rental "
                + "INNER JOIN costumer c ON rental.c_id = c.c_id INNER JOIN device d ON rental.d_id = d.d_id WHERE r_id = ?";
            
            PreparedStatement findRow = conf.connectDB().prepareStatement(rentalQuery);
            findRow.setInt(1, rid);
            
            try (ResultSet result = findRow.executeQuery()) {
                String costumerName = result.getString("c_name");
                int rentID = result.getInt("r_id");
                String deviceName = result.getString("d_name");
                String rentDuration = result.getString("r_due_date");
                String rentDate = result.getString("r_rent_date");
                String rentStatus = result.getString("r_payment_status");
                
                System.out.println("\nRECEIPT: ");
                System.out.println("--------------------------------");
                System.out.println("Transacton Details: ");
                System.out.println("\nSelected ID: "+rentID);
                System.out.println("Name: "+costumerName);
                System.out.println("Device: "+deviceName);
                System.out.println("Date Rented: "+rentDate);
                System.out.println("Due Date: "+rentDuration);
                System.out.println("--------------------------------");
                System.out.println("Payment Status: "+rentStatus);
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
