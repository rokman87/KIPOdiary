package com.reportage.kipodiary;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import org.json.JSONObject;

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


public class GetData extends AsyncTask<Void, Void, String[]> {

    private int lCount = 0;
    private Context context;
    private TextView textTime;
    private TextView textDiscipline;
    private TextView textAuditorium;
    private TextView textTeacher;
    private int id = 1; //1 = Сегодня, 2= Завтра
    private String[] myArray;

    public GetData(Context context, TextView tTime, TextView tDiscipline, TextView tAuditorium, TextView tTeacher, int day, String[] maArray, int count) {

        this.context= context;
        this.textTime= tTime;
        this.textDiscipline= tDiscipline;
        this.textAuditorium= tAuditorium;
        this.textTeacher= tTeacher;
        this.id= day;
        this.myArray = maArray;
        this.lCount=count;
    }

    @Override
    protected String[] doInBackground(Void... params) {

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



        URL url = new URL("http://mrnikkly.beget.tech/api.php");
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

        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String response = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            response += line;
        }
        in.close();

        // Преобразуем полученную строку в JSON-объект
        JSONObject jsonObject = new JSONObject(response);

        // Извлекаем данные из объекта JSON
        String lesson_time = jsonObject.getString("Время");
        String building = jsonObject.getString("Корпус");
        String auditorium = jsonObject.getString("Аудитория");
        String discipline = jsonObject.getString("Дисциплина");
        String teacher = jsonObject.getString("Преподаватель");
        myArray = new String[]{lesson_time,building + " " + auditorium,discipline,teacher};
        conn.disconnect();
    }


        //При ошибке в try
        catch (Exception e) {
            return new String[]{new String("Exception: " + e.getMessage())};
        }
return myArray;
    }

    //Вывод
    @Override
    protected void onPostExecute(String[] result) {
        this.textTime.setText(Html.fromHtml(myArray[0]));
        this.textDiscipline.setText(Html.fromHtml(myArray[2]));
        this.textAuditorium.setText(Html.fromHtml(myArray[3]));
        this.textTeacher.setText(Html.fromHtml(myArray[1]));
    }

}