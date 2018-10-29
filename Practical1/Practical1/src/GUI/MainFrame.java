package GUI;

import Aritmetic.ArithmeticEncoder;
import Huffman.HuffmanEncoder;
import Huffman.HuffmanObject;
import Logic.StringGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class defining the GUI design and actions connected to specified components.
 */
public class MainFrame {
    private JPanel mainPanel;
    private JLabel label1;
    private JTextField symbolInput;
    private JTextArea textOutput;
    private JButton addObject;
    private JTextField probInput;
    private JButton encodeButton;
    private JCheckBox endOfStringCharacterCheckBox;
    private JButton resetDefaultButton;
    private JLabel label2;
    private HuffmanEncoder binaryHuffmanEncoder = new HuffmanEncoder(2);
    private HuffmanEncoder tertiaryHuffmanEncoder = new HuffmanEncoder(3);
    private ArithmeticEncoder arithmeticEncoder = new ArithmeticEncoder(16);
    private ArrayList<Character> symbolList = new ArrayList<>();
    private ArrayList<Double> probList = new ArrayList<>();
    private double probSum = 0.0;
    private String eodChar = "";
    private StringGenerator strGen = new StringGenerator();

    public MainFrame() {
        /**
         * Action listener for when the add object button is pressed. Runs through input validation before adding
         * objects.
         */
        addObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String symbol = symbolInput.getText();
                    if (symbol.length() != 1) {
                        throw new InputSymbolLengthException();
                    } else {
                        if (symbolList.contains(symbol.charAt(0))) {
                            throw new InputSymbolExistsException();
                        }
                    }

                    double prob;
                    prob = Double.parseDouble(probInput.getText());

                    if (prob >= 1.0) {
                        throw new ProbabilityTooHighException();
                    } else {
                        double tempProbSum = probSum + prob;
                        if (tempProbSum > 1) {
                            throw new ProbabiltySumException();
                        }
                    }

                    if (endOfStringCharacterCheckBox.isSelected()) {
                        if (eodChar.equals("")) {
                            eodChar = symbol;
                            arithmeticEncoder.setEodChar(eodChar.charAt(0));
                        } else {
                            throw new EODCharacterAlreadyChosenException();
                        }
                    }

                    symbolList.add(symbol.charAt(0));
                    probList.add(prob);
                    probSum += prob;

                    if (!eodChar.equals("")) {
                        if (!(symbol.charAt(0) == eodChar.charAt(0))) {
                            textOutput.append("{" + symbol + ", " + prob + "}\n");
                        } else {
                            textOutput.append("{" + symbol + ", " + prob + "} <--- EOD\n");
                        }
                    } else {
                        textOutput.append("{" + symbol + ", " + prob + "}\n");
                    }
                    symbolInput.setText("");
                    probInput.setText("");

                    HuffmanObject obj = new HuffmanObject(symbol, prob);
                    binaryHuffmanEncoder.addObjectToAlphabet(obj);
                    tertiaryHuffmanEncoder.addObjectToAlphabet(obj);
                    arithmeticEncoder.getAlphabet().add(symbol.charAt(0));
                    arithmeticEncoder.getProbs().add(prob);

                } catch (NumberFormatException ex) {
                    probInput.setBackground(Color.RED);
                    JOptionPane.showMessageDialog(null, "Probabilities must be in" +
                            " deciamal format.");
                    probInput.setBackground(Color.WHITE);

                } catch (InputSymbolLengthException ex) {
                    symbolInput.setBackground(Color.RED);
                    JOptionPane.showMessageDialog(null, "Symbols must only be one character.");
                    symbolInput.setBackground(Color.WHITE);
                } catch (ProbabilityTooHighException ex) {
                    probInput.setBackground(Color.RED);
                    JOptionPane.showMessageDialog(null, "A probability must be less than 1.");
                    probInput.setBackground(Color.WHITE);
                } catch (ProbabiltySumException ex) {
                    probInput.setBackground(Color.RED);
                    JOptionPane.showMessageDialog(null, "The sum of all probabilities must not " +
                            "exceed 1.");
                    probInput.setBackground(Color.WHITE);
                } catch (InputSymbolExistsException ex) {
                    symbolInput.setBackground(Color.RED);
                    JOptionPane.showMessageDialog(null, "A symbol can only be added once.");
                    symbolInput.setBackground(Color.WHITE);
                } catch (EODCharacterAlreadyChosenException ex) {
                    JOptionPane.showMessageDialog(null, "An EOD character has already been " +
                            "selected.");
                }
            }
        });

        /**
         * Action listener for when the encode button is pressed. Runs validation checks before encoding data and
         * printing output data.
         */
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (symbolList.size() < 3) {
                        throw new AlphabetTooSmallException();
                    }
                    if (eodChar.equals("")) {
                        JOptionPane.showMessageDialog(null, "No EOD character was specified. The most" +
                                " improbable symbol shall be assigned as the EOD character.");
                        double temp = 1.0;
                        int index = 0;
                        for (int i = 0; i < probList.size(); i++) {
                            if (probList.get(i) <= temp) {
                                temp = probList.get(i);
                                index = i;
                            }
                        }
                        eodChar = symbolList.get(index).toString();
                        arithmeticEncoder.setEodChar(eodChar.charAt(0));
                    }

                    String message = strGen.generateString(symbolList, eodChar);
                    binaryHuffmanEncoder.setMessage(message);
                    tertiaryHuffmanEncoder.setMessage(message);
                    arithmeticEncoder.setMessage(message);

                    textOutput.append("==========\n");
                    binaryHuffmanEncoder.encode();
                    textOutput.append("Randomised message:\n");
                    textOutput.append(binaryHuffmanEncoder.getMessage() + "\n");

                    textOutput.append("==========\n");
                    textOutput.append("Message coded using binary Huffman encoding:\n");
                    textOutput.append(binaryHuffmanEncoder.getCodedMessage() + "\n");

                    textOutput.append("==========\n");
                    textOutput.append("Coded message then decoded using Huffman decoder:\n");
                    binaryHuffmanEncoder.decode();
                    textOutput.append(binaryHuffmanEncoder.getDecodedMessage() + "\n");

                    textOutput.append("==========\n");
                    textOutput.append("Message coded using tertiary Huffman encoding:\n");
                    tertiaryHuffmanEncoder.encode();
                    textOutput.append(tertiaryHuffmanEncoder.getCodedMessage() + "\n");

                    textOutput.append("==========\n");
                    textOutput.append("Coded message then decoded using Huffman decoder:\n");
                    tertiaryHuffmanEncoder.decode();
                    textOutput.append(tertiaryHuffmanEncoder.getDecodedMessage() + "\n");

                    textOutput.append("==========\n");
                    textOutput.append("Message coded using arithmetic encoding with 16 bit precision:\n");
                    String arithmeticEncoding = arithmeticEncoder.encode();
                    textOutput.append(arithmeticEncoding + "\n");

                    textOutput.append("==========\n");
                    textOutput.append("Coded message then decoded using arithmetic decoder:\n");
                    textOutput.append(arithmeticEncoder.decode() + "\n");

                    textOutput.append("==========\n");

                    textOutput.append("Length of message (assuming 7 bit ASCII): " + message.length()*7 + "\n\n");

                    textOutput.append("Average code word length of binary huffman code: " +
                            binaryHuffmanEncoder.generateAverageLength() + "\n");

                    textOutput.append("Compression ratio of binary Huffman encoding: " +
                            binaryHuffmanEncoder.calculateCompressionRatio() + "\n");

                    textOutput.append("Length of encoded string: " + binaryHuffmanEncoder.getCodedMessage().length()
                            + "\n\n");

                    textOutput.append("Average code word length of tertiary huffman code: " +
                            tertiaryHuffmanEncoder.generateAverageLength() + "\n");

                    textOutput.append("Compression ratio of tertiary Huffman code: " +
                            tertiaryHuffmanEncoder.calculateCompressionRatio() + "\n");

                    textOutput.append("Length of encoded string: " + tertiaryHuffmanEncoder.getCodedMessage().length()
                            + "\n\n");


                    textOutput.append("Compression ratio of arithmetic encoding: "
                            + arithmeticEncoder.calculateCompressionRatio() + "\n");

                    textOutput.append("Length of arithmetic code: "
                            + arithmeticEncoder.getTag().length() + "\n\n");

                    textOutput.append("Entropy of given information source: " + calculateEntropy() + "\n");

                } catch (AlphabetTooSmallException ex) {
                    JOptionPane.showMessageDialog(null, "Given source alphabet is too small. " +
                            "Either add more symbols or start again.");
                }
            }
        });

        /**
         * Action listener for reset button. Returns all variables to default states.
         */
        resetDefaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textOutput.setText("Source Alphabet:\n");
                arithmeticEncoder = new ArithmeticEncoder(16);
                binaryHuffmanEncoder = new HuffmanEncoder(2);
                tertiaryHuffmanEncoder = new HuffmanEncoder(3);
                symbolList = new ArrayList<Character>();
                probList = new ArrayList<Double>();
                eodChar = "";
                probSum = 0.0;
            }
        });
    }

    /**
     * Calculates the entropy of the source alphabet.
     * @return
     */
    public double calculateEntropy() {
        double entropy = 0.0;
        for (int i = 0; i < probList.size(); i++) {
            entropy += -1 * probList.get(i) * Math.log(probList.get(i))/Math.log(2);
        }
        return entropy;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CS3302 Practical 1");
        frame.setContentPane(new MainFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(650, 400);
        frame.setVisible(true);
    }
}
