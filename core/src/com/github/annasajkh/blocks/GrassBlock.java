package com.github.annasajkh.blocks;

import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;


public class GrassBlock extends Block
{

    public GrassBlock(float x, float y)
    {
        super(x, y, Minecraft2D.grassBlockTexture, ItemType.GRASS_BLOCK);
    }

}
