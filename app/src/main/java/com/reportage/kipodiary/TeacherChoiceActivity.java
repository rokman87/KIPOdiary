package com.reportage.kipodiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_choice);
        //Добавляю селектор
        Spinner spinner = findViewById(R.id.mySpinner);
        new GetTeacher(this, spinner).execute();

        Button nextButton = findViewById(R.id.submit_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем выбранную группу
                String selected_teacher = spinner.getSelectedItem().toString();

                // Сохраняем выбранный статус и группу в SharedPreferences
                SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("selected_teacher", selected_teacher);
                editor.apply();

                // Переходим на следующий экран
                Intent intent = new Intent(TeacherChoiceActivity.this, TeacherActivity.class);
                startActivity(intent);
            }
        });
    }
}