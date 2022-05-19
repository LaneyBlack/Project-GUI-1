package bits.squad.employee;

public abstract class Employee{
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
        madeOrders=0;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getSalary() {
        return salary;
    }

    public int getMadeOrders() {
        return madeOrders;
    }

    public void countOrder(){
        madeOrders++;
    }
}
