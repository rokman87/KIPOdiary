package com.reportage.kipodiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class ClassSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_selection);


                // Получаем выбранный статус
        RadioGroup radioGroup = findViewById(R.id.status_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.student_radio:
                        Intent intent1 = new Intent(ClassSelectionActivity.this, StartActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.teacher_radio:
                        Intent intent2 = new Intent(ClassSelectionActivity.this, TeacherChoiceActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
