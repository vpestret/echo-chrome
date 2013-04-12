package com.echogames.echochrome;

import android.os.Bundle;

public class Order {

    public final static int ORDER_TURN = 0;
    public final static int ORDER_SIDE = 1;
    public final static int ORDER_RUN  = 2;
    public final static int ORDER_ERR  = -1;
    private final static String str_ORDER_TURN = "T";
    private final static String str_ORDER_SIDE = "S";
    private final static String str_ORDER_RUN  = "R";
    private final static String str_ORDER_ERR  = "X";
    
    private int type;
    private float x;
    private float y;
   
    Order( int type)
    {
    	Create( type, 0f, 0f);
    }
    
    Order( int type, float x, float y)
    {
    	Create( type, x, y);
    }
    
    Order( String prefix, Bundle savedInstanceState)
    {
    	type = savedInstanceState.getInt( prefix + "type");
        x = savedInstanceState.getFloat( prefix + "x");
        y = savedInstanceState.getFloat( prefix + "y");
    }
    
    public void saveState( String prefix, Bundle targetInstanceState)
    {
    	targetInstanceState.putInt( prefix + "type", type);
        targetInstanceState.putFloat( prefix + "x", x);
        targetInstanceState.putFloat( prefix + "y", y);
    }
    
    private void Create( int type, float x, float y)
    {
    	switch ( type )
    	{
    	case ORDER_TURN: case ORDER_SIDE: case ORDER_RUN:
    		this.type = type;
    		break;
    	default:
    		this.type = ORDER_ERR;	
    	}
    	this.x = x;
    	this.y = y;
    }
    
    public int getType()
    {
    	return type;
    }
    
    public float getX()
    {
    	return x;
    }
    
    public float getY()
    {
    	return y;
    }
    
	public String getName()
	{
    	switch ( type )
    	{
    	case ORDER_TURN:
    		return str_ORDER_TURN;
    	case ORDER_SIDE:
    		return str_ORDER_SIDE;
    	case ORDER_RUN:
    		return str_ORDER_RUN;
    	default:
    		return str_ORDER_ERR;
    	}		
	}
}
