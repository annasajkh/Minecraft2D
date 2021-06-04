package com.github.annasajkh.blocks;

import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;


public class Stone extends Block
{

	public Stone(float x,float y)
	{
		super(x, y,Minecraft2D.stoneTexture,ItemType.STONE);
	}

}
