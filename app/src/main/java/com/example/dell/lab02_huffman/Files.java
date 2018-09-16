package com.example.dell.lab02_huffman;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Files extends AppCompatActivity{

    String text;
    boolean origin = false;
    public String Read(String path) {
        try {
            BufferedReader read =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput(path)));
            text = read.readLine();
            read.close();
        } catch (Exception ex) {
            //Toast
        }

        return text;
    }
    public void Write(String path, String line)
    {
        try
        {
            OutputStreamWriter writer =
                    new OutputStreamWriter(
                            openFileOutput(path, Context.MODE_PRIVATE));

            writer.write(line);
            writer.close();
        }
        catch (Exception ex)
        {
            //Toast
        }
    }
    //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

    public void Create(String nameFile, String extension)
    {
        try {
            File newFile = new File(Environment.getExternalStorageDirectory(), "Sources");
            if (!newFile.exists()) {
                newFile.mkdir();
            }
            try {
                File file = new File(newFile, nameFile + extension);
                file.createNewFile();
            } catch (Exception ex) {
                Log.e("Error", "ex: " + ex);
            }
        } catch (Exception e) {
            Log.e("Error", "e: " + e);
        }
    }

    public String Explorer()
    {
        int select_origin = 1;
        Intent intentOrigen = new Intent(Intent.ACTION_GET_CONTENT);
        intentOrigen.setType("*/storage");
        startActivityForResult(intentOrigen, select_origin);
        origin = true;
        String path = "";

        if (select_origin == RESULT_OK) {
            if (origin) {
                origin = false;
                path = intentOrigen.getData().getPath();
            }
        }
        return path;
    }

}
