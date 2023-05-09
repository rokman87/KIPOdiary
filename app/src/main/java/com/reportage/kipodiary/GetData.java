package com.reportage.kipodiary;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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




    @Override
    protected String doInBackground (String... arg0) {

        try {
            // Первый день недели
            Calendar calFirstDay = Calendar.getInstance();
            calFirstDay.set(Calendar.DAY_OF_WEEK,calFirstDay.getFirstDayOfWeek());
            Date firstDayOfWeek = calFirstDay.getTime();
            String firstDayOfWeekStr = new SimpleDateFormat("dd.MM.yyyy").format(firstDayOfWeek);

            //Последний день недели
            Calendar calLastDay = Calendar.getInstance();
            calLastDay.set(Calendar.DAY_OF_WEEK, calLastDay.getFirstDayOfWeek() + 6);
            Date lastDayOfWeek = calLastDay.getTime();
            String lastDayOfWeekStr = new SimpleDateFormat("dd.MM.yyyy").format(lastDayOfWeek);
            //Объединяем
            String weekData = firstDayOfWeekStr + " - " + lastDayOfWeekStr;




            //PHP Скрипт
            String link="http://mrnikkly.beget.tech/getschedule.php";

            //Задаем значение id и Дату недели
            String data= URLEncoder.encode("id", "UTF-8")+ "=" + id
                    + "&" + URLEncoder.encode("weekData", "UTF-8")+ "=" + weekData;

            // Создаем соединение
            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);

            //Жду что выдаст PHP
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            //Буфер ридера
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            //Результат
            StringBuilder sb = new StringBuilder();
            String line = null;

            //Читаем ответ сервера
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }


            return sb.toString();
        }

        //При ошибке в try
        catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    //Вывод
    @Override
    protected void onPostExecute(String result) {

        this.text.setText(Html.fromHtml(result));
    }

}
