package com.github.annasajkh.blocks;

import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;

public class Leaves extends Block
{

	public Leaves(float x, float y)
	{
		super(x, y, Minecraft2D.leavesTexture, ItemType.LEAVES,true);
	}

}
