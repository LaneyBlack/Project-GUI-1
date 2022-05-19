package bits.squad.employee.hander;

public class Waiter extends Handler {
    public Waiter(int id, String name, String phoneNumber, int salary) {
        super(id, name,phoneNumber, salary,30 * 1000); //30 * 1000
    }
}
