package bits.squad;


import bits.squad.employee.Cook;
import bits.squad.employee.Employee;
import bits.squad.employee.Kitchen;
import bits.squad.employee.hander.Handler;
import bits.squad.orders.DeliveryOrder;
import bits.squad.orders.Item;
import bits.squad.orders.Order;
import bits.squad.orders.StationaryOrder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Restaurant extends Thread {
    private final String name;
    private final String address;
    private final byte tables;
    private long turnOver;
    private final Kitchen kitchen;
    private boolean isPaused;

    private static final int lateTime = 1000 * 60 * 15;
    public static boolean messagesOnStatusChanges = false;
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

    public long getTurnOver() {
        return turnOver;
    }

    public String getAddress() {
        return address;
    }

    public String getRestName() {
        return name;
    }

    public void startRestaurant() {
        if (isPaused)
            pause();
        else {
            System.out.println(Colors.getTextColor("GREEN") + name + " started working" + Colors.getTextColor("RESET"));
            start();
            kitchen.start();
        }

    }

    public void pause() {
        isPaused = !isPaused;
        System.out.println(isPaused ? "Paused" : "Unpaused");
    }

    public void close() {
        System.out.println("Closing " + name + "...");
        kitchen.stop();
        if (hasBusyHandlers()) {
            System.out.println("There is still some deliveries going on.");
        }
        while (hasBusyHandlers()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
        System.out.println("Today turnover is " + turnOver + "zl from " + madeOrders.size() + " orders");
        printRealisedOrders();
        printAllEmployees();
        System.out.println(Colors.getTextColor("BLUE") + "--------" + name + " is closed--------" + Colors.getTextColor("RESET"));
    }

    //-------------------------------------------------------Menu-------------------------------------------------------

    public void addToMenu(Item<Integer> item) {
        menu.add(item);
    }

    public Item<Integer> getItemById(int id) {
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
            if (menu.get(i).isAvailable())
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

    public int getMenuSize() {
        return menu.size();
    }

    //-------------------------------------------------------Employees-------------------------------------------------------

    public int getNewEmployeeId() {
        return employeesCount++;
    }

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

    public void addEmployee(Employee employee) {
        if (employee.getClass().getSimpleName().equals("Cook"))
            kitchen.addCook((Cook) employee);
        else
            handlers.add((Handler) employee);
    }

    public void deleteEmployee(int id) {
        handlers.removeIf(handler -> id == handler.getId());
        kitchen.getCooks().removeIf(cook -> id == cook.getId());
        if (kitchen.getCooks().isEmpty())
            System.out.println("WARNING: There is no cooks left");
        boolean deliveryIsEmpty = true, waitersIsEmpty = true;
        for (Handler handler : handlers) {
            if (handler.getClass().getSimpleName().equals("Waiter"))
                waitersIsEmpty = false;
            else if (handler.getClass().getSimpleName().equals("Deliveryman"))
                deliveryIsEmpty = false;
            if (!deliveryIsEmpty && !waitersIsEmpty)
                break;
        }
        if (deliveryIsEmpty)
            System.out.println("WARNING: There is no deliverymans left");
        if (waitersIsEmpty)
            System.out.println("WARNING: There is no waiters left");
    }

    public boolean hasBusyHandlers() {
        for (Handler handler : handlers) {
            if (handler.isBusy())
                return true;
        }
        return false;
    }

    public boolean hasNotBusyHandlers(String handlerType) {
        for (Handler handler : handlers) {
            if (handler.getClass().getSimpleName().equals(handlerType) && !handler.isBusy())
                return true;
        }
        return false;
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

    public void printAllEmployees() {
        System.out.println("--------Employees--------");
        System.out.println("---Cooks---");
        kitchen.getCooks().forEach(System.out::println);
        System.out.println("---Waiters---");
        handlers.forEach(handler -> {
            if (handler.getClass().getSimpleName().equals("Waiter")) {
                System.out.println(handler);
            }
        });
        System.out.println("---Deliverymans---");
        handlers.forEach(handler -> {
            if (handler.getClass().getSimpleName().equals("Deliveryman")) {
                System.out.println(handler);
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
        for (int i = 0; i < (int) (Math.random() * 10) + 1; i++) { //Adding random items from menu
            int itemIndex;
            do {
                itemIndex = (int) (Math.random() * menu.size());
            } while (!menu.get(itemIndex).isAvailable());
            items.add(menu.get(itemIndex));
        }
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
        order.setTimestamp(System.currentTimeMillis());
        orderQueue.add(order);
        System.out.println("Your order id is " + order.getId());
    }

    public void printWaitingOrders() {
        if (orderQueue.isEmpty())
            System.out.println(name + " has no waiting orders");
        else {
            System.out.println("--------WaitingOrdersQueue--------");
            orderQueue.forEach(System.out::println);
        }
    }

    public void printRealisedOrders() {
        if (madeOrders.isEmpty())
            System.out.println(name + " has no realised orders yet");
        else {
            System.out.println("--------RealisedOrders--------(in order they were delivered)");
            madeOrders.forEach(order -> System.out.println(madeOrders.indexOf(order) + 1 + ". " + order));
        }
    }

    //-------------Administration(Thread)-------------
    @Override
    public void run() {
        while (!isInterrupted()) {
            while (!isPaused) {
                ArrayList<Order> process = new ArrayList<>();//for safety and no coChanging values
                //working with Kitchen
                if (!kitchen.isBusy() && orderQueue.peek() != null && orderQueue.peek().getStatus() == Order.Status.TO_COOK) {
                    kitchen.process(orderQueue.peek());
                } else {
                    for (Order order : orderQueue) {
                        if (order.getStatus() == Order.Status.TO_COOK && !kitchen.isBusy()) {
                            kitchen.process(order);
                            break;
                        }
                    }
                }
                //checking queue for statuses
                for (Order order : orderQueue) {
                    if (order.getStatus() == Order.Status.TO_HANDLE
                            && hasNotBusyHandlers(order.getClass().getSimpleName().equals("DeliveryOrder") ? "Deliveryman" : "Waiter")) {
                        process.add(order);
                    } else if (order.getStatus() == Order.Status.DONE) {
                        process.add(order);
                    }
                    if (order.getStatus() == Order.Status.TO_COOK && !order.isLate() && (System.currentTimeMillis() - order.getTimestamp() >= lateTime)) {
                        process.add(order);
                    }
                }
                //working with those orders that either had a proper status or are late
                for (Order order : process) {
                    if (order.getStatus() == Order.Status.TO_COOK) { //TO_COOK comes here only if the order is late
                        int rnd = (int) (Math.random() * 2);
                        orderQueue.remove(order);
                        if (rnd == 1) {
                            order.setLateTrue();
                            orderQueue.addFirst(order);
                            if (messagesOnStatusChanges)
                                System.out.println("Order[" + order.getId() + "] goes next due to being late.");
                        } else {
                            if (messagesOnStatusChanges)
                                System.out.println("Order[" + order.getId() + "] was canceled due to being late.");
                        }
                    } else if (order.getStatus() == Order.Status.TO_HANDLE) {
                        if (order.getClass().getSimpleName().equals("DeliveryOrder"))
                            handlerProcessOrder(order, "Deliveryman");
                        else if (order.getClass().getSimpleName().equals("StationaryOrder"))
                            handlerProcessOrder(order, "Waiter");
                    } else if (order.getStatus() == Order.Status.DONE) {
                        if (!order.isLate()) {
                            order.getHandler().addTips(order.getPrice() * 0.1);
                        } else {
                            int minutesLate = (int) (System.currentTimeMillis() - order.getTimestamp() / 60_000);
                            order.getHandler().addTips(order.getPrice() * (0.1 - (0.01 * minutesLate)));
                        }
                        turnOver += order.getPrice();
                        order.setStatus(Order.Status.COUNTED);
                        orderQueue.remove(order);
                        madeOrders.add(order);
                    }
                }
            }
        }
    }
}
