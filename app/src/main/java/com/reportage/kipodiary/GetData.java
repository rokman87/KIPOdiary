package com.reportage.kipodiary;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GetData extends AsyncTask<Void, Void, String[]> {

    private final String selectedGroup;
    private final View paraDayView;
    private TextView tLessonId;
    private TextView textTime;
    private TextView textDiscipline;
    private TextView textAuditorium;
    private TextView textTeacher;
    private String weekData;
    private int lCount = 0, id = 1;
    private Context context;
    private String[] myArray;

    public GetData(Context context, TextView tTime, TextView tDiscipline, TextView tAuditorium, TextView tTeacher, TextView tLessonId, int day, String weekData, String[] maArray, int count, String selectedGroup, View paraDayView) {

        this.context = context;
        this.textTime = tTime;
        this.textDiscipline = tDiscipline;
        this.textAuditorium = tAuditorium;
        this.textTeacher = tTeacher;
        this.id = day;
        this.myArray = maArray;
        this.lCount = count;
        this.selectedGroup = selectedGroup;
        this.tLessonId=tLessonId;
        this.paraDayView= paraDayView;
        this.weekData=weekData;
    }

    @Override
    protected String[] doInBackground(Void... params) {

        try {

            URL url = new URL("https://ginkel.ru/kipo/api.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Добавляем параметры, если нужно
            String postData = "id=" + id + "&weekData=" + weekData + "&lCount=" + lCount + "&group=" + selectedGroup;
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
            String id = jsonObject.getString("id");
            //Отрабатываю кнопочку
            String lessonId = id;
            System.out.println("lessonId: " + lessonId);
            getNotesFromDataBase(paraDayView, lessonId);
            //Заполняю массив
            myArray = new String[]{lesson_time, building + " " + auditorium, discipline, teacher, id};

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
        //Время
        String time = myArray[0];
        String[] timeParts = time.split("-");
        String formattedTime = "<b>" + timeParts[0] + "</b><br>" + timeParts[1];
        this.textTime.setText(Html.fromHtml(formattedTime));

        //Предмет
        this.textDiscipline.setText(Html.fromHtml(myArray[2]));
        //Аудитория
        this.textAuditorium.setText(Html.fromHtml(myArray[3]));
        //Преподаватель
        this.textTeacher.setText(Html.fromHtml(myArray[1]));
        this.tLessonId.setText(Html.fromHtml(myArray[4]));

    }
    private void getNotesFromDataBase(View paraDayView, String lesson_id) {
        String url = "https://ginkel.ru/kipo/check_note_img_button.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(Objects.equals(response, "true")){
                            ImageView myImg = paraDayView.findViewById(R.id.imageViewButton);
                            myImg.setVisibility(View.VISIBLE);
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
        Volley.newRequestQueue(context).add(stringRequest);
    }
}