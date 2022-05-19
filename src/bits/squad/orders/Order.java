package bits.squad.orders;

import bits.squad.Item;
import bits.squad.employee.Handler;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class Order {
    int id;
    boolean isLate;
    long timestamp;
    int price;
    Status status;

    static int idCount = 0;

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
        isLate = false;
        timestamp = System.currentTimeMillis();
        status = Status.TO_COOK;
        items.forEach((item) -> {
            price += item.getIntPrice();
        });
        id = idCount++;
        readWriteLock = new ReentrantReadWriteLock();
    }

    public ArrayList<Item<Integer>> getItems() {
        return items;
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
        Lock readLock = readWriteLock.writeLock();
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

}
