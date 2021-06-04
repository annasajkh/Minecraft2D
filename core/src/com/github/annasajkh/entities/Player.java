package com.github.annasajkh.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.github.annasajkh.Item;
import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;


public class Player extends Entity
{
	public Item[] inventory = new Item[8];
	public int selectedInventoryIndex = 0;
	public Sprite sprite;
	public int cooldown = 10;
	
	public Player(float x, float y,Item[] inventory)
	{
		this(x,y);
		this.inventory = inventory;
	}

	public Player(float x, float y)
	{
		super(x, y,Minecraft2D.size,Minecraft2D.size * 2,null,100);
		type = Type.PLAYER;
		sprite = new Sprite(Minecraft2D.playerTexture);
		
		ItemType[] items = new ItemType[] {ItemType.SAND,ItemType.BEDROCK,ItemType.WATER};
		
		for (int i = 0; i < inventory.length; i++)
		{
			ItemType itemType = null;
			if(i > items.length - 1)
			{
				itemType = ItemType.EMPTY;
			}
			else
			{
				itemType = items[i];
			}
			inventory[i] = new Item(itemType,100);
		}
		
		Minecraft2D.camera.position.x = x;
		Minecraft2D.camera.position.y = y;
		
	}
	
	@Override
	public void update()
	{
		
		for (Item item : inventory)
		{
			item.update();
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.NUM_1))
		{
			selectedInventoryIndex = 0;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_2))
		{
			selectedInventoryIndex = 1;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_3))
		{
			selectedInventoryIndex = 2;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_4))
		{
			selectedInventoryIndex = 3;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_5))
		{
			selectedInventoryIndex = 4;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_6))
		{
			selectedInventoryIndex = 5;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_7))
		{
			selectedInventoryIndex = 6;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.NUM_8))
		{
			selectedInventoryIndex = 7;
		}
		
		if(!Minecraft2D.cameraMode)
		{
			if(Gdx.input.isKeyPressed(Keys.D))
			{
				velocity.x = moveSpeed;
				
				if(sprite.isFlipX())
				{
					sprite.flip(true,false);
				}
			}
			else if (Gdx.input.isKeyPressed(Keys.A))
			{
				velocity.x = -moveSpeed;
					
				if(!sprite.isFlipX())
				{
					sprite.flip(true,false);
				}
			}
			else
			{
				velocity.x = 0;
			}
			
			if(Gdx.input.isKeyPressed(Keys.SPACE))
			{
				jump();
			}
			updateEntity();
		}
		else
		{
			health = 100;
			
			for (Entity entity : Minecraft2D.entities)
			{
				entity.resolveCollisionAmount(this,2);
			}
		}

		Minecraft2D.camera.position.x = x;
		Minecraft2D.camera.position.y = y;
		
	}
	
	public void drawUI(SpriteBatch spriteBatch,ShapeRenderer shapeRenderer) 
	{	
		float posX;
		float posY;
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.rect(10,Gdx.graphics.getHeight() - 60 ,100,50);
		shapeRenderer.end();
	
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.rect(Gdx.graphics.getWidth() * 0.5f - Minecraft2D.itemSize * inventory.length * 0.5f - 
						Minecraft2D.itemSize * 0.5f - 10,0,Minecraft2D.itemSize * inventory.length + 20,  
						Minecraft2D.itemSize + 20);
		shapeRenderer.end();
		
		spriteBatch.begin();
		
		if(health > 50)
		{
			spriteBatch.draw(Minecraft2D.healthSprite[0],10,Gdx.graphics.getHeight() - 60,100,50);
		}
		else if(health > 25 && health <= 50)
		{
			spriteBatch.draw(Minecraft2D.healthSprite[1],10,Gdx.graphics.getHeight() - 60,100,50);
		}
		else if(health <= 25)
		{
			spriteBatch.draw(Minecraft2D.healthSprite[2],10,Gdx.graphics.getHeight() - 60,100,50);
		}
		
		for (int i = 0; i < inventory.length; i++)
		{
			inventory[i].update();
			if(inventory[i].texture != null)
			{
				posX =  Gdx.graphics.getWidth() * 0.5f + i *  Minecraft2D.itemSize - inventory.length * 
						Minecraft2D.itemSize * 0.5f;
				posY =  Minecraft2D.itemSize * 0.5f + 10;
				spriteBatch.draw(inventory[i].texture,posX - Minecraft2D.itemSize * 0.5f,posY - 
						Minecraft2D.itemSize * 0.5f, Minecraft2D.itemSize , Minecraft2D.itemSize );
			}
		}
		
		for (int i = 0; i < inventory.length; i++)
		{
			if(inventory[i].texture != null)
			{
				posX =  Gdx.graphics.getWidth() * 0.5f + i *  Minecraft2D.itemSize - inventory.length * 
						Minecraft2D.itemSize * 0.5f;
				posY =  Minecraft2D.itemSize * 0.5f + 10;
				if(inventory[i].count == Double.POSITIVE_INFINITY)
				{
					Minecraft2D.font.draw(spriteBatch,"Inf",posX - Minecraft2D.size * 0.25f,posY);
				}
				else
				{
					Minecraft2D.font.draw(spriteBatch,String.valueOf((int) inventory[i].count),posX - 
							Minecraft2D.size * 0.25f,posY);
				}
			}
		}
		

		Minecraft2D.font.draw(spriteBatch,"BlockHashes : " + Minecraft2D.blockHash.size(), 10,20);

		Minecraft2D.font.draw(spriteBatch,"Fps : " + Gdx.graphics.getFramesPerSecond(), 10,40);

		Minecraft2D.font.draw(spriteBatch,"Chunks : " + Minecraft2D.chunks.size(), 10,60);

		Minecraft2D.font.draw(spriteBatch,"Entities : " + Minecraft2D.entities.size(), 10,80);
		
		Minecraft2D.font.draw(spriteBatch,"BlockStates : " + Minecraft2D.modifiedBlocks.size(), 10,100);
		
		spriteBatch.end();

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		
		for (int i = 0; i < inventory.length; i++)
		{
			posX =  Gdx.graphics.getWidth() * 0.5f + i *  Minecraft2D.itemSize - 
									inventory.length * Minecraft2D.itemSize * 0.5f;
			posY =  Minecraft2D.itemSize * 0.5f + 10;
			shapeRenderer.rect(posX  - Minecraft2D.itemSize * 0.5f,posY - Minecraft2D.itemSize * 
									0.5f,Minecraft2D.itemSize,Minecraft2D.itemSize);
		}
		posX =  Gdx.graphics.getWidth() * 0.5f + selectedInventoryIndex *  Minecraft2D.itemSize - 
				inventory.length * Minecraft2D.itemSize * 0.5f;
		posY =  Minecraft2D.itemSize * 0.5f + 10;
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(posX  - Minecraft2D.itemSize * 0.5f,posY - Minecraft2D.itemSize * 0.5f,
				Minecraft2D.itemSize,Minecraft2D.itemSize);
		shapeRenderer.end();
		
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		spriteBatch.draw(sprite,x - width * 0.5f,y - height * 0.5f,width,height);
	}
	

}
