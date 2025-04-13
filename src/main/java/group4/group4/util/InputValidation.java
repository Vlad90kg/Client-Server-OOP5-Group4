package group4.group4.util;


public class InputValidation {

    public static boolean validateInt(String input) {
        boolean valid = input != null && !(input.trim()).isEmpty();
        if (valid) {
            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input is not an integer or too long");
                return false;
            }
            return  true;
        }
        return false;
    }

    public static boolean  validateString(String input) {
        boolean valid = input != null && !(input).trim().isEmpty();
        if (valid) {
            return true;
        }
        System.out.println("Invalid input: " + input);
        return false;
    }

    public static boolean validateDouble(String input) {
        boolean valid = input != null && !(input.trim()).isEmpty();
        if (valid) {
            try {
                Double.parseDouble(input);
                return true;
            }catch (NumberFormatException e) {
                System.out.println("Input is not a number or too long");
                return false;
            }
        }
        System.out.println("Invalid input: " + input);
        return false;
    }

}
