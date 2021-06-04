package com.github.annasajkh.entities;

import com.badlogic.gdx.Gdx;
import com.github.annasajkh.Minecraft2D;

public class Slime extends Entity
{

	float jumpCoolDown = 0;
	float cooldown = jumpCoolDown;

	public static float spawnChance = 0.05f;
	
	
	public Slime(float x, float y)
	{
		super(x, y, Minecraft2D.size, Minecraft2D.size, Minecraft2D.slimeTexture,50);
		type = Type.SLIME;
	}
	
	@Override
	public void update()
	{
		if(!Minecraft2D.isOffChunk(x))
		{
			if(cooldown <= 0)
			{
				if(x > Minecraft2D.player.x)
				{
					velocity.x = jumpHeight * 0.5f;
				}
				else
				{
					velocity.x = jumpHeight * 0.5f * -1;
				}
				jump();
				jumpCoolDown = Math.abs(Minecraft2D.player.x - x) * 0.0001f;
				cooldown = jumpCoolDown;
			}
			cooldown -= Gdx.graphics.getRawDeltaTime();
			updateEntity();
		}
	}

}
