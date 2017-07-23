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
 * Created by Live With M3 N0W on 10-11-2015.
 */
public class Connect_GetAttendanceList extends AsyncTask<String, Void, String> {
    String result,YEAR,BRAANCH,GROUP,SUBGROUP;

    public void getData(String YEAR,String BRANCH,String GROUP,String SUBGROUP){
        this.YEAR=YEAR;
        this.BRAANCH=BRANCH;
        this.SUBGROUP=SUBGROUP;
        this.GROUP=GROUP;
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

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            con.connect();


            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

            String data="year"+"="+ URLEncoder.encode(YEAR, "UTF-8")+"&"+"branch"+"="+ URLEncoder.encode(BRAANCH, "UTF-8")+"&"+"group"+"="+ URLEncoder.encode(GROUP, "UTF-8")+"&"+"subgroup"+"="+ URLEncoder.encode(SUBGROUP, "UTF-8");

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

