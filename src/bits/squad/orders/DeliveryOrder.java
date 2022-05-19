package bits.squad.orders;

import bits.squad.Item;

import java.util.ArrayList;

public class DeliveryOrder extends Order{
    private final String address;

    public DeliveryOrder(ArrayList<Item<Integer>> items, String address) {
        super(items);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
