package prob1.clothmatching;

/*
 * pass the sock pair to the order manager
 * */
class MatchingMachine {
    private OrderManager orderManager;

    MatchingMachine(OrderManager orderManager) {
        this.orderManager = orderManager;
    }


    void MatchCloth(int sock,int num, int orderID) {
        if (sock == Constants.SMALL)
            orderManager.ManageOrder(sock, num, orderID);
        else if (sock == Constants.MEDIUM)
            orderManager.ManageOrder(sock, num, orderID);
        else if (sock == Constants.LARGE)
            orderManager.ManageOrder(sock, num, orderID);
        else if (sock == Constants.CAP)
            orderManager.ManageOrder(sock, num, orderID);
    }
}
