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
    @BindView(R.id.btnMComp)
    Button btnMComp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnComp)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnMComp)
    public void onViewClicked1() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }
}
