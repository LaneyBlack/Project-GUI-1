package bits.squad.orders;

import bits.squad.Item;
import bits.squad.Restaurant;

import java.util.ArrayList;

public abstract class Order {
    ArrayList<Item> items;
    boolean isLate;
    long timestamp;
    int tips;
    Status status;
    enum Status {
        ORDERED,
        IN_PROGRESS,
        DONE;
    }

    public Order(ArrayList<Item> items) {
        this.items = items;
        isLate = false;
        timestamp = System.currentTimeMillis();
        status = Status.ORDERED;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTips() {
        return tips;
    }

    public void setTips(int tips) {
        this.tips = tips;
    }
}
