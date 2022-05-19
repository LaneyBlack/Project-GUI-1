package bits.squad;


import bits.squad.employee.Cook;
import bits.squad.employee.Employee;
import bits.squad.employee.Handler;
import bits.squad.employee.Kitchen;
import bits.squad.orders.DeliveryOrder;
import bits.squad.orders.Order;
import bits.squad.orders.StationaryOrder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static bits.squad.Colors.errorPrint;

public class Restaurant extends Thread {
    private final String name;
    private final String address;
    private final byte tables;
    private long turnOver;
    private Kitchen kitchen;

    private static final int lateTime = 60000 * 15;
    public static final boolean messagesOnStatusChanges = true;
    private static int employeesCount;

    private ArrayList<Item<Integer>> menu;
    private final ArrayDeque<Order> orderQueue;
    private final ArrayList<Order> madeOrders;
    private final ArrayList<Handler> handlers;

    public Restaurant(String name, String address, int tables) {
        this.name = name;
        this.address = address;
        this.tables = (byte) tables;
        this.turnOver = 0;

        employeesCount = 1;

        kitchen = new Kitchen();
        menu = new ArrayList<>();
        orderQueue = new ArrayDeque<>();
        madeOrders = new ArrayList<>();
        handlers = new ArrayList<>();
    }

    public byte getTables() {
        return tables;
    }

    public String getRestName() {
        return name;
    }

    public void startRestaurant() {
        this.start();
        kitchen.start();
    }

    public void close() {
        System.out.println("Closing " + name + "...");
        //ToDo Restaurant Close
        System.out.println(Colors.getTextColor("BLUE") + "--------" + name + " is closed--------" + Colors.getTextColor("RESET"));
    }

    //-------------------------------------------------------Menu-------------------------------------------------------

    public void addToMenu(Item item) {
        menu.add(item);
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
        for (Item<Integer> v : menu)
            fileWriter.append(v.getName()).append("\t").append(v.getDescription()).append("\t").append(String.valueOf(v.isAvailable())).append("\t").append(String.valueOf(v.getPrice())).append("\n");
        fileWriter.close();
    }

    public void readMenuFromText(String path) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        menu = new ArrayList<>();
        String txt;
        while ((txt = bufferedReader.readLine()) != null) {
            String[] values = txt.trim().split("\t");
            menu.add(new Item<>(values[0], values[1], values[2].equals("true"), Integer.parseInt(values[3])));
        }
    }

    //-------------------------------------------------------Employees-------------------------------------------------------
    public Employee getEmployeeById(int id) {
        for (Cook cook : kitchen.getCooks()) {
            if (id == cook.getId())
                return cook;
        }
        for (Handler handler : handlers) {
            if (id == handler.getId())
                return handler;
        }
        return null;
    }

    public Employee getHandlerById(int id) {
        for (Employee handler : handlers) {
            if (id == handler.getId())
                return handler;
        }
        return null;
    }

    public boolean hasNotBusyHandlers(String handlerType) {
        for (Handler handler : handlers) {
            if (handler.getClass().getSimpleName().equals(handlerType) && !handler.isBusy())
                return true;
        }
        return false;
    }

    public int getNewEmployeeId() {
        return employeesCount++;
    }

    public void addEmployee(Employee employee) {
        if (employee.getClass().getSimpleName().equals("Cook"))
            kitchen.addCook((Cook) employee);
        else
            handlers.add((Handler) employee);
    }

    public void deleteEmployee(int id) {
        handlers.removeIf(handler -> id == handler.getId());
        kitchen.getCooks().removeIf(cook -> id == cook.getId());
    }

    public void printAllEmployees() {
        System.out.println("--------Employees--------");
        System.out.println("---Cooks---");
        for (Cook cook : kitchen.getCooks()) {
            //ToDo redefine toString
            System.out.println(cook.getId() + " - " + cook.getName());
            System.out.println("Salary: " + cook.getSalary() + "zl/month\t");
        }
        System.out.println("---Waiters---");
        handlers.forEach(handler -> {
            if (handler.getClass().getSimpleName().equals("Waiter")) {
                System.out.println(handler.getId() + " - " + handler.getName());
                System.out.println("Salary: " + handler.getSalary() + "zl/month\t");
            }
        });
        System.out.println("---Delivery---");
        handlers.forEach(handler -> {
            if (handler.getClass().getSimpleName().equals("Delivery")) {
                System.out.println(handler.getId() + " - " + handler.getName());
                System.out.println("Salary: " + handler.getSalary() + "zl/month\t");
            }
        });
    }

    public void printEmployeeNames() {
        kitchen.getCooks().forEach(cook -> System.out.println(cook.getId() + "---" + cook.getName()));
        handlers.forEach(handler -> System.out.println(handler.getId() + "---" + handler.getName()));
    }

    //-------------------------------------------------------Orders-------------------------------------------------------
    public void makeRandomOrder() {
        Order order;
        ArrayList<Item<Integer>> items = new ArrayList<>();
        for (int i = 0; i < (int) (Math.random() * 10) + 1; i++) //Adding random items from menu
            items.add(menu.get((int) (Math.random() * menu.size())));

        int type = (int) (Math.random() * 2); //Choosing type of the order
        if (type == 0) {
            order = new StationaryOrder(items, (byte) (Math.random() * tables + 1)); //Randomising table number
        } else {
            order = new DeliveryOrder(items, switch ((int) (Math.random() * 5)) { //randomising address
                case 0 -> "ul. Pushkina 2";
                case 1 -> "ul. Chelmska 12";
                case 2 -> "ul. Arkadia 17";
                case 3 -> "ul. Biala 3";
                case 4 -> "ul. Urbanistyczna 26";
                default -> "ul. Impossible 13";
            });
        }
        orderQueue.add(order);
    }

    public void makeAnOrder(Order order) {
        //ToDo refactor tihs method
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
            order.setTimestamp(System.currentTimeMillis());
            orderQueue.add(order);
            System.out.println("Your order id is " + order.getId());
        } catch (IllegalStateException | InputMismatchException e) {
            errorPrint("Invalid order input exception!");
        }
    }

    public void printWaitingOrders() {
        System.out.println("--------WaitingOrders--------");
        orderQueue.forEach(order -> System.out.println("Order Id: " + order.getId() + "---" + order.getStatus()));
    }

    //-------------Thread-------------
    @Override
    public void run() {
        while (!isInterrupted()) {
            //ToDo rewrite RUN!!!
            ArrayList<Order> process = new ArrayList<>();//for safety and no coChanging values
            if (!kitchen.isBusy() && orderQueue.peek() != null) //working with Kitchen
                if (orderQueue.peek().getStatus() == Order.Status.TO_COOK) {
                    kitchen.process(orderQueue.peek());
                } else {
                    for (Order order : orderQueue) {
                        if (order.getStatus() == Order.Status.TO_COOK) {
                            if (!kitchen.isBusy()) {
                                kitchen.process(order);
                            }
                            break;
                        }
                    }
                }
            for (Order order : orderQueue) {
                if (order.getStatus() == Order.Status.TO_HANDLE
                        && hasNotBusyHandlers(order.getClass().getSimpleName().equals("DeliveryOrder") ? "Delivery" : "Waiter"))
                    process.add(order);
                else if (order.getStatus() == Order.Status.DONE)
                    process.add(order);
                if (!order.isLate() && (System.currentTimeMillis() - order.getTimestamp() >= lateTime)) {
                    process.add(order);
                    //ToDo order deque
                }
            }

            for (Order order : process) {
                if (order.getStatus() == Order.Status.TO_COOK) {
                    order.setLateTrue();
                    orderQueue.remove(order);
                    orderQueue.addFirst(order);
                } else if (order.getStatus() == Order.Status.TO_HANDLE) {
                    if (order.getClass().getSimpleName().equals("DeliveryOrder"))
                        handlerProcessOrder(order, "Delivery");
                    else if (order.getClass().getSimpleName().equals("StationaryOrder"))
                        handlerProcessOrder(order, "Waiter");
                } else if (order.getStatus() == Order.Status.DONE) {
                    turnOver += order.getPrice();
                    order.setStatus(Order.Status.COUNTED);
                    System.out.println("Order " + order.getId() + " was handed over.");
                    orderQueue.remove(order);
                }
            }
//                    case DONE -> {
//                        turnOver += order.getPrice();
//                        order.setStatus(Order.Status.COUNTED);
//                        System.out.println("Order " + order.getId() + " was handed over.");
//                        delete.add(order);
////                        System.out.println("Do you want to leave some tips? (-1/amount)");
////                        Scanner in = new Scanner(System.in);
////                        int tips = in.nextInt();
////                        if (tips > 0)
////                            getEmployeeById(order.getHandlerId()).addTips(tips);
////                        else if (order.isLate())
////                            System.out.println("We are sorry for long arrangement. Your opinion means a lot to us.");
//                    }
        }
    }

    public void handlerProcessOrder(Order order, String handlerType) {
        for (Handler handler : handlers) {
            if (!handler.isBusy() && handler.getClass().getSimpleName().equals(handlerType)) {
                order.setStatus(Order.Status.HANDLING);
                handler.setOrder(order);
                handler.process();
                break;
            }
        }
    }
}
