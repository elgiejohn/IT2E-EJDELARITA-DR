package implementation;

import devicerental.config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class history {
    
    public void showRentHistory(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        customers c = new customers();
        
        c.viewCostumer();
        
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
            result.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        
        String[] rentalHeaders = {"ID", "Costumer", "Device", "Due Date", "Date Rented", "Payment Status"};
        String[] rentalColumns = {"r_id", "c_name", "d_name", "r_due_date", "r_rent_date", "r_payment_status"};
        String[] whereValues = {String.valueOf(cid)};

        conf.viewRecords(rentalQuery, rentalHeaders, rentalColumns, whereValues);
    }
}
