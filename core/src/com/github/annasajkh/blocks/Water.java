package com.github.annasajkh.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.github.annasajkh.BlockState;
import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;

public class Water extends ParticleBlock
{
	
	public Water(float x, float y)
	{
		super(x, y, Minecraft2D.waterTexture, ItemType.WATER);
		isBackground = true;
	}

	@Override
	public void update()
	{
		if(delay >= 0.05)
		{
			delay = 0;
			boolean blockUnder = false;
			boolean blockRightUnder = false;
			boolean blockLeftUnder = false;
			boolean blockRight = false;
			boolean blockLeft = false;
				
			if(Minecraft2D.blockHash.containsKey(BlockState.getString(x,y - Minecraft2D.size)))
			{
				Block block = Minecraft2D.blockHash.get(BlockState.getString(x,y - Minecraft2D.size));
				
				if(!block.isBackground || block.isParticle)
				{
					blockUnder = true;
				}
			}
			if(Minecraft2D.blockHash.containsKey(BlockState.getString(x + Minecraft2D.size, y - Minecraft2D.size)))
			{
				Block block = Minecraft2D.blockHash.get(BlockState.getString(x + Minecraft2D.size, y - Minecraft2D.size));
				
				if(!block.isBackground || block.isParticle)
				{
					blockRightUnder = true;
				}
			}
			if(Minecraft2D.blockHash.containsKey(BlockState.getString(x - Minecraft2D.size, y - Minecraft2D.size)))
			{
				Block block = Minecraft2D.blockHash.get(BlockState.getString(x - Minecraft2D.size, y - Minecraft2D.size));
				
				if(!block.isBackground || block.isParticle)
				{
					blockLeftUnder = true;
				}
			}
			if(Minecraft2D.blockHash.containsKey(BlockState.getString(x + Minecraft2D.size, y)))
			{
				Block block = Minecraft2D.blockHash.get(BlockState.getString(x + Minecraft2D.size, y));
				
				if(!block.isBackground || block.isParticle)
				{
					blockRight = true;
				}
			}
			if(Minecraft2D.blockHash.containsKey(BlockState.getString(x - Minecraft2D.size, y)))
			{
				
				Block block = Minecraft2D.blockHash.get(BlockState.getString(x - Minecraft2D.size, y));
				
				if(!block.isBackground || block.isParticle)
				{
					blockLeft = true;
				}
			}
			
			if(!blockUnder)
			{
				Minecraft2D.blockHash.remove(BlockState.getString(x,y));
				y -= Minecraft2D.size;
				Minecraft2D.blockHash.put(BlockState.getString(x,y),this);
			}
			else if(!blockRightUnder && !blockRight && !blockLeftUnder && !blockLeft)
			{
				if(MathUtils.randomBoolean())
				{
					Minecraft2D.blockHash.remove(BlockState.getString(x,y));
					y -= Minecraft2D.size;
					x -=  Minecraft2D.size;
					Minecraft2D.blockHash.put(BlockState.getString(x,y),this);
				}
				else
				{
					Minecraft2D.blockHash.remove(BlockState.getString(x,y));
					
					y -= Minecraft2D.size;
					x += Minecraft2D.size;
					
					Minecraft2D.blockHash.put(BlockState.getString(x,y),this);
				}
				
			}
			else if(!blockLeftUnder && !blockLeft)
			{
				Minecraft2D.blockHash.remove(BlockState.getString(x,y));
				
				y -= Minecraft2D.size;
				x -= Minecraft2D.size;
				Minecraft2D.blockHash.put(BlockState.getString(x,y),this);
			}
			else if(!blockRightUnder && !blockRight)
			{
				Minecraft2D.blockHash.remove(BlockState.getString(x,y));
				y -= Minecraft2D.size;
				x += Minecraft2D.size;
				Minecraft2D.blockHash.put(BlockState.getString(x,y),this);
			}
			else if(!blockRight && !blockLeft)
			{
				if(MathUtils.randomBoolean())
				{
					Minecraft2D.blockHash.remove(BlockState.getString(x,y));
					x -=  Minecraft2D.size;
					Minecraft2D.blockHash.put(BlockState.getString(x,y),this);
				}
				else
				{
					Minecraft2D.blockHash.remove(BlockState.getString(x,y));
					x += Minecraft2D.size;
					Minecraft2D.blockHash.put(BlockState.getString(x,y),this);
				}
			}
			else if(!blockRight)
			{
				Minecraft2D.blockHash.remove(BlockState.getString(x,y));
				x += Minecraft2D.size;
				Minecraft2D.blockHash.put(BlockState.getString(x,y),this);
			}
			else if(!blockLeft)
			{

				Minecraft2D.blockHash.remove(BlockState.getString(x,y));
				x -= Minecraft2D.size;
				Minecraft2D.blockHash.put(BlockState.getString(x,y),this);
			}
			
		}
			
		delay += Gdx.graphics.getDeltaTime();
		
	}

}
