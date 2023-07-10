package com.github.annasajkh.blocks;

import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;


public class Log extends Block
{

    public Log(float x, float y)
    {
        super(x, y, Minecraft2D.logTexture, ItemType.LOG, true);
    }

}
