package net.runelite.client.plugins.gildedaltar.tasks;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.gildedaltar.GildedAltarConfig;
import net.runelite.client.plugins.gildedaltar.GildedAltarPlugin;
import net.runelite.client.plugins.gildedaltar.InventoryHelper;
import net.runelite.client.plugins.gildedaltar.Task;

import java.util.List;

public class StopConditionTask extends Task
{
	public StopConditionTask(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 0;
	}

	public boolean validate()
	{
		List<Widget> results = InventoryHelper.getInventoryItems(this.client, (this.config.boneId() + 1));
		if (results != null && !results.isEmpty())
		{
			Widget notedBones = results.get(0);
			if (notedBones == null)
			{
				return true;
			}
			else
			{
				List<Widget> gpResults = InventoryHelper.getInventoryItems(this.client, 995);
				if (gpResults != null && !gpResults.isEmpty())
				{
					Widget gp = gpResults.get(0);
					if (gp == null)
					{
						return true;
					}
					else
					{
						return gp.getItemQuantity() < 1000;
					}
				}
				else
				{
					return true;
				}
			}
		}
		else
		{
			return true;
		}
	}

	public void onGameTick(GameTick event)
	{
		List<Widget> results = InventoryHelper.getInventoryItems(this.client, (this.config.boneId() + 1));
		if (results != null && !results.isEmpty())
		{
			Widget notedBones = results.get(0);
			if (notedBones == null)
			{
				this.plugin.stopPlugin("Out of bones. (query)");
			}
			else
			{
				List<Widget> gpResults = InventoryHelper.getInventoryItems(this.client, 995);
				if (gpResults != null && !gpResults.isEmpty())
				{
					Widget gp = gpResults.get(0);
					if (gp == null)
					{
						this.plugin.stopPlugin("GP not found (first)");
					}
					else if (gp.getItemQuantity() < 1000)
					{
						this.plugin.stopPlugin("GP < 1000");
					}
					else
					{
						this.plugin.stopPlugin("Stop condition met (unspecified).");
					}
				}
				else
				{
					this.plugin.stopPlugin("GP not found. (query)");
				}
			}
		}
		else
		{
			this.plugin.stopPlugin("Out of bones. (first)");
		}
	}
}