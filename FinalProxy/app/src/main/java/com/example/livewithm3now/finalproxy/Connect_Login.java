package com.example.livewithm3now.finalproxy;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Live With M3 N0W on 09-11-2015.
 */
public class Connect_Login extends AsyncTask<String, Void, String> {
    String id;
    public void getData(String a){
        id=a;
    }
    public ConnectPhpResponse response=null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {


            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    public String downloadUrl(String myurl) throws IOException {

        InputStream is = null;
        int len = 500;
        try {
            Log.d("frst", "yes");
            URL url = new URL(myurl);
            Log.d("secnd","yes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d("thrd","yes");
            conn.setReadTimeout(10000000 /* milliseconds */);
            conn.setConnectTimeout(150000000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            Log.d("thrd1", "yes");
            conn.connect();
            Log.d("thrd2", "yes");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            String data="id"+"="+ URLEncoder.encode(id, "UTF-8");
            wr.write(data);
            wr.flush();
            Log.d("Got it2", "yes");
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String contentAsString = readIt(is, len);
            return contentAsString;
        }
        finally {
            if (is != null) {
                is.close();
            }

        }

    }
    @Override
    protected void onPostExecute(String s) {
        Log.d("here", s);
        response.phpresponseHandler(s);
        Log.d("there", "yes");

    }
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
