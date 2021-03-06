package com.example.zyr.ecgdemo;

import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is used to realize http get/post request, Json data parsing and anything related to network process
 * Used in other Activities and Services:
 * mNetwork = new NetworkUtility();
 */

public class NetworkUtility {
    public void sendGetHttpRequest(final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    //establish and set up a connection
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //get response stream from the server
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    message.what = 0;
                    message.obj = response.toString();

                    if (listener != null) {
                        //callback onFinish(String response) method,
                        //overwrite when the methods been called
                        listener.onFinish(response.toString(),message);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    // The method is used for posting abnormal data immediately
    public void sendPostHttpRequest(final String address,final String time, final float value,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                JSONObject json= new JSONObject();
                try {
                    json.put("time",time);
                    json.put("value", value);
                    //json.put("value2"," ");

                    //establish and set up a connection
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    //The data to be posted
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(json.toString());
                    out.flush();
                    out.close();

                    //get response stream from the server
                    InputStream in = connection.getInputStream();
                    //InputStream err = connection.getErrorStream();
                    int status = connection.getResponseCode();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("The status is :" + status);
                    System.out.println("The response is :" + response);
                    Message message = new Message();
                    message.what = 0;
                    message.obj = response.toString();

                    if (listener != null) {
                        //callback onFinish(String response); method
                        listener.onFinish(response.toString(),message);
                    }
                    //Thread.sleep(2000);
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public void sendLoginRequest(final String username, final String password, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                JSONObject user= new JSONObject();
                try {
                    user.put("name", username);
                    user.put("pass",password);
                    //establish and set up a connection

                    URL url = new URL("http://104.236.126.112/api/login");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //connection.setRequestProperty("accept", "application/json");
                    connection.setRequestProperty("Content-Type", "application/json");

                    //The data to be posted
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(user.toString());
                    out.flush();
                    out.close();

                    //get response stream from the server
                    //int status = connection.getResponseCode();
                    InputStream in = connection.getInputStream();
                    //InputStream in = connection.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    //Message is used for showing the response on UI thread
                    Message message = new Message();
                    message.what = 0;
                    message.obj = response.toString();

                    if (listener != null) {
                        listener.onFinish(response.toString(),message);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public void sendRegisterRequest(final String username, final String password,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                JSONObject user= new JSONObject();
                try {
                    user.put("name", username);
                    user.put("pass",password);
                    //establish and set up a connection

                    URL url = new URL("http://104.236.126.112/api/register");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    //The data to be posted
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(user.toString());
                    out.flush();
                    out.close();

                    //get response stream from the server
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    //Message is used for showing the response on UI thread
                    Message message = new Message();
                    message.what = 0;
                    message.obj = response.toString();

                    if (listener != null) {
                        listener.onFinish(response.toString(),message);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //This method is used for Uploading all data in a table(the data will be wrapped into a JsonObject first)
    public void postWholeTable(final String address, final JSONObject jsonObject, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    //establish and set up a connection
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(60*1000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    //The data to be posted
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(jsonObject.toString());
                    out.flush();
                    out.close();

                    //get response stream from the server
                    InputStream in = connection.getInputStream();
                    //InputStream err = connection.getErrorStream();
                    int status = connection.getResponseCode();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("The status is :" + status);
                    System.out.println("The response is :" + response);
                    Message message = new Message();
                    message.what = 0;
                    message.obj = response.toString();

                    if (listener != null) {
                        listener.onFinish(response.toString(),message);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //This method is used for send a query request to the server
    public void queryFromServer(final String address, final String startTime, final String endTime, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                JSONObject json= new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json.put("time1", startTime);
                    json.put("time2", endTime);
                    data.put("data", json);
                    System.out.println("data is : " + data);
                    //establish and set up a connection
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(10000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    //The data to be posted
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(data.toString());
                    out.flush();
                    out.close();

                    //get response stream from the server
                    InputStream in = connection.getInputStream();
                    //InputStream err = connection.getErrorStream();
                    int status = connection.getResponseCode();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("The status is :" + status);
                    System.out.println("The response is :" + response);
                    Message message = new Message();
                    message.what = 0;
                    message.obj = response.toString();

                    if (listener != null) {
                        //callback onFinish(String response); method
                        listener.onFinish(response.toString(),message);
                    }
                    //Thread.sleep(2000);
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}
