package com.example.dell.lab02_huffman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity3 extends AppCompatActivity {

    public ArrayAdapter<String> adapter;
    @BindView(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
        ReceiveData();


    }

    public void ReceiveData() {
        Bundle extras = getIntent().getExtras();
        ArrayList<String> list = extras.getStringArrayList("array");
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.scheme, list);
        listview.setAdapter(adapter);
    }
}
