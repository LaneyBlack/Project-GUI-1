package bits.squad.orders;

import bits.squad.Item;

import java.util.ArrayList;

public class DeliveryOrder extends Order{
    private String address;

    public DeliveryOrder(ArrayList<Item> items, String address) {
        super(items);
        this.address = address;
    }
}
