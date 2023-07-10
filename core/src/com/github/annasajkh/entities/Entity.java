package com.github.annasajkh.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.annasajkh.*;
import com.github.annasajkh.blocks.Block;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity extends Rect implements Update
{
    enum Type
    {
        ENEMY,
        SLIME,
        CLOUD,
        PLAYER
    }

    public float moveSpeed = 10;
    public float maxGravity = 75;
    public float jumpHeight = 15;
    public float gravity = Minecraft2D.gravity;
    public boolean affectedByGravity = true;
    public Chunk entityInChunk = null;
    public boolean collideWithWater = false;
    public boolean remove = false;
    public int health;
    public int air = 500;
    public float distToPlayer2 = 0;

    Type type;

    public Entity(float x, float y, float width, float height, Texture texture, int health)
    {
        super(x, y, width, height, texture);
        this.health = health;
    }

    public void jump()
    {
        if (isOnFloor || collideWithWater)
        {
            velocity.y = jumpHeight;
            isOnFloor = false;
        }
    }


    public void updateEntity()
    {
        if (Minecraft2D.spawnCooldown <= 0)
        {
            distToPlayer2 = Vector2.dst2(this.x, this.y, Minecraft2D.player.x, Minecraft2D.player.y);
        }

        List<Block> possibleCollision = new ArrayList<>();


        for (int i = -3; i <= 3; i++)
        {
            for (int j = -3; j <= 3; j++)
            {

                Vector2Simple blockPos = Block.getBlockPos(x + j * Minecraft2D.size, y + i * Minecraft2D.size);
                Block b = Minecraft2D.blockHash.get(BlockState.getString(blockPos.x, blockPos.y));

                if (b != null)
                {
                    possibleCollision.add(b);
                }
            }
        }


        if (health <= 0)
        {
            remove = true;
        }

        collideWithWater = false;

        for (Block block : possibleCollision)
        {
            if (block.itemType == ItemType.WATER && block.intersect(this))
            {
                collideWithWater = true;
            }
        }

        if (collideWithWater)
        {
            if (type == Type.CLOUD)
            {
                remove = true;
                return;
            }

            air -= Gdx.graphics.getDeltaTime();

            if (air <= 0)
            {
                health--;
            }
            gravity = Minecraft2D.gravity * 0.2f;
            jumpHeight = 5;
        }
        else
        {
            gravity = Minecraft2D.gravity;
            jumpHeight = 15;
            air = 500;
        }

        if (!affectedByGravity)
        {
            gravity = 0;
        }

        velocity.y -= gravity;
        velocity.y = MathUtils.clamp(velocity.y, -maxGravity, maxGravity);

        x += velocity.x * Gdx.graphics.getDeltaTime() * 50;
        y += velocity.y * Gdx.graphics.getDeltaTime() * 50;


        for (Entity entity : Minecraft2D.entities)
        {
            if (!entity.equals(this))
            {
                dynamicResolveCollision(entity);
            }
        }


        for (Block block : possibleCollision)
        {
            if (!block.isBackground)
            {
                resolveCollision(block);
            }
        }

    }

    @Override
    public String toString()
    {
        return "E" + "," + x + "," + y + "," + type + '\n';
    }

}
