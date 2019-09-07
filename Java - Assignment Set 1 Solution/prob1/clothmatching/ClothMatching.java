package prob1.clothmatching;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/*
* Cloth ordering machine
* There is a heap of new labelled clothes. Each cloth is among one of four items: Small Tshirt, Medium Tshirt,
Large Tshirt and Cap. There are several robots deployed to process the orders, one order per robot at a time and
pass it to a matching machine. The matching machine forwards the order to a order manager robot.
The order manager robot then completes the request and updates number of clothes.
**/
public class ClothMatching {
    private int NumberOfRobots; // Number of robot arms
    private final List<Integer> Cloths;  // Cloths buffer
    private final List<Integer> Num;  // Num buffer
    private final List<Integer> OrderID;  // OrderID buffer
    private List<Robot> Robots; // List of Robot Threads
    private MatchingMachine matchingMachine;
    private OrderManager orderManager;  // Order manager
    private List<Semaphore> SemLocks;   // Semaphore locks for the clothes
    private Random rand = new Random(); // random generator

    /*
     * Start the robots and wait for each robot to terminate.
     * Print the final clothes shelves count at the end.
     * */
    private void startMachine() throws InterruptedException {
        // Print the collected clothes count
        orderManager.PrintInventory();

        // Activate all the robot arms
        for (Robot robot : Robots) {
            robot.start();
        }

        // wait for all robotarms to stop
        for (Robot robot : Robots) {
            robot.join();
        }

        // Print the collected clothes count
        orderManager.PrintInventory();
    }

    /*
     * Create semaphores for each cloth.
     * */
    private void createClothLocks() {
        SemLocks = new ArrayList<>();
        for (int i = 0; i < Cloths.size(); i++) {
            Semaphore SemLock = new Semaphore(1);
            SemLocks.add(SemLock);
        }
    }

    /*
     * Create the required number of robot arms
     * */
    private void createRobotArms() {
        Robots = new ArrayList<>();
        for (int i = 0; i < NumberOfRobots; i++) {
            Robot robot = new Robot(this, this.matchingMachine, i);
            Robots.add(robot);
        }
    }

    /*
     * Constructor
     * */
    private ClothMatching(int numberOfRobots, List<Integer> clothes, List<Integer> num, List<Integer> inventory, List<Integer> orderID) {
        NumberOfRobots = numberOfRobots;
        Cloths = clothes;
        Num = num;
        OrderID = orderID;

        // Create Order manager
        orderManager = new OrderManager(inventory, orderID);

        // Create matching machine
        matchingMachine = new MatchingMachine(orderManager);

        // Create robotic arms
        createRobotArms();

        // Create locks
        createClothLocks();
    }

    /*
     * Pick a cloth
     * If no cloth is left, return NULL_SOCK
     * */
    int[] PickCloth() {
        int[] cloth = new int[3];
        int n = -1;
        boolean flag = false;

        // Generate a random number and lock that cloth
        synchronized (Cloths) {
            synchronized (Num) {
                synchronized (OrderID) {
                    if (Cloths.size() > 0) {
                        n = rand.nextInt(Cloths.size());
                    } else {
                        flag = true;
                    }
                }
            }
        }

        // If no cloth is left to return
        if (flag) {
            cloth[0] = Constants.NULL_SOCK;
            cloth[1] = Constants.NULL_SOCK;
            cloth[2] = Constants.NULL_SOCK;
            return cloth;
        }
        boolean success = SemLocks.get(n).tryAcquire();

        // Lock the cloth and return the locked object
        // Release the lock so that it can be acquired by some other thread
        synchronized (Cloths) {
            synchronized (Num) {
                synchronized (OrderID) {
                    if (success && (n < Cloths.size())) {
                        cloth[0] = Cloths.get(n);
                        cloth[1] = Num.get(n);
                        cloth[2] = OrderID.get(n);
                        Cloths.remove(n);
                        Num.remove(n);
                        OrderID.remove(n);
                        SemLocks.get(n).release();
                        return cloth;
                    }
                }
            }
        } 
        return PickCloth();
    }

    /*
     * The main function to run Cloth Ordering Machine
     * */
    public static void main(String[] args) throws IOException, InterruptedException {
        // File to take input from
        File file = new File(Constants.INPUT_FILE);
        Scanner scanner = new Scanner(file);
        List<Integer> inventory = new ArrayList<>();
        for(int i=0;i<4;i++){
            inventory.add(scanner.nextInt());
        }

        // Take the number of robots as input
        int number_of_robots = scanner.nextInt();

        // Take the list of clothes as input
        List<Integer> orderID = new ArrayList<>();
        List<Integer> clothes = new ArrayList<>();
        List<Integer> num = new ArrayList<>();
        while (scanner.hasNextInt()) {
            orderID.add(scanner.nextInt());
            clothes.add(scanner.nextInt());
            num.add(scanner.nextInt());
        }

        // Create a cloth matching machine
        ClothMatching clothMatching = new ClothMatching(number_of_robots, clothes, num, inventory, orderID);

        // Start the Clothmatching
        clothMatching.startMachine();
    }


}
