package com.reportage.kipodiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {

    private String selectedTeacher;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // Получаем выбранного преподавателя из предыдущей активности
        selectedTeacher = getIntent().getStringExtra("selected_teacher");
        System.out.println("selected_teacher: " + selectedTeacher);

        // Находим TextView и устанавливаем в него выбранного преподавателя
        TextView teacherTextView = findViewById(R.id.textView);
        teacherTextView.setText(selectedTeacher);

        // Находим EditText для ввода пароля
        passwordEditText = findViewById(R.id.editTextPassword);


        // Находим кнопку для входа по паролю и устанавливаем обработчик нажатия
        Button loginButton = findViewById(R.id.buttonSubmit);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Получаем введенный пароль
                String password = passwordEditText.getText().toString();

                // Проверяем пароль и выполняем вход, если пароль верный
                if (checkPassword(password)) {
                    // Выполняем вход и переходим на следующую активность
                    Intent intent = new Intent(PasswordActivity.this, TeacherActivity.class);
                    intent.putExtra("selected_teacher", selectedTeacher);
                    intent.putExtra("password", password);
                    startActivity(intent);
                } else {
                    // Отображаем сообщение об ошибке
                    Toast.makeText(PasswordActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Находим кнопку для перехода в другую активность и устанавливаем обработчик нажатия
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button otherButton = findViewById(R.id.withoutPassword);
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переходим в другую активность и передаем выбранного преподавателя
                Intent intent = new Intent(PasswordActivity.this, TeacherActivity.class);
                intent.putExtra("selected_teacher", selectedTeacher);
                startActivity(intent);
            }
        });
    }

    private boolean checkPassword(String password) {
        // Выполняем POST запрос к PHP скрипту на сервере
        String url = "https://ginkel.ru/kipo/password_verification.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // Обрабатываем ответ от PHP скрипта
                        if (response.equals("true")) {
                            // Пароль верный
                            // Вызываем метод для обработки результата
                            handlePasswordCheckResult(true);
                        } else {
                            // Пароль неверный
                            // Вызываем метод для обработки результата
                            handlePasswordCheckResult(false);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Ошибка при выполнении запроса
                        // Вызываем метод для обработки результата
                        handlePasswordCheckResult(false);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                // Передаем параметры запроса (введенный пароль и выбранный преподаватель)
                Map<String, String> params = new HashMap<String, String>();
                params.put("selected_teacher", selectedTeacher);
                params.put("password", password);
                return params;
            }
        };

        // Добавляем запрос в очередь
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);

        // Возвращаем результат
        return false;
    }


    private void handlePasswordCheckResult(boolean isPasswordCorrect) {
        // Проверяем пароль и выполняем вход, если пароль верный
        if (isPasswordCorrect) {
            // Выполняем вход и переходим на следующую активность
            Intent intent = new Intent(PasswordActivity.this, TeacherActivity.class);
            intent.putExtra("selected_teacher", selectedTeacher);
            //intent.putExtra("password", passwordEditText.getText().toString());
            intent.putExtra("password", "true");
            startActivity(intent);
        } else {
            // Отображаем сообщение об ошибке
            Toast.makeText(PasswordActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
        }
    }


}
