package GUI;

public class InputSymbolLengthException extends Exception {
    InputSymbolLengthException() {
        System.out.println("Input symbol must be only one character long.");
    }
}
