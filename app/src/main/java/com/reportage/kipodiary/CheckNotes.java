package com.reportage.kipodiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheckNotes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_notes);

        Intent intent = getIntent();
        String lesson_id = intent.getStringExtra("lesson_id");
        TextView lessonIdTextView = findViewById(R.id.lesson_id_textview);
        lessonIdTextView.setText(lesson_id);


    }
}