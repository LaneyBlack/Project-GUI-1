package bits.squad.orders;


import java.util.ArrayList;

public class StationaryOrder extends Order{
    private final byte tableNumber;

    public StationaryOrder(ArrayList<Item<Integer>> items, byte tableNumber) {
        super(items);
        this.tableNumber = tableNumber;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\ttableNumber: " + tableNumber;
    }
}
