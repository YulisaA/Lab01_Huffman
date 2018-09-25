package com.example.dell.lab02_huffman;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.*;


public class LZW {


    @TargetApi(Build.VERSION_CODES.O)
    public static String compress(String text) {
        //Diccionary with text characters
        int size = 0;
        String previousCharacter = "";
        Map<String,Integer> dictionary = new HashMap<String,Integer>();

        //values no repeated
        initValues(dictionary, text);
        String dictionaryValues = dictionaryToString(dictionary);
        size = dictionary.size();

        List<String> result = new ArrayList<String>();
        for (char actualCharacter : text.toCharArray()) {
            String previousConcatActual = previousCharacter + actualCharacter;
            if (dictionary.containsKey(previousConcatActual))
                previousCharacter = previousConcatActual;
            else {
                result.add(Integer.toString(dictionary.get(previousCharacter)));
                //Add key and value to diccionary
                dictionary.put(previousConcatActual, 1 + size++);
                previousCharacter = "" + actualCharacter;
            }
        }

        //Characters to list
        result.add(Integer.toString(dictionary.get(previousCharacter)));

        //List is converted to string, to write on file.lzw
        String joined = String.join(",", result);
        return dictionaryValues + joined;
    }

    //This method insert the initial values of the text received to compress.
    public static Map<String, Integer> initValues(Map<String,Integer> dictionary, String text)
    {
        int count = removeRepeatedChars(text);
        String [] orderText = new String[count];
        int j = 0;
        for (int i = 0; i < text.length(); i++)
            if(!isRepeated(orderText, Character.toString(text.charAt(i))))
            {
                orderText[j] = Character.toString(text.charAt(i));
                j++;
            }
        //The array is sort to create a sorted diccionary
        Arrays.sort(orderText);
        for (int i = 0; i < count; i++)
        {
            dictionary.put(orderText[i], i + 1);
        }
        return dictionary;
    }

    public static boolean isRepeated(String[] array, String valor) {
        boolean isrepeated = false;
        for(int i = 0; i < array.length; i++) {
            if(array[i] != null)
            {
                String actual = array[i];
                if (actual.equals(valor)) {
                    isrepeated = true;
                }
            }
        }
        return isrepeated;
    }
    public static int removeRepeatedChars(String text)
    {
        char[] textToArray = text.toCharArray();
        Arrays.sort(textToArray);
        int count = 0;

        for (int i = 0; i < textToArray.length ; i++)
        {
            if((i+1) < textToArray.length)
            {
                if (textToArray[i] != (textToArray[i + 1]))
                {
                    count++;
                }
            }
        }
        return count + 1;
    }
    @TargetApi(Build.VERSION_CODES.N)
    private static String dictionaryToString(Map<String,Integer> dictionary) {

        StringBuilder stringBuilder = new StringBuilder();

        dictionary.forEach((k,v) -> stringBuilder.append(k +":" + v + ";"+ "\n"));

        return stringBuilder.toString();

    }

}
