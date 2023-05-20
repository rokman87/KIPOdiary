package com.reportage.kipodiary;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class GetData extends AsyncTask<String, Void, String> {

    private Context context;
    private TextView text;
    private int id = 1; //1 = Сегодня, 2= Завтра

    public GetData(Context context, TextView text,int day) {

        this.context= context;
        this.text= text;
        this.id= day;

    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    @Override
    protected String doInBackground (String... arg0) {

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

            URL url = new URL("http://mrnikkly.beget.tech/getschedule.php"); // URL-адрес PHP-скрипта
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write("id=" + id + "&weekData=" + weekData); // Здесь указываются параметры в формате "имя=значение&имя=значение"
            writer.flush();
            writer.close();
            os.close();

            connection.connect();

            int responseCode = connection.getResponseCode();
            response = null;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                response = convertStreamToString(is);
                is.close();
            }

            connection.disconnect();

        }

        //При ошибке в try
        catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

        return response;
    }

    //Вывод
    @Override
    protected void onPostExecute(String result) {

        this.text.setText(Html.fromHtml(result));
    }

}