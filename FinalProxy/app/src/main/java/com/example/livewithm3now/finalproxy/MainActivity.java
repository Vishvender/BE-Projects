package com.example.livewithm3now.finalproxy;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ConnectPhpResponse{

    EditText userid,pass;
    String ID,USERNAME,PASSWORD,password,data,stringURL=null;
    int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userid=(EditText)findViewById(R.id.userid);
        pass=(EditText)findViewById(R.id.password);
        Button loggedin=(Button)findViewById(R.id.Loggedin);
        loggedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((android.widget.RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioTeacher:
                if (checked) {
                    a = 1;
                    stringURL="http://172.31.250.225:9999/proxy/teacher_login_password.php";
                    break;
                }
            case R.id.radioStudent:
                if (checked) {
                    a = 2;
                    stringURL = "http://172.31.250.225:9999/proxy/student_login_password.php";
                    break;
                }
        }
    }
    public void login(){

        ID=userid.getText().toString();
        password=pass.getText().toString();

        if(a==0){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Choose a radiobutton");
            builder1.setCancelable(true);
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else{
            Connect_Login c1=new Connect_Login();
            c1.getData(ID);
            c1.response=this;
            c1.execute(stringURL);
        }
    }
    public void phpresponseHandler(String data) {
        Log.d("ansawer", data);
        this.data=data;
        checkLogin();
    }
    public void checkLogin(){
        try {
            JSONArray mJsonArray = new JSONArray(data);
            JSONObject mJsonObject = new JSONObject();
            mJsonObject = mJsonArray.getJSONObject(0);
            PASSWORD=mJsonObject.optString("password");
            USERNAME=mJsonObject.optString("name");
        }
        catch (JSONException e) {
        }
        if(a==1) {
            if (PASSWORD.equals(password)) {
                Intent intent = new Intent(this, T_Home_Page.class);
                intent.putExtra("username",USERNAME);
                intent.putExtra("id",ID);
                startActivity(intent);
            }
        }
        if(a==2){
            if(PASSWORD.equals(password)){
                Intent intent = new Intent(this, S_Home_Page.class);
                intent.putExtra("username",USERNAME);
                intent.putExtra("id",ID);
                Log.d("student","yes");
                startActivity(intent);
            }
        }

    }

}
