package com.reportage.kipodiary;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClassSchedule extends AsyncTask<String, Integer, String> {

    private Context context;

    private  int lessonCount;
    private int id = 1;

    public ClassSchedule(Context context, int day, int count) {

        this.context= context;
        this.id= day;
        this.lessonCount = count;
    }

    @Override
    protected String doInBackground(String... arg0) {

        String response;
        try {
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


            URL url = new URL("http://mrnikkly.beget.tech/get_schedule_count.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Добавляем параметры, если нужно
            String postData = "id=" + id + "&weekData=" + weekData + "&lCount=" + lessonCount;
            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.flush();
            os.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            response = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                response += line;
            }
            in.close();
            conn.disconnect();
        }


        //При ошибке в try
        catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
        return response.toString();
    }

    //Вывод
    @Override
    protected void onPostExecute(String result) {

    }
}

