package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MineSweeperActivity.class);
            startActivity(intent);
        });
    }
}