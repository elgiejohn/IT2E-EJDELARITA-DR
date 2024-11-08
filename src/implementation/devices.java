package implementation;

import devicerental.config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class devices {
    
    public void addDevice(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Device Name: ");
        String dname = sc.nextLine();
        
        System.out.print("Device Type: ");
        String dtype = sc.nextLine();
        
        System.out.print("Price: ");
        double price = conf.validateDouble();

        String sql = "INSERT INTO device (d_name, d_type, d_price) VALUES (?, ?, ?)";

        conf.addRecord(sql, dname, dtype, price);
    }
    
    public void editDevice(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter device ID to edit: ");
        int did = conf.validateInt();
        
        while(conf.getSingleValue("SELECT d_id FROM device WHERE d_id = ?", did) == 0){
            System.out.print("ID doesn't exist, try again: ");
            did = conf.validateInt();
        }
        
        System.out.print("New Device Name: ");
        sc.nextLine();
        String newName = sc.nextLine();
        
        System.out.print("New Device Type: ");
        String newType = sc.nextLine();
        
        System.out.print("New Price: ");
        double newPrice = conf.validateDouble();
        
        String sqlUpdate = "UPDATE device SET d_name = ?, d_type = ?, d_price = ? WHERE d_id = ?";

        conf.updateRecord(sqlUpdate, newName, newType, newPrice, did);
    }
    
    public void removeDevice(){
        config conf = new config();
        
        System.out.print("Enter device ID to delete: ");
        int did = conf.validateInt();
        
        while(conf.getSingleValue("SELECT d_id FROM device WHERE d_id = ?", did) == 0){
            System.out.print("ID doesn't exist, try again: ");
            did = conf.validateInt();
        }
        
        String sqlDelete = "DELETE FROM device WHERE d_id = ?";
        
        conf.deleteRecord(sqlDelete, did);
    }
    
    public void viewDevice(){
        config conf = new config();
        
        String deviceQuery = "SELECT * FROM device";
        String[] deviceHeaders = {"ID", "Name", "Type", "Price"};
        String[] deviceColumns = {"d_id", "d_name", "d_type", "d_price"};
        String[] whereValues = null;

        conf.viewRecords(deviceQuery, deviceHeaders, deviceColumns, whereValues);
    }
    
    public void viewIndivReport(){
        config conf = new config();
        
        System.out.print("Enter Device ID: ");
        int did = conf.validateInt();
        
        while(conf.getSingleValue("SELECT d_id FROM device WHERE d_id = ?", did) == 0){
            System.out.print("ID doesn't exist, try again: ");
            did = conf.validateInt();
        }
        
        try{
            PreparedStatement findRow = conf.connectDB().prepareStatement("SELECT * FROM device WHERE d_id = ?");
            findRow.setInt(1, did);
            
            try (ResultSet result = findRow.executeQuery()) {
                String deviceName = result.getString("d_name");
                int deviceID = result.getInt("d_id");
                String deviceType = result.getString("d_type");
                double devicePrice = result.getDouble("d_price");
                
                System.out.println("\nSelected ID: "+deviceID);
                System.out.println("Name: "+deviceName);
                System.out.println("Type: "+deviceType);
                System.out.println("Price: "+devicePrice);
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
