package com.github.annasajkh.blocks;

import com.github.annasajkh.BlockState;
import com.github.annasajkh.ItemType;
import com.github.annasajkh.Minecraft2D;

public class Grass extends Block
{

    public static float spawnChance = 0.9f;

    public Grass(float x, float y)
    {
        super(x, y, Minecraft2D.grassTexture, ItemType.GRASS, true);
        isParticle = false;
        isBackground = true;
    }

    @Override
    public void update()
    {
        if (Minecraft2D.blockHash.get(BlockState.getString(x, y - Minecraft2D.size)) == null)
        {
            remove = true;
        }
    }

}
