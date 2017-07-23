package com.example.livewithm3now.finalproxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Live With M3 N0W on 08-11-2015.
 */
public class S_Home_Page extends Activity implements ConnectPhpResponse {
    String USERNAME,ID,result;
    TextView Heading;
    int a=1;
    ListView Attendance_List;
    ArrayList<Att> Name_List;
    Button logout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_home_page_layout);
        USERNAME= getIntent().getExtras().getString("username");
        ID= getIntent().getExtras().getString("id");
        Heading=(TextView)findViewById(R.id.heading);
        Heading.setText("Hello!   " + USERNAME);
        logout=(Button)findViewById(R.id.button2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });
        Attendance_List = (ListView) findViewById(R.id.att_list);
        Name_List = new ArrayList<Att>();
        if(a>0){
            Connect_GetAttendanceCourse con=new Connect_GetAttendanceCourse();
            con.getData(ID);
            Log.d("connect", "noyes");
            con.response = this;
            con.execute("http://172.31.250.225:9999/proxy/Get_Attendance_Course.php");
            a--;
        }
    }
    public void phpresponseHandler(String data) {
        Log.d("yes7","yes");
        result = data;
        Log.d("data",data);
        setvalues();
    }

    public void setvalues() {
        onCreate(new Bundle());
        try {
            JSONArray mJsonArray = new JSONArray(result);
            JSONObject mJsonObject = new JSONObject();

            for (int i = 0; i < mJsonArray.length(); i++) {
                mJsonObject = mJsonArray.getJSONObject(i);
                Name_List.add(new Att(mJsonObject.optString("COURSE_CODE"),mJsonObject.optString("ATTENDANCE")));
            }
            Log.d("herenow","yes");
            ListAdapter AdapterRoll = new CustomAdapter_Att(this, Name_List);
            Attendance_List.setAdapter(AdapterRoll);

        }
        catch (JSONException e) {
        }
    }
    public void LogOut(){
        Intent intent1=new Intent(this,MainActivity.class);
        startActivity(intent1);
    }

}
