package com.example.bruker.edubackpromo.data.tasks;

import android.os.AsyncTask;

import com.example.anna.eduback2.data.models.Fag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class JSONTask extends AsyncTask<String,String,String> {

    private final OnJSONReadyListener mListener;
    Fag nyttFag;


    public JSONTask(OnJSONReadyListener listener) {
        mListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            //read from API

            InputStream stream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));


            StringBuffer buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            //Get objekt course from the whole API
            JSONObject parentObject = new JSONObject(finalJson);
            JSONObject course = parentObject.getJSONObject("course");
            JSONArray assessment = course.getJSONArray("assessment");
            JSONArray educationalRole = course.getJSONArray("educationalRole");


            StringBuffer finalBufferedData = new StringBuffer();
            String coursename = course.getString("name");
            String code = course.getString("code");

            String eksamen = null;
            if(assessment.getJSONObject(0).has("date")) {
                eksamen = assessment.getJSONObject(0).getString("date");
                eksamen = assessment.getJSONObject(0).getString("date");
            }else if(assessment.getJSONObject(1).has("date")){
                eksamen = assessment.getJSONObject(1).getString("date");
                eksamen = assessment.getJSONObject(1).getString("date");
            }else{
                eksamen = null;

            }

            String fornavnLærer = educationalRole.getJSONObject(0).getJSONObject("person").getString("firstName");
            String etternavnLærer =educationalRole.getJSONObject(0).getJSONObject("person").getString("lastName");
            String lærer = fornavnLærer + " " + etternavnLærer;

            finalBufferedData.append(code + "-" +  coursename + "-" + eksamen + "-" + lærer);

            return finalBufferedData.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private String code;
    private String coursename;
    private String examdate;
    private String teacher;

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result == null){
            nyttFag = null;
        }else {
            String linje[] = result.split("-");
            if (linje.length == 6) {
                code = linje[0].toString();
                coursename = linje[1].toString();
                examdate = linje[4].toString() + "-" + linje[3].toString() + "-" + linje[2].toString();
                teacher = linje[5].toString();
            } else {
                code = linje[0].toString();
                coursename = linje[1].toString();
                examdate = linje[2].toString();
                teacher = linje[3].toString();
            }
            nyttFag = new Fag(code, coursename, examdate, teacher);

        }
        mListener.onJsonReady(nyttFag);
    }

    public interface OnJSONReadyListener{
        void onJsonReady(Fag nyttFag);
    }
}
