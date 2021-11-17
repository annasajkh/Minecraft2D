package com.github.annasajkh;

import com.badlogic.gdx.Gdx;
import com.github.annasajkh.blocks.Block;

public class WorldGeneration
{
	
	private WorldGeneration() { }
	
	public static int getNBlock(float x)
	{
		return (int) (x / Minecraft2D.size);
	}
	
	public static void removeAndAddChunk()
	{
		Chunk remove = null;
		
		for(int i = Minecraft2D.chunks.size() - 1; i >= 0; i--)
		{
			Chunk chunk = Minecraft2D.chunks.get(i);
			
			if(chunk.isOffscreen())
			{
				if(chunk.minPosX < Minecraft2D.camera.position.x - Gdx.graphics.getWidth() * 0.5f)
				{
					Minecraft2D.chunks.add(Chunk.createChunk(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f,getNBlock(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f)));
					remove = Minecraft2D.chunks.get(0);
					for(Block block : remove.blocks)
					{
						if(block.isParticle && block.isModifiedBlock)
						{
							Minecraft2D.modifiedBlocks.put(BlockState.getString(block.x,block.y),new BlockState(block.x,block.y,block.itemType,false));
						}
					}
					break;
					
				}
				else if(chunk.minPosX > Minecraft2D.camera.position.x + Gdx.graphics.getWidth() * 0.5f)
				{
					Minecraft2D.chunks.add(0,Chunk.createChunk(Minecraft2D.chunks.get(0).negPosX,getNBlock(Minecraft2D.chunks.get(0).negPosX)));
					remove = Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1);
					for(Block block : remove.blocks)
					{
						if(block.isParticle && block.isModifiedBlock)
						{
							Minecraft2D.modifiedBlocks.put(BlockState.getString(block.x,block.y),new BlockState(block.x,block.y,block.itemType,false));
						}
					}
					break;
				}
			}
		}
		
		if(remove != null)
		{
			for(Block block : remove.blocks)
			{
				Minecraft2D.blockHash.remove(BlockState.getString(block.x,block.y));
			}
			
			Minecraft2D.chunks.remove(remove);
		}
		
	}
	
	public static void generateAroundPlayer()
	{

		float leftChunkPos = Chunk.getChunkPosMin(Minecraft2D.player.x);
		
		Minecraft2D.chunks.add(Chunk.createChunk(leftChunkPos,getNBlock(leftChunkPos)));

		Minecraft2D.chunks.add(0,Chunk.createChunk(Minecraft2D.chunks.get(0).negPosX,getNBlock(Minecraft2D.chunks.get(0).negPosX)));
		Minecraft2D.chunks.add(0,Chunk.createChunk(Minecraft2D.chunks.get(0).negPosX,getNBlock(Minecraft2D.chunks.get(0).negPosX)));
		
		
		//chunk to add
		Minecraft2D.chunks.add(0,Chunk.createChunk(Minecraft2D.chunks.get(0).negPosX,getNBlock(Minecraft2D.chunks.get(0).negPosX)));
		Minecraft2D.chunks.add(0,Chunk.createChunk(Minecraft2D.chunks.get(0).negPosX,getNBlock(Minecraft2D.chunks.get(0).negPosX)));

		Minecraft2D.chunks.add(Chunk.createChunk(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f,getNBlock(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f)));
		Minecraft2D.chunks.add(Chunk.createChunk(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f,getNBlock(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f)));
		
		Minecraft2D.chunks.add(0,Chunk.createChunk(Minecraft2D.chunks.get(0).negPosX,getNBlock(Minecraft2D.chunks.get(0).negPosX)));
		Minecraft2D.chunks.add(0,Chunk.createChunk(Minecraft2D.chunks.get(0).negPosX,getNBlock(Minecraft2D.chunks.get(0).negPosX)));

		Minecraft2D.chunks.add(Chunk.createChunk(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f,getNBlock(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f)));
		Minecraft2D.chunks.add(Chunk.createChunk(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f,getNBlock(Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX + Minecraft2D.size * 0.5f)));
		
		Minecraft2D.chunks.add(0,Chunk.createChunk(Minecraft2D.chunks.get(0).negPosX,getNBlock(Minecraft2D.chunks.get(0).negPosX)));
		Minecraft2D.chunks.add(0,Chunk.createChunk(Minecraft2D.chunks.get(0).negPosX,getNBlock(Minecraft2D.chunks.get(0).negPosX)));
	}
	
}
