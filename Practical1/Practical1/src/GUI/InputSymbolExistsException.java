package GUI;

public class InputSymbolExistsException extends Exception {
    InputSymbolExistsException() {
        System.out.println("The given symbol already exists.");
    }
}
