package GUI;

public class AlphabetTooSmallException extends Exception {
    AlphabetTooSmallException() {
        System.out.println("Given source alphabet is too small.");
    }
}
