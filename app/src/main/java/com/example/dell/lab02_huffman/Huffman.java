package com.example.dell.lab02_huffman;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.LogPrinter;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.security.AccessControlContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

import static java.lang.Math.round;

@TargetApi(Build.VERSION_CODES.N)
public class Huffman {

    static PriorityQueue<Node> nodes = new PriorityQueue<>((o1, o2) -> (o1.value < o2.value) ? -1 : 1);
    static TreeMap<Character, String> codes = new TreeMap<>();
    static String text = "";
    static String encoded = "";
    static String decoded = "";
    static int ASCII[] = new int[128];
    static Integer size = 0;

    public static String Encode (String mytext)
    {
        text = mytext;
        Scanner scanner = new Scanner(text);
        text = scanner.nextLine();
        ASCII = new int[128];
        nodes.clear();
        codes.clear();
        encoded = "";
        decoded = "";
        System.out.println("Text: " + text);
        String myCodes = calculateCharIntervals(nodes, true);
        buildTree(nodes);
        generateCodes(nodes.peek(), "");
        printCodes();

        System.out.println("-- Encoding/Decoding --");
        encodeText();

        return myCodes + encodeText();
    }
    public String Decode(String o) {

        decodeText(o);
        return decodeText(o);
    }


    private static boolean IsSameCharacterSet() {
        boolean flag = true;
        for (int i = 0; i < text.length(); i++)
            if (ASCII[text.charAt(i)] == 0) {
                flag = false;
                break;
            }
        return flag;
    }

    private static String decodeText(String encode) {
        decoded = "";
        Node node = nodes.peek();
        for (int i = 0; i < encode.length(); ) {
            Node tmpNode = node;
            while (tmpNode.left != null && tmpNode.right != null && i < encode.length()) {
                if (encode.charAt(i) == '1')
                    tmpNode = tmpNode.right;
                else tmpNode = tmpNode.left;
                i++;
            }
            if (tmpNode != null)
                if (tmpNode.character.length() == 1)
                    decoded += tmpNode.character;
                else
                    System.out.println("Input not Valid");

        }
        System.out.println("Decoded Text: " + decoded);
        return decoded;
    }

    private String decodeTextf(String Textdesc) {
        decoded = "";
        TreeMap<String, String> myCodes = new TreeMap<>();
        String[] parts = Textdesc.split(";");
        for (int i = 0; i < parts.length -1; i++){
            String[] subparts = parts[i].split(":");
            myCodes.put(subparts[1].trim(), subparts[0].trim());
        }
        String cod = parts[parts.length - 1];
        StringBuilder sb = new StringBuilder();
        String []aux = new String[(cod.length())/8];
        int i = 0;
        for(int k = 0; k < cod.length();  k+=8)
        {
            aux[i] = cod.substring(k, k+8);
            i++;
        }
        System.out.println(aux[2]);
        for(int j = 0; j < cod.length()/8; j++)
        {
            sb.append(myCodes.get(aux[j]));
        }
        return sb.toString();
    }

    private static String encodeText() {
        encoded = "";
        for (int i = 0; i < text.length(); i++)
            encoded += codes.get(text.charAt(i));
        System.out.println("Encoded Text: " + encoded);

        return encoded;
    }

    private static void buildTree(PriorityQueue<Node> vector) {
        while (vector.size() > 1)
            vector.add(new Node(vector.poll(), vector.poll()));
    }

    private static String calculateCharIntervals(PriorityQueue<Node> vector, boolean printIntervals) {
        if (printIntervals) System.out.println("-- intervals --");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++)
            ASCII[text.charAt(i)]++;

        for (int i = 0; i < ASCII.length; i++)
            if (ASCII[i] > 0) {
                vector.add(new Node(ASCII[i] / (text.length() * 1.0), ((char) i) + ""));
                if (printIntervals)
                    System.out.println("'" + ((char) i) + "' : " + ASCII[i] / (text.length() * 1.0));
                    stringBuilder.append(((char) i) +":" + ASCII[i] / (text.length() * 1.0) +";"+ "\n");
            }
        return stringBuilder.toString();
    }
    private static void printCodes() {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("--- Printing Codes ---");
        codes.forEach((k, v) -> System.out.println("'" + k + "' : " + v));

    }

    private static void generateCodes(Node node, String s) {
        if (node != null) {
            if (node.right != null)
                generateCodes(node.right, s + "1");

            if (node.left != null)
                generateCodes(node.left, s + "0");

            if (node.left == null && node.right == null)
            {
                codes.put((node.character.charAt(0)), s);
            }

        }
    }
}

class Node {
    Node left, right;
    double value;
    String character;

    public Node(double value, String character) {
        this.value = value;
        this.character = character;
        left = null;
        right = null;
    }

    public Node(Node left, Node right) {
        this.value = left.value + right.value;
        character = left.character + right.character;
        if (left.value < right.value) {
            this.right = right;
            this.left = left;
        } else {
            this.right = left;
            this.left = right;
        }
    }
}

