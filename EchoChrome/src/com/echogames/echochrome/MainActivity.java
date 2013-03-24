package com.echogames.echochrome;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			setContentView(R.layout.main_layout);
		} else
		{
			setContentView(R.layout.main_layout_portrait);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
