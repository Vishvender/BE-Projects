package com.example.vishvender.scantosolve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main_activity extends AppCompatActivity {
    Button camera;
    Button sample1,sample2,sample3,sample4,sample5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        camera=(Button)findViewById(R.id.cameraButton);
//        sample1=(Button)findViewById(R.id.sample1);
        sample2=(Button)findViewById(R.id.sample2);
        sample3=(Button)findViewById(R.id.sample3);
        sample4=(Button)findViewById(R.id.sample4);
        sample5=(Button)findViewById(R.id.sample5);
    }
    public void call_sample2(View view){
        Intent open_viewPhoto=new Intent(this,ViewPhoto.class);
        open_viewPhoto.putExtra("sampleno","2");
        open_viewPhoto.putExtra("mode","2");
        startActivity(open_viewPhoto);
    }
    public void call_sample3(View view){
        Intent open_viewPhoto=new Intent(this,ViewPhoto.class);
        open_viewPhoto.putExtra("sampleno","3");
        open_viewPhoto.putExtra("mode","2");
        startActivity(open_viewPhoto);
    }
    public void call_sample4(View view){
        Intent open_viewPhoto=new Intent(this,ViewPhoto.class);
        open_viewPhoto.putExtra("sampleno","4");
        open_viewPhoto.putExtra("mode","2");
        startActivity(open_viewPhoto);
    }
    public void call_sample5(View view){
        Intent open_viewPhoto=new Intent(this,ViewPhoto.class);
        open_viewPhoto.putExtra("sampleno","5");
        open_viewPhoto.putExtra("mode","2");
        startActivity(open_viewPhoto);
    }
    public void openCameraMethod(View view){
        Intent opencamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(opencamera,5);

//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.new1);
//        Intent open_viewPhoto=new Intent(this,ViewPhoto.class);
//        Bitmap bmnew=Bitmap.createScaledBitmap(bm,600,300,true);
//        open_viewPhoto.putExtra("sampleno",0);
//        open_viewPhoto.putExtra("image",bm);
//            open_viewPhoto.putExtra("image",bitmap);
//        startActivity(open_viewPhoto);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        if (requestCode == 5 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            Intent open_viewPhoto=new Intent(this,ViewPhoto.class);
            open_viewPhoto.putExtra("image",bitmap);
            open_viewPhoto.putExtra("mode","1");
            startActivity(open_viewPhoto);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
