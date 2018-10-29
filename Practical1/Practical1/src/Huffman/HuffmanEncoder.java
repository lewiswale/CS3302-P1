package Huffman;

import java.util.ArrayList;

/**
 * Class that defines methods to generate and decode a huffman code.
 */
public class HuffmanEncoder {
    private ArrayList<HuffmanObject> alphabet;
    private String message;
    private String codedMessage;
    private String decodedMessage;
    private double weightedMean = 0.0;
    private int codeAlphabetSize;

    /**
     * Constructor for a huffman encoder.
     */
    public HuffmanEncoder() {
        this.alphabet = new ArrayList<HuffmanObject>();
    }

    public HuffmanEncoder(int codeAlphabetSize) {
        this.alphabet = new ArrayList<HuffmanObject>();
        this.codeAlphabetSize = codeAlphabetSize;
    }

    /**
     * Method to add a huffman object to the alphabet
     * @param obj
     */
    public void addObjectToAlphabet(HuffmanObject obj) {
        alphabet.add(obj);
    }

    public HuffmanObject addObjects(ArrayList<HuffmanObject> objects) {
        HuffmanObject newObj = new HuffmanObject();
        String newSymbol = "";
        double newProb = 0.0;

        for (int i = 0; i < objects.size(); i++) {
            newSymbol += objects.get(i).getSymbol();
            newProb += objects.get(i).getProb();
            newObj.getChildren().add(objects.get(i));
        }

        newObj.setSymbol(newSymbol);
        newObj.setProb(newProb);
        return newObj;
    }

    /**
     * Combines multiple objects together, adding the new object to the list and removing the redundent ones.
     */
    public void updateAlphabet() {
        int difference = alphabet.size() % codeAlphabetSize;
        ArrayList<HuffmanObject> objectsToAdd = new ArrayList<HuffmanObject>();

        if (difference > 0 && difference != 1 && codeAlphabetSize != 2) {
            for (int i = 0; i < difference; i++) {
                objectsToAdd.add(alphabet.get(alphabet.size() - i - 1));
            }

            alphabet.add(addObjects(objectsToAdd));

            for (int i = 0; i < difference; i++) {
                alphabet.remove(alphabet.size() - 2);
            }
        } else {
            for (int i = 0; i < codeAlphabetSize; i++) {
                objectsToAdd.add(alphabet.get(alphabet.size() - i - 1));
            }

            alphabet.add(addObjects(objectsToAdd));

            for (int i = 0; i < codeAlphabetSize; i++) {
                alphabet.remove(alphabet.size() - 2);
            }
        }

    }

    /**
     * Orders alphabet in descending order of probability values.
     */
    public void orderAlphabet() {
        HuffmanObject temp;
        for (int i = 0; i < alphabet.size(); i++) {
            for (int j = 0; j < alphabet.size() - 1; j++) {
                if (alphabet.get(j).getProb() + 0.0001 < alphabet.get(j + 1).getProb()) {
                    temp = alphabet.get(j);
                    alphabet.set(j, alphabet.get(j + 1));
                    alphabet.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Prints all objects within the alphabet.
     */
    public void printAlphabet() {
        for (int i = 0; i < alphabet.size(); i++) {
            System.out.println(alphabet.get(i).getSymbol() + " " + alphabet.get(i).getProb() + " " + alphabet.get(i).getCode());
        }
        System.out.println();
    }

    /**
     * Recursive method that builds tree of huffman objects.
     */
    public void recurseAlphabet() {
        if (alphabet.size() != 1) {
            orderAlphabet();
            updateAlphabet();
            printAlphabet();
            recurseAlphabet();
        }
    }

    /**
     * Recursive method used to build code tree.
     * @param obj root node.
     */
    public void buildCode(HuffmanObject obj) {
        if (obj.getChildren().size() == 0) {
            obj.print();
        } else {
            for (int i = 0; i < obj.getChildren().size(); i++) {
                HuffmanObject currentObj = obj.getChildren().get(i);
                currentObj.setCode(obj.getCode() + (codeAlphabetSize - 1 - i));
                buildCode(currentObj);
            }
        }
    }

    /**
     * Explores code tree for huffman object with specific symbol.
     * @param symbol desired symbol
     * @param obj root node
     * @return object containing specific symbol or null if object cannot be found.
     */
    public HuffmanObject findSymbolCode(char symbol, HuffmanObject obj) {
        HuffmanObject toReturn = null;
        if (obj.getChildren().size() == 0) {
            if (obj.getSymbol().charAt(0) == symbol) {
                return obj;
            }
        } else {
            for (int i = 0; i < obj.getChildren().size(); i++) {
                if (toReturn == null) {
                    toReturn = findSymbolCode(symbol, obj.getChildren().get(i));
                }
            }
        }
        return toReturn;
    }

    /**
     * Uses Huffman encoding to encode a string of symbols constricted from the source alphabet.
     */
    public void encode() {
        printAlphabet();
        recurseAlphabet();

        buildCode(alphabet.get(0));

        System.out.println(message);

        String coded = "";

        for (int i = 0; i < message.length(); i++) {
            coded += findSymbolCode(message.charAt(i), alphabet.get(0)).getCode();
        }

        codedMessage = coded;
        System.out.println(coded);
    }

    /**
     * Explores code tree for object with specific code.
     * @param code desired code to find
     * @param obj root node
     * @return object with matching code or null if matching object cannot be found.
     */
    public HuffmanObject findSymbolFromCode(String code, HuffmanObject obj) {
        HuffmanObject toReturn = null;
        if (obj.getChildren().size() == 0) {
            if (obj.getCode().equals(code)) {
                return obj;
            }
        } else {
            for (int i = 0; i < obj.getChildren().size(); i++) {
                if (toReturn == null) {
                    toReturn = findSymbolFromCode(code, obj.getChildren().get(i));
                }
            }
        }
        return toReturn;
    }

    /**
     * Decodes a string that was encoded using huffman encoding.
     */
    public void decode() {
        String decoded = "";
        String currentCode = "";
        for (int i = 0; i < codedMessage.length(); i++) {
            currentCode += codedMessage.charAt(i);
            HuffmanObject obj = findSymbolFromCode(currentCode, alphabet.get(0));
            if (obj != null) {
                decoded += obj.getSymbol();
                currentCode = "";
            }
        }

        decodedMessage = decoded;
        System.out.println(decoded);
    }

    public String getMessage() {
        return message;
    }

    public String getCodedMessage() {
        return codedMessage;
    }

    public String getDecodedMessage() {
        return decodedMessage;
    }

    /**
     * Calculates the average length of a code word within the huffman encoding.
     * @return
     */
    public double generateAverageLength() {
        buildMean(alphabet.get(0));
        return weightedMean;
    }

    private void buildMean(HuffmanObject obj) {
        if (obj.getChildren().size() == 0) {
            weightedMean += obj.getProb() * obj.getCode().length();
        } else {
            for (int i = 0; i < obj.getChildren().size(); i++) {
                buildMean(obj.getChildren().get(i));
            }
        }
    }

    /**
     * Calculates the huffman compression ratio.
     * @return compression ratio
     */
    public double calculateCompressionRatio() {
        return (message.length() * 7)/codedMessage.length();
    }

    /**
     * Sets message to be encoded.
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
