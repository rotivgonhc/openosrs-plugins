package net.runelite.client.plugins.gildedaltar.tasks;

import java.awt.event.KeyEvent;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.gildedaltar.GildedAltarConfig;
import net.runelite.client.plugins.gildedaltar.GildedAltarPlugin;
import net.runelite.client.plugins.gildedaltar.Task;

public class ResetIdleTask extends Task
{
	public ResetIdleTask(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 0;
	}

	public boolean validate()
	{
		return this.getIdleTicks();
	}

	public String getTaskDescription()
	{
		return "Resetting idle timer";
	}

	public void onGameTick(GameTick gameTick)
	{
		if (this.getIdleTicks())
		{
			this.pressKey();
			this.client.setKeyboardIdleTicks(0);
			this.client.setMouseIdleTicks(0);
		}

	}

	private void pressKey()
	{
		int key = this.client.getTickCount() % 2 == 1 ? 37 : 39;
		KeyEvent keyPress = new KeyEvent(this.client.getCanvas(), 401, System.currentTimeMillis(), 0, key, '\uffff');
		this.client.getCanvas().dispatchEvent(keyPress);
		KeyEvent keyRelease = new KeyEvent(this.client.getCanvas(), 402, System.currentTimeMillis(), 0, key, '\uffff');
		this.client.getCanvas().dispatchEvent(keyRelease);
	}

	private boolean getIdleTicks()
	{
		int idleClientTicks = this.client.getKeyboardIdleTicks();
		if (this.client.getMouseIdleTicks() > idleClientTicks)
		{
			idleClientTicks = this.client.getMouseIdleTicks();
		}

		return idleClientTicks > 12500;
	}
}
