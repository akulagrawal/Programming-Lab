package prob1.clothordering;

import java.util.List;
import java.util.concurrent.Semaphore;

/*
 * Order Manager recieves an order and processes it
 * */
class OrderManager {
    // Count of clothes represents the different orders
    private Integer SmallTshirts;
    private Integer MediumTshirts;
    private Integer LargeTshirts;
    private Integer Caps;
    List<Integer> OrderID;
    private Semaphore semS;
    private Semaphore semM;
    private Semaphore semL;
    private Semaphore semC;


    /*
     * Constructor
     * */
    OrderManager(List<Integer> inventory, List<Integer> orderID) {
        SmallTshirts = inventory.get(0);
        MediumTshirts = inventory.get(1);
        LargeTshirts = inventory.get(2);
        Caps = inventory.get(3);
        OrderID = orderID;
        semS = new Semaphore(1);
        semM = new Semaphore(1);
        semL = new Semaphore(1);
        semC = new Semaphore(1);
    }

    /*
     * Recieves an order of some cloth
     * and process it correctly
     * */
    void ManageOrder(int cloth, int num, int orderID) {
        try {
            if (cloth == Constants.SMALL) {
                semS.acquire();
                if (SmallTshirts >= num) {  // have sufficient clothes
                    SmallTshirts -= num;    // num clothes
                    System.out.println(String.format("Order %d successful", orderID));
                    PrintInventory();
                } else { // don't have sufficient clothes of that color
                    System.out.println(String.format("Order %d failed", orderID));
                }
                semS.release();
            } else if (cloth == Constants.MEDIUM) {
                semM.acquire();
                if (MediumTshirts >= num) {  // have sufficient clothes
                    MediumTshirts -= num;    // num clothes
                    System.out.println(String.format("Order %d successful", orderID));
                    PrintInventory();
                } else { // don't have sufficient clothes of that color
                    System.out.println(String.format("Order %d failed", orderID));
                }
            } else if (cloth == Constants.LARGE) {
                semL.acquire();
                if (LargeTshirts >= num) {  // have sufficient clothes
                    LargeTshirts -= num;    // num clothes
                    System.out.println(String.format("Order %d successful", orderID));
                    PrintInventory();
                } else { // don't have sufficient clothes of that color
                    System.out.println(String.format("Order %d failed", orderID));
                }
            } else if (cloth == Constants.CAP) {
                semC.acquire();
                if (Caps >= num) {  // have sufficient clothes
                    Caps -= num;    // num clothes
                    System.out.println(String.format("Order %d successful", orderID));
                    PrintInventory();
                } else { // don't have sufficient clothes of that color
                    System.out.println(String.format("Order %d failed", orderID));
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    // Print the Inventory finally when the program ends.
    void PrintInventory() {
        System.out.println(String.format("T shirt Small: %d\tT shirt Medium: %d\tT shirt Large: %d\tCap: %d", SmallTshirts, MediumTshirts, LargeTshirts, Caps));
    }
}
