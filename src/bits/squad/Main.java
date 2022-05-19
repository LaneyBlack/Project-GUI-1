package bits.squad;

import bits.squad.employee.Cook;
import bits.squad.employee.Delivery;
import bits.squad.employee.Waiter;
import bits.squad.orders.DeliveryOrder;
import bits.squad.orders.StationaryOrder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import static bits.squad.Colors.errorPrint;

public class Main {
    public static void main(String[] args) {
        //Constructors
        Restaurant restaurant = new Restaurant("High Voltage", "ul. Chełmska 8", 20);
        try {
            restaurant.readMenuFromText("C:\\PJATK\\GUI\\Project(GUI 1)\\src\\bits\\squad\\menu\\menu.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        restaurant.addEmployee(new Cook(restaurant.getNewEmployeeId(), "John Sandman", "478-00-24", 16000));
        restaurant.addEmployee(new Cook(restaurant.getNewEmployeeId(), "Shown White", "571-78-21", 14000));
        restaurant.addEmployee(new Delivery(restaurant.getNewEmployeeId(), "Lewis Hamilton", "124-23-46", 9000));
        restaurant.addEmployee(new Waiter(restaurant.getNewEmployeeId(), "Pam Beezly", "523-23-55", 8000));
        restaurant.addEmployee(new Delivery(restaurant.getNewEmployeeId(), "Sonic The Man", "521-123-56", 9500));
        restaurant.addEmployee(new Waiter(restaurant.getNewEmployeeId(), "Jim Halpert", "521-123-57", 8500));
        for (int i = 0; i < 10; i++) {
            restaurant.makeRandomOrder();
        }
        Scanner in = new Scanner(System.in);
        //System booting
        System.out.println(Colors.getTextColor("blue") + "--------Welcome to " + restaurant.getRestName() + "--------" + "\n" +
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
            try {
                switch (command) {
                    case "-e", "-exit" -> restaurant.close();
                    case "-h", "-help" -> System.out.println("""
                                Commands list:
                            -help(-h) = help
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
                            -printemps(-pe) = print all employees
                            -addemp(-ae) = adds employee
                            -deleteemp(-de) = deletes employee
                                    -Orders:
                            -makeorder(-mo) = make a order
                            -makerandomorder(-mro)-
                            -printwaitingorders(-pwo)-
                            -printrealisedorders(-pro)-
                                    -Restaurant:
                            -start(-s)-
                            -exit(-e) = closing the restaurant
                            """);
                    //-------------------------------------------------------Restaurant-------------------------------------------------------
                    case "-s","-start" -> restaurant.startRestaurant();
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
                        restaurant.addToMenu(new Item(name, description, isAvailable, price));
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
                    case "-pe", "-printemps" -> restaurant.printAllEmployees();
                    case "-ae", "-addemp" -> {
                        System.out.println("Please fill data of the employee:");
                        System.out.println("Name:");
                        String name = in.nextLine().trim();
                        System.out.println("Salary:");
                        int salary = in.nextInt();
                        System.out.println("Phone Number:");
                        String phoneNumber = in.nextLine();
                        System.out.println("Choose employee type (1-Cook, 2-Delivery, 3-Waiter)");
                        restaurant.addEmployee(switch (in.nextInt()) {
                            case 1 -> new Cook(restaurant.getNewEmployeeId(),phoneNumber, name, salary);
                            case 2 -> new Delivery(restaurant.getNewEmployeeId(),phoneNumber, name, salary);
                            case 3 -> new Waiter(restaurant.getNewEmployeeId(),phoneNumber, name, salary);
                            default -> throw new IllegalStateException("Unexpected value: " + in.nextInt());
                        });
                        System.out.println("Employee added successfully");
                        in.nextLine(); //Scanner goes to new line
                    }
                    case "-de", "-deleteemp" -> {
                        restaurant.printEmployeeNames();
                        System.out.println("Please enter id of the employee you want to delete:");
                        restaurant.deleteEmployee(in.nextInt());
                        System.out.println("Employee deleted successfully");
                        in.nextLine(); //Scanner goes to new line
                    }
                    //-------------------------------------------------------Orders-------------------------------------------------------
                    case "-mo", "-makeorder" -> {
                        System.out.println("Do you want stationary or delivery order? (1-stationary, 2-delivery)");
                        int type = in.nextInt();
                        in.nextLine(); //Scanner goes to new line
                        if (type == 1) {
                            System.out.println("Please enter the table number:");
                            byte tableNumber = in.nextByte();
                            in.nextLine(); //Scanner goes to new line
                            if (tableNumber > restaurant.getTables() || tableNumber < 0)
                                throw new InvalidKeyException("Invalid table number input");
                            StationaryOrder order = new StationaryOrder(new ArrayList<>(), tableNumber);
                            restaurant.makeAnOrder(order);
                        } else if (type == 2) {
                            System.out.println("Please enter your address:");
                            String address = in.nextLine();
                            if (!(address.contains("ul") && !address.contains("[0-9]")))
                                throw new InvalidKeyException("Invalid address input");
                            DeliveryOrder order = new DeliveryOrder(new ArrayList<>(), address);
                            restaurant.makeAnOrder(order);
                        } else
                            throw new InvalidKeyException("Invalid order type!");
                    }
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
