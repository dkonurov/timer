package com.example.rest.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.rest.AlarmModel;
import com.example.rest.R;
import com.example.rest.Utils;

import java.util.ArrayList;

public class AlarmListAdapter extends BaseAdapter {

    private ArrayList<AlarmModel> alarmModels = new ArrayList<AlarmModel>();
    private Context mContext;

    public AlarmListAdapter(ArrayList<AlarmModel> alarmModels) {
        this.alarmModels = alarmModels;
        mContext = Utils.context;
    }

    @Override
    public int getCount() {
        return alarmModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),
            mContext.getString(R.string.font));
        LinearLayout layout = new LinearLayout(mContext);
        View.inflate(mContext, R.layout.element_alarm_list, layout);
        int[] time = new int [2];
        time = alarmModels.get(i).getTime();

        EditText hour = (EditText) layout.findViewById(R.id.hour_list);
        EditText minute = (EditText) layout.findViewById(R.id.minute_list);
        hour.setTypeface(typeface);
        minute.setTypeface(typeface);
        hour.setText(String.valueOf(time[0]));
        minute.setText(String.valueOf(time[1]));
        layout.setPadding(alarmModels.get(i).getLeftPadding(), 0, 0, 0);
        return layout;
    }
}
