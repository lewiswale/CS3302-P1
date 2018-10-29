package Huffman;

import java.util.ArrayList;

/**
 * Class used to store data of Huffman objects.
 */
public class HuffmanObject {
    private String symbol;
    private double prob;
    private String code;
//    private HuffmanObject prevObj1;
//    private HuffmanObject prevObj2;
    private ArrayList<HuffmanObject> children;

    HuffmanObject() {
        this.children = new ArrayList<HuffmanObject>();
        this.code = "";
    }

    /**
     * Constructor of huffman object
     * @param symbol specified symbol
     * @param prob specified probability
     */
    public HuffmanObject(String symbol, double prob) {
        this.symbol = symbol;
        this.prob = prob;
        this.code = "";
//        this.prevObj1 = null;
//        this.prevObj2 = null;
        this.children = new ArrayList<HuffmanObject>();
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

//    public HuffmanObject getPrevObj1() {
//        return prevObj1;
//    }
//
//    public void setPrevObj1(HuffmanObject prevObj1) {
//        this.prevObj1 = prevObj1;
//    }
//
//    public HuffmanObject getPrevObj2() {
//        return prevObj2;
//    }
//
//    public void setPrevObj2(HuffmanObject prevObj2) {
//        this.prevObj2 = prevObj2;
//    }


    public ArrayList<HuffmanObject> getChildren() {
        return children;
    }

    public void print() {
        System.out.println(getSymbol());
        System.out.println(getProb());
        System.out.println(getCode());
        System.out.println();
    }
}
