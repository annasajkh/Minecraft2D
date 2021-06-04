package com.github.annasajkh;

import com.badlogic.gdx.graphics.Texture;


public class Item implements Update
{

	public double count;
	ItemType itemType;
	float size;
	public Texture texture;
	
	public void updateTexture()
	{
		switch(itemType)
		{
			case GRASS:
				texture = Minecraft2D.grassTexture;
				break;
			case GRASS_BLOCK:
				texture = Minecraft2D.grassBlockTexture;
				break;
			case DIRT:
				texture = Minecraft2D.dirtTexture;
				break;
			case STONE:
				texture = Minecraft2D.stoneTexture;
				break;
			case BEDROCK:
				texture = Minecraft2D.bedrockTexture;
				break;
			case LOG:
				texture = Minecraft2D.logTexture;
				break;
			case LEAVES:
				texture = Minecraft2D.leavesTexture;
				break;
			case WATER:
				texture = Minecraft2D.waterTexture;
				break;
			case SAND:
				texture = Minecraft2D.sandTexture;
				break;
			case EMPTY:
				texture = null;
				
		}
	}
	
	public Item(ItemType itemType,double count)
	{
		this.itemType = itemType;
		
		if(count == 0)
		{
			this.itemType = ItemType.EMPTY;
		}
		
		this.count = count;
		this.size = Minecraft2D.size * 0.5f;
		updateTexture();
	}
	
	@Override
	public void update()
	{
		if(count == 0)
		{
			itemType = ItemType.EMPTY;
		}
		updateTexture();
	}
	
	@Override
	public String toString()
	{
		return itemType + "," + String.valueOf(count) + ";";
	}

}
