package bits.squad.employee.hander;


public class Deliveryman extends Handler {
    public Deliveryman(int id, String name, String phoneNumber, int salary) {
        super(id, name, phoneNumber, salary,1000*60*2); //1000*60*2
    }
}
