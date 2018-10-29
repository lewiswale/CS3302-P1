package Logic;

import Huffman.HuffmanObject;

import java.util.ArrayList;
import java.util.Random;

public class StringGenerator {
    public String generateString(ArrayList<Character> sourceAlphabet, String eodChar) {
        String alphabetBuffer = "";

        for (int i = 0; i < sourceAlphabet.size(); i++) {
            if (!(sourceAlphabet.get(i) == eodChar.charAt(0))) {
                alphabetBuffer += sourceAlphabet.get(i);
            }
        }

        StringBuilder builder = new StringBuilder();
        Random rnd = new Random();

        while (builder.length() < 100) {
            int index = (int) (rnd.nextFloat() * alphabetBuffer.length());
            builder.append(alphabetBuffer.charAt(index));
        }
        builder.append(eodChar);

        return builder.toString();
    }
}
