package com.example.dell.lab02_huffman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity2 extends AppCompatActivity {
    @BindView(R.id.txtFile)
    EditText txtFile;
    @BindView(R.id.btnExplorer)
    Button btnComp;
    @BindView(R.id.txtFileContent)
    EditText txtFileContent;

    String path;
    Files fileOptions = new Files();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnExplorer)
    public void onViewClicked() {
        fileOptions.Explorer();
        String text = fileOptions.Read(fileOptions.Explorer());
        txtFileContent.setText(text);
    }


}


