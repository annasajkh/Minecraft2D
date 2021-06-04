package com.github.annasajkh.entities;

import com.badlogic.gdx.Gdx;
import com.github.annasajkh.Minecraft2D;

public class Enemy extends Entity
{

	public int damage = 10;
	public static float spawnChance = 0.1f;
	
	public Enemy(float x, float y)
	{
		super(x, y,Minecraft2D.size,Minecraft2D.size * 2, Minecraft2D.enemyTexture,30);
		moveSpeed = 1;
		type = Type.ENEMY;
	}
	

	@Override
	public void update()
	{
		if(intersect(Minecraft2D.player))
		{
			if(Minecraft2D.player.cooldown <= 0)
			{
				Minecraft2D.player.health -= damage;
				Minecraft2D.player.cooldown = 10;
			}
			Minecraft2D.player.cooldown -= Gdx.graphics.getRawDeltaTime();
		}
		
		if(!Minecraft2D.isOffChunk(x))
		{
			if(x > Minecraft2D.player.x)
			{
				x -= moveSpeed;
			}
			else
			{
				x += moveSpeed;
			}
			if(isOnFloor)
			{				
				jump();
			}
			
			updateEntity();
		}
		
	}


}
