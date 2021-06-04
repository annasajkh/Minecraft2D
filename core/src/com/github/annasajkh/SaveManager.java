package com.github.annasajkh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.github.annasajkh.blocks.Block;
import com.github.annasajkh.entities.Enemy;
import com.github.annasajkh.entities.Entity;
import com.github.annasajkh.entities.Slime;

public class SaveManager
{
	public static void save()
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter("world.txt"));
			StringBuilder dataBuilder = new StringBuilder();

			dataBuilder.append(Minecraft2D.seed);
			dataBuilder.append('\n');
			
			for(Item item : Minecraft2D.player.inventory)
			{
				dataBuilder.append(item.toString());
			}
			
			dataBuilder.append('\n');
			
			dataBuilder.append(Minecraft2D.cameraMode);
			dataBuilder.append('\n');
			
			for (BlockState blockState : Minecraft2D.modifiedBlocks.values())
			{
				dataBuilder.append(blockState.toString() + '\n');
			}
			
			for(Chunk chunk : Minecraft2D.chunks)
			{
				for (Block block : chunk.blocks)
				{
					if(block.isParticle && block.isModifiedBlock)
					{
						dataBuilder.append(new BlockState(block.x,block.y,block.itemType,false) + ",P" + '\n');
					}
				}
			}
			
			for (Entity entity : Minecraft2D.entities)
			{
				dataBuilder.append(entity.toString());
			}
			writer.write(dataBuilder.toString());
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void load()
	{
		File file = new File("world.txt");
		
		if(file.exists() && !file.isDirectory())
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader("world.txt"));
				String string = "";
				Minecraft2D.seed = Integer.parseInt(reader.readLine());
				String[] playerInventoryData = reader.readLine().split(";");
				
				for(int i = 0; i < playerInventoryData.length; i++)
				{
					String[] data = playerInventoryData[i].split(",");
					
					Minecraft2D.player.inventory[i] = new Item(ItemType.valueOf(data[0]),Double.parseDouble(data[1]));
				}
				
				Minecraft2D.cameraMode = Boolean.parseBoolean(reader.readLine());
				
				while((string = reader.readLine()) != null)
				{
					if(string.startsWith("B"))
					{
						BlockState blockState = new BlockState(string);
						
						if(blockState.isParticle)
						{
							Minecraft2D.particleBlocks.add(Block.createBlock(blockState.x,blockState.y,blockState.itemType));
						}
						else
						{							
							Minecraft2D.modifiedBlocks.put(blockState.getString(),blockState);
						}
					}
					else if(string.startsWith("E"))
					{
						String[] dataArray = string.split(",");
						if(dataArray[3].equals("ENEMY"))
						{
							Minecraft2D.entities.add(new Enemy(Float.parseFloat(dataArray[1]),Float.parseFloat(dataArray[2])));
						}
						else if(dataArray[3].equals("SLIME"))
						{
							Minecraft2D.entities.add(new Slime(Float.parseFloat(dataArray[1]),Float.parseFloat(dataArray[2])));
						}
					}
					
				}
				reader.close();
				
			}
			catch (IOException e)
			{
				System.out.println("Error While Reading The File");
			}
		}
		
	}
}
