package utils;

import javax.swing.*;

/************************************************************************
 Made by        PatrickSys
 Date           26/02/2022
 Package        utils
 Description:
 ************************************************************************/


public class Utils {
    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static String inputData(String message, String title) throws UserNullinputException {
        String data =  JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
        if(null == data) {
            throw new UserNullinputException();
        }
        return data;
    }

    public static String inputNonBlankData(String message, String title) throws UserNullinputException {
        String data = inputData(message, title);
        if(null == data) {
            throw new UserNullinputException();
        }
        if (data.isBlank()) {
            String errorMessage =  "No puedes dejar un campo en blanco\n";
            message = message.replaceAll(errorMessage, "");
            return inputNonBlankData("No puedes dejar un campo en blanco\n" + message, title );
        }
        return data;
    }


    // Checks if input data is not a number
    public static boolean notNumber(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    public static int inputNumber(String message, String title) throws UserNullinputException {
        String input = inputNonBlankData(message, title);
        if (!isNumberValid(input)) {
            return inputNumber("Please enter a valid number", title);
        }
        return Integer.parseInt(input);
    }

    public static String inputString(String message, String title) throws UserNullinputException {
        String input = inputNonBlankData(message, title);
        if (notNumber(input)) {
            return input;
        }
        return inputString("Por favor introduce un nombre", title);
    }
    public static boolean isNumberValid(String number) {
        return !notNumber(number);
    }

}
