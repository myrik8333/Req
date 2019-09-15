package com.example.req;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends Activity {

    ProgressDialog dialog;
    String amount="15";
    String payer="5774e24d8d4e1d6636d769b47fc5a1161cc6133c";
    public HttpResponse postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://89.208.84.235:31080/api/v1/invoice/");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("amount", "10"));
            nameValuePairs.add(new BasicNameValuePair("currencyCode", "810"));
            nameValuePairs.add(new BasicNameValuePair("description", "zaprosic"));
            nameValuePairs.add(new BasicNameValuePair("number", "123123dsfsdf2"));
            nameValuePairs.add(new BasicNameValuePair("payer", "5774e24d8d4e1d6636d769b47fc5a1161cc6133c"));
            nameValuePairs.add(new BasicNameValuePair("recipient", "34d63a8494c444dabe650689f194e5e118d2d936"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
return response;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //postData();
        setContentView(R.layout.activity_main);
      //  amount=getIntent().getStringExtra();

        //new RequestTask().execute("http://89.208.84.235:31080/api/v1/invoice/"); // скрипт, на который посылаем запрос
        new RequestTask().execute("http://ping21ping21.com.xsph.ru");
    }

    public String getJSON(String amount, String curCode,String description,String number,String payer,String recipient) // получаем json объект в виде строки
    {

        JSONObject bot = new JSONObject();
        try {
            URL url = new URL("http://89.208.84.235:31080/api/v1/invoice/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);

            // HTTP request header
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            con.connect();

            // HTTP request
            JSONObject data = new JSONObject();
            data.put("amount", amount);
            data.put("currencyCode", curCode);
            data.put("description", description);
            data.put("number", number);
            data.put("payer", payer);
            data.put("recipient", "34d63a8494c444dabe650689f194e5e118d2d936");

            OutputStream os = con.getOutputStream();
            os.write(data.toString().getBytes("UTF-8"));
            os.close();

            // Read the response into a string
            InputStream is = con.getInputStream();
            String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
            is.close();

            // Parse the JSON string and return the notification key
            JSONObject response = new JSONObject(responseString);
            return response.getString("notification_key");
            //bot.put("amount", amount);

           // bot.put("currencyCode", curCode);
            //bot.put("description", description);
           // bot.put("number", number);
           // bot.put("payer", payer);
            //bot.put("recipient", recipient);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bot.toString();
    }

    public class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
               // postData();
                //System.out.println("Post data done");
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                HttpPost postMethod = new HttpPost(params[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                // ключ - "json", параметр - json в виде строки
                nameValuePairs.add(new BasicNameValuePair("json", getJSON(amount, "810","shitty",String.valueOf(Math.random()*4000),payer,"34d63a8494c444dabe650689f194e5e118d2d936")));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                postMethod.setEntity(entity);
                //return hc.execute(postMethod, res);
            } catch (Exception e) {
                System.out.println("Exp=" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {

            dialog.dismiss();

            // res - ответ сервера

            super.onPostExecute(res);
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Ожидание");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }
    }
}