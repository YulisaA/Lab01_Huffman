package com.example.dell.lab02_huffman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnComp)
    Button btnComp;
    @BindView(R.id.btnLZW)
    Button btnLZW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnComp)
    public void onViewClickedHuffman() {
        Intent intent = new Intent(this, MainActivity2.class);

        //Send 1 when user chooses Huffman
        intent.putExtra("sendNumber", 1);
        startActivity(intent);
    }

    @OnClick(R.id.btnLZW)
    public void onViewClickedLZW() {
        Intent intent = new Intent(this, MainActivity2.class);
        //Send 1 when user chooses Huffman
        intent.putExtra("sendNumber", 2);
        startActivity(intent);
    }
}
