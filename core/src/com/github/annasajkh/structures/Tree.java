package com.github.annasajkh.structures;

import java.util.ArrayList;
import java.util.List;

import com.github.annasajkh.Minecraft2D;
import com.github.annasajkh.blocks.Block;
import com.github.annasajkh.blocks.Leaves;
import com.github.annasajkh.blocks.Log;

public class Tree
{
	
	public static float spawnChance = 0.5f;
	
	public static List<Block> createTree(float x, float y)
	{
		List<Block> tree = new ArrayList<>();
		for (int i = 0; i < 4; i++)
		{
			Block log = new Log(x,y + Minecraft2D.size * i);
			tree.add(log);
		}
		
		Block offset = tree.get(tree.size()-1);
		
		for (int i = 1; i < 4; i++)
		{
			for (int j = 1; j < 4; j++)
			{
				Block leaves = new Leaves(x - Minecraft2D.size *(j - 2),offset.y + Minecraft2D.size * i);
				tree.add(leaves);
			}
		}
		
		return tree;
	}
}
