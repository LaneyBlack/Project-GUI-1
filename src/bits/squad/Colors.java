package bits.squad;

public class Colors {
    public static String getTextColor (String color) {
        color = color.trim().toUpperCase();
        return switch (color){
            case "RESET" -> "\u001B[0m";
            case "BLACK" -> "\u001B[30m";
            case "RED" -> "\u001B[31m";
            case "GREEN" -> "\u001B[32m";
            case "YELLOW" -> "\u001B[33m";
            case "BLUE" -> "\u001B[34m";
            case "PURPLE" -> "\u001B[35m";
            case "CYAN" -> "\u001B[36m";
            case "WHITE" -> "\u001B[37m";
            default -> "";
        };
    }

    public static String getTextBGColor (String color) {
        color = color.trim().toUpperCase();
        return switch (color){
            case "RESET" -> "\u001B[0m";
            case "BLACK" -> "\u001B[40m";
            case "RED" -> "\u001B[41m";
            case "GREEN" -> "\u001B[42m";
            case "YELLOW" -> "\u001B[43m";
            case "BLUE" -> "\u001B[44m";
            case "PURPLE" -> "\u001B[45m";
            case "CYAN" -> "\u001B[46m";
            case "WHITE" -> "\u001B[47m";
            default -> "";
        };
    }

//    public static void main(String[] args) {
//        System.out.println(getTextColor("reset") + "Reset");
//        System.out.println(getTextColor("black") + "Black");
//        System.out.println(getTextColor("red") + "Red");
//        System.out.println("\u001B[32m" + "Green");
//        System.out.println("\u001B[33m" + "Yellow");
//        System.out.println("\u001B[34m" + "Blue");
//        System.out.println("\u001B[35m" + "Purple");
//        System.out.println("\u001B[36m" + "Cyan");
//        System.out.println("\u001B[37m" + "White");
//        System.out.println("--------------------");
//        System.out.println("\u001B[0m" + "Reset");
//        System.out.println(getTextBGColor("black") + "Black");
//        System.out.println(getTextBGColor("red") + "Red");
//        System.out.println("\u001B[42m" + "Green");
//        System.out.println("\u001B[43m" + "Yellow");
//        System.out.println("\u001B[44m" + "Blue");
//        System.out.println("\u001B[45m" + "Purple");
//        System.out.println("\u001B[46m" + "Cyan");
//        System.out.println("\u001B[47m" + "White");
//    }
}
