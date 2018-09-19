package com.example.dell.lab02_huffman;

public class Nodo<T> {

    public Nodo<T> left;
    public Nodo<T> right;
    public T type;
    public int frequency;
    public String code;

    //Obtain frequency
    public int getFrequency(){
        return frequency;
    }

    //Verify is the node is leaf
    public boolean Leaf()
    {
        return (right == null) && (left == null);
    }

    //Constructor
    public Nodo(T type, int frequency)
    {
        this.type = type;
        this.frequency = frequency;
    }
}