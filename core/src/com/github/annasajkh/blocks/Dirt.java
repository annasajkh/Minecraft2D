package com.github.annasajkh.blocks;

import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;

public class Dirt extends Block
{

    public Dirt(float x, float y)
    {
        super(x, y, Minecraft2D.dirtTexture, ItemType.DIRT);
    }

}
