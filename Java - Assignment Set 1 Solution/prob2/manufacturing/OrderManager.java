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
    //private List<Semaphore> SemLocks;   // Semaphore locks for the socks

   /* private void createClothLocks() {
        SemLocks = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Semaphore SemLock = new Semaphore(1);
            SemLocks.add(SemLock);
        }
    }
    */

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
        //createClothLocks();
    }

    void Package(int n, boolean sealed) {
        //boolean success1 = SemLocks.get(0 + n).tryAcquire();
        //boolean success2 = SemLocks.get(6).tryAcquire();
        //boolean success3 = SemLocks.get(4 + n).tryAcquire();
        //if(success1 && success2 && success3) {
        synchronized (Packaged[n]) {
            synchronized (Godown[n]) {
                Packaged[n] += 1;
                if (sealed) {
                    Godown[n] += 1;
                }
                // Packaging complete
            }
        }
            
        //}
        //SemLocks.get(0 + n).release();
        //SemLocks.get(6).release();
        //SemLocks.get(4 + n).release();
        //return flag;
    }

    void Seal(int n, boolean packaged) {
        //boolean success1 = SemLocks.get(2 + n).tryAcquire();
        //boolean success2 = SemLocks.get(6).tryAcquire();
        //boolean success3 = SemLocks.get(4 + n).tryAcquire();
        //if(success1 && success2 && success3) {
        synchronized (Sealed[n]) {
            synchronized (Godown[n]) {
                Sealed[n] += 1;
                if (packaged) {
                    Godown[n] += 1;
                    // Sealing complete
                }
            }
        }
            
        //}
        //SemLocks.get(2 + n).release();
        //SemLocks.get(6).release();
        //SemLocks.get(4 + n).release();
        //return flag;
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
