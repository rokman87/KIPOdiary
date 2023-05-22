package com.reportage.kipodiary;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private int lessonCount =2;


    public class PostRequest extends AsyncTask<Void, Void, Integer> {

        private String url;
        private String data;
        private int lessonCount;
        private int day;
        private Context context;


        public PostRequest(Context context, String url, String data, int day) {
            this.url = url;
            this.data = data;
            this.day = day;
            this.context = context;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL url = new URL("http://mrnikkly.beget.tech/get_schedule_count.php");
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

        @Override
        protected void onPostExecute(Integer result) {

            this.lessonCount = result;
            System.out.println("lessonCount: " + lessonCount);


            LinearLayout linearLayout = findViewById(R.id.linear_layout); // получаем корневой элемент макета
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

                //Текст под датами

                tTime = textViews.get(0);
                tDiscipline = textViews.get(1);
                tAuditorium = textViews.get(3);
                tTeacher = textViews.get(2);
                String[] myArray = new String[]{"Ничего", "Ничего", "Ничего", "Ничего", "Ничего", "Ничего"};
                //Запрос к гетдата
                new GetData(context, tTime, tDiscipline, tAuditorium, tTeacher, day, myArray, count).execute();
            }
        }
    }



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



            String url = "http://mrnikkly.beget.tech/get_schedule_count.php";
            String data = "id=" + day + "&weekData=" + weekData;

            PostRequest postRequest = new PostRequest(this,url, data, day);
            postRequest.execute();
        }

    }


}
