package com.github.annasajkh.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.github.annasajkh.*;

public class Block extends Rect implements Update
{
    public boolean isModifiedBlock = false;
    public boolean isBackground = false;
    public boolean isParticle = false;
    public ItemType itemType;
    public boolean remove = false;


    public static Block createBlock(float x, float y, ItemType itemType)
    {
        Block block = null;

        switch (itemType)
        {
            case GRASS:
                block = new Grass(x, y);
                break;
            case GRASS_BLOCK:
                block = new GrassBlock(x, y);
                break;
            case DIRT:
                block = new Dirt(x, y);
                break;
            case STONE:
                block = new Stone(x, y);
                break;
            case BEDROCK:
                block = new Bedrock(x, y);
                break;
            case LOG:
                block = new Log(x, y);
                break;
            case LEAVES:
                block = new Leaves(x, y);
                break;
            case WATER:
                block = new Water(x, y);
                break;
            case SAND:
                block = new Sand(x, y);
                break;
            case EMPTY:
        }

        return block;
    }

    public static Vector2Simple getBlockPos(float x, float y)
    {
        return new Vector2Simple(MathUtils.ceil(x / Minecraft2D.size) * Minecraft2D.size - Minecraft2D.size / 2,
                                 MathUtils.ceil(y / Minecraft2D.size) * Minecraft2D.size - Minecraft2D.size / 2);
    }

    public boolean posEqual(Block otherBlock)
    {
        return x == otherBlock.x && y == otherBlock.y;
    }

    public boolean posEqual(float x, float y)
    {
        return this.x == x && this.y == y;
    }


    public Block(float x, float y, Texture texture, ItemType itemType)
    {
        super(x, y, Minecraft2D.size, Minecraft2D.size, texture);
        this.itemType = itemType;
    }

    public Block(float x, float y, Texture texture, ItemType itemType, boolean isBackground)
    {
        this(x, y, texture, itemType);
        this.isBackground = isBackground;
    }

    @Override
    public void update()
    {
    }


}
