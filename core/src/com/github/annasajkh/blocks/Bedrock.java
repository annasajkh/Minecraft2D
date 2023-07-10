package com.github.annasajkh.blocks;

import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;


public class Bedrock extends Block
{

    public Bedrock(float x, float y)
    {
        super(x, y, Minecraft2D.bedrockTexture, ItemType.BEDROCK);
    }

}
