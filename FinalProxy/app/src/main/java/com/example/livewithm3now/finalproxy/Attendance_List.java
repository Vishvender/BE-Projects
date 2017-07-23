package com.example.livewithm3now.finalproxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Live With M3 N0W on 10-11-2015.
 */
public class Attendance_List extends Activity implements ConnectPhpResponse, android.widget.CompoundButton.OnCheckedChangeListener {
    String YEAR,BRANCH,COURSE,TYPE,GROUP,SUBGROUP,DATE,result;
    int b;
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
    ListView Attendance_List;
    ArrayList<Names> Name_List;
    Button Mark,NewAtt,LogOut;
    int a=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_list_layout);
        YEAR = getIntent().getExtras().getString("year");
        BRANCH = getIntent().getExtras().getString("branch");
        COURSE = getIntent().getExtras().getString("course");
        TYPE = getIntent().getExtras().getString("type");
        GROUP = getIntent().getExtras().getString("group");
        SUBGROUP = getIntent().getExtras().getString("subgroup");
        DATE= dateFormat.format(date).toString();
        TextView date_today=(TextView)findViewById(R.id.date_today);
        date_today.setText(DATE);
        TextView info=(TextView)findViewById(R.id.info);
        if(TYPE=="L") {
            info.setText("Branch:- " + BRANCH + " Group:- "+GROUP);
        }
        else {
            info.setText("Branch:- " + BRANCH + " SubGroup:- "+SUBGROUP);
        }
        Mark=(Button)findViewById(R.id.markattendance);
        Mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markAttendance();
            }
        });
        LogOut=(Button)findViewById(R.id.logout);
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        Attendance_List = (ListView) findViewById(R.id.att_list);
        Name_List = new ArrayList<Names>();

        if(a>0) {
            Connect_GetAttendanceList connection = new Connect_GetAttendanceList();
            connection.getData(YEAR, BRANCH, GROUP, SUBGROUP);
            connection.response = this;
            connection.execute("http://172.31.250.225:9999/proxy/Get_Attendance_List.php");
            a--;
        }


    }
    public void phpresponseHandler(String data) {
        result = data;
        setvalues();
    }

    public void setvalues() {
        onCreate(new Bundle());
        try {
            JSONArray mJsonArray = new JSONArray(result);
            JSONObject mJsonObject = new JSONObject();
            b = mJsonArray.length();

            for (int i = 0; i < mJsonArray.length(); i++) {
                mJsonObject = mJsonArray.getJSONObject(i);
                Name_List.add(new Names(mJsonObject.optString("NAME"),mJsonObject.optString("id_STUDENT")));
            }
            ListAdapter rollAdapter = new CustomAdapter_List(this, Name_List);
            Attendance_List.setAdapter(rollAdapter);

        } catch (JSONException e) {
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int pos = Attendance_List.getPositionForView(buttonView);
        Log.d("hi", "there");
        if (pos != ListView.INVALID_POSITION) {
            Names p = Name_List.get(pos);
            Boolean a = p.isSelect();
            if (a == false) {
                p.setSelect(true);
                Log.d("checked", "yes");
            }
            else {
                p.setSelect(false);
                Log.d("unchecked", "yes");
            }
            for (int i = 0; i < b; i++) {
                Names na = Name_List.get(i);
                String m=Boolean.toString(na.isSelect());
            }

        }
    }

    public void markAttendance(){
        String[] NAME=new String[b];
        String[] attendance=new String[b];
        String[] ID=new String[b];
        for(int i=0;i<b;i++){
            Names na = Name_List.get(i);
            String att=Boolean.toString(na.isSelect());
            if(att=="true") attendance[i]="1";
            if(att=="false") attendance[i]="0";
            NAME[i]=na.getName();
            ID[i]=na.getId();
        }
        Connect_MarkAttendance connection2 = new Connect_MarkAttendance();
        connection2.getData(DATE, COURSE, TYPE, ID, NAME, attendance, BRANCH, YEAR, b);
        connection2.execute("http://172.31.250.225:9999/proxy/insert_attendance.php");
        Toast.makeText(
                this,
                "Attendance Saved.", Toast.LENGTH_SHORT).show();

        Connect_MarkAttendance connection3 = new Connect_MarkAttendance();
        connection3.getData(DATE, COURSE, TYPE, ID, NAME, attendance, BRANCH, YEAR, b);
        connection3.execute("http://172.31.250.225:9999/proxy/CALCULATE.php");
        Toast.makeText(
                this,
                "Attendance Calculated.", Toast.LENGTH_SHORT).show();
    }
    public void logOut(){
        Intent intent1=new Intent(this,MainActivity.class);
        startActivity(intent1);
    }
}






