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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    Huffman huffman = new Huffman();
    @BindView(R.id.btnDesc)
    Button btnDesc;
    String filename = "";
    String texto = "";
    String name = "";
    Integer size = 0;

    //To show the properties files
    public ArrayList<String> Lista;
    public ArrayAdapter<String> adapter;
    Integer sizeComp = 0;
    @BindView(R.id.listview)
    ListView listview;
    Integer receiveHuffmanOrLZW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        Lista = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.scheme, Lista);
        listview.setAdapter(adapter);

        ReceiveData();
    }

    public void ReceiveData() {
        Bundle extras = getIntent().getExtras();
        receiveHuffmanOrLZW = extras.getInt("sendNumber");
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

            try {

                texto = Read(selectedfile);
                name = selectedfile.getPath();
            } catch (IOException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                e.printStackTrace();
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
            if (exist) {
                File newFile;
                newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/MisCompresiones");
                newFile.mkdirs();
            }
            File f;
            f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MisCompresiones", filename);


            try {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(content.getBytes());
                fos.close();
                Toast.makeText(this, "Archivo en: storage/MisCompresiones", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();

            }
            if(size != 0 && sizeComp != 0)
            {
                Double Razon = (double) sizeComp / size;
                Double Factor = (double) size / sizeComp;
                Double Porcentaje = (double) (((size - sizeComp) / size) * 100);
                String NombreCod = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/MisCompresiones" + filename;
                String Aux = "Nombre Archivo :" + name + "\n" + "Path : " + NombreCod + " " + "\n" +"Razón de Compresión: " + Razon.toString() + "\n" + "Factor de Compresión:" + Factor.toString()
                        + "\n" + "Porcentaje de Reducción: " + Porcentaje + "%";
                Lista.add(Aux);
                adapter.notifyDataSetChanged();
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
        try{
            if(receiveHuffmanOrLZW == 1 )
            {
                //Filename with extension .huff
                String[] split = filename.split("\\.");
                String nameFile = split[0] + ".huff";

                String resultHuffman = huffman.Encode(txtFileContent.getText().toString());
                txtFileContent.setText("");

                WriteFile(nameFile, resultHuffman);
            }

            if(receiveHuffmanOrLZW == 2){
                //Filename with extension .lzw
                String[] split = filename.split("\\.");
                String nameFile = split[0] + ".lzw";

                String resultLZW = huffman.Encode(txtFileContent.getText().toString());
                txtFileContent.setText("");

                WriteFile(nameFile, resultLZW);
            }

        }
        catch (Exception e){
            Toast.makeText(this, "No se pudo comprimir.", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.btnDesc)
    public void onViewClickedDesc() {

        try{
            if(receiveHuffmanOrLZW == 1)
            {
                String[] split = filename.split("\\.");
                String[] split2 = split[0].split("/");
                System.out.println(split[0]);

                String nameFile = split2[1] + ".txt";
                System.out.println(nameFile);

                String[] parts = txtFileContent.getText().toString().split(";");
                String cod = parts[parts.length - 1];
                String resultHuffman = huffman.Decode(cod, parts);
                sizeComp = cod.getBytes().length;
                txtFileContent.setText(resultHuffman);
                size = resultHuffman.getBytes().length;

                WriteFile(nameFile, resultHuffman);

            }
            if(receiveHuffmanOrLZW == 2)
            {
                String[] split = filename.split("\\.");
                String[] split2 = split[0].split("/");
                System.out.println(split[0]);

                String nameFile = split2[1] + ".txt";
                System.out.println(nameFile);

                String[] parts = txtFileContent.getText().toString().split(";");
                String cod = parts[parts.length - 1];
                String resultLZW = huffman.Decode(cod, parts);
                sizeComp = cod.getBytes().length;
                txtFileContent.setText(resultLZW);
                size = resultLZW.getBytes().length;

                WriteFile(nameFile, resultLZW);
            }

        }
        catch (Exception e) {
            if (receiveHuffmanOrLZW == 1)
            {Toast.makeText(this, "Elija un archivo .huff", Toast.LENGTH_LONG).show();}
            else{
                Toast.makeText(this, "Elija un archivo .lzw", Toast.LENGTH_LONG).show();
            }


        }

    }
}


