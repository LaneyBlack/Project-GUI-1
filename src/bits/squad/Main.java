package bits.squad;

import bits.squad.workers.Cook;
import bits.squad.workers.Delivery;
import bits.squad.workers.Waiter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import static bits.squad.Colors.errorPrint;

public class Main {
    public static void main(String[] args) {
        //Constructors
        Restaurant restaurant = new Restaurant("High Voltage", "ul. Chełmska 8", 20, 3);
        restaurant.addToMenu(new Item("Spaghetti", "Pone of spaghetti bolognese with cheese.", true, 18));
        restaurant.addToMenu(new Item("Large Spaghetti", "Bigger pone of spaghetti bolognese with cheese and meetballs.", true, 24));
        restaurant.addToMenu(new Item("Pizza Margarita", "Old as this world, but as balanced, as the universe.", true, 31));
        restaurant.addWorker(new Cook(1, "John Sandman", 16000));
        restaurant.addWorker(new Cook(2, "Shown White", 14000));
        restaurant.addWorker(new Delivery(3, "Lewis Hamilton", 9000));
        restaurant.addWorker(new Waiter(4, "Pam Beezly", 8000));
        restaurant.addWorker(new Delivery(5, "Sonic The Man", 9500));
        restaurant.addWorker(new Waiter(6, "Jim Halpert", 8500));
        Scanner in = new Scanner(System.in);
        //System booting
        System.out.println(Colors.getTextColor("blue") + "--------Welcome to " + restaurant.getName() + "--------" + "\n" +
                Colors.getTextColor("YELLOW") + "WARNING!!!\n" + Colors.getTextColor("RESET") +
                "Using this app requires using a commandline commands. \n" +
                "All the commands should start with `-` symbol.\n" +
                "Please use `-help` to see all the commands." + "\n"
        );
        //Console
        System.out.println("System booted and ready to go:");
        String command = "";
        while (!(command.equals("-e") || command.equals("-exit"))) {
            command = in.nextLine().trim();
            switch (command) {
                case "-e", "-exit" -> restaurant.close();
                case "-h", "-help" -> System.out.println("""
                            Commands list:
                        -help(-h) = help
                        -exit(-e) = closing the restaurant
                                -Menu:
                        -printmenu(-pm) = printing menu
                        -printavailablemenu(-pam) = printing available menu
                        -addtomenu(-atm) = add item to menu
                        -deletefrommenu(-dfm) = delete item from menu
                        -markoutofstock(-mos) = mark item as out of stock
                        -markinstock(-mis) = mark item as in stock
                        -readtextmenu (-rtm) = read menu from text file
                        -writetextmenu (-wtm) = write menu into text file
                                -Workers:
                        -printallworkers(-paw) = print all workers
                        -addworker(-aw) = adds worker
                        -deleteworker(-dw) = deletes worker
                                -Orders:
                        -makeorder(-mo)-
                        -makedeliveryorder(-mdo)-
                        -makestationaryorder(-mso)-
                        -makerandomorder(-mro)-
                        -printwaitingorders(-pwo)-
                        -printrealisedorders(-pro)-
                                -Restaurant:
                        -start(-s)-
                        -pause(-p)-
                        """);
                //-------------------------------------------------------Menu-------------------------------------------------------
                case "-pm", "-printmenu" -> restaurant.printMenu();
                case "-pam", "-printavailablemenu" -> restaurant.printAvailableMenu();
                case "-atm", "-addtomenu" -> {
                    try {
                        System.out.println("Please enter data of the item:");
                        System.out.println("Name");
                        String name = in.nextLine().trim();
                        System.out.println("Description");
                        String description = in.nextLine().trim();
                        System.out.println("Is in stock? (yes/no)");
                        boolean isAvailable = in.nextLine().trim().toLowerCase(Locale.ROOT).equals("yes");
                        System.out.println("Price");
                        int price = in.nextInt();
                        restaurant.addToMenu(new Item(name, description, isAvailable, price));
                        System.out.println("Item added successfully");
                    } catch (IllegalStateException | InputMismatchException e) {
                        errorPrint("Bad values exception!");
                    }
                    in.nextLine();
                }
                case "-dfm", "-deletefrommenu" -> {
                    try {
                        System.out.println("Please enter number of the item you want to delete:");
                        restaurant.deleteFromMenu(in.nextInt());
                        System.out.println("Item deleted successfully");
                    } catch (IllegalStateException | InputMismatchException e) {
                        errorPrint("Bad values exception!");
                    }
                    in.nextLine();
                }
                case "-mos", "-markoutofstock" -> {
                    restaurant.printDishes();
                    try {
                        System.out.println("Please enter dish number to mark as out of stock:");
                        restaurant.markAsOutOfStock(in.nextInt());
                        System.out.println("Item marked successfully");
                    } catch (IllegalStateException | InputMismatchException e) {
                        errorPrint("Bad values exception!");
                    }
                    in.nextLine(); //in.nextInt does not goes to another Line
                }
                case "-mis", "-markinstock" -> {
                    restaurant.printDishes();
                    try {
                        System.out.println("Please enter item number to mark as in stock:");
                        restaurant.markAsInStock(in.nextInt());
                        System.out.println("Item marked successfully");
                    } catch (IllegalStateException | InputMismatchException e) {
                        errorPrint("Bad values exception!");
                    }
                    in.nextLine();
                }
                case "-rtm", "-readtextmenu" -> {
                    System.out.println("Please paste file path:");
                    try {
                        restaurant.readMenuFromText(in.nextLine().trim());
                        System.out.println("File was read successfully");
                    } catch (FileNotFoundException e) {
                        errorPrint("File not found!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "-wtm", "-writetextmenu" -> {
                    try {
                        restaurant.writeMenuToText();
                        System.out.println("File written successfully");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //-------------------------------------------------------Workers-------------------------------------------------------
                case "-paw", "-printallworkers" -> restaurant.printAllWorkers();
                case "-aw", "-addworker" -> {
                    System.out.println("Please fill data of the worker:");
                    try {
                        System.out.println("Name");
                        String name = in.nextLine().trim();
                        System.out.println("Salary");
                        int salary = in.nextInt();
                        System.out.println("Choose worker type (1-Cook, 2-Delivery, 3-Waiter)");
                        restaurant.addWorker(switch (in.nextInt()) {
                            case 1 -> new Cook(restaurant.newWorkerId(), name, salary);
                            case 2 -> new Delivery(restaurant.newWorkerId(), name, salary);
                            case 3 -> new Waiter(restaurant.newWorkerId(), name, salary);
                            default -> throw new IllegalStateException("Unexpected value: " + in.nextInt());
                        });
                        System.out.println("Worker added successfully");
                    } catch (IllegalStateException | InputMismatchException e) {
                        errorPrint("Bad values exception!");
                    }
                    in.nextLine();
                }
                case "-dw", "-deleteworker" -> {
                    try {
                        restaurant.printWorkerNames();
                        System.out.println("Please enter id of the worker you want to delete:");
                        restaurant.deleteWorker(in.nextInt());
                        System.out.println("Worker deleted successfully");
                    } catch (IllegalStateException | InputMismatchException e) {
                        errorPrint("Bad values exception!");
                    }
                    in.nextLine();
                }
                //-------------------------------------------------------Orders-------------------------------------------------------
                default -> errorPrint("Command not recognised.");
            }
        }
    }
}
