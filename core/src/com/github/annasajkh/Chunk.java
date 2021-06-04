package com.github.annasajkh;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.github.annasajkh.blocks.Bedrock;
import com.github.annasajkh.blocks.Block;
import com.github.annasajkh.blocks.Dirt;
import com.github.annasajkh.blocks.Grass;
import com.github.annasajkh.blocks.GrassBlock;
import com.github.annasajkh.blocks.Stone;
import com.github.annasajkh.entities.Entity;
import com.github.annasajkh.structures.Tree;

public class Chunk
{
	public List<Block> blocks;
	public float minPosX;
	public float negPosX;
	public float maxPosX;
	static boolean isCreated = false;
	
	public Chunk(float x)
	{
		blocks = new ArrayList<>();
		minPosX = x - Minecraft2D.size * 0.5f;
		maxPosX = minPosX + Minecraft2D.chunkLength * Minecraft2D.size;
		negPosX = x - Minecraft2D.chunkLength * Minecraft2D.size;
	}
	
	public static boolean chunkAlreadyContainsEntity(Chunk chunk)
	{
		for (Entity entity : Minecraft2D.entities)
		{
			if(chunk.isInChunk(entity.x))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void updateSlice(List<Block> slice)
	{
		List<Block> blockToAdd = new ArrayList<>();
		
		for (int i = slice.size() - 1;i >= 0; i--)
		{
			Block block = slice.get(i);
			if(Minecraft2D.modifiedBlocks.containsKey(BlockState.getString(block.x, block.y)))
			{
				if(Minecraft2D.modifiedBlocks.get(BlockState.getString(block.x, block.y)).removed)
				{
					slice.remove(block);
				}
			}
		}
		
		for(String key : Minecraft2D.modifiedBlocks.keySet())
		{
			
			if(!Minecraft2D.modifiedBlocks.get(key).removed)
			{
				String[] posString = key.split(",");
				
				float x = Float.valueOf(posString[0]);
				float y = Float.valueOf(posString[1]);
				
				if(x == slice.get(0).x)
				{
					blockToAdd.add(Block.createBlock(x,y,Minecraft2D.modifiedBlocks.get(key).itemType));
				}
			}
		}
		slice.addAll(blockToAdd);
	}
	
	private static List<Block> getSlice(float posX, int n)
	{
		List<Block> slice = new ArrayList<>();
		
		int noiseY = (int) MathUtils.map(-1, 1, 0, Minecraft2D.worldHeightInBlock, Minecraft2D.worldNoise.GetNoise(0,n));
		
		slice.add(new GrassBlock(posX,noiseY *  Minecraft2D.size -  Minecraft2D.size * 0.5f));
		
		Block grass = slice.get(0);
		float y = grass.y -  Minecraft2D.size;
		int count = 0;
		
		while(y > 0)
		{
			count++;
			
			if(count > 3)
			{
				slice.add(new Stone(grass.x,y));
			}
			else
			{
				slice.add(new Dirt(grass.x,y));
			}
			y -=  Minecraft2D.size;
		}

		slice.add(new Bedrock(grass.x,y));

		for(int  i = 3; i < 6; i++)
		{
			for(int j = slice.size() - 1;j >= 0; j--)
			{
				Block block = slice.get(j);
				
				if(Minecraft2D.caveNoise.GetNoise((block.x/Minecraft2D.size) * i,(block.y/Minecraft2D.size) * i) > -0.4f && !(block.itemType == ItemType.BEDROCK))
				{
					if(block.y > Minecraft2D.seaLevel * Minecraft2D.size - Minecraft2D.seaLevel * Minecraft2D.size * 0.5f &&
							block.y < Minecraft2D.seaLevel * Minecraft2D.size)
					{						
						slice.add(Block.createBlock(block.x,block.y,MathUtils.randomBoolean(0.7f) ? ItemType.WATER : ItemType.SAND));
					}
					slice.remove(block);
				}
			}
		}
		
		
		for(int i = slice.size() - 1; i >= 0; i--)
		{
			Block block = slice.get(i);
			
			if(block.itemType == ItemType.GRASS_BLOCK && !block.isModifiedBlock)
			{
				Minecraft2D.enviromentRandom.setSeed((long) block.y);
				if(Minecraft2D.enviromentRandom.nextFloat() <= Grass.spawnChance)
				{
					slice.add(new Grass(block.x,block.y + Minecraft2D.size));
				}
			}
		}
		
		List<Block> grasses = new ArrayList<>();
		List<Block> grassBlocks = new ArrayList<>();
		
		for(Block block : slice)
		{
			if(block.itemType == ItemType.GRASS)
			{
				grasses.add(block);
			}
			else if(block.itemType == ItemType.GRASS_BLOCK)
			{
				grassBlocks.add(block);
			}
		}
		
		List<Block> tree = null;
		
		if(!isCreated)
		{
			for(Block grassBlock : grassBlocks)
			{
				Minecraft2D.enviromentRandom.setSeed((long) grassBlock.y);
				
				if(Minecraft2D.enviromentRandom.nextFloat() <= Tree.spawnChance)
				{
					tree = Tree.createTree(grassBlock.x,grassBlock.y + Minecraft2D.size);
					isCreated = true;
					break;
				}
			}
		}
		
		
		if(tree != null)
		{
			for (int i = grasses.size() - 1; i >= 0; i--)
			{
				Block grass1 = grasses.get(i);
				
				if(tree.get(0).posEqual(grass1))
				{
					slice.remove(grass1);
				}
			}
			slice.addAll(tree);
		}
		
		updateSlice(slice);
		
		
		return slice;
	}
	
	public static Chunk createChunk(float posX,int index)
	{
		isCreated = false;
		Chunk chunk = new Chunk(posX);
		
		for (int i = 0; i < 10; i++)
		{
			chunk.blocks.addAll(getSlice(posX + i * Minecraft2D.size,index + i));
		}
		
		return chunk;
	}
	
	public static float getChunkPosMin(float x)
	{
		return (int) (x / Minecraft2D.chunkLength) + Minecraft2D.size + Minecraft2D.size * 0.5f;
	}
	
	public boolean isOffscreen()
	{
		return minPosX > Minecraft2D.camera.position.x + (Minecraft2D.chunkLength * Minecraft2D.size) * 6 || 
				maxPosX < Minecraft2D.camera.position.x - (Minecraft2D.chunkLength * Minecraft2D.size) * 6 ;
	}
	
	public static void drawChunkBorder(Chunk chunk)
	{
		Minecraft2D.borderShapeRenderer.rectLine(chunk.minPosX,0,chunk.minPosX,Minecraft2D.camera.position.y + Gdx.graphics.getHeight() * 0.5f,Minecraft2D.camera.zoom * 2);
		Minecraft2D.borderShapeRenderer.rectLine(chunk.maxPosX,0,chunk.maxPosX,Minecraft2D.camera.position.y + Gdx.graphics.getHeight() * 0.5f,Minecraft2D.camera.zoom * 2);
	}
	
	public boolean isInChunk(float x)
	{
		return x >= minPosX && x <= maxPosX;
	}
	

}
