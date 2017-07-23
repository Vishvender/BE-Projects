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
public class Connect_MarkAttendance extends AsyncTask<String, Void, String> {
    String result;
    String[] id=new String[150];
    String[] name=new String[150];
    String[] att=new String[150];
    String date,year,course,department,type;
    int b;

    public void getData(String date,String course,String type,String[] id,String[] name,String[] att,String department,String year,int b){
        this.name=name;
        this.id=id;
        this.att=att;
        this.date=date;
        this.year=year;
        this.course=course;
        this.department=department;
        this.type=type;
        this.b=b;

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

            conn.connect();
            Log.d("four",Integer.toString(b));
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            String data="size"+"="+ URLEncoder.encode(Integer.toString(b), "UTF-8")+"&"+"date"+"="+ URLEncoder.encode(date, "UTF-8")+"&"+"course"+"="+ URLEncoder.encode(course, "UTF-8")+"&"+"type"+"="+ URLEncoder.encode(type, "UTF-8")+"&"+"branch"+"="+ URLEncoder.encode(department, "UTF-8")+"&"+"year"+"="+ URLEncoder.encode(year, "UTF-8")+"&";
            for(int i=0;i<b;i++){
                String j=Integer.toString(i);
                String z="id"+URLEncoder.encode(j, "UTF-8")+"="+ URLEncoder.encode(id[i], "UTF-8")+"&"+"name"+URLEncoder.encode(j, "UTF-8")+"="+ URLEncoder.encode(name[i], "UTF-8")+"&"+"att"+URLEncoder.encode(j, "UTF-8")+"="+ URLEncoder.encode(att[i], "UTF-8")+"&";
                data=data.concat(z);
            }
            Log.d("dataaa",data);
            wr.write(data);


            wr.flush();
            //  conn.setRequestMethod("GET");
            //conn.setDoInput(true);
            Log.d("Got it2", "yes");
            int response = conn.getResponseCode();
            is = conn.getInputStream();
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
        result=s;
    }
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
    public String senddata(){
        return result;
    }

}