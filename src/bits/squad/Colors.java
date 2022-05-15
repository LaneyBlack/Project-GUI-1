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

    public static void successPrint(String string){
        System.out.println(Colors.getTextColor("GREEN") + string + Colors.getTextColor("RESET"));
    }
    public static void errorPrint(String string){
        System.out.println(Colors.getTextColor("RED") + string + Colors.getTextColor("RESET"));
    }
}
