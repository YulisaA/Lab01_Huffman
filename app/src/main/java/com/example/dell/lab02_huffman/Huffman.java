package com.example.dell.lab02_huffman;

import android.annotation.TargetApi;
import android.os.Build;

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
        String myCodes = calculateFrequencies(nodes);
        //Create tree with nodes on Queue
        buildTree(nodes);
        generateCodes(nodes.peek(), "");

        encodeText();

        //myCodes return the values with frequencies, and encodeText show the text in 0´s and 1´s
        return myCodes + encodeText();
    }

    //In order to decode, the tree is created with the frequencies and values received from file
    public static String Decode(String o, String[] parts) {
        ASCII = new int[128];
        nodes.clear();
        codes.clear();
        encoded = "";
        decoded = "";
        utilities(parts, nodes);
        buildTree(nodes);
        generateCodes(nodes.peek(), "");
        return decodeText(o.trim());
    }

    public static void utilities(String[] parts, PriorityQueue<Node> justnodes )
    {
        for (int i = 0; i < parts.length -1; i++){
            String[] subparts = parts[i].split(":");
            justnodes.add(new Node(Double.parseDouble(subparts[1].trim()), subparts[0].trim()));
            System.out.println( Double.parseDouble(subparts[1].trim()) + ":"+ subparts[0].trim());
        }
    }


    private static String decodeText(String encode) {
        decoded = "";
        Node node = nodes.peek();

        //verify nodes to find the prefix and get value
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
                    System.out.println("Error");
        }
        System.out.println("Decoded: " + decoded);
        return decoded;
    }


    //insert characters and codes
    private static String encodeText() {
        encoded = "";
        for (int i = 0; i < text.length(); i++)
            encoded += codes.get(text.charAt(i));
        System.out.println("Encoded Text: " + encoded);

        return encoded;
    }

    private static void buildTree(PriorityQueue<Node> mynodes) {
        while (mynodes.size() > 1)
            mynodes.add(new Node(mynodes.poll(), mynodes.poll()));
    }

    private static String calculateFrequencies(PriorityQueue<Node> mynodes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++)
            ASCII[text.charAt(i)]++;

        for (int i = 0; i < ASCII.length; i++)
            if (ASCII[i] > 0) {
                mynodes.add(new Node(ASCII[i] / (text.length() * 1.0), ((char) i) + ""));
                System.out.println("'" + ((char) i) + "' : " + ASCII[i] / (text.length() * 1.0));
                stringBuilder.append(((char) i) +":" + ASCII[i] / (text.length() * 1.0) +";"+ "\n");
            }
        return stringBuilder.toString();
    }

    //Generate prefix
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

//Node class
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

