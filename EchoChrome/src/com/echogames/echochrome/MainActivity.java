package com.echogames.echochrome;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private GameContext mGameContext;
    
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
                CommandBar cb_view = (CommandBar) findViewById(R.id.view2);
                tgt_v.releaseSelection();
                cb_view.updateView();
            }
        });
        
        mGameContext = new GameContext();
        if ( savedInstanceState == null )
        { 
            mGameContext.genRamdomUnits( 20, ( float) 0.05, ( float) 0.1);
            Log.d( TAG, "Units are generated");
        } else
        {
            mGameContext.restoreState( savedInstanceState); 
            Log.d( TAG, "State is restored");
        }
        
        final EchoChromeView ec_view = (EchoChromeView) findViewById(R.id.view1);
        final CommandBar cb_view = (CommandBar) findViewById(R.id.view2);
        
        ec_view.setGameContext( mGameContext);
        ec_view.setCommandBar( cb_view);
        
        
        cb_view.setGameContext( mGameContext);
        cb_view.setEchoChromeView( ec_view);
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        mGameContext.saveState( savedInstanceState);
        Log.d( TAG, "State is saved");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
