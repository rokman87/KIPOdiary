package com.reportage.kipodiary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.widget.TextView;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
public class MainActivity extends AppCompatActivity {

    private TextView today_text, today_head_date;
    private int num;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<TextView> headers = new ArrayList<>();
        headers.add((TextView) findViewById(R.id.monday_head_date_view));
        headers.add((TextView) findViewById(R.id.tuesday_head_date_view));
        headers.add((TextView) findViewById(R.id.wednesday_head_date_view));
        headers.add((TextView) findViewById(R.id.thursday_head_date_view));
        headers.add((TextView) findViewById(R.id.friday_head_date_view));
        headers.add((TextView) findViewById(R.id.saturday_head_date_view));
        headers.add((TextView) findViewById(R.id.sunday_head_date_view));

        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add((TextView) findViewById(R.id.monday_text_view));
        textViews.add((TextView) findViewById(R.id.tuesday_text_view));
        textViews.add((TextView) findViewById(R.id.wednesday_text_view));
        textViews.add((TextView) findViewById(R.id.thursday_text_view));
        textViews.add((TextView) findViewById(R.id.friday_text_view));
        textViews.add((TextView) findViewById(R.id.saturday_text_view));
        textViews.add((TextView) findViewById(R.id.sunday_text_view));



        //Номер дня недели
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;

            for(int i = dayOfWeek; i > 1; i--){
                num = num + 1;
            }


        for (int t = 0; t < headers.size(); t++) {

            //Получение даты
            Date today_date = new Date();
            today_date = new Date(today_date.getTime() + ((86400000)*t) - ((86400000)*num));


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

            //Текст под датами
            int day = t + 1;
            today_text = textViews.get(t);
            new GetData(this, today_text, day).execute();

        }

    }
}
