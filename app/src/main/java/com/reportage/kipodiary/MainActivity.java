package com.reportage.kipodiary;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        if (isAppInBackground()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private boolean isAppInBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }

    private TextView today_text, today_head_date, tTime, tDiscipline, tAuditorium, tTeacher;
    private int num;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = findViewById(R.id.linear_layout); // получаем корневой элемент макета

        //лист хедеров
        ArrayList<TextView> headers = new ArrayList<>();
        headers.add((TextView) findViewById(R.id.monday_head_date_view));


        //Номер дня недели
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

        for (int i = dayOfWeek; i > 1; i--) {
            num = num + 1;
        }


        for (int t = 0; t < headers.size(); t++) {
            //Получение дня недели
            Date today_date = new Date();
            today_date = new Date(today_date.getTime() + ((86400000) * t) - ((86400000) * num));
            //Преобразование в простой формат даты
            String date_str = new SimpleDateFormat("dd-MM-yyyy").format(today_date);
            //День недели
            String weekday = new SimpleDateFormat("EEEE").format(today_date);
            weekday = weekday.substring(0, 1).toUpperCase() + weekday.substring(1);
            //Строки текста заголовков
            String head_today = weekday + " " + date_str;
            //Обозначение переменных хэдеров
            today_head_date = headers.get(t);
            headers.get(t).setText(head_today);
            int day = t + 1;
            new ClassSchedule(day).execute();


            // Добавление первого макета "para_day.xml"
            View paraDayView = getLayoutInflater().inflate(R.layout.para_day, null);
            TextView auditoriumTextView = paraDayView.findViewById(R.id.text_auditorium);
            auditoriumTextView.setText("Аудитория");
            TextView timeTextView = paraDayView.findViewById(R.id.text_time);
            timeTextView.setText("Время");
            TextView disciplineTextView = paraDayView.findViewById(R.id.text_discipline);
            disciplineTextView.setText("Дисциплина");
            TextView teacherTextView = paraDayView.findViewById(R.id.text_teacher);
            teacherTextView.setText("Преподаватель");
            linearLayout.addView(paraDayView);

            //лист подписей
            ArrayList<TextView> textViews = new ArrayList<>();
            textViews.add((TextView) paraDayView.findViewById(R.id.text_time));
            textViews.add((TextView) paraDayView.findViewById(R.id.text_discipline));
            textViews.add((TextView) paraDayView.findViewById(R.id.text_auditorium));
            textViews.add((TextView) paraDayView.findViewById(R.id.text_teacher));

            //Текст под датами

            tTime = textViews.get(0);
            tDiscipline = textViews.get(1);
            tAuditorium = textViews.get(3);
            tTeacher = textViews.get(2);
            String[] myArray = new String[]{"Ничего", "Ничего", "Ничего", "Ничего", "Ничего", "Ничего"};
            //Запрос к гетдата
            new GetData(this, tTime, tDiscipline, tAuditorium, tTeacher, day, myArray).execute();
        }

    }
}
