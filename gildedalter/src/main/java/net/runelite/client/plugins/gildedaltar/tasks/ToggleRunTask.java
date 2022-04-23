package net.runelite.client.plugins.gildedaltar.tasks;

import java.util.concurrent.ThreadLocalRandom;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.gildedaltar.GildedAltarConfig;
import net.runelite.client.plugins.gildedaltar.GildedAltarPlugin;
import net.runelite.client.plugins.gildedaltar.Task;

public class ToggleRunTask extends Task
{
	int nextRunEnergy = this.getRandomIntBetweenRange(20, 100);

	public ToggleRunTask(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 0;
	}

	public boolean validate()
	{
		if (this.client.getVarpValue(173) == 1)
		{
			return false;
		}
		else
		{
			return this.client.getEnergy() > this.nextRunEnergy;
		}
	}

	public void onGameTick(GameTick event)
	{
		boolean runEnabled = this.client.getVarpValue(173) == 1;
		if (this.client.getEnergy() > this.nextRunEnergy && !runEnabled)
		{
			this.nextRunEnergy = this.getRandomIntBetweenRange(20, 100);
			Widget runOrb = this.client.getWidget(WidgetInfo.MINIMAP_RUN_ORB);
			if (runOrb != null)
			{
				this.clientThread.invoke(() -> {
					this.client.invokeMenuAction("Toggle Run", "", 1, MenuAction.CC_OP.getId(), -1, WidgetInfo.MINIMAP_TOGGLE_RUN_ORB.getId());
				});
			}
		}

	}

	public int getRandomIntBetweenRange(int min, int max)
	{
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
}