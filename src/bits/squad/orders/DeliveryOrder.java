package bits.squad.orders;

import java.util.ArrayList;

public class DeliveryOrder extends Order{
    private final String address;

    public DeliveryOrder(ArrayList<Item<Integer>> items, String address) {
        super(items);
        this.address = address;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\taddress: " + address;
    }
}
