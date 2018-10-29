package Aritmetic;

/**
 * Abstract class containing methods used in both encoding and decoding.
 */
public abstract class Arithmetic {
    public String upperString;
    public String lowerString;

    /**
     * Checks whether or not type A rescaling is required.
     * @return
     */
    public boolean caseA() {
        return upperString.charAt(0) == lowerString.charAt(0);
    }

    /**
     * Checks whether or not type B rescaling is required.
     * @return
     */
    public boolean caseB() {
        return upperString.charAt(0) != lowerString.charAt(0) && lowerString.charAt(1) == '1'
                && upperString.charAt(1) == '0';
    }

    public abstract void rescaleA();

    public abstract void rescaleB();

    public void rescale() {
        while (caseA() || caseB()) {
            if (caseA()) {
                rescaleA();
            } else if (caseB()) {
                rescaleB();
            }
        }
    }

}
