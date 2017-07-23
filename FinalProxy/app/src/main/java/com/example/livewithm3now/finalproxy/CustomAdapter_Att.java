package com.example.livewithm3now.finalproxy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Live With M3 N0W on 12-11-2015.
 */
public class CustomAdapter_Att extends ArrayAdapter<Att> {

    private List<Att> namelist;
    private Context context;
    public TextView textone,texttwo;
    public CheckBox boxone;
    public CustomAdapter_Att(Context context, List<Att> namelist) {
        super(context,R.layout.customrow1,namelist);
        this.namelist = namelist;
        this.context=context;

    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent ){

        View v=convertView;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.customrow1, null);
            textone = (TextView) v.findViewById(R.id.course);
            texttwo = (TextView) v.findViewById(R.id.att);
        }
        else{
            v.getTag();
        }
        Att p = namelist.get(position);
        textone.setText(p.getCourse());
        texttwo.setText(p.getAtt());
        return v;
    }
}

class Att{
    String course,att;


    public Att(String name,String id) {

        course = name;
        att=id;
    }

    public String getCourse() {

        return course;
    }

    public void setCourse(String name) {
        this.course = name;
    }
    public String getAtt() {

        return att;
    }

    public void setAtt(String id) {
        this.att = id;
    }
}

