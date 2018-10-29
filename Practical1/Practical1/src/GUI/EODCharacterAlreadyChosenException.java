package GUI;

public class EODCharacterAlreadyChosenException extends Exception {
    EODCharacterAlreadyChosenException() {
        System.out.println("EOD character has already been chosen.");
    }
}
