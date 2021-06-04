package com.github.annasajkh.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.annasajkh.Minecraft2D;

public class DesktopLauncher
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1000;
		config.height = 600;
		config.title = "Minecraft2D";
		config.resizable = false;
		new LwjglApplication(new Minecraft2D(), config);
	}
}
