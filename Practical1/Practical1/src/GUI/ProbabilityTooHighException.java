package GUI;

public class ProbabilityTooHighException extends Exception {
    ProbabilityTooHighException() {
        System.out.println("Input probability was greater than 1.");
    }
}
