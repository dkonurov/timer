package com.example.rest.elements;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rest.AlarmManagerBroadcastReceiver;
import com.example.rest.CustomTimePickerDialog;
import com.example.rest.R;


public class TimerLayout extends RelativeLayout implements View.OnClickListener {


    private static int START_TIME = 0;
    private static int END_TIME = 2;
    private static int PERIODIC_TIME = 4;

    private Animation anim;

    private boolean checkPeriodic = false;
    private int check = 0;
    private int setHour = 0;
    private int setMinute = 0;
    private int periodicHour = 0;
    private int periodicMinute = 0;

    private Button installTimer;
    private EditText timer[] = new EditText[4];
    private LinearLayout mContainerView;
    private AlarmManagerBroadcastReceiver alarm;

    private Context context;
    private Activity activity;

    public TimerLayout(Activity activity) {
        super(activity.getApplicationContext());

        this.activity = activity;
        this.context = activity.getApplicationContext();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.timer_view, null);

        LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.main_timer_layout);

        RelativeLayout.LayoutParams mainLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        installTimer = (Button) rootView.findViewById(R.id.set);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.checkPeriodic);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.number, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        timer[0] = (EditText) rootView.findViewById(R.id.startHours);
        timer[1] = (EditText) rootView.findViewById(R.id.startMinute);
        timer[2] = (EditText) rootView.findViewById(R.id.endHours);
        timer[3] = (EditText) rootView.findViewById(R.id.endMinute);

        Drawable shape = getResources().getDrawable(R.drawable.text_for_timer);
        for (int i=0; i<=3; i++) {
            timer[i].setFocusable(false);
        }

        mContainerView = (LinearLayout) findViewById(R.id.scrollContent);

        installTimer.setOnClickListener(this);;

        for(int i=0; i<=3; i++) {
            timer[i].setOnClickListener(this);
        }

//        loadCheck();

        alarm = new AlarmManagerBroadcastReceiver();

        addView(mainLayout, mainLayoutParams);
    }

    @Override
    public void onClick(View view) {
        CustomTimePickerDialog timePicker;
        switch(view.getId()) {
            case R.id.set:
                if (check == 0) {
                    String[] stringTimer = new String[6];
                    int[] intTimer = new int[6];
                    boolean chekerTime = true;
                    int n;
                    if (checkPeriodic) {
                        n = 5;
                    } else {
                        n = 3;
                    }
                    for (int i=0;i<=n;i++) {
                        stringTimer[i] = timer[i].getText().toString();
                        if (stringTimer[i].length() == 0) {
                            Toast.makeText(getContext(), "все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
                            chekerTime = false;
                            break;
                        } else {
                            intTimer[i] = Integer.valueOf(stringTimer[i]);
                            if (i == 5 && intTimer[4] == 0 && intTimer[5] == 0) {
                                Toast.makeText(getContext(), "должны быть время между отдыхом", Toast.LENGTH_LONG).show();
//                                showDialog(PERIODIC_TIME);
                                chekerTime = false;
                                break;
                            }
                        }
                    }
                    if (!chekerTime) {
                        return;
                    }

                    installTimer.setText("Остановить таймер");
                    installTimer.setBackgroundResource(R.drawable.button_red_selector);
                    Context context = getContext();
//                  alarm.setChecker(setSound, setVibration);
//                    alarm.SetAlarm(context, intTimer[0], intTimer[1], intTimer[2], intTimer[3]);
                    check =1;
//                    saveCheck();
                } else {
                    Context context = getContext();
                    alarm.CancelAlarm(context);
                    installTimer.setText("Установить таймер");
                    installTimer.setBackgroundResource(R.drawable.button_blue_selector);
                    check = 0;
//                    saveCheck();
                }
                break;
            case R.id.startHours:
                Log.d("fail", "fail");
                activity.showDialog(START_TIME);
                break;
            case R.id.startMinute:
                activity.showDialog(START_TIME);
                break;
            case R.id.endHours:
                activity.showDialog(END_TIME);
                break;
            case R.id.endMinute:
                activity.showDialog(END_TIME);
                break;
            case 4:
                activity.showDialog(PERIODIC_TIME);
                break;
            case 5:
                activity.showDialog(PERIODIC_TIME);
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {
        int seterHours;
        int seterMinute;
        if (id == START_TIME) {
            seterHours = setHour;
            seterMinute = setMinute;
        } else if (id == END_TIME) {
            seterHours = setHour;
            seterMinute = setMinute;
        } else {
            seterHours = periodicHour;
            seterMinute = periodicMinute;
        }
        CustomTimePickerDialog TimePicker = new CustomTimePickerDialog(context, timer[id], timer [id+1],seterHours, seterMinute);
        return TimePicker;
    }
}
