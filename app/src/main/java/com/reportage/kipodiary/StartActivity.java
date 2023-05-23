package com.reportage.kipodiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //Добавляю селектор
        Spinner spinner = findViewById(R.id.mySpinner);
        new GetGroups(this, spinner).execute();

        Button nextButton = findViewById(R.id.submit_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем выбранный статус и группу
                RadioGroup radioGroup = findViewById(R.id.status_group);
                int selectedStatus = radioGroup.getCheckedRadioButtonId();
                String selectedGroup = spinner.getSelectedItem().toString();

                // Сохраняем выбранный статус и группу в SharedPreferences
                SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("selected_status", selectedStatus);
                editor.putString("selected_group", selectedGroup);
                editor.apply();

                // Переходим на следующий экран
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}