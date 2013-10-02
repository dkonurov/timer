package com.example.rest;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class ScrollLinearLayout extends LinearLayout {
	
	private Scroller mScroller;
	
	private Button hour[] = new Button[2];
	
	private Button minute[] = new Button[2];

	public ScrollLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mScroller = new Scroller(context);
		setGravity(Gravity.CENTER);
		setOrientation(VERTICAL);
		
	}

	public void fling () {
		
	}
}
