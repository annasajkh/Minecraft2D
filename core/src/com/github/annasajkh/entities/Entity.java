package com.github.annasajkh.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.github.annasajkh.BlockState;
import com.github.annasajkh.Chunk;
import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;
import com.github.annasajkh.Rect;
import com.github.annasajkh.Update;
import com.github.annasajkh.Vector2Simple;
import com.github.annasajkh.blocks.Block;

public abstract class Entity extends Rect implements Update
{
	enum Type
	{
		ENEMY,
		SLIME,
		CLOUD,
		PLAYER
	}
	
	public float moveSpeed = 5;
	public float maxGravity = 75;
	public float jumpHeight = 10;
	public float gravity = Minecraft2D.gravity;
	public boolean affectedByGravity = true;
	public Chunk entityInChunk = null;
	public boolean collideWithWater = false;
	public boolean remove = false;
	public int health;
	public int air = 500;
	
	Type type;

	public Entity(float x, float y, float width, float height, Texture texture,int health)
	{
		super(x, y, width, height, texture);
		this.health = health;
	}
	
	public void jump()
	{
		if(isOnFloor || collideWithWater)
		{
			velocity.y = jumpHeight;
			isOnFloor = false;
		}
	}
	
	
	public void updateEntity()
	{
		
		List<Block> possibleCollision = new ArrayList<>();

		
		for (int i = -3; i <= 3; i++)
		{
			for (int j = -3; j <= 3; j++)
			{

				Vector2Simple blockPos = Block.getBlockPos(x + j * Minecraft2D.size,y + i * Minecraft2D.size);
				Block b = Minecraft2D.blockHash.get(BlockState.getString(blockPos.x,blockPos.y));
				
				if(b != null)
				{
					possibleCollision.add(b);
				}
			}
		}
		
		
		if(health <= 0)
		{
			remove = true;
		}
		
		collideWithWater = false;
		
		for(Block block : possibleCollision)
		{
			if(block.itemType == ItemType.WATER && block.intersect(this))
			{
				collideWithWater = true;
			}
		}
		
		if(collideWithWater)
		{
			if(type == Type.CLOUD)
			{
				remove = true;
				return;
			}
			
			air -= Gdx.graphics.getDeltaTime();
			
			if(air <= 0)
			{
				health--;
			}
			gravity =  Minecraft2D.gravity * 0.2f;
			jumpHeight = 3f;
		}
		else
		{
			gravity = Minecraft2D.gravity;
			jumpHeight = 10;
			air = 500;
		}
		
		if(!affectedByGravity)
		{
			gravity = 0;
		}
		
		velocity.y -= gravity;
		velocity.y = MathUtils.clamp(velocity.y,-maxGravity,maxGravity);
		
		x += velocity.x;
		y += velocity.y;
		
		
		for(Entity entity : Minecraft2D.entities)
		{
			if(!entity.equals(this))
			{
				dynamicResolveCollision(entity);
			}
		}
		
			
		for(Block block : possibleCollision)
		{
			if(!block.isBackground)
			{
				resolveCollision(block);
			}
		}
		
	}
	
	@Override
	public String toString()
	{
		return "E" + "," + x + "," + y + "," + type + '\n';
	}

}
