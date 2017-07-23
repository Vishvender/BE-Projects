package com.example.vishvender.scantosolve;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ViewPhoto extends AppCompatActivity {
    ImageView iv,pic1,pic2,pic3;
    Bitmap img;
    TextView expression,answer;
    double dwh[][]=new double[785][100];
    double dwo[][]=new double[101][10];
    double owh[][]=new double[785][50];
    double owo[][]=new double[51][4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        expression=(TextView)findViewById(R.id.expression);
        answer=(TextView)findViewById(R.id.answer);
        String i=getIntent().getExtras().getString("sampleno");
        String mode=getIntent().getExtras().getString("mode");
        if(mode.equals("1")){
            img=getIntent().getParcelableExtra("image");
        }
        else {
            img = BitmapFactory.decodeResource(getResources(), R.drawable.new1);
            if (i.equals("1"))
                img = BitmapFactory.decodeResource(getResources(), R.drawable.new1);
            if (i.equals("2"))
                img = BitmapFactory.decodeResource(getResources(), R.drawable.new2);
            if (i.equals("3"))
                img = BitmapFactory.decodeResource(getResources(), R.drawable.new3);
            if (i.equals("4"))
                img = BitmapFactory.decodeResource(getResources(), R.drawable.new4);
            if (i.equals("5"))
                img = BitmapFactory.decodeResource(getResources(), R.drawable.new5);
        }
//        img = BitmapFactory.decodeResource(getResources(), R.drawable.new1);expression=(EditText)findViewById(R.id.expression);answer=(EditText)findViewById(R.id.answer);
        iv=(ImageView)findViewById(R.id.setImage);
        pic1=(ImageView)findViewById(R.id.pic1);
        pic2=(ImageView)findViewById(R.id.pic2);
        pic3=(ImageView)findViewById(R.id.pic3);
//        img=getIntent().getParcelableExtra("image");
        iv.setImageBitmap(img);
    }
    ProgressDialog pd;
    public void EvaluateExpression(View view){
        readCSVfiles();
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
    public void readCSVfiles(){
        try {
            dwo = readcsvfile("weight_Output.csv",101,10);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        try {
            dwh = readcsvfile("weight_Hidden.csv",785,100);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        try {
            owo = readcsvfile("operator_Weight_Output.csv",51,4);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        try {
            owh = readcsvfile("operator_Weight_Hidden.csv",785,50);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    Bitmap [] series;
    String[] s;
    class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(ViewPhoto.this, "Waiting...",
                    "Evaluating the expression.", true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            EvaluatePicture ep=new EvaluatePicture();
            series=ep.segmentPictures(img,ep.readPixelsOfOriginalImage(img));
            s=ep.recognizepics(series,dwh,dwo,owh,owo);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            showData();
            super.onPostExecute(aVoid);
        }
    }

    private void showData(){
        pic1.setImageBitmap(series[0]);
        pic2.setImageBitmap(series[1]);
        pic3.setImageBitmap(series[2]);

        pd.dismiss();
        Log.d("whatshere",s[0]);
        expression.setText("Expression: "+s[0]);
//        expression.setText("555");

        answer.setText("Answer: "+s[1]);
    }

    public double[][] readcsvfile(String filename,int rows,int cols)  throws IOException {
        AssetManager am=this.getAssets();
        InputStream is=new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        try {
            is = am.open(filename);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        EvaluatePicture ep=new EvaluatePicture();
        double weight[][]=ep.readCSVfile(r,rows,cols);
        return weight;
    }
}