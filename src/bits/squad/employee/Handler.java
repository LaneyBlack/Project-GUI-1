package bits.squad.employee;

import bits.squad.orders.Order;

import static bits.squad.Restaurant.messagesOnStatusChanges;

public abstract class Handler extends Employee {
    double tips;
    Order order;
    Thread thread;
    final int processTime;

    public Handler(int employeeId, String name, String phoneNumber, int salary, int processTime) {
        super(employeeId, name, phoneNumber, salary);
        tips = 0;
        this.processTime = processTime;
        thread = new Thread();
    }

    public void addTips(double tips) {
        this.tips += tips;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void process() {
        thread = new Thread(() -> {
            if (messagesOnStatusChanges)
                System.out.println("Started handling " + order.getId() + " by " + id);
            order.setHandler(this);
            try {
                Thread.sleep(processTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countOrder();
            if (messagesOnStatusChanges)
                System.out.println("Was handled " + order.getId());
            order.setStatus(Order.Status.DONE);
            thread.stop();
        });
        thread.start();
    }

    public boolean isBusy() {
        return thread.isAlive();
    }
}
