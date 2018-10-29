package Aritmetic;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Class defining an arithmetic code decoder.
 */
public class ArithmeticDecoder extends Arithmetic{
    private ArrayList<Character> alphabet;
    private ArrayList<Double> probs;
    private char eodChar;
    private BigDecimal[] ranges;
    private String decodedMessage = "";
    private BigDecimal lower;
    private BigDecimal upper;
    private int precision;
    private String buffer;
    private BigDecimal bufferValue;
    public String tag;
    private BigDecimal currentRangeSize;

    /**
     * Constructor for an arithmetic decoder.
     * @param alphabet the source alphabet used in encoding.
     * @param probs the source probabilities used in encoding.
     * @param precision the precision used in encoding.
     * @param tag the coded message.
     * @param eodChar the specified EOD character.
     */
    ArithmeticDecoder(ArrayList<Character> alphabet, ArrayList<Double> probs, int precision, String tag, char eodChar) {
        this.alphabet = alphabet;
        this.probs = probs;
        this.precision = precision;
        this.tag = tag;
        this.eodChar = eodChar;
    }

    /**
     * Performs type A rescaling.
     */
    public void rescaleA() {
        lowerString = lowerString.substring(1) + "0";
        upperString = upperString.substring(1) + "1";
        lower = new BigDecimal(Integer.parseInt(lowerString, 2));
        upper = new BigDecimal(Integer.parseInt(upperString, 2));
        buffer = buffer.substring(1) + tag.charAt(0);
        tag = tag.substring(1);
    }

    /**
     * Performs type B rescaling.
     */
    public void rescaleB() {
        if (lowerString.charAt(1) == '0') {
            lowerString = lowerString.charAt(0) + "1" + lowerString.substring(2);
        } else {
            lowerString = lowerString.charAt(0) + "0" + lowerString.substring(2);
        }

        if (upperString.charAt(1) == '0') {
            upperString = upperString.charAt(0) + "1" + upperString.substring(2);
        } else {
            upperString = upperString.charAt(0) + "0" + upperString.substring(2);
        }

        if (buffer.charAt(1) == '0') {
            buffer = buffer.charAt(0) + "1" + buffer.substring(2);
        } else {
            buffer = buffer.charAt(0) + "0" + buffer.substring(2);
        }

        lowerString = lowerString.substring(1) + "0";
        upperString = upperString.substring(1) + "1";
        lower = new BigDecimal(Integer.parseInt(lowerString, 2));
        upper = new BigDecimal(Integer.parseInt(upperString, 2));
        buffer = buffer.substring(1) + tag.charAt(0);
        tag = tag.substring(1);
    }

    /**
     * Decodes a bit string generated by an arithmetic encoder.
     * @return the decoded bit string.
     */
    public String decode() {
        lower = new BigDecimal(0.0);
        upper = new BigDecimal(Math.pow(2, precision) - 1);
        buffer = tag.substring(0, precision);
        bufferValue = new BigDecimal(Integer.parseInt(buffer, 2));
        tag = tag.substring(precision);
        ranges = new BigDecimal[alphabet.size()+1];
        boolean kill = false;

        while (!kill) {
            ranges[0] = lower;
            currentRangeSize = upper.subtract(lower).add(new BigDecimal(1));
            for (int i = 1; i < ranges.length; i++) {
                ranges[i] = new BigDecimal((int) (currentRangeSize.multiply(new BigDecimal(probs.get(i-1))).add(ranges[i-1]).doubleValue()));
            }

            BigDecimal tempLower = new BigDecimal(0.0);
            BigDecimal tempUpper = new BigDecimal(0.0);
            bufferValue = new BigDecimal(Integer.parseInt(buffer, 2));

            for (int i = 0; i < alphabet.size(); i++) {
                if ((ranges[i].compareTo(bufferValue) <= 0) && (ranges[i+1].compareTo(bufferValue) > 0)) {
                    char currentChar = alphabet.get(i);
                    System.out.println("DECODING " + currentChar);
                    System.out.println(ranges[i] + " " + bufferValue + " " + ranges[i+1]);
                    decodedMessage += currentChar;
                    if (currentChar == eodChar) {
                        System.out.println("KILL SWITCH");
                        kill = true;
                        break;
                    }
                    currentRangeSize = upper.add(new BigDecimal(1)).subtract(lower);
                    BigDecimal lProb = new BigDecimal(0.0);
                    for (int j = 0; j < i; j++) {
                        lProb = lProb.add(new BigDecimal(probs.get(j)));
                    }
                    BigDecimal uProb = lProb.add(new BigDecimal(probs.get(i)));

                    tempLower = lower.add(lProb.multiply(currentRangeSize));
                    tempUpper = lower.add(currentRangeSize.multiply(uProb).subtract(new BigDecimal(1)));
                }
            }
            if (kill) {
                break;
            }
            lower = new BigDecimal((int) tempLower.doubleValue());
            lowerString = Integer.toBinaryString(lower.intValueExact());
            lowerString = String.format("%" + precision + "s", lowerString).replace(' ', '0');

            upper = new BigDecimal((int) tempUpper.doubleValue());
            upperString = Integer.toBinaryString(upper.intValueExact());
            upperString = String.format("%" + precision + "s", upperString).replace(' ', '0');

            rescale();
        }
        System.out.println(decodedMessage);
        return decodedMessage;
    }
}