package bits.squad.orders;

import bits.squad.employee.hander.Handler;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class Order {
    int id;
    boolean isLate;
    long timestamp;
    int price;
    Status status;

    static int idCount = 1;

    ReentrantReadWriteLock readWriteLock;
    Handler handler;
    ArrayList<Item<Integer>> items;

    public enum Status {
        TO_COOK,
        COOKING,
        TO_HANDLE,
        HANDLING,
        DONE,
        COUNTED
    }

    public Order(ArrayList<Item<Integer>> items) {
        this.items = items;
        this.items.forEach((item) -> {
            price += item.getIntPrice();
        });
        isLate = false;
        timestamp = System.currentTimeMillis();
        status = Status.TO_COOK;
        id = idCount++;
        readWriteLock = new ReentrantReadWriteLock();
    }

    public ArrayList<Item<Integer>> getItems() {
        return items;
    }

    public void addItem(Item<Integer> item) {
        items.add(item);
        price += item.getPrice();
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLateTrue() {
        isLate = true;
        price *= 0.8;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Status getStatus() {
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            return status;
        } finally {
            readLock.unlock();
        }
    }

    public void setStatus(Status status) {
        Lock readLock = readWriteLock.writeLock(); //safety, cause 2 threads are working with order
        readLock.lock();
        try {
            this.status = status;
        } finally {
            readLock.unlock();
        }
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + id + "]:\tprice:" + price + "zl\t--" + status + "--\titems:" + items.size()
                + "\twas ordered " + (int) ((System.currentTimeMillis() - timestamp) / 60_000)
                + " mins ago\t" + "\t" + (isLate ? "late" : "") + (handler != null ? "Handler:" + handler.getName() : "");
    }
}
