package prob1.clothordering;

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
pass it to a order manager robot. The order manager robot then completes the request and updates number of clothes.
**/
public class ClothOrdering {
    private int NumberOfRobots; // Number of robot arms
    private final List<Integer> Clothes;  // Clothes buffer
    private final List<Integer> Num;  // Num buffer
    private final List<Integer> OrderID;  // OrderID buffer
    private List<Robot> Robots; // List of Robot Threads
    private OrderManager orderManager;  // Order manager
    private Random rand = new Random(); // random generator
    private Semaphore semClothes;
    private Semaphore semNum;
    private Semaphore semOrderID;

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
     * Create the required number of robot arms
     * */
    private void createRobotArms() {
        Robots = new ArrayList<>();
        for (int i = 0; i < NumberOfRobots; i++) {
            Robot robot = new Robot(this, this.orderManager, i);
            Robots.add(robot);
        }
    }

    /*
     * Constructor
     * */
    private ClothOrdering(int numberOfRobots, List<Integer> clothes, List<Integer> num, List<Integer> inventory, List<Integer> orderID) {
        NumberOfRobots = numberOfRobots;
        Clothes = clothes;
        Num = num;
        OrderID = orderID;
        semClothes = new Semaphore(1);
        semNum = new Semaphore(1);
        semOrderID = new Semaphore(1);

        // Create Order manager
        orderManager = new OrderManager(inventory, orderID);

        // Create robotic arms
        createRobotArms();
    }

    void semLock() {
        try {
            semClothes.acquire();
            semNum.acquire();
            semOrderID.acquire();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    void semRelease() {
        try {
            semClothes.release();
            semNum.release();
            semOrderID.release();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
     * Pick a cloth
     * If no cloth is left, return NULL_SOCK
     * */
    int[] PickCloth() {
        int[] cloth = new int[3];
        int n = -1;

        // Generate a random number and lock that cloth
        try {
            semLock();
            if (Clothes.size() > 0) {
                n = rand.nextInt(Clothes.size());
            } else {
                cloth[0] = Constants.NULL_SOCK;
                cloth[1] = Constants.NULL_SOCK;
                cloth[2] = Constants.NULL_SOCK;
                semRelease();
                return cloth;
            }
            if (n < Clothes.size()) {
                cloth[0] = Clothes.get(n);
                cloth[1] = Num.get(n);
                cloth[2] = OrderID.get(n);
                Clothes.remove(n);
                Num.remove(n);
                OrderID.remove(n);
                semRelease();
                return cloth;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        semRelease();
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

        // Create a cloth ordering machine
        ClothOrdering clothOrdering = new ClothOrdering(number_of_robots, clothes, num, inventory, orderID);

        // Start the Clothordering
        clothOrdering.startMachine();
    }


}
