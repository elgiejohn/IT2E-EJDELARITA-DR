package devicerental;

import implementation.customers;

public class costumer {
    public void Costumer(){
        config conf = new config();
        customers c = new customers();
        
        boolean isSelected = false;
        
        do{
            c.viewCostumer();
                            
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
                    c.addCostumer();
                    break;
                case 2:
                    c.editCostumer();
                    break;
                case 3:
                    c.removeCostumer();
                    break;
                case 4:
                    c.viewIndivReport();
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
