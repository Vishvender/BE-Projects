package com.example.livewithm3now.finalproxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Live With M3 N0W on 07-11-2015.
 */
public class T_Home_Page extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    String USERNAME,ID,YEAR,BRANCH,COURSE,TYPE,GROUP,SUBGROUP;
    TextView Heading;
    Spinner spinner_courses,spinner_years,spinner_branches,spinner_subgroups,spinner_groups;
    Button Continue;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_home_page_layout);
        USERNAME= getIntent().getExtras().getString("username");
        ID= getIntent().getExtras().getString("id");
        Heading=(TextView)findViewById(R.id.heading);
        Heading.setText("Hello!   "+USERNAME);

        spinner_courses=(Spinner)findViewById(R.id.courses);
        ArrayAdapter adapter1=ArrayAdapter.createFromResource(this,R.array.Course_Code,android.R.layout.simple_spinner_dropdown_item);
        spinner_courses.setAdapter(adapter1);
        spinner_courses.setOnItemSelectedListener(this);
        spinner_years=(Spinner)findViewById(R.id.years);
        ArrayAdapter adapter2=ArrayAdapter.createFromResource(this,R.array.Year,android.R.layout.simple_spinner_dropdown_item);
        spinner_years.setAdapter(adapter2);
        spinner_years.setOnItemSelectedListener(this);
        spinner_branches=(Spinner)findViewById(R.id.branches);
        ArrayAdapter adapter3=ArrayAdapter.createFromResource(this,R.array.Branch,android.R.layout.simple_spinner_dropdown_item);
        spinner_branches.setAdapter(adapter3);
        spinner_branches.setOnItemSelectedListener(this);
        spinner_groups=(Spinner)findViewById(R.id.group);
        ArrayAdapter adapter4=ArrayAdapter.createFromResource(this,R.array.Groups,android.R.layout.simple_spinner_dropdown_item);
        spinner_groups.setAdapter(adapter4);
        spinner_groups.setOnItemSelectedListener(this);
        spinner_subgroups=(Spinner)findViewById(R.id.subgroup);
        ArrayAdapter adapter5=ArrayAdapter.createFromResource(this,R.array.SubGroups,android.R.layout.simple_spinner_dropdown_item);
        spinner_subgroups.setAdapter(adapter5);
        spinner_subgroups.setOnItemSelectedListener(this);

        Continue=(Button)findViewById(R.id.button);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });

    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((android.widget.RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_lecture:
                if (checked)
                    TYPE="L";
                break;
            case R.id.radio_tutorial:
                if (checked)
                    TYPE="T";
                break;
            case R.id.radio_lab:
                if (checked)
                    TYPE="P";
                break;
        }
    }

    public void getList(){
        COURSE=spinner_courses.getSelectedItem().toString();
        YEAR=spinner_years.getSelectedItem().toString();
        BRANCH=spinner_branches.getSelectedItem().toString();
        GROUP=spinner_groups.getSelectedItem().toString();
        SUBGROUP=spinner_subgroups.getSelectedItem().toString();
        Intent intent = new Intent(this,Attendance_List.class);
        Log.d("clik","yes");

        intent.putExtra("year",YEAR);
        intent.putExtra("branch", BRANCH);
        intent.putExtra("course", COURSE);
        intent.putExtra("type",TYPE);
        intent.putExtra("group",GROUP);
        intent.putExtra("subgroup",SUBGROUP);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}