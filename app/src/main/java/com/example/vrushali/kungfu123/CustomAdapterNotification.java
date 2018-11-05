package com.example.vrushali.kungfu123;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterNotification extends ArrayAdapter {

    private ArrayList dataSet;
    Context mContext;
    SharedPreferences notify_id;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }

    public CustomAdapterNotification(ArrayList data, Context context) {
        super(context, R.layout.listview_notify, data);
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public UserModelNotify getItem(int position) {


        return (UserModelNotify) dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_notify, parent, false);
//            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tv_name);
//            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.tv_checkbox);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

       UserModelNotify item = getItem(position);


        viewHolder.txtName.setText(item.name);
        viewHolder.checkBox.setChecked(item.checked);

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewHolder.checkBox.isChecked()) {


                    notify_id = getContext().getSharedPreferences("getid", Context.MODE_PRIVATE);
                    final String temp = notify_id.getString("studid", "");

//                  final String temp1 = viewHolder.checkBox.setId(Integer.parseInt(temp));


//                    Log.e("idsss",);

                }

            }
        });

        return result;
    }
}
