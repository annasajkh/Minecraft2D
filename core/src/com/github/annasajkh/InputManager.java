package com.github.annasajkh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.github.annasajkh.blocks.Block;
import com.github.annasajkh.entities.Enemy;
import com.github.annasajkh.entities.Entity;

public class InputManager implements InputProcessor
{

    public static Vector3 mousePos = new Vector3();

    public static void getInput()
    {
        mousePos.x = Gdx.input.getX();
        mousePos.y = Gdx.input.getY();
        mousePos = Minecraft2D.camera.unproject(mousePos);

        if (Minecraft2D.cameraMode)
        {
            if (Gdx.input.isKeyPressed(Keys.D))
            {
                Minecraft2D.player.x += Minecraft2D.cameraModeSpeed;

                if (Minecraft2D.player.sprite.isFlipX())
                {
                    Minecraft2D.player.sprite.flip(true, false);
                }
            }
            else if (Gdx.input.isKeyPressed(Keys.A))
            {

                Minecraft2D.player.x -= Minecraft2D.cameraModeSpeed;

                if (!Minecraft2D.player.sprite.isFlipX())
                {
                    Minecraft2D.player.sprite.flip(true, false);
                }
            }

            if (Gdx.input.isKeyPressed(Keys.W))
            {
                Minecraft2D.player.y += Minecraft2D.cameraModeSpeed;

            }
            else if (Gdx.input.isKeyPressed(Keys.S))
            {
                Minecraft2D.player.y -= Minecraft2D.cameraModeSpeed;
            }
        }


        if (Gdx.input.isKeyJustPressed(Keys.K))
        {
            Minecraft2D.entities.clear();
        }

        if (Gdx.input.isKeyJustPressed(Keys.I))
        {
            if (Minecraft2D.player.inventory[Minecraft2D.player.selectedInventoryIndex].itemType != ItemType.EMPTY)
            {
                Minecraft2D.player.inventory[Minecraft2D.player.selectedInventoryIndex].count = Double.POSITIVE_INFINITY;
            }
        }

        if ((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) && Gdx.input.isButtonPressed(0))
        {
            boolean isEmpty = true;

            out:
            for (Chunk chunk : Minecraft2D.chunks)
            {
                for (Block block : chunk.blocks)
                {
                    if (block.intersectPoint(mousePos.x, mousePos.y) && !block.isBackground)
                    {
                        isEmpty = false;
                        break out;
                    }
                }
            }

            if (isEmpty)
            {
                Minecraft2D.entities.add(new Enemy(mousePos.x, mousePos.y));
            }
        }
        else if (Gdx.input.isButtonPressed(0))
        {
            boolean isEmpty = true;

            out:
            for (Chunk chunk : Minecraft2D.chunks)
            {
                for (Block block : chunk.blocks)
                {
                    if (block.intersectPoint(mousePos.x, mousePos.y))
                    {
                        isEmpty = false;
                        break out;
                    }
                }
            }

            if (isEmpty && Minecraft2D.player.inventory[Minecraft2D.player.selectedInventoryIndex].count > 0)
            {
                Vector2Simple blockPos = Block.getBlockPos(mousePos.x, mousePos.y);
                Block block = Block.createBlock(blockPos.x, blockPos.y,
                                                Minecraft2D.player.inventory[Minecraft2D.player.selectedInventoryIndex].itemType);

                block.isModifiedBlock = true;

                if (!block.isParticle)
                {
                    Minecraft2D.modifiedBlocks.put(BlockState.getString(block.x, block.y), new BlockState(block.x, block.y, block.itemType, false));
                }

                for (Chunk chunk : Minecraft2D.chunks)
                {
                    if (chunk.isInChunk(block.x))
                    {
                        chunk.blocks.add(block);
                        break;
                    }
                }

                Minecraft2D.player.inventory[Minecraft2D.player.selectedInventoryIndex].count--;
            }
        }

        if ((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) && Gdx.input.isKeyJustPressed(Keys.C))
        {
            Minecraft2D.cameraMode = false;
        }
        else if (Gdx.input.isKeyJustPressed(Keys.C))
        {
            Minecraft2D.cameraMode = true;
        }

        if ((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) && Gdx.input.isKeyJustPressed(Keys.G))
        {
            Minecraft2D.gravity = 0;
        }
        else if (Gdx.input.isKeyJustPressed(Keys.G))
        {
            Minecraft2D.gravity = 0.5f;
        }

        if ((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) && Gdx.input.isKeyPressed(Keys.O))
        {
            Minecraft2D.camera.rotate(-1);
        }
        else if (Gdx.input.isKeyPressed(Keys.O))
        {
            Minecraft2D.camera.rotate(1);
        }

        if ((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) && Gdx.input.isButtonPressed(1))
        {
            for (int i = Minecraft2D.entities.size() - 1; i >= 0; i--)
            {
                Entity entity = Minecraft2D.entities.get(i);

                if (entity.intersectPoint(mousePos.x, mousePos.y))
                {
                    Minecraft2D.entities.remove(entity);
                }
            }

        }
        else if (Gdx.input.isButtonPressed(1))
        {
            Vector2Simple realPos = Block.getBlockPos(mousePos.x, mousePos.y);
            Block block = Minecraft2D.blockHash.get(BlockState.getString(realPos.x, realPos.y));

            if (block != null)
            {
                for (int j = 0; j < Minecraft2D.player.inventory.length; j++)
                {
                    if (block.itemType == Minecraft2D.player.inventory[j].itemType)
                    {
                        Minecraft2D.player.inventory[j].count++;

                        if (!block.isParticle)
                        {
                            Minecraft2D.modifiedBlocks.put(BlockState.getString(block.x, block.y), new BlockState(block.x, block.y, block.itemType, true));
                        }


                        Minecraft2D.blockHash.remove(BlockState.getString(block.x, block.y));

                        for (Chunk chunk : Minecraft2D.chunks)
                        {
                            if (chunk.blocks.contains(block))
                            {
                                chunk.blocks.remove(block);
                                return;
                            }
                        }
                    }
                }

                for (int j = 0; j < Minecraft2D.player.inventory.length; j++)
                {
                    if (Minecraft2D.player.inventory[j].itemType == ItemType.EMPTY)
                    {
                        Minecraft2D.player.inventory[j].itemType = block.itemType;
                        Minecraft2D.player.inventory[j].count = 1;

                        if (!block.isParticle)
                        {
                            Minecraft2D.modifiedBlocks.put(BlockState.getString(block.x, block.y), new BlockState(block.x, block.y, block.itemType, true));
                        }

                        Minecraft2D.blockHash.remove(BlockState.getString(block.x, block.y));

                        for (Chunk chunk : Minecraft2D.chunks)
                        {
                            if (chunk.blocks.contains(block))
                            {
                                chunk.blocks.remove(block);
                                return;
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        if (amount < 0)
        {
            Minecraft2D.camera.zoom -= Minecraft2D.zoomSpeed * Minecraft2D.camera.zoom * Minecraft2D.zoomFactor;
        }
        else
        {
            Minecraft2D.camera.zoom += Minecraft2D.zoomSpeed * Minecraft2D.camera.zoom * Minecraft2D.zoomFactor;
        }
        Minecraft2D.camera.zoom = MathUtils.clamp(Minecraft2D.camera.zoom, 0.0001f, 5f);
        return true;
    }

}
