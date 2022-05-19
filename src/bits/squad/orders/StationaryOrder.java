package bits.squad.orders;


import bits.squad.Item;

import java.util.ArrayList;

public class StationaryOrder extends Order{
    private final byte tableNumber;

    public StationaryOrder(ArrayList<Item<Integer>> items, byte tableNumber) {
        super(items);
        this.tableNumber = tableNumber;
    }

    public byte getTableNumber() {
        return tableNumber;
    }
}
