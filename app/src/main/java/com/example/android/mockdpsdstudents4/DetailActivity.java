package com.example.android.mockdpsdstudents4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

public class DetailActivity extends AppCompatActivity {

    TextView dtlTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dtlTextview = findViewById(R.id.dtl_textview);

        //receiving data through Intent.
        Intent i = getIntent();

        // first way, receive a simple string (student's name)
        //String name = i.getStringExtra("just a name");
        //dtlTextview.setText("hello "+name);

        // second way, receive whole object in JSON format
        Gson gson = new Gson();
        DPSDStudent selectedStudent = gson.fromJson(getIntent().getStringExtra("the whole object"), DPSDStudent.class);
        String name = selectedStudent.getName();
        dtlTextview.setText("hello "+name);

    }
}