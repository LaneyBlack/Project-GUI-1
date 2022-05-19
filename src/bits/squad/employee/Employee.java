package bits.squad.employee;

public abstract class Employee {
    int id;
    String name;
    String phoneNumber;
    int salary;
    int madeOrders;


    public Employee(int employeeId, String name, String phoneNumber, int salary) {
        this.id = employeeId;
        this.name = name;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        madeOrders = 0;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void countOrder() {
        madeOrders++;
    }

    @Override
    public String toString() {
        return id +"." + name + "\tphone:" + phoneNumber + '\n'
                + salary + "zl/month\torders made:" + madeOrders;
    }
}
