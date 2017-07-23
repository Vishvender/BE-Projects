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
 * Created by Live With M3 N0W on 12-11-2015.
 */
public class Connect_GetAttendanceCourse extends AsyncTask<String, Void, String> {
    String ID;

    public void getData(String id){
        ID=id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public ConnectPhpResponse response=null;
    @Override
    protected String doInBackground(String... urls) {
        try {
            Log.d("", urls[0]);
            Log.d("a4","4");
            Log.d("yes","yes");
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            Log.d("not","yes");
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        String res;
        int len = 500;
        try {
            URL url = new URL(myurl);
            Log.d("yes1","yes");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            Log.d("yes2","yes");
            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            Log.d("yes3", "yes");
            con.connect();

            Log.d("yes4", "yes");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

            String data="ID"+"="+ URLEncoder.encode(ID, "UTF-8");
            Log.d("yes5","yes");
            wr.write(data);
            wr.flush();
            int response = con.getResponseCode();
            is = con.getInputStream();
            String contentAsString = readIt(is, len);
            is.close();
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
        Log.d("yes6",s);
        response.phpresponseHandler(s);
    }
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
