package devicerental;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class device {
    public void Device(){
        Scanner sc = new Scanner(System.in);
        
        boolean isSelected = false;
        
        do{
            System.out.println("\nDevice:");
            System.out.println("1. Add Device");
            System.out.println("2. Edit Device");
            System.out.println("3. Remove Device");
            System.out.println("4. View Device");
            System.out.println("5. Select Device");
            System.out.println("6. Exit");
            System.out.print("Enter option: ");
            int option = sc.nextInt();

            switch(option){
                case 1:
                    addDevice();
                    break;
                case 2:
                    editDevice();
                    break;
                case 3:
                    removeDevice();
                    break;
                case 4:
                    viewDevice();
                    break;
                case 5:
                    viewIndivReport();
                    break;
                case 6:
                    isSelected = true;
                    break;
                default: 
                    System.out.println("Error, invalid option");
            }
        } while(!isSelected);
    }
    
    public void addDevice(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Device Name: ");
        String dname = sc.nextLine();
        
        System.out.print("Device Type: ");
        String dtype = sc.nextLine();
        
        System.out.print("Price: ");
        double price = sc.nextDouble();

        String sql = "INSERT INTO device (d_name, d_type, d_price) VALUES (?, ?, ?)";

        conf.addRecord(sql, dname, dtype, price);
    }
    
    public void editDevice(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter device ID to edit: ");
        int did = sc.nextInt();
        
        while(conf.getSingleValue("SELECT d_id FROM device WHERE d_id = ?", did) == 0){
            System.out.print("ID doesn't exist, try again: ");
            did = sc.nextInt();
        }
        
        System.out.print("New Device Name: ");
        sc.nextLine();
        String newName = sc.nextLine();
        
        System.out.print("New Device Type: ");
        String newType = sc.nextLine();
        
        System.out.print("New Price: ");
        double newPrice = sc.nextDouble();
        
        String sqlUpdate = "UPDATE device SET d_name = ?, d_type = ?, d_price = ? WHERE d_id = ?";

        conf.updateRecord(sqlUpdate, newName, newType, newPrice, did);
    }
    
    public void removeDevice(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter device ID to delete: ");
        int did = sc.nextInt();
        
        while(conf.getSingleValue("SELECT d_id FROM device WHERE d_id = ?", did) == 0){
            System.out.print("ID doesn't exist, try again: ");
            did = sc.nextInt();
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
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Device ID: ");
        int did = sc.nextInt();
        
        while(conf.getSingleValue("SELECT d_id FROM device WHERE d_id = ?", did) == 0){
            System.out.print("ID doesn't exist, try again: ");
            did = sc.nextInt();
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
