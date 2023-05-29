package com.reportage.kipodiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public class PostRequest extends AsyncTask<Void, Void, Integer> {

        private final TextView today_head_date;
        private String url;
        private String data;
        private int lessonCount;
        private int day;
        private Context context;
        private LinearLayout linearLayout;


        public PostRequest(Context context, String url, String data, int day, TextView today_head_date) {
            this.url = url;
            this.data = data;
            this.day = day;
            this.context = context;
            this.today_head_date= today_head_date;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ginkel.ru/kipo/get_schedule_count.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                // Добавляем параметры, если нужно

                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes("UTF-8"));
                os.flush();
                os.close();


                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String response = "";
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                in.close();

                // преобразуем ответ в число
                int result = Integer.parseInt(response);
                System.out.println("result: " + result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        @SuppressLint("WrongViewCast")
        @Override
        protected void onPostExecute(Integer result) {

            this.lessonCount = result;

            System.out.println("lessonCount: " + lessonCount);
            System.out.println("day: " + day);

            switch (day){
                case 1:
                    linearLayout = findViewById(R.id.linear_layout1);
                    break;
                case 2:
                    linearLayout = findViewById(R.id.linear_layout2);
                    break;
                case 3:
                    linearLayout = findViewById(R.id.linear_layout3);
                    break;
                case 4:
                    linearLayout = findViewById(R.id.linear_layout4);
                    break;
                case 5:
                    linearLayout = findViewById(R.id.linear_layout5);
                    break;
                case 6:
                    linearLayout = findViewById(R.id.linear_layout6);
                    break;
            }
            
            // получаем корневой элемент макета
            for (int count = 0; count < lessonCount; count++) {
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
                textViews.add((TextView) paraDayView.findViewById(R.id.lesson_id));

                //Текст под датами

                tTime = textViews.get(0);
                tDiscipline = textViews.get(1);
                tAuditorium = textViews.get(3);
                tTeacher = textViews.get(2);
                tLessonId = textViews.get(4);
                String[] myArray = new String[]{"Ничего", "Ничего", "Ничего", "Ничего", "Ничего", "Ничего"};

                //Получаю группу
                SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                String selectedGroup = sharedPreferences.getString("selected_group", "");
                //Запрос к гетдата
                new GetData(context, tTime, tDiscipline, tAuditorium, tTeacher, tLessonId, day, myArray, count, selectedGroup).execute();
                TextView textLessonId = (TextView) paraDayView.findViewById(R.id.lesson_id);
                String lesson_id = textLessonId.getText().toString();
                System.out.println("lesson_id: " + lesson_id);
                getNotesFromDataBase(paraDayView, lesson_id);
                paraDayView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, CheckNotes.class);
                            // Получаем текст из TextView
                            TextView textLessonId = (TextView) paraDayView.findViewById(R.id.lesson_id);
                            String lesson_id = textLessonId.getText().toString();
                            // Передаем текст в Intent
                            intent.putExtra("lesson_id", lesson_id);
                            // Запускаем новую активность
                            startActivity(intent);
                    }
                });


            }
        }
    }

    private TextView today_head_date, tTime, tDiscipline, tAuditorium, tTeacher, tLessonId;
    private int num;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //лист хедеров
        ArrayList<TextView> headers = new ArrayList<>();
        headers.add((TextView) findViewById(R.id.monday_head_date_view));
        headers.add((TextView) findViewById(R.id.tuesday_head_date_view));
        headers.add((TextView) findViewById(R.id.wednesday_head_date_view));
        headers.add((TextView) findViewById(R.id.thursday_head_date_view));
        headers.add((TextView) findViewById(R.id.friday_head_date_view));
        headers.add((TextView) findViewById(R.id.saturday_head_date_view));

        //Номер дня недели
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = dayOfWeek; i > 1; i--) {
            num = num + 1;
        }


        // Первый день недели
        Calendar calFirstDay = Calendar.getInstance();
        calFirstDay.set(Calendar.DAY_OF_WEEK, calFirstDay.getFirstDayOfWeek());
        Date firstDayOfWeek = calFirstDay.getTime();
        String firstDayOfWeekStr = new SimpleDateFormat("dd.MM.yyyy").format(firstDayOfWeek);
        //Последний день недели
        Calendar calLastDay = Calendar.getInstance();
        calLastDay.set(Calendar.DAY_OF_WEEK, calLastDay.getFirstDayOfWeek() + 6);
        Date lastDayOfWeek = calLastDay.getTime();
        String lastDayOfWeekStr = new SimpleDateFormat("dd.MM.yyyy").format(lastDayOfWeek);
        //Объединяем
        String weekData = firstDayOfWeekStr + " - " + lastDayOfWeekStr;
        //Устанавливаю дату в топ
        ((TextView) findViewById(R.id.date)).setText(weekData);

        for (int t = 0; t < headers.size(); t++) {

            //Получение дня недели
            Date today_date = new Date();
            today_date = new Date(today_date.getTime() + ((86400000) * t) - ((86400000) * num));
            //Преобразование в простой формат даты
            String date_str = new SimpleDateFormat("dd.MM").format(today_date);
            //День недели
            String weekday = new SimpleDateFormat("EEEE").format(today_date);
            weekday = weekday.substring(0, 1).toUpperCase() + weekday.substring(1);
            //Строки текста заголовков
            String head_today = weekday + " " + date_str;

            //Обозначение переменных хэдеров
            today_head_date = headers.get(t);
            headers.get(t).setText(head_today);
            int day = t + 1;


            //Получаю группу
            SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
            String selectedGroup = sharedPreferences.getString("selected_group", "");

            String url = "https://ginkel.ru/kipo/get_schedule_count.php";
            String data = "id=" + day + "&weekData=" + weekData + "&group=" + selectedGroup;

            PostRequest postRequest = new PostRequest(this,url, data, day,today_head_date);
            postRequest.execute();
        }

    }

    private void getNotesFromDataBase(View paraDayView, String lesson_id) {
        String url = "https://ginkel.ru/kipo/check_note_img_button.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response: "+response);
                        if(response == "true"){
                            ImageView myImg = paraDayView.findViewById(R.id.imageViewButton);
                            myImg.setVisibility(View.VISIBLE);
                            System.out.println("Добавил кнопку");
                        }
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
                params.put("lessonId", lesson_id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }


}
