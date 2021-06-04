package com.github.annasajkh.entities;

import com.github.annasajkh.Minecraft2D;

public class Cloud extends Entity
{
	

	public static float spawnChance = 0.8f;

	public Cloud(float x, float y)
	{
		super(x, y, Minecraft2D.size,Minecraft2D.size, Minecraft2D.cloudTexture,30);
		affectedByGravity = false;
		type = Type.CLOUD;
		
	}

	@Override
	public void update()
	{
		if(!Minecraft2D.isOffChunk(x))
		{
			updateEntity();
		}
	}

}
