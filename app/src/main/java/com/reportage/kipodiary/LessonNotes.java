package com.reportage.kipodiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LessonNotes extends AppCompatActivity {

    private EditText notesEditText;
    private Button saveNotesButton;
    private String lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_notes);
        // Получаем lessonId
        lessonId = getIntent().getStringExtra("lesson_id");
        System.out.println("lessonId: " + lessonId);
        // Находим элементы
        notesEditText = findViewById(R.id.notesEditText);
        saveNotesButton = findViewById(R.id.saveNotesButton);
        //Получаем заметку
        getNotesFromDataBase();
        // Добавляем слушатель нажатия на кнопку
        saveNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем текст из EditText
                String notesNew = notesEditText.getText().toString();
                // Сохраняем данные в базу данных
                saveNotesToDatabase(notesNew);
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backButton = findViewById(R.id.go_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void saveNotesToDatabase(String notesNew) {
        String url = "https://ginkel.ru/kipo/lesson_notes.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(LessonNotes.this, "Заметка добавлена", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LessonNotes.this, TeacherActivity.class);
                        intent.putExtra("password", "true");
                        startActivity(intent);}},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}}) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lessonId", lessonId);
                params.put("notes", notesNew);
                return params;
            }};
        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void getNotesFromDataBase() {
        String url = "https://ginkel.ru/kipo/get_notes_from_db.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        notesEditText.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Обработка ошибки
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lessonId", lessonId);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
