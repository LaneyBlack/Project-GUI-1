package bits.squad.employee;


public class Delivery extends Handler {
    public Delivery(int id, String name, String phoneNumber, int salary) {
        super(id, name, phoneNumber, salary,1000*2); //ToDo set time 1000*60*2
    }
}
