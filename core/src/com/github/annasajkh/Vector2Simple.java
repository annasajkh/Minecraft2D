package com.github.annasajkh;

public class Vector2Simple
{
	public float x;
	public float y;
	
	public Vector2Simple(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean equal(float x, float y)
	{
		return this.x == x && this.y == y;
	}
}
