package com.reportage.kipodiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LessonNotes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_notes);


        // Получаем значение TextView из Intent
        Intent intent = getIntent();
        String textValue = intent.getStringExtra("textValue");

// Используем значение TextView
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = findViewById(R.id.textView);
        textView.setText(textValue);
    }
}