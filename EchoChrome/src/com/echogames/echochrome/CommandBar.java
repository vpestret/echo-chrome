package com.echogames.echochrome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class CommandBar extends View {
    //private static final String TAG = "CommandBar";
    
    public CommandBar(Context context) {
        this(context, null, 0);
    }
    
    public CommandBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public CommandBar(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    }
}
