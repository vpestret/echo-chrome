package com.echogames.echochrome;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

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
		
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	EchoChromeView tgt_v = ( EchoChromeView) findViewById(R.id.view1);
            	tgt_v.releaseSelection();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
