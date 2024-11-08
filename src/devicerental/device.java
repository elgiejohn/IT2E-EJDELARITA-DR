package devicerental;

import implementation.devices;

public class device {
    public void Device(){
        config conf = new config();
        devices d = new devices();
        
        boolean isSelected = false;
        
        do{
            d.viewDevice();
            
            System.out.println("\nDevice:");
            System.out.println("1. Add Device");
            System.out.println("2. Edit Device");
            System.out.println("3. Remove Device");
            System.out.println("4. Select Device");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");
            int option = conf.validateInt();

            switch(option){
                case 1:
                    d.addDevice();
                    break;
                case 2:
                    d.editDevice();
                    break;
                case 3:
                    d.removeDevice();
                    break;
                case 4:
                    d.viewIndivReport();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default: 
                    System.out.println("Error, invalid option");
            }
        } while(!isSelected);
    }
}
