package com.reportage.kipodiary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClassSchedule {

    public ClassSchedule(int day) {
    }

    public int getClassesCount(int day) {

        int classesCount = 0;

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

            // создаем ссылку на ваш PHP-скрипт на сервере
            URL url = new URL("http://mrnikkly.beget.tech/get_schedule_count.php?day=" + day + "&weekData=" + weekData);

            // создаем соединение с PHP-скриптом
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();

            // получаем ответ из PHP-скрипта
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // преобразуем ответ из PHP-скрипта в число пар
            classesCount = Integer.parseInt(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return classesCount;

    }

    public void execute() {
    }
}