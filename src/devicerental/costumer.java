package devicerental;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class costumer {
    public void Costumer(){
        config conf = new config();
        
        boolean isSelected = false;
        
        do{
            viewCostumer();
                            
            System.out.println("\nCostumer:");
            System.out.println("1. Add Costumer");
            System.out.println("2. Edit Costumer");
            System.out.println("3. Remove Costumer");
            System.out.println("4. Select Costumer");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");
            int option = conf.validateInt();

            switch(option){
                case 1:
                    addCostumer();
                    break;
                case 2:
                    editCostumer();
                    break;
                case 3:
                    removeCostumer();
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
    
    public void addCostumer(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Costumer Name: ");
        String fname = sc.next();
        System.out.print("Costumer Address: ");
        String address = sc.next();
        
        System.out.print("Costumer Phone Number: ");
        String pnumber = sc.next();
        
        System.out.print("Costumer Email: ");
        String email = sc.next();

        String sql = "INSERT INTO costumer (c_name, c_address, c_phone_number, c_email) VALUES (?, ?, ?, ?)";

        conf.addRecord(sql, fname, address, pnumber, email);
    }
    
    public void editCostumer(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter costumer id to update: ");
        int cid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT c_id FROM costumer WHERE c_id = ?", cid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            cid = conf.validateInt();
        }
        
        System.out.print("New Costumer Name: ");
        String newFname = sc.next();
        
        System.out.print("New Costumer Address: ");
        String newAddress = sc.next();
        
        System.out.print("New Costumer Number: ");
        String newNumber = sc.next();
        
        System.out.print("New Costumer Email: ");
        String newEmail = sc.next();

        String sqlUpdate = "UPDATE costumer SET c_name = ?, c_address = ?, c_phone_number = ?, c_email = ? WHERE c_id = ?";

        conf.updateRecord(sqlUpdate, newFname, newAddress, newNumber, newEmail, cid);
    }
    
    public void removeCostumer(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter costumer id to remove: ");
        int cid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT c_id FROM costumer WHERE c_id = ?", cid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            cid = conf.validateInt();
        }
        
        String sqlDelete = "DELETE FROM costumer WHERE c_id = ?";
        
        conf.deleteRecord(sqlDelete, cid);
    }
    
    public void viewCostumer(){
        config conf = new config();
        
        String costumerQuery = "SELECT * FROM costumer";
        String[] costumerHeaders = {"ID", "Name", "Address", "Phone Number", "Email"};
        String[] costumerColumns = {"c_id", "c_name", "c_address", "c_phone_number", "c_email"};
        String[] whereValues = null;

        conf.viewRecords(costumerQuery, costumerHeaders, costumerColumns, whereValues);
    }
    
    public void viewIndivReport(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Costumer ID: ");
        int cid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT c_id FROM costumer WHERE c_id = ?", cid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            cid = conf.validateInt();
        }
        
        try{
            PreparedStatement findRow = conf.connectDB().prepareStatement("SELECT * FROM costumer WHERE c_id = ?");
            findRow.setInt(1, cid);
            
            try (ResultSet result = findRow.executeQuery()) {
                String costumerName = result.getString("c_name");
                int costumerID = result.getInt("c_id");
                String costumerAddress = result.getString("c_address");
                String costumerNumber = result.getString("c_phone_number");
                String costumerEmail = result.getString("c_email");
                
                System.out.println("\nSelected ID: "+costumerID);
                System.out.println("Name: "+costumerName);
                System.out.println("Address: "+costumerAddress);
                System.out.println("Phone Number: "+costumerNumber);
                System.out.println("Email: "+costumerEmail);
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
