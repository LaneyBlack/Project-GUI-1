package bits.squad;


import bits.squad.orders.Order;
import bits.squad.workers.Person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import static bits.squad.Colors.errorPrint;

public class Restaurant {
    private String name;
    private String address;
    private byte tables;
    private static int workersCount;
    private ArrayList<Item> menu;
    private ArrayList<Order> orders;
    private HashMap<String, ArrayList<Person>> workers;

    public Restaurant(String name, String address, int tables) {
        this.name = name;
        this.address = address;
        this.tables = (byte) tables;
        menu = new ArrayList<>();
        orders = new ArrayList<>();
        workersCount = 1;
        workers = new HashMap<>();
        workers.put("Cook", new ArrayList<>());
        workers.put("Delivery", new ArrayList<>());
        workers.put("Waiter", new ArrayList<>());
    }

    public byte getTables() {
        return tables;
    }

    public String getName() {
        return name;
    }

    public void close() {
        System.out.println("Closing " + name + "...");
        //ToDo Restaurant Close
        System.out.println(Colors.getTextColor("BLUE") + "--------" + name + " is closed--------" + Colors.getTextColor("RESET"));
    }

    //-------------------------------------------------------Menu-------------------------------------------------------

    public void addToMenu(Item val) {
        menu.add(val);
    }

    public Item getItemById(int id) {
        return menu.get(id - 1);
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
        System.out.println("--------Menu--------");
        for (int i = 0; i < menu.size(); i++)
            System.out.println((i + 1) + ". " + menu.get(i));
    }

    public void printDishes() {
        for (int i = 0; i < menu.size(); i++)
            System.out.println((i + 1) + " - " + menu.get(i).getName());
    }

    public void printAvailableMenu() {
        System.out.println("--------Available Menu--------");
        for (int i = 0; i < menu.size(); i++)
            if (menu.get(i).isAvailable()) {
                System.out.println((i + 1) + ". " + menu.get(i));
            }
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

    public int getNewWorkerId() {
        return workersCount++;
//        AtomicInteger counter = new AtomicInteger();
//        workers.forEach((k, v) -> counter.addAndGet(v.size()));
//        return counter.get() + 1;
//        //Probably better to create new const and create a constructor without Id.
    }

    public void addWorker(Person person) {
        workers.get(person.getClass().getSimpleName()).add(person);
    }

    public void deleteWorker(int id) {
        workers.forEach((k, array) -> array.removeIf(person -> person.getWorkerId() == id));
    }

    public void printAllWorkers() {
        System.out.println("--------Workers--------");
        workers.forEach((k, array) -> {
            System.out.println("---" + k + "---");
            for (Person v : array) {
                System.out.println(v.getWorkerId() + " - " + v.getName());
                System.out.println("Salary: " + v.getSalary() + "zl/month\t");
            }
        });
    }

    public void printWorkerNames() {
        workers.forEach((k, array) -> {
            for (Person v : array) {
                System.out.println(v.getWorkerId() + "---" + v.getName());
            }
        });
    }

    //-------------------------------------------------------Orders-------------------------------------------------------
    public void makeAnOrder(Order order) {
        Scanner in = new Scanner(System.in);
        try {
            String input = "-a";
            while (input.equals("-a")) {
                System.out.println("Please select item id to order:");
                int itemIndex = in.nextInt();
                System.out.println("Please enter count of items:");
                int times = in.nextInt();
                in.nextLine(); //Scanner goes to new line
                if ((itemIndex - 1 < 0 || itemIndex > menu.size()) || times < 1)
                    throw new InputMismatchException("Invalid order input");
                for (int i = 0; i < times; i++)
                    order.getItems().add(menu.get(itemIndex - 1));
                System.out.println("Anything else? (type `-a` to order more or type anything to continue)");
                input = in.nextLine().trim();
            }
            orders.add(order);
            //ToDo Streams
            System.out.println("Order was successfully made.");
        } catch (IllegalStateException | InputMismatchException e) {
            errorPrint("Invalid order input exception!");
        }
    }
}
