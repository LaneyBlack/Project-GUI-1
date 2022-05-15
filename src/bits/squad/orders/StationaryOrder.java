package bits.squad.orders;


import bits.squad.Item;

import java.util.ArrayList;

public class StationaryOrder extends Order{
    private byte tableNumber;

    public StationaryOrder(ArrayList<Item> items, byte tableNumber) {
        super(items);
        this.tableNumber = tableNumber;
    }
}
