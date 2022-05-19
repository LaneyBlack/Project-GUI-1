package bits.squad;

import bits.squad.employee.Cook;
import bits.squad.employee.hander.Deliveryman;
import bits.squad.employee.hander.Waiter;
import bits.squad.orders.DeliveryOrder;
import bits.squad.orders.Item;
import bits.squad.orders.Order;
import bits.squad.orders.StationaryOrder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bits.squad.Colors.errorPrint;

public class Main {
    public static void main(String[] args) {
        //Constructors
        Restaurant restaurant = new Restaurant("High Voltage", "ul. Chełmska 8", 20);
        try {
            restaurant.readMenuFromText("src\\bits\\squad\\menu\\menu.txt"); //ToDo may not be suitable on another device
        } catch (IOException e) {
            e.printStackTrace();
        }
        restaurant.addEmployee(new Cook(restaurant.getNewEmployeeId(), "Jim Halpert", "478-002-243", 16000));
        restaurant.addEmployee(new Cook(restaurant.getNewEmployeeId(), "Shown White", "571-781-213", 14000));
        restaurant.addEmployee(new Deliveryman(restaurant.getNewEmployeeId(), "Lewis Hamilton", "124-234-462", 9000));
        restaurant.addEmployee(new Waiter(restaurant.getNewEmployeeId(), "Pam Beezly", "523-233-552", 8000));
        restaurant.addEmployee(new Cook(restaurant.getNewEmployeeId(), "Billy Jean", "571-783-612", 14000));
        for (int i = 0; i < 12; i++) {
            restaurant.makeRandomOrder();
        }
        Scanner in = new Scanner(System.in);
        //System booting
        System.out.println(Colors.getTextColor("blue") + "--------Welcome to " + restaurant.getRestName() + "--------" + Colors.getTextColor("RESET") + "\n" +
                "Address: " + restaurant.getAddress() + "\n" +
                "This app requires using it's own commandline commands. \n" +
                "For your convenience you can type both Higher and Lower case (f.e. -printRealisedOrders).\n" +
                Colors.getTextColor("YELLOW") + "WARNING!!!\n" + Colors.getTextColor("RESET") +
                "All the commands should start with `-` (minus) symbol.\n" +
                "Please use `-help` to see the commands list." + "\n"
        );
        //Console
        System.out.println("System booted and ready to go:");
        String command = "";
        while (!(command.equals("-e") || command.equals("-exit"))) {
            command = in.nextLine().trim().toLowerCase(Locale.ROOT);
            try {
                switch (command) {
                    case "-e", "-exit" -> restaurant.close();
                    case "-msgon" -> {
                        Restaurant.messagesOnStatusChanges = true;
                        System.out.println("Messages are on");
                    }
                    case "-msgoff" -> {
                        Restaurant.messagesOnStatusChanges = false;
                        System.out.println("Messages are off");
                    }
                    case "-h", "-help" -> System.out.println("""
                                    Commands list:
                            -help(-h) = help
                            -msgon = order messages on (default off)
                            -msgoff = order messages off
                                 -Menu:
                            -printmenu(-pm) = printing menu
                            -printavailablemenu(-pam) = printing available menu
                            -addtomenu(-atm) = add item to menu
                            -deletefrommenu(-dfm) = delete item from menu
                            -markoutofstock(-mos) = mark item as out of stock
                            -markinstock(-mis) = mark item as in stock
                            -readtextmenu (-rtm) = read menu from text file
                            -writetextmenu (-wtm) = write menu into text file
                                 -Employees:
                            -printemployees(-pe) = print all employees
                            -addemployee(-ae) = adds employee
                            -deleteemployee(-de) = deletes employee
                            -printemployeebyid(-pebid) = print employee info by id
                                 -Orders:
                            -makeorder(-mo) = make a order
                            -makerandomorder(-mro) = makes a random order
                            -printwaitingorders(-pwo) = prints waiting orders
                            -printrealisedorders(-pro) = prints realised orders
                                  -Restaurant:
                            -getturnover(-gto) = gets today's turn over
                            -start(-s) = starts the restaurant
                            -pause(-p) = pauses and unpauses the restaurant
                            -exit(-e) = closing the restaurant
                            """);
                    //-------------------------------------------------------Restaurant-------------------------------------------------------
                    case "-s", "-start" -> restaurant.startRestaurant();
                    case "-p", "-pause" -> restaurant.pause();
                    case "-gto", "-getturnover" -> System.out.println("TurnOver is" + restaurant.getTurnOver() + "zl");
                    //-------------------------------------------------------Menu-------------------------------------------------------
                    case "-pm", "-printmenu" -> restaurant.printMenu();
                    case "-pam", "-printavailablemenu" -> restaurant.printAvailableMenu();
                    case "-atm", "-addtomenu" -> {
                        System.out.println("Please enter data of the item:");
                        System.out.println("Name:");
                        String name = in.nextLine().trim();
                        System.out.println("Description:");
                        String description = in.nextLine().trim();
                        System.out.println("Is in stock? (yes/no):");
                        boolean isAvailable = in.nextLine().trim().toLowerCase(Locale.ROOT).equals("yes");
                        System.out.println("Price:");
                        int price = in.nextInt();
                        restaurant.addToMenu(new Item<>(name, description, isAvailable, price)); //adding to menu here
                        System.out.println("Item added successfully");
                        in.nextLine(); //Scanner goes to new line
                    }
                    case "-dfm", "-deletefrommenu" -> {
                        System.out.println("Please enter number of the item you want to delete:");
                        restaurant.deleteFromMenu(in.nextInt());
                        System.out.println("Item deleted successfully");
                        in.nextLine(); //Scanner goes to new line
                    }
                    case "-mos", "-markoutofstock" -> {
                        restaurant.printDishes();
                        System.out.println("Please enter dish number to mark as out of stock:");
                        restaurant.markAsOutOfStock(in.nextInt());
                        System.out.println("Item marked successfully");
                        in.nextLine(); //Scanner goes to new line
                    }
                    case "-mis", "-markinstock" -> {
                        restaurant.printDishes();
                        System.out.println("Please enter item number to mark as in stock:");
                        restaurant.markAsInStock(in.nextInt());
                        System.out.println("Item marked successfully");
                        in.nextLine(); //Scanner goes to new line
                    }
                    case "-rtm", "-readtextmenu" -> {
                        System.out.println("Please paste file path:");
                        restaurant.readMenuFromText(in.nextLine().trim());
                        System.out.println("File was read successfully");
                    }
                    case "-wtm", "-writetextmenu" -> {
                        restaurant.writeMenuToText();
                        System.out.println("File written successfully");
                    }
                    //-------------------------------------------------------Employees-------------------------------------------------------
                    case "-pe", "-printemployees" -> restaurant.printAllEmployees();
                    case "-ae", "-addemployee" -> {
                        System.out.println("Please fill data of the employee:");
                        System.out.println("Name:");
                        String name = in.nextLine().trim();
                        System.out.println("Salary:");
                        int salary = in.nextInt();
                        in.nextLine();
                        System.out.println("Phone Number:");
                        String phoneNumber = in.nextLine().trim();
                        Matcher matcherSymbols = Pattern.compile("[A-Za-z]").matcher(phoneNumber);
                        Matcher matcherNums = Pattern.compile("[0-9]").matcher(phoneNumber);
                        if (matcherSymbols.find() || !(matcherNums.results().count()==9))
                            throw new InvalidKeyException("Invalid phoneNumber");
                        System.out.println("Choose employee type (1-Cook, 2-Delivery, 3-Waiter)");
                        restaurant.addEmployee(switch (in.nextInt()) {
                            case 1 -> new Cook(restaurant.getNewEmployeeId(), name, phoneNumber, salary);
                            case 2 -> new Deliveryman(restaurant.getNewEmployeeId(), name, phoneNumber, salary);
                            case 3 -> new Waiter(restaurant.getNewEmployeeId(), name, phoneNumber, salary);
                            default -> throw new InvalidKeyException("Invalid employee type: " + in.nextInt());
                        });
                        System.out.println("Employee added successfully");
                        in.nextLine(); //Scanner goes to new line
                    }
                    case "-de", "-deleteemployee" -> {
                        restaurant.printEmployeeNames();
                        System.out.println("Please enter id of the employee you want to delete:");
                        restaurant.deleteEmployee(in.nextInt());
                        System.out.println("Employee deleted successfully");
                        in.nextLine(); //Scanner goes to new line
                    }
                    case "-pebid", "-printemployeebyid" -> {
                        restaurant.printEmployeeNames();
                        System.out.println("Please enter id of the employee you want to print:");
                        System.out.println(restaurant.getEmployeeById(in.nextInt()));
                        in.nextLine();
                    }
                    //-------------------------------------------------------Orders-------------------------------------------------------
                    case "-mo", "-makeorder" -> {
                        Order order;
                        System.out.println("Do you want stationary or delivery order? (1-stationary, 2-delivery)");
                        int type = in.nextInt();
                        in.nextLine(); //Scanner goes to new line
                        if (type == 1) {
                            System.out.println("Please enter the table number(from 1 to " + restaurant.getTables() + ")");
                            byte tableNumber = in.nextByte();
                            in.nextLine(); //Scanner goes to new line
                            if (tableNumber > restaurant.getTables() || tableNumber < 0)
                                throw new InvalidKeyException("Invalid table number input");
                            order = new StationaryOrder(new ArrayList<>(), tableNumber);
                        } else if (type == 2) {
                            System.out.println("Please enter your address:");
                            String address = in.nextLine().trim();
                            Matcher matcher = Pattern.compile("[0-9]").matcher(address);
                            if (!address.contains("ul") || !matcher.find() || address.split(" ").length!=3)
                                throw new InvalidKeyException("Invalid address input");
                            order = new DeliveryOrder(new ArrayList<>(), address);
                        } else
                            throw new InvalidKeyException("Invalid order type!");
                        do {
                            restaurant.printDishes();
                            System.out.println("Please select item id to order:");
                            int itemIndex = in.nextInt();
                            System.out.println("Please enter count of items:");
                            int times = in.nextInt();
                            in.nextLine(); //Scanner goes to new line
                            if ((itemIndex - 1 < 0 || itemIndex > restaurant.getMenuSize()) || times < 1 || !restaurant.getItemById(itemIndex).isAvailable())
                                throw new InputMismatchException("Invalid order input");
                            for (int i = 0; i < times; i++)
                                order.addItem(restaurant.getItemById(itemIndex));
                            System.out.println("Anything else? (type `-a` to order more or type anything to continue)");
                            command = in.nextLine().trim();
                        } while (command.equals("-a"));
                        restaurant.makeAnOrder(order);
                    }
                    case "-mro", "-makerandomorder" -> restaurant.makeRandomOrder();
                    case "-printrealisedorders", "-pro" -> restaurant.printRealisedOrders();
                    case "-printwaitingorders", "-pwo" -> restaurant.printWaitingOrders();
                    default -> errorPrint("Command not recognised");
                }
            } catch (IllegalStateException | InputMismatchException e) {
                errorPrint("Invalid input exception!");
                in.nextLine(); //Scanner goes to new line
            } catch (FileNotFoundException e) {
                errorPrint("File not found!");
            } catch (Exception e) {
                errorPrint(e.getMessage());
            }
        }
    }
}
