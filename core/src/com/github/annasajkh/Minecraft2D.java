package com.github.annasajkh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.github.annasajkh.blocks.Block;
import com.github.annasajkh.entities.Entity;
import com.github.annasajkh.entities.Player;
import com.github.annasajkh.libs.FastNoiseLite;
import com.github.annasajkh.libs.FastNoiseLite.NoiseType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Minecraft2D extends ApplicationAdapter
{

    public static FastNoiseLite worldNoise;
    public static FastNoiseLite caveNoise;
    public static Random enviromentRandom;

    public static SpriteBatch worldBatch;
    public static SpriteBatch uiBatch;
    public static ShapeRenderer uiShapeRenderer;
    public static ShapeRenderer borderShapeRenderer;
    public static BitmapFont font;

    public static OrthographicCamera camera;

    public static Player player;
    public static Texture grassTexture;
    public static Texture grassBlockTexture;
    public static Texture dirtTexture;
    public static Texture stoneTexture;
    public static Texture bedrockTexture;
    public static Texture playerTexture;
    public static Texture enemyTexture;
    public static Texture cloudTexture;
    public static Texture slimeTexture;
    public static Texture logTexture;
    public static Texture leavesTexture;
    public static Texture waterTexture;
    public static Texture sandTexture;
    public static Sprite[] healthSprite;

    public static List<Chunk> chunks;
    public static List<Entity> entities;
    public static HashMap<String, BlockState> modifiedBlocks;
    public static HashMap<String, Block> blockHash;
    public static List<Block> particleBlocks;


    public static float size = 50;
    public static float worldHeightInBlock = 150;
    public static float gravity = 0.7f;
    public static float itemSize = size * 0.5f;
    public static float skyColorBlue = 0;
    public static boolean cameraMode = false;
    public static float cameraModeSpeed = 30;
    public static boolean flip = false;
    public static int seed = MathUtils.random(Integer.MAX_VALUE - 1);
    public static int chunkLength = (int) (10 * 50 / size);
    public static int entityLimit = 500;
    public static float saveTimer = 100000;
    public static float zoomSpeed = 0.1f;
    public static int seaLevel = 100;
    public static float spawnCooldown = 0.5f;
    public static float zoomFactor = 0.5f;
    public static float cameraSmoothFactor = 5f;


    public static boolean isOffScreen(float x, float y)
    {
        return isOffScreenX(x) || isOffScreenY(y);
    }

    public static boolean isOffChunk(float x)
    {
        return Minecraft2D.chunks.get(0).minPosX >= x ||
                Minecraft2D.chunks.get(Minecraft2D.chunks.size() - 1).maxPosX <= x;
    }

    public static boolean isOffScreenX(float x)
    {
        return x < ((camera.position.x - chunkLength * size * camera.zoom - size * 2) - size * 0.5f) ||
                x > ((camera.position.x + chunkLength * size * camera.zoom + size * 2) + size * 0.5f);
    }

    public static boolean isOffScreenY(float y)
    {
        return y < ((camera.position.y - chunkLength * size * camera.zoom - size) - size) ||
                y > ((camera.position.y + chunkLength * size * camera.zoom + size) + size);
    }

    public void respawn()
    {
        Item[] playerInv = player.inventory.clone();
        create();
        player.inventory = playerInv;
    }

    @Override
    public void create()
    {
        grassTexture = new Texture("blocks/grass.png");
        grassBlockTexture = new Texture("blocks/grass_block.png");
        dirtTexture = new Texture("blocks/dirt.png");
        stoneTexture = new Texture("blocks/stone.png");
        bedrockTexture = new Texture("blocks/bedrock.png");
        logTexture = new Texture("blocks/log.png");
        leavesTexture = new Texture("blocks/leaves.png");
        waterTexture = new Texture("blocks/water.png");
        sandTexture = new Texture("blocks/sand.png");

        playerTexture = new Texture("entities/player.png");
        enemyTexture = new Texture("entities/enemy.png");
        slimeTexture = new Texture("entities/slime.png");
        cloudTexture = new Texture("entities/cloud.png");

        healthSprite = new Sprite[3];
        healthSprite[0] = new Sprite(new Texture("ui/health_bar_green.png"));
        healthSprite[1] = new Sprite(new Texture("ui/health_bar_yellow.png"));
        healthSprite[2] = new Sprite(new Texture("ui/health_bar_red.png"));

        worldNoise = new FastNoiseLite();
        caveNoise = new FastNoiseLite();
        enviromentRandom = new Random();

        chunks = new ArrayList<>();
        entities = new ArrayList<>();
        modifiedBlocks = new HashMap<>();
        particleBlocks = new ArrayList<>();
        blockHash = new HashMap<>();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        worldBatch = new SpriteBatch();
        uiBatch = new SpriteBatch();
        uiShapeRenderer = new ShapeRenderer();
        borderShapeRenderer = new ShapeRenderer();
        font = new BitmapFont();

        player = new Player(0, 0);

        SaveManager.load();

        worldNoise.SetNoiseType(NoiseType.Perlin);
        worldNoise.SetSeed(seed);
        caveNoise.SetNoiseType(NoiseType.Cellular);
        worldNoise.SetSeed(seed);

        WorldGeneration.generateAroundPlayer();

        out:
        for (Chunk chunk : chunks)
        {
            if (chunk.isInChunk(player.x))
            {
                for (Block block : chunk.blocks)
                {
                    if (block.itemType == ItemType.GRASS_BLOCK)
                    {
                        player.x = block.x;
                        player.y = block.y + size;
                        break out;
                    }
                }
            }
        }
        chunks.get(0).blocks.addAll(particleBlocks);
        particleBlocks.clear();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new InputManager());
        Gdx.input.setInputProcessor(inputMultiplexer);

        for (Chunk chunk : chunks)
        {
            for (Block block : chunk.blocks)
            {
                Minecraft2D.blockHash.put(BlockState.getString(block.x, block.y), block);
            }
        }
    }

    public void update()
    {
        skyColorBlue += flip ? -0.0001f : 0.0001f;

        if (skyColorBlue > 1 || skyColorBlue < -1)
        {
            flip = !flip;
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.update();
        WorldGeneration.removeAndAddChunk();

        while (entities.size() >= entityLimit)
        {
            Entity largestDstToPlayer = entities.get(0);

            for (Entity e : entities)
            {
                if (largestDstToPlayer.distToPlayer2 < e.distToPlayer2)
                {
                    largestDstToPlayer = e;
                }
            }
            entities.remove(largestDstToPlayer);
        }

        for (Chunk chunk : chunks)
        {
            for (int i = chunk.blocks.size() - 1; i >= 0; i--)
            {
                Block block = chunk.blocks.get(i);
                if (!blockHash.containsKey(BlockState.getString(block.x, block.y)) && !chunk.isOffscreen())
                {
                    Minecraft2D.blockHash.put(BlockState.getString(block.x, block.y), block);
                }

                if (block.isParticle && !isOffChunk(block.x))
                {
                    block.update();

                    if (block.y < -Minecraft2D.size)
                    {
                        if (chunk.isOffscreen())
                        {
                            blockHash.remove(BlockState.getString(block.x, block.y));
                        }
                        chunk.blocks.remove(block);
                    }
                }


                if (block.remove)
                {
                    chunk.blocks.remove(block);
                }
            }
        }

        InputManager.getInput();
    }

    @Override
    public void render()
    {
        update();
        Gdx.gl.glClearColor(0, skyColorBlue / 2, skyColorBlue, 1);

        worldBatch.setProjectionMatrix(camera.combined);
        borderShapeRenderer.setProjectionMatrix(camera.combined);

        camera.update();

        worldBatch.begin();

        for (Chunk chunk : chunks)
        {
            for (Block block : chunk.blocks)
            {
                if (block.isBackground && !isOffScreen(block.x, block.y) && !isOffChunk(block.x))
                {
                    block.draw(worldBatch);
                }

                if (!isOffScreen(block.x, block.y) && !isOffChunk(block.x) && !block.isBackground)
                {
                    block.draw(worldBatch);
                }
            }
        }


        spawnCooldown -= Gdx.graphics.getDeltaTime();

        for (int i = entities.size() - 1; i >= 0; i--)
        {
            Entity entity = entities.get(i);

            entity.update();

            if (entity.remove)
            {
                entities.remove(entity);
            }

            if (!isOffScreen(entity.x, entity.y) && !isOffChunk(entity.x))
            {
                entity.draw(worldBatch);
            }
        }

        player.draw(worldBatch);

        worldBatch.end();

        for (Item item : player.inventory)
        {
            item.update();
        }

        if (Gdx.input.isKeyPressed(Keys.B))
        {
            borderShapeRenderer.begin(ShapeType.Filled);
            borderShapeRenderer.setColor(Color.RED);

            for (Chunk chunk : chunks)
            {
                Chunk.drawChunkBorder(chunk);
            }

            borderShapeRenderer.end();
        }


        player.drawUI(uiBatch, uiShapeRenderer);

        if (Minecraft2D.player.health <= 0)
        {
            respawn();
        }

        if (Gdx.input.isKeyJustPressed(Keys.R))
        {
            respawn();
        }

        if (saveTimer <= 0)
        {
            SaveManager.save();
            saveTimer = 1000;
        }

        saveTimer -= Gdx.graphics.getDeltaTime();

    }

    @Override
    public void dispose()
    {
        uiShapeRenderer.dispose();
        worldBatch.dispose();
        uiBatch.dispose();
        borderShapeRenderer.dispose();
    }

}
