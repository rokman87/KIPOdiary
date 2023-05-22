package com.reportage.kipodiary;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClassSchedule extends AsyncTask<Integer, Void, String> {
    private Context context;
    private int id = 1;
    private int lCount =1;

    public ClassSchedule(Context context, int day, int lessonCount) {
        this.context= context;
        this.id= day;
        this.lCount =lessonCount;
    }


    protected String doInBackground(Integer... arg0) {
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

            // выполнение запроса на сервер
            URL url = new URL("http://mrnikkly.beget.tech/get_schedule_count.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Добавляем параметры, если нужно
            String postData = "id=" + id + "&weekData=" + weekData + "&lCount=" +lCount;
            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.flush();
            os.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (Exception e) {
            Log.e("RequestTask", "Exception", e);
            return null;
        }
    }

    protected void onPostExecute(String result) {
        // передача результата в MainActivity
        if (result != null) {
            ((MainActivity) context).onRequestCompleted(Integer.valueOf(result));
        }
    }
}
