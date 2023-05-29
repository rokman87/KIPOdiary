package com.reportage.kipodiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CheckNotes extends AppCompatActivity {
    private TextView notesTextView;
    private String lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_notes);

        //Получаю id Пары
        Intent intent = getIntent();
        lessonId = intent.getStringExtra("lesson_id");
        System.out.println("lessonId: " + lessonId);
        //Задаю значение TextView
        getNotesFromDataBase();


    }

    private void getNotesFromDataBase() {
        String url = "https://ginkel.ru/kipo/get_notes_from_db.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        notesTextView = findViewById(R.id.lesson_id_textview);
                        notesTextView.setText(response);// Находим элементы
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