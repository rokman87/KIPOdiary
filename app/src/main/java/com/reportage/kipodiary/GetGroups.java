package com.reportage.kipodiary;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetGroups extends AsyncTask<Void, Void, String[]> {

    private Context context;
    private Spinner spinner;

    public GetGroups(Context context, Spinner spinner) {
        this.context = context;
        this.spinner = spinner;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        try {
            URL url = new URL("https://ginkel.ru/kipo/get_groups.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            // Получение данных в виде массива
            JSONArray jsonArray = new JSONArray(response.toString());
            conn.disconnect();

            // Преобразование JSONArray в массив строк
            List<String> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
            return list.toArray(new String[0]);
        } catch (Exception e) {
            return new String[]{new String("Exception: " + e.getMessage())};
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, result);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void execute() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}