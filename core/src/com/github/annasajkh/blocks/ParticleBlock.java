package com.github.annasajkh.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.github.annasajkh.ItemType;

public class ParticleBlock extends Block
{
    float delay = 0;

    public ParticleBlock(float x, float y, Texture texture, ItemType itemType)
    {
        super(x, y, texture, itemType);
        isParticle = true;
    }

}
