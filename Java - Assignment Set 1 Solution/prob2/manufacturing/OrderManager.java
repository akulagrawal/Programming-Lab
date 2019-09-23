package prob2.manufacturing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
/*
 * Order Manager recieves an order and processes it
 * */
class OrderManager {
    private Integer Godown[];
    private Integer Packaged[];
    private Integer Sealed[];

    /*
     * Constructor
     * */
    OrderManager() {
        Packaged = new Integer[2];
        Sealed = new Integer[2];
        Godown = new Integer[2];
        for(int i=0;i<2;i++){
            Packaged[i] = 0;
            Sealed[i] = 0;
            Godown[i] = 0;
        }
    }

    void Package(int n, boolean sealed) {
        synchronized (Packaged[n]) {
            synchronized (Godown[n]) {
                Packaged[n] += 1;
                if (sealed) {
                    Godown[n] += 1;
                }
                // Packaging complete
            }
        }
    }

    void Seal(int n, boolean packaged) {
        synchronized (Sealed[n]) {
            synchronized (Godown[n]) {
                Sealed[n] += 1;
                if (packaged) {
                    Godown[n] += 1;
                    // Sealing complete
                }
            }
        }
    }

    // Print the Inventory finally when the program ends.
    void PrintInventory() {
        System.out.println(String.format("Bottle: 1\tStatus: Packaged \tCount: %d\n", Packaged[0]));
        System.out.println(String.format("Bottle: 1\tStatus: Sealed   \tCount: %d\n", Sealed[0]));
        System.out.println(String.format("Bottle: 1\tStatus: In Godown\tCount: %d\n", Godown[0]));
        System.out.println(String.format("Bottle: 2\tStatus: Packaged \tCount: %d\n", Packaged[1]));
        System.out.println(String.format("Bottle: 2\tStatus: Sealed   \tCount: %d\n", Sealed[1]));
        System.out.println(String.format("Bottle: 2\tStatus: In Godown\tCount: %d\n", Godown[1]));
    }

}
