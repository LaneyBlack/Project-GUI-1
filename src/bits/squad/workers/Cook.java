package bits.squad.workers;

public class Cook extends Person {
    public Cook(int id, String name, int salary) {
        super(id, name, salary);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
