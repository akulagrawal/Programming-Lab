package prob1.clothmatching;

/*
 * Robot is used to pick up a cloth from the buffer and
 * pass it to the matching machine.
 * */
class Robot extends Thread {
    private ClothMatching clothMatching;  // To acces the buffer of clothes and pick one cloth
    private MatchingMachine matchingMachine;    // To pass the picked cloth

    /*
     * Constructor
     * */
    Robot(ClothMatching clothMatching, MatchingMachine matchingMachine, int name) {
        super();
        setName(String.valueOf(name));
        this.clothMatching = clothMatching;
        this.matchingMachine = matchingMachine;
    }

    @Override
    public void run() {
        while (true) {
            // Pick a cloth from the buffer
            int[] clothReceived = clothMatching.PickCloth();
            if (clothReceived[0] == Constants.NULL_SOCK) {   // If no cloth left
                // Stop the thread
                System.out.println("Thread " + getName() + " Stopped!");
                stop();
            }
            System.out.println("Cloth of color " + clothReceived[0] + " recieved by Thread - " + getName());
            // Pass the picked cloth the cloth matcher
            matchingMachine.MatchCloth(clothReceived[0], clothReceived[1], clothReceived[2]);
        }
    }
}
