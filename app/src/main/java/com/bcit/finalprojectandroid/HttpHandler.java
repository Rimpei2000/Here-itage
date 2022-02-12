package com.bcit.finalprojectandroid;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HttpHandler<T> implements Callable<T> {

    private static final String TAG = HttpHandler.class.getSimpleName();

    private final String reqUrl;


    public HttpHandler(String reqUrl)
    {
        this.reqUrl = reqUrl;
    }

    @Override
    public T call() {
        T response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET"); //GET is default

            //read the response
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());


            response = (T) convertStreamToString(inputStream);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
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

    public Future<String> CreateHttpRequestForJson(String url){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        HttpHandler<String> httpHandler = new HttpHandler<String>(url);
        return executor.submit(httpHandler);
    }

    Data.Root ParseJson(String jsonStr){
        System.out.println(jsonStr);
        if (jsonStr != null){
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, Data.Root.class);
        }else{
            Log.e("MainActivity", "json is null");
            return null;
        }
    }
}

