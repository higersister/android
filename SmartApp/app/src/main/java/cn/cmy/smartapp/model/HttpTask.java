package cn.cmy.smartapp.model;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpTask extends Thread {

    private String address;

    private String requestMethod;

    private HttpCallBack httpCallBack;

    public HttpTask(String address, String requestMethod, HttpCallBack httpCallBack) {
        this.address = address;
        this.requestMethod = requestMethod;
        this.httpCallBack = httpCallBack;
    }

    @Override
    public void run() {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            connection.setRequestMethod(requestMethod);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String str = null;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
            if (httpCallBack != null) {
                httpCallBack.onSuccess(builder.toString());
            }


        } catch (MalformedURLException e) {
            if (httpCallBack != null) {
                httpCallBack.onFailure(e);
            }
        } catch (IOException e) {
            if (httpCallBack != null) {
                httpCallBack.onFailure(e);
            }
        } finally {
            if (connection != null) {

                connection.disconnect();
            }
        }
    }
}
