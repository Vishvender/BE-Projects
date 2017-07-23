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
 * Created by Live With M3 N0W on 10-11-2015.
 */
public class CustomAdapter_List extends ArrayAdapter<Names> {

    private List<Names> namelist;
    private Context context;
    public TextView textone;
    public CheckBox boxone;
    public CustomAdapter_List(Context context, List<Names> namelist) {
        super(context,R.layout.customrow,namelist);
        this.namelist = namelist;
        this.context=context;

    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent ){

        View v=convertView;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.customrow, null);
            textone = (TextView) v.findViewById(R.id.textone);
            boxone = (CheckBox) v.findViewById(R.id.boxone);
            boxone.setOnCheckedChangeListener((Attendance_List) context);
        }
        else{
            v.getTag();
        }
        Names p = namelist.get(position);
        textone.setText(p.getName());
        boxone.setChecked(p.isSelect());
        boxone.setTag(p);
        return v;
    }
}

class Names{
    String name;
    boolean select=false;
    String id;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Names(String name,String id) {

        this.name = name;
        this.id=id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
