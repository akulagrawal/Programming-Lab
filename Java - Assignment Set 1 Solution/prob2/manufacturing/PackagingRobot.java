package prob2.manufacturing;

/*
 * Robot is used to pick up a bottle from the buffer and
 * pass it to the order manager
 * */
class PackagingRobot extends Thread {
    private OrderManager orderManager;
    private ManufacturingUnit unit;

    /*
     * Constructor
     * */
    PackagingRobot(ManufacturingUnit unit, OrderManager orderManager, int name) {
        super();
        setName("P" + String.valueOf(name));
        this.orderManager = orderManager;
        this.unit = unit;
    }

    @Override
    public void run() {
        while (true) {
            // Pick a bottle from the buffer
            int n = unit.PickBottle(0);
            int m = n%2;
            if (n == -1) {   // If no bottle left
                // Stop the thread
                System.out.println("Thread " + getName() + " Stopped!");
                stop();
            }
            System.out.println("B" + (m+1) + " recieved by Package Thread - " + getName() + " n = " + String.valueOf(n));
            // Pass the picked bottle to the order manager
            orderManager.Package(m, (n >= 2));
            try {
                sleep(200);
            }
            catch (Exception e) {
            }
            if (n < 2)
                unit.addToTray(1, m);
        }
    }
}
