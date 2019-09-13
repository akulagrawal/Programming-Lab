package prob1.clothordering;

/*
 * Robot is used to pick up a cloth from the buffer and
 * pass it to the order manager
 * */
class Robot extends Thread {
    private ClothOrdering clothOrdering;  // To acces the buffer of clothes and pick one cloth
    private OrderManager orderManager;    // To pass the picked cloth

    /*
     * Constructor
     * */
    Robot(ClothOrdering clothOrdering, OrderManager orderManager, int name) {
        super();
        setName(String.valueOf(name));
        this.clothOrdering = clothOrdering;
        this.orderManager = orderManager;
    }

    @Override
    public void run() {
        while (true) {
            // Pick a cloth from the buffer
            int[] clothReceived = clothOrdering.PickCloth();
            if (clothReceived[0] == Constants.NULL_SOCK) {   // If no cloth left
                // Stop the thread
                System.out.println("Thread " + getName() + " Stopped!");
                stop();
            }
            System.out.println("Cloth " + clothReceived[0] + " recieved by Thread - " + getName());
            // Pass the picked cloth the order manager
            orderManager.ManageOrder(clothReceived[0], clothReceived[1], clothReceived[2]);
        }
    }
}
