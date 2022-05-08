package bits.squad.orders;

import bits.squad.Dish;

import java.util.ArrayList;

public class DeliveryOrder extends Order{
    private String address;

    public DeliveryOrder(ArrayList<Dish> dishes, String address) {
        super(dishes);
        this.address = address;
    }
}
