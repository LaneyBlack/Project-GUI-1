package bits.squad.employee;

public class Waiter extends Handler {
    public Waiter(int id, String name, String phoneNumber, int salary) {
        super(id, name,phoneNumber, salary,15 * 1000); //ToDo set time
    }
}
