package bits.squad;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Constructors
        Restaurant restaurant = new Restaurant("Cafe Garage", "ul. Chełmska 8", 20, 3);
        restaurant.addToMenu(new Dish("Spaghetti", "Pone of spaghetti bolognese with cheese.", true, 18));
        restaurant.addToMenu(new Dish("Large Spaghetti", "Bigger pone of spaghetti bolognese with cheese and meetballs.", true, 24));
        restaurant.addToMenu(new Dish("Pizza Margarita", "Old as this world, but as balanced, as the universe.", true, 31));
        Scanner in = new Scanner(System.in);
        //System booting
        System.out.println("--------Welcome to " + restaurant.getName() + " App--------" + "\n" +
                Colors.getTextColor("YELLOW") + "WARNING!!!\n" + Colors.getTextColor("RESET") +
                "Using this app requires using a commandline commands. \n" +
                "All the commands should start with " + "`-`" + " symbol.\n" +
                "Please use `-help` to see all the commands." + "\n\n" +
                "System booted and ready to work:"
        );
        String command = "";
        while (!(command.equals("-e") || command.equals("-exit"))) {
            command = in.nextLine().trim();
            switch (command) {
                case "-e", "-exit" -> restaurant.close();
                case "-h", "-help" -> System.out.println("""
                        Commands list:
                         -help(-h) = help
                         -exit(-e) = closing the restaurant
                         -Menu
                          -printmenu(-pm) = printing menu
                          -addtomenu(-atm) = add dish to menu
                          -markoutofstock(-mos) = mark dish as out of stock
                          -markinstock(-mis) = mark dish as in stock
                          -readtextmenu (-rtm) = read menu from text file
                          -writetextmenu (-wtm) = write menu into text file
                        """);
                case "-pm", "-printmenu" -> restaurant.printMenu();
                case "-atm", "-addtomenu" -> {
                    System.out.println("Write data of the dish:");
                    System.out.println("Name");
                    String name = in.nextLine().trim();
                    System.out.println("Description");
                    String description = in.nextLine().trim();
                    System.out.println("Is in stock? (yes/no)");
                    boolean isAvailable = in.nextLine().trim().toLowerCase(Locale.ROOT).equals("yes");
                    System.out.println("Price");
                    int price = in.nextInt();
                    restaurant.addToMenu(new Dish(name, description, isAvailable, price));
                    System.out.println("Dish added successfully");
                    in.nextLine();
                }
                case "-mos", "-markoutofstock" -> {
                    restaurant.printDishes();
                    System.out.println("Please enter dish number to mark it as out of stock:");
                    restaurant.markAsOutOfStock(in.nextInt());
                    in.nextLine(); //in.nextInt does not goes to another Line
                }
                case "-mis", "-markinstock" -> {
                    restaurant.printDishes();
                    System.out.println("Please enter dish number to mark it as in stock:");
                    restaurant.markAsInStock(in.nextInt());
                    in.nextLine();
                }
                case "-rtm", "-readtextmenu" -> {
                    System.out.println("Please paste file path:");
                    try {
                        restaurant.readMenuFromText(in.nextLine().trim());
                    } catch (FileNotFoundException e) {
                        System.out.println(Colors.getTextColor("RED") + "File not found!" + Colors.getTextColor("RESET"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "-wtm", "-writetextmenu" -> {
                    try {
                        restaurant.writeMenuToText();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                default -> System.out.println("Command not recognised.");
            }
        }
    }
}
