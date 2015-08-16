package com.threet.game.view;

import android.app.Activity;
import android.os.Bundle;

/**
 * The BaseActivity class is the mother activity.
 * @author tchin
 *
 */
public abstract class BaseActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState, int layout) {
		super.onCreate(savedInstanceState);
		setContentView(layout);
		initActivity();
	}
	
	// Abstract Methods
	/**
	 * Initial prerequisite on each activity.
	 */
	protected abstract void initActivity();
}
