package com.example.rest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private int START = 0;
	private int END = 0;
	private int PERIODIC = 0;
	private TextView OUTPUT;
	private EditText SET_START;
	private EditText SET_END;
	private EditText SET_PERIODIC;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button installTimer = (Button) findViewById(R.id.set);
		OUTPUT = (TextView) findViewById(R.id.textView1);
		
		SET_START = (EditText) findViewById(R.id.start);
		SET_END = (EditText) findViewById(R.id.end);
		SET_PERIODIC = (EditText) findViewById(R.id.periodic);
		
		
		installTimer.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.set:
				String stringStart = SET_START.getText().toString();
				String stringEnd = SET_END.getText().toString();
				String stringPeriodic = SET_PERIODIC.getText().toString();
				if (stringStart.length() == 0 || stringEnd.length() == 0 || stringPeriodic.length() == 0) {
					Toast.makeText(this, "Всё поля должны быть заполнены", Toast.LENGTH_SHORT).show();
				} else {
				END = Integer.valueOf(stringStart);
				PERIODIC = Integer.valueOf(stringEnd);
				String out = String.valueOf(stringPeriodic);
				startService(new Intent(this, MyService.class));
				OUTPUT.setText(out);
				}
				break;
		}
	}

}
