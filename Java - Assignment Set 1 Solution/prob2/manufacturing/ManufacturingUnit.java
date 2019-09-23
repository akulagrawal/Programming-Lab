package prob2.manufacturing;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class ManufacturingUnit {
    private final int NumberOfRobots; // Number of robot arms
    private Integer B1Unfin;          // Number of B1 bottles in Unfinished Tray
    private Integer B2Unfin;          // Number of B2 bottles in Unfinished Tray
    private Integer BPackChance;      // 0/1: Stores if Packaging Unit chooses B1/B2 from Unfinished Tray in next pick
    private Integer BSealChance;      // 0/1: Stores if sealing Unit chooses B1/B2 from Unfinished Tray in next pick
    private Integer B1PackBuf;        // Number of B1 bottles in Packaging Tray  
    private Integer B2PackBuf;        // Number of B2 bottles in Packaging Tray
    private Integer Priority;         // 0/1: Priority of B1/B2 Packaging trays
    private ArrayBlockingQueue<Integer> SealBuf; // Buffer of Sealing Tray
    private final Integer Time;       // Total Time available
    private long startTime;           // Start time

    private List<PackagingRobot> PackagingRobots; // List of Robot Threads
    private List<SealingRobot> SealingRobots; // List of Robot Threads
    private OrderManager orderManager;  // Order managerr

    /*
     * Start the robots and wait for each robot to terminate.
     * Print the final socks shelves count at the end.
     * */
    private void startMachine() throws InterruptedException {
        // Activate all the robot arms
        for (PackagingRobot robot : PackagingRobots) {
            robot.start();
        }
        for (SealingRobot robot : SealingRobots) {
            robot.start();
        }


        // wait for all robotarms to stop
        for (PackagingRobot robot : PackagingRobots) {
            robot.join();
        }
        for (SealingRobot robot : SealingRobots) {
            robot.join();
        }


        // Print the collected socks count
        orderManager.PrintInventory();
    }


    /*
     * Create the required number of robot arms
     * */
    private void createRobotArms() {
        PackagingRobots = new ArrayList<>();
        for (int i = 0; i < NumberOfRobots; i++) {
            PackagingRobot robot = new PackagingRobot(this, this.orderManager, i);
            PackagingRobots.add(robot);
        }
        SealingRobots = new ArrayList<>();
        for (int i = 0; i < NumberOfRobots; i++) {
            SealingRobot robot = new SealingRobot(this, this.orderManager, i);
            SealingRobots.add(robot);
        }
    }

    /*
     * Constructor
     * */
    private ManufacturingUnit(int numberOfRobots, int b1Unfin, int b2Unfin, int time) {
        NumberOfRobots = numberOfRobots;
        B1Unfin = b1Unfin;
        B2Unfin = b2Unfin;
        Priority = 0;
        BPackChance = 0;
        BSealChance = 0;
        B1PackBuf = 0;
        B2PackBuf = 0;
        Time = time;
        SealBuf = new ArrayBlockingQueue<Integer>(2);
        startTime = System.currentTimeMillis();

        // Create matching machine
        orderManager = new OrderManager();

        // Create robotic arms
        createRobotArms();
    }

    void addToTray(int state, int n) {
        if (state == Constants.STATE_PACK) {
            if (n == Constants.B1) {
                try {
                    synchronized (B1PackBuf) {
                        if (B1PackBuf == Constants.MAX_B1_PACK) {
                            B1PackBuf.wait();
                        }
                        B1PackBuf++;
                    }
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            else {
                try {
                    synchronized (B2PackBuf) {
                        if (B2PackBuf == Constants.MAX_B2_PACK) {
                            B2PackBuf.wait();
                        }
                        B2PackBuf++;
                    }
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }

        }
        else {
            try {
                synchronized(SealBuf) {
                    if (SealBuf.size() >= Constants.MAX_SEAL_BUF) {  
                        SealBuf.wait(); //wait for the queue to become empty
                    }
                    SealBuf.put(n);
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    int choose(int state, int a, int b) {
        int n;
        if(state == 0) {
            n = BPackChance;
            BPackChance = 1 - BPackChance;
        }
        else {
            n = BSealChance;
            BSealChance = 1 - BSealChance;
        }
        if (n == 0) {
            if (B1Unfin > 0) {
                return 0;
            }
            else if (B2Unfin > 0) {
                return 1;
            }
            return -1;
        }
        else {
            if (B2Unfin > 0) {
                return 1;
            }
            if (B1Unfin > 0) {
                return 0;
            }
            return -1;
        }
    }

    /*
    returns
    0/1: B1/B2 picked from Unfinished Tray
    2/3: B1/B2 coming from Sealing Unit to Packaging Unit
    4/5: B1/B2 coming from Packaging Unit to Sealing Unit
    */
    int PickBottle(int state) {
        int n = -1;

        if (state == Constants.STATE_PACK) {
            try {
                synchronized (B1PackBuf) {
                    synchronized (B2PackBuf) {
                        synchronized (B1Unfin) {
                            synchronized (B2Unfin) {

                                if(Priority == 0) {
                                    Priority = 1;
                                    if(B1PackBuf > 0) {
                                        n = 2;
                                        B1PackBuf--;
                                        if(B1PackBuf == Constants.MAX_B1_PACK - 1)
                                            B1PackBuf.notify();
                                    }
                                    else if(B2PackBuf > 0) {
                                        n = 3;
                                        B2PackBuf--;
                                        if(B2PackBuf == Constants.MAX_B2_PACK - 1)
                                            B2PackBuf.notify();
                                    }
                                    else {
                                        n = choose(state, B1Unfin, B2Unfin);
                                        if(n == 0) {
                                            B1Unfin--;
                                        }
                                        else if(n == 1) {
                                            B2Unfin--;
                                        }
                                    }
                                }
                                else {
                                    Priority = 0;
                                    if(B2PackBuf > 0) {
                                        n = 3;
                                        B2PackBuf--;
                                        if(B2PackBuf == Constants.MAX_B2_PACK - 1)
                                            B2PackBuf.notify();
                                    }
                                    else if(B1PackBuf > 0) {
                                        n = 2;
                                        B1PackBuf--;
                                        if(B1PackBuf == Constants.MAX_B1_PACK - 1)
                                            B1PackBuf.notify();
                                    }
                                    else {
                                        n = choose(state, B1Unfin, B2Unfin);
                                        if(n == 0) {
                                            B1Unfin--;
                                        }
                                        else if(n == 1) {
                                            B2Unfin--;
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
        else {
            synchronized (B1Unfin) {
                synchronized (B2Unfin) {

                    synchronized(SealBuf) {
                        if(SealBuf.size() > 0) {
                            try {
                                int m = SealBuf.take();
                                if (SealBuf.size() == Constants.MAX_SEAL_BUF - 1) {
                                    SealBuf.notify();
                                }
                                n = 4 + m;
                            }
                            catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                        else {
                            n = choose(state, B1Unfin, B2Unfin);
                            if(n == 0) {
                                B1Unfin--;
                            }
                            else if(n == 1) {
                                B2Unfin--;
                            }
                        }
                    }

                }
            }
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        if(((state == 0) && (totalTime > Time - 150)) || ((state == 1) && (totalTime > Time - 250))) {
            n = -1;
        }
        return n;
    }

    /*
     * The main function to run the machine
     * */
    public static void main(String[] args) throws IOException, InterruptedException {
        // File to take input from
        File file = new File(Constants.INPUT_FILE);
        Scanner scanner = new Scanner(file);

        // Take the input
        int numB1 = scanner.nextInt();
        int numB2 = scanner.nextInt();
        int time = scanner.nextInt();
        int number_of_robots = 1;

        // Create a machine
        ManufacturingUnit unit = new ManufacturingUnit(number_of_robots, numB1, numB2, time*100);

        unit.startMachine();
    }


}
