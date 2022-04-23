package net.runelite.client.plugins.gildedaltar.tasks;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.gildedaltar.GildedAltarConfig;
import net.runelite.client.plugins.gildedaltar.GildedAltarPlugin;
import net.runelite.client.plugins.gildedaltar.Task;

public class BreakTask extends Task
{
	public BreakTask(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 0;
	}

	public boolean validate()
	{
		return this.plugin.chinBreakHandler.shouldBreak(this.plugin);
	}

	public String getTaskDescription()
	{
		return "Taking a break";
	}

	public void onGameTick(GameTick gameTick)
	{
		if (this.plugin.chinBreakHandler.shouldBreak(this.plugin))
		{
			this.plugin.chinBreakHandler.startBreak(this.plugin);
		}

	}
}
