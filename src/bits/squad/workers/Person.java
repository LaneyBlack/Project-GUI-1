package bits.squad.workers;

public abstract class Person extends Thread {
    int workerId;
    int salary;

    public Person( int workerId, String name,int salary) {
        super(name);
        this.workerId = workerId;
        this.salary = salary;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
