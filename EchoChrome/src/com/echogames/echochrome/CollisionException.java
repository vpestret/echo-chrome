package com.echogames.echochrome;

public class CollisionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RectEntity self;
	public RectEntity it;
	CollisionException( String s, RectEntity self, RectEntity it)
	{
		super(s);
		this.self = self;
		this.it = it;
	}

}
