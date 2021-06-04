package com.github.annasajkh;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Rect
{
	public float x,y;
	public float width, height;
	protected float leftSide, rightSide, topSide, bottomSide;
	public Vector2 velocity = new Vector2();
	protected Texture texture;
	protected Sprite sprite;
	protected boolean isOnFloor = false;
	protected boolean isOnWall = false;
	
	
	public Rect(float x, float y,float width,float height,Texture texture)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = texture;
	}
	
	public void updateBounds()
	{
		rightSide = x + width * 0.5f;
		leftSide = x - width * 0.5f;
		topSide = y + height * 0.5f;
		bottomSide = y - height * 0.5f;
	}
	
	public boolean intersectPoint(float x,float y)
	{
		return x >= this.x - width * 0.5f &&
				x <= this.x + width * 0.5f && 
				y >= this.y - height * 0.5f && 
				y <= this.y + height * 0.5f;
	}
	
	public boolean intersect(Rect otherRect)
	{
		updateBounds();
		otherRect.updateBounds();
		return (rightSide > otherRect.leftSide && 
				leftSide < otherRect.rightSide && 
				bottomSide < otherRect.topSide &&
                topSide > otherRect.bottomSide);
			
	}
	
	public void draw(SpriteBatch spriteBatch)
	{
		updateBounds();
		spriteBatch.draw(texture,x - width * 0.5f,y - height * 0.5f,width,height);
	}
	
	public void resolveCollision(Rect otherRect)
	{
		if(intersect(otherRect))
		{
            float collisionDeepX;
            float collisionDeepY;

            if (x > otherRect.x)
            {
                collisionDeepX = Math.abs(x - otherRect.x - width / 2 - otherRect.width / 2);
            }
            else
            {
                collisionDeepX = Math.abs(otherRect.x - x - width / 2 - otherRect.width / 2);
            }

            if (y > otherRect.y)
            {
                velocity.x = 0;
                collisionDeepY = Math.abs(y - otherRect.y - height / 2 - otherRect.height / 2);
            }
            else
            {
                velocity.x = 0;
                collisionDeepY = Math.abs(otherRect.y - y - height / 2 - otherRect.height / 2);
            }

            if (x < otherRect.x && collisionDeepX < collisionDeepY)
            {
            	isOnWall = true;
                velocity.x = 0;
                x -= collisionDeepX;
            }
            else if (x > otherRect.x && collisionDeepX < collisionDeepY)
            {
            	isOnWall = true;
                velocity.x = 0;
                x += collisionDeepX;
            }
            else if (y > otherRect.y && collisionDeepX > collisionDeepY)
            {
            	isOnFloor = true;
            	isOnWall = false;
            	
                velocity.y = 0;
                y += collisionDeepY;
            }
            else if (y < otherRect.y && collisionDeepX > collisionDeepY)
            {
            	isOnFloor = false;
            	isOnWall = false;
            	
                velocity.y = 0;
                y -= collisionDeepY;
            }
            else
            {
            	isOnFloor = false;
            	isOnWall = false;
            }
        }
	}
	
	
	public void resolveCollisionAmount(Rect otherRect,float amountScale)
	{
		if(intersect(otherRect))
		{
            float collisionDeepX;
            float collisionDeepY;

            if (x > otherRect.x)
            {
                collisionDeepX = Math.abs(x - otherRect.x - width / 2 - otherRect.width / 2);
            }
            else
            {
                collisionDeepX = Math.abs(otherRect.x - x - width / 2 - otherRect.width / 2);
            }

            if (y > otherRect.y)
            {
                velocity.x = 0;
                collisionDeepY = Math.abs(y - otherRect.y - height / 2 - otherRect.height / 2);
            }
            else
            {
                velocity.x = 0;
                collisionDeepY = Math.abs(otherRect.y - y - height / 2 - otherRect.height / 2);
            }

            if (x < otherRect.x && collisionDeepX < collisionDeepY)
            {
            	isOnWall = true;
            	isOnFloor = false;
                velocity.x = 0;
                x -= collisionDeepX * amountScale;
            }
            else if (x > otherRect.x && collisionDeepX < collisionDeepY)
            {
            	isOnWall = true;
            	isOnFloor = false;
                velocity.x = 0;
                x += collisionDeepX * amountScale;
            }
            else if (y > otherRect.y && collisionDeepX > collisionDeepY)
            {
            	isOnFloor = true;
            	isOnWall = false;
            	
                velocity.y = 0;
                y += collisionDeepY * amountScale;
            }
            else if (y < otherRect.y && collisionDeepX > collisionDeepY)
            {
            	isOnFloor = false;
            	isOnWall = false;
            	
                velocity.y = 0;
                y -= collisionDeepY * amountScale;
            }
            else
            {
            	isOnFloor = false;
            	isOnWall = false;
            }
        }
	}
	
	public void dynamicResolveCollision(Rect otherRect)
	{
		if(intersect(otherRect))
		{
            float collisionDeepX;
            float collisionDeepY;

            if (x > otherRect.x)
            {	
                collisionDeepX = Math.abs(x - otherRect.x - width / 2 - otherRect.width / 2);
            }
            else
            {
                collisionDeepX = Math.abs(otherRect.x - x - width / 2 - otherRect.width / 2);
            }

            if (y > otherRect.y)
            {

                velocity.x = 0;
                otherRect.velocity.x = 0;
                collisionDeepY = Math.abs(y - otherRect.y - height / 2 - otherRect.height / 2);
            }
            else
            {

                velocity.x = 0;
                otherRect.velocity.x = 0;
                collisionDeepY = Math.abs(otherRect.y - y - height / 2 - otherRect.height / 2);
            }
            

            collisionDeepX *= 0.5f;
            collisionDeepY *= 0.5f;

            if (x < otherRect.x && collisionDeepX < collisionDeepY)
            {
            	isOnWall = true;
            	isOnFloor = false;
            	
                velocity.x = 0;
                
                otherRect.velocity.x = 0;
                
                x -= collisionDeepX;
                otherRect.x += collisionDeepX;
            }
            else if (x > otherRect.x && collisionDeepX < collisionDeepY)
            {
            	isOnWall = true;
            	isOnFloor = false;
            	velocity.x = 0;
                otherRect.velocity.x = 0;
                
                x += collisionDeepX;
                otherRect.x -= collisionDeepX;
            }
            else if (y > otherRect.y && collisionDeepX > collisionDeepY)
            {

            	isOnFloor = true;
            	isOnWall = false;
            	
                velocity.y = 0;
                otherRect.velocity.y = 0;
                
                y += collisionDeepY;
                otherRect.y -= collisionDeepY;
            }
            else if (y < otherRect.y && collisionDeepX > collisionDeepY)
            {
            	isOnWall = false;
            	isOnFloor = false;
                velocity.y = 0;
                otherRect.velocity.y = 0;
                
                y -= collisionDeepY;
                otherRect.y += collisionDeepY;
            }
            else
            {
            	isOnFloor = false;
            	isOnWall = false;
            }
        }
	}

}
