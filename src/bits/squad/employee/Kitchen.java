package bits.squad.employee;

import bits.squad.orders.Item;
import bits.squad.orders.Order;

import java.util.ArrayList;

import static bits.squad.Restaurant.messagesOnStatusChanges;

public class Kitchen extends Thread {
    private long processTime;
    private boolean isBusy;
    private ArrayList<Cook> cooks;
    private Order order;


    public Kitchen() {
        this.cooks = new ArrayList<>();
        this.processTime = 30 * 1000; //30*1000
    }

    public void addCook(Cook cook) {
        cooks.add(cook);
        processTime = (30 * 1000) / cooks.size();
    }

    public void process(Order order) {
        setOrder(order);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (order != null && !isBusy && order.getStatus() == Order.Status.TO_COOK) {
                isBusy = true;
                order.setStatus(Order.Status.COOKING);
                if (messagesOnStatusChanges)
                    System.out.println("Kitchen started cooking order[" + order.getId() + "]");
                for (Item<Integer> item : order.getItems())
                    try {
                        Thread.sleep(processTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                for (Cook cook : cooks)
                    cook.countOrder();
                if (messagesOnStatusChanges)
                    System.out.println("Order[" + order.getId() + "] was cooked");
                order.setStatus(Order.Status.TO_HANDLE);
                isBusy = false;
            }
        }
    }

    public ArrayList<Cook> getCooks() {
        return cooks;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isBusy() {
        return isBusy;
    }
}
