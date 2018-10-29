package Aritmetic;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Class defining arithmetic encoder.
 */
public class ArithmeticEncoder extends Arithmetic{
    private ArrayList<Character> alphabet = new ArrayList<>();
    private ArrayList<Double> probs = new ArrayList<>();
    private char eodChar;
    private String message;
    private int precision;
    private BigDecimal lower;
    private BigDecimal upper;
    private int rescaleCounter;
    private BigDecimal currentRangeSize;
    private String tag;

    /**
     * Constructor for arithmetic encoder.
     * @param precision is the chosen bit precision.
     */
    public ArithmeticEncoder(int precision) {
        this.precision = precision;
        this.lower = new BigDecimal(0.0);
        this.upper = new BigDecimal(Math.pow(2, this.precision) - 1);
        this.rescaleCounter = 0;
        this.tag = "";
    }

    /**
     * Method used to take a message and encode it using arithmetic encoding.
     * @return a binary string of encoded data.
     */
    public String encode() {
        for (int j = 0; j < message.length(); j++) {
            for (int i = 0; i < alphabet.size(); i++) {
                if (alphabet.get(i) == message.charAt(j)) {
                    System.out.println("ENCODING " + alphabet.get(i));
                    BigDecimal lProb = new BigDecimal(0.0);
                    for (int k = 0; k < i; k++) {
                        lProb = lProb.add(new BigDecimal(probs.get(k)));
                    }
                    BigDecimal uProb = lProb.add(new BigDecimal(probs.get(i)));
                    currentRangeSize = upper.subtract(lower).add(new BigDecimal(1));
                    upper = new BigDecimal((int) (lower.add(currentRangeSize.multiply(uProb).subtract(new BigDecimal( 1))).doubleValue()));
                    lower = new BigDecimal((int) (lower.add(currentRangeSize.multiply(lProb))).doubleValue());

                    upperString = Integer.toBinaryString(upper.intValueExact());
                    upperString = String.format("%" + precision + "s", upperString).replace(' ', '0');

                    lowerString = Integer.toBinaryString(lower.intValueExact());
                    lowerString = String.format("%" + precision + "s", lowerString).replace(' ', '0');

                    rescale();
                }
            }
        }

        tag += lowerString.charAt(0);
        if (lowerString.charAt(0) == '0') {
            for (int i = 0; i < rescaleCounter; i++) {
                tag += "1";
            }
        } else {
            for (int i = 0; i < rescaleCounter; i++) {
                tag += "0";
            }
        }
        System.out.println(lowerString.length());
        tag += lowerString.substring(lowerString.length() - (precision - 1));
        return tag;
    }

    /**
     * Carries out case A rescaling.
     */
    public void rescaleA() {
        tag += upperString.charAt(0);
        for (int i = 0; i < rescaleCounter; i++) {
            if (upperString.charAt(0) == '0') {
                tag += "1";
            } else {
                tag += "0";
            }
        }
        rescaleCounter = 0;
        lowerString = lowerString.substring(1) + "0";
        upperString = upperString.substring(1) + "1";
        lower = new BigDecimal(Integer.parseInt(lowerString, 2));
        upper = new BigDecimal(Integer.parseInt(upperString, 2));
    }

    /**
     * Carries out case B rescaling.
     */
    public void rescaleB() {
        if (upperString.charAt(1) == '0') {
            upperString = upperString.charAt(0) + "1" + upperString.substring(2);
        } else {
            upperString = upperString.charAt(0) + "0" + upperString.substring(2);
        }

        if (lowerString.charAt(1) == '0') {
            lowerString = lowerString.charAt(0) + "1" + lowerString.substring(2);
        } else {
            lowerString = lowerString.charAt(0) + "0" + lowerString.substring(2);
        }

        lowerString = lowerString.substring(1) + "0";
        upperString = upperString.substring(1) + "1";
        lower = new BigDecimal(Integer.parseInt(lowerString, 2));
        upper = new BigDecimal(Integer.parseInt(upperString, 2));
        rescaleCounter++;
    }

    /**
     * Returns source alphabet.
     * @return
     */
    public ArrayList<Character> getAlphabet() {
        return alphabet;
    }

    /**
     * Returns source probabilities.
     * @return
     */
    public ArrayList<Double> getProbs() {
        return probs;
    }

    /**
     * Sets message to be encoded.
     * @param message chosen message to be encoded.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets EOD character.
     * @param eodChar chosen EOD character to be used.
     */
    public void setEodChar(char eodChar) {
        this.eodChar = eodChar;
    }

    /**
     * Calculates amount of bits used to represent each character in the original message.
     * @return
     */
    public double calculateAverageLength() {
        return tag.length()/message.length();
    }

    /**
     * Calculates the ratio between the length of the encoded string and the original message in 7 bit ASCII.
     * @return
     */
    public double calculateCompressionRatio() {
        return ((message.length() * 7)/tag.length());
    }

    /**
     * Decodes an encoded string.
     * @return decoded string.
     */
    public String decode() {
        ArithmeticDecoder decoder = new ArithmeticDecoder(alphabet, probs, precision, tag, eodChar);
        return decoder.decode();
    }

    public String getTag() {
        return tag;
    }
}
