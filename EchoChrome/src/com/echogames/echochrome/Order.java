package com.echogames.echochrome;

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
    
    Order( int type)
    {
    	switch ( type )
    	{
    	case ORDER_TURN: case ORDER_SIDE: case ORDER_RUN:
    		this.type = type;
    		break;
    	default:
    		this.type = ORDER_ERR;	
    	}
    }
    
    public int getType()
    {
    	return type;
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
