package bits.squad;


import bits.squad.orders.Order;
import bits.squad.workers.Person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Restaurant {
    private String name;
    private String address;
    private byte tables;
    private byte cooksNum;
    private ArrayList<Item> menu;
    private HashMap<Status, Order> orders;
    private HashMap<String, ArrayList<Person>> workers;

    enum Status {
        ORDERED,
        IN_PROGRESS,
        DONE
    }

    public Restaurant(String name, String address, int tables, int cooksNum) {
        this.name = name;
        this.address = address;
        this.tables = (byte) tables;
        this.cooksNum = (byte) cooksNum;
        menu = new ArrayList<>();
        orders = new HashMap<>();
        workers = new HashMap<>();
        workers.put("Cook", new ArrayList<>());
        workers.put("Delivery", new ArrayList<>());
        workers.put("Waiter", new ArrayList<>());
    }

    public void close() {
        System.out.println("Closing restaurant");
        //ToDo Restaurant Close
        System.out.println(name + " is closed");
    }

    public String getName() {
        return name;
    }

    //-------------------------------------------------------Menu-------------------------------------------------------

    public void addToMenu(Item val) {
        menu.add(val);
    }

    public void deleteFromMenu(int index) {
        menu.remove(index - 1);
    }

    public void markAsOutOfStock(int index) {
        menu.get(index - 1).setAvailable(false);
    }

    public void markAsInStock(int index) {
        menu.get(index - 1).setAvailable(true);
    }

    public void printMenu() {
        System.out.println(Colors.getTextColor("blue") + "\t\t\t---Menu---" + Colors.getTextColor("RESET"));
        for (int i = 0; i < menu.size(); i++)
            System.out.println((i + 1) + ". " + menu.get(i));
    }

    public void printDishes() {
        for (int i = 0; i < menu.size(); i++)
            System.out.println((i + 1) + " - " + menu.get(i).getName());
    }

    public void printAvailableMenu() {
        System.out.println(Colors.getTextColor("blue") + "\t\t\t---Available Menu---" + Colors.getTextColor("RESET"));
        for (int i = 0; i < menu.size(); i++)
            if (menu.get(i).isAvailable())
                System.out.println((i + 1) + ". " + menu.get(i));
    }

    public void writeMenuToText() throws IOException {
        FileWriter fileWriter = new FileWriter("src/bits/squad/menu/menu.txt");
        for (Item v : menu)
            fileWriter.append(v.getName()).append("\t").append(v.getDescription()).append("\t").append(String.valueOf(v.isAvailable())).append("\t").append(String.valueOf(v.getPrice())).append("\n");
        fileWriter.close();
    }

    public void readMenuFromText(String path) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        menu = new ArrayList<>();
        String txt;
        while ((txt = bufferedReader.readLine()) != null) {
            String[] values = txt.trim().split("\t");
            menu.add(new Item(values[0], values[1], values[2].equals("true"), Integer.parseInt(values[3])));
        }
    }

    //-------------------------------------------------------Workers-------------------------------------------------------

    public int newWorkerId() {
        AtomicInteger counter = new AtomicInteger();
        workers.forEach((k, v) -> counter.addAndGet(v.size()));
        return counter.get() + 1;
        //ToDo if you remove worker with id 4. When there is 5 workers and then try to add another one. You'll have two workers with same id.
        //Probably better to create new const and create a constructor without Id.
    }

    public void addWorker(Person person) {
        workers.get(person.getClass().getSimpleName()).add(person);
    }

    public void deleteWorker(int id) {
        workers.forEach((k, array) -> array.removeIf(person -> person.getWorkerId() == id));
    }

    public void printAllWorkers() {
        System.out.println(Colors.getTextColor("BLUE") + "\t\t\t---Workers---" + Colors.getTextColor("RESET"));
        workers.forEach((k, array) -> {
            System.out.println("---" + k + "---");
            for (Person v : array) {
                System.out.println(v.getWorkerId() + " - " + v.getName());
                System.out.println("Salary: " + v.getSalary() + "zl/month\t");
            }
        });
    }

    public void printWorkerNames(){
        workers.forEach((k, array) -> {
            for (Person v : array) {
                System.out.println(v.getWorkerId() + "---" + v.getName());
            }
        });
    }

    //-------------------------------------------------------Orders-------------------------------------------------------
//    public void makeStationaryOrder(Order order, int table) {
////        orders.put(Status.ORDERED,Order order);
//        workers.forEach((k,v)->{
//            if (k && v.getClass().getName().equals("Cook")){
//
//            }
//        });
//    }
}
