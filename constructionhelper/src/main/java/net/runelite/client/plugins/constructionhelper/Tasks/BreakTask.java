package net.runelite.client.plugins.constructionhelper.Tasks;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperConfig;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperPlugin;
import net.runelite.client.plugins.constructionhelper.Task;

public class BreakTask extends Task
{
	public BreakTask(ConstructionHelperPlugin plugin, Client client, ClientThread clientThread, ConstructionHelperConfig config)
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
