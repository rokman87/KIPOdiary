package com.reportage.kipodiary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        // Добавляем слушатель нажатия на кнопку
        saveNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем текст из EditText
                String notes = notesEditText.getText().toString();

                // Сохраняем данные в базу данных
                saveNotesToDatabase(notes);
            }
        });
    }

    private void saveNotesToDatabase(String notes) {
        String url = "https://ginkel.ru/kipo/lesson_notes.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Обработка ответа от сервера
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
                params.put("notes", notes);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
