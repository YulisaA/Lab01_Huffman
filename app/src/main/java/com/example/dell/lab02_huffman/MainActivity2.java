package com.example.dell.lab02_huffman;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.constraint.Guideline;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.security.AccessController.getContext;

public class MainActivity2 extends AppCompatActivity {
    @BindView(R.id.txtFileContent)
    EditText txtFileContent;

    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.btnComp)
    Button btnComp;
    @BindView(R.id.btnExplorer)
    ImageButton btnExplorer;
    String text = "";
    String temp = "";

    Huffman huffman = new Huffman();
    @BindView(R.id.btnDesc)
    Button btnDesc;
    String filename = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btnExplorer)
    public void onViewClicked() {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();


            filename = selectedfile.getLastPathSegment();

            Toast.makeText(this, selectedfile.getPath(), Toast.LENGTH_LONG).show();
            try {
                Read(selectedfile);
                txtFileContent.setText(Read(selectedfile));

            } catch (IOException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String Read(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringBuilder = new StringBuilder();
        String Line;
        while ((Line = reader.readLine()) != null) {
            stringBuilder.append(Line);
        }
        input.close();
        reader.close();
        return stringBuilder.toString();
    }

    private Boolean isExternalStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "writable");
            return true;
        } else {
            return false;
        }
    }

    public void WriteFile(String filename, String content) {
        if (isExternalStorage() && verifyPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            boolean exist = true;
            if (exist){
                File nuevaCarpeta;
                nuevaCarpeta = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/MisCompresiones");
                nuevaCarpeta.mkdirs();
                exist = false;
            }
            File f;
            f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/MisCompresiones",filename);
            txtFileContent.setText(Environment.getExternalStorageDirectory().toString());

            try {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(content.getBytes());
                fos.close();
                Toast.makeText(this, "Archivo en: storage/MisCompresiones", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    public Boolean verifyPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        if (check == PackageManager.PERMISSION_GRANTED) {
            Log.i("w", "permisiongranted");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        return (check == PackageManager.PERMISSION_GRANTED);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.btnComp)
    public void onViewClickedComp() {

        String[]split = filename.split("\\.");
        String nameFile = split[0] + ".huff";

        String result = huffman.Encode(txtFileContent.getText().toString());
        WriteFile(nameFile, result);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.btnDesc)
    public void onViewClickedDesc() {
        String[]split = filename.split("\\.");


        String nameFile = split[0] + ".desc";

        String result = huffman.Decode(txtFileContent.getText().toString());
        WriteFile(nameFile, result);
        txtFileContent.setText(result);
    }
}


