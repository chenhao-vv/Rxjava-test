package com.vivo.chmusicdemo.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d(TAG, response + " ");
                    if(listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if(listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if(connection != null) {
                        connection.disconnect();
                    }
                    try {
                        if(inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public interface HttpCallbackListener {
        void onFinish(String response);
        void onError(Exception e);
    }
}
