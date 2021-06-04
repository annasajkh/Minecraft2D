package com.github.annasajkh;

public class BlockState
{
	
	public ItemType itemType;
	public boolean removed = false;
	public float x;
	public float y;
	public boolean isParticle = false;
	
	public BlockState(float x, float y,ItemType itemType,boolean removed)
	{
		this.itemType = itemType;
		this.removed = removed;
		this.x = x;
		this.y = y;
	}
	
	public BlockState(String data)
	{
		String[] dataArray = data.split(",");
		this.x = Float.parseFloat(dataArray[1]);
		this.y = Float.parseFloat(dataArray[2]);
		this.removed = Boolean.parseBoolean(dataArray[3]);
		this.itemType = ItemType.valueOf(dataArray[4]);
		if(5 == dataArray.length -1)
		{
			isParticle = true;
		}
	}
	
	public static String getString(float x, float y)
	{
		return String.valueOf(x) + "," + String.valueOf(y);
	}
	
	public String getString()
	{
		return String.valueOf(x) + "," + String.valueOf(y);
	}
	
	@Override
	public String toString() 
	{
		return "B" + ',' + String.valueOf(x) + ',' + String.valueOf(y) + ',' + removed + ',' + itemType;
	}

}
