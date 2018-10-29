package GUI;

public class ProbabiltySumException extends Exception {
    ProbabiltySumException() {
        System.out.println("Sum of all probabilities exceeds 1");
    }
}
