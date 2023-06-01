package com.reportage.kipodiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    private String password;
    private String selectedTeacher;
    private String newPass;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Получаем Intent, который запустил эту Activity
        Intent intent = getIntent();
        // Получаем значение параметра "password"
        password = intent.getStringExtra("password");
        selectedTeacher = intent.getStringExtra("selected_teacher");


        // Находим TextView и устанавливаем в него выбранного преподавателя
        TextView teacherTextView = findViewById(R.id.teacher_view);
        teacherTextView.setText(selectedTeacher);

        // Находим EditText для ввода пароля
        passwordEditText = findViewById(R.id.editTextPassword);
        // Получаем введенный пароль
        newPass = passwordEditText.getText().toString();

        // Находим кнопку для входа по паролю и устанавливаем обработчик нажатия
        Button loginButton = findViewById(R.id.buttonSubmit);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Отображаем сообщение
                Toast.makeText(ChangePassword.this, "Пароль изменен", Toast.LENGTH_SHORT).show();
                // Меняем пароль
                changePass(selectedTeacher, newPass);
            }
        });

    }

    private void changePass(String selectedTeacher, String newPass) {
        String url = "https://ginkel.ru/kipo/change_password.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(ChangePassword.this, TeacherActivity.class);
                        intent.putExtra("password", "true");
                        startActivity(intent);}},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}}) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("selected_teacher", selectedTeacher);
                params.put("newPass", newPass);
                return params;
            }};
        Volley.newRequestQueue(this).add(stringRequest);
    }
}