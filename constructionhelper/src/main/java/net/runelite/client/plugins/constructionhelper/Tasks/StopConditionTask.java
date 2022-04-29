package net.runelite.client.plugins.constructionhelper.Tasks;

import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperConfig;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperPlugin;
import net.runelite.client.plugins.constructionhelper.InventoryHelper;
import net.runelite.client.plugins.constructionhelper.Task;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StopConditionTask extends Task
{
	public StopConditionTask(ConstructionHelperPlugin plugin, Client client, ClientThread clientThread, ConstructionHelperConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 0;
	}

	public boolean validate()
	{
		List<Widget> sawResults = Stream.concat(InventoryHelper.getInventoryItems(this.client, ItemID.SAW).stream(), InventoryHelper.getInventoryItems(this.client, ItemID.CRYSTAL_SAW).stream()).collect(Collectors.toList());
		if (sawResults != null && !sawResults.isEmpty())
		{
			Widget sawWidget = sawResults.get(0);
			if (sawWidget == null)
			{
				return true;
			}
			else
			{
				List<Widget> hammerResults = InventoryHelper.getInventoryItems(this.client, ItemID.HAMMER);
				if (hammerResults != null && !hammerResults.isEmpty())
				{
					Widget hammerWidget = hammerResults.get(0);
					if (hammerWidget == null)
					{
						return true;
					}
					else
					{
						int[] var5 = this.config.mode().getOtherReqs();
						int var6 = var5.length;

						for (int var7 = 0; var7 < var6; ++var7)
						{
							int req = var5[var7];
							List<Widget> reqResults = InventoryHelper.getInventoryItems(this.client, req);
							if (reqResults == null || reqResults.isEmpty())
							{
								return true;
							}

							Widget reqWidget = reqResults.get(0);
							if (reqWidget == null)
							{
								return true;
							}
						}
						List<Widget> results = InventoryHelper.getInventoryItems(this.client, (this.config.mode().getPlankId() + 1));
						if (results != null && !results.isEmpty())
						{
							Widget notedPlanks = results.get(0);
							if (notedPlanks == null)
							{
								return true;
							}
							else if (notedPlanks.getItemQuantity() < this.config.mode().getPlankCost())
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
									else if (gp.getItemQuantity() < 1000)
									{
										return true;
									}
									else
									{
										return false;
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
		List<Widget> sawResults = Stream.concat(InventoryHelper.getInventoryItems(this.client, ItemID.SAW).stream(), InventoryHelper.getInventoryItems(this.client, ItemID.CRYSTAL_SAW).stream()).collect(Collectors.toList());
		if (sawResults != null && !sawResults.isEmpty())
		{
			Widget sawWidget = sawResults.get(0);
			if (sawWidget == null)
			{
				this.plugin.stopPlugin("Saw not found. (first)");
			}
			else
			{
				List<Widget> hammerResults = InventoryHelper.getInventoryItems(this.client, ItemID.HAMMER);
				if (hammerResults != null && !hammerResults.isEmpty())
				{
					Widget hammerWidget = hammerResults.get(0);
					if (hammerWidget == null)
					{
						this.plugin.stopPlugin("Hammer not found. (first)");
					}
					else
					{
						int[] var6 = this.config.mode().getOtherReqs();
						int var7 = var6.length;

						for (int var8 = 0; var8 < var7; ++var8)
						{
							int req = var6[var8];
							List<Widget> reqResults = InventoryHelper.getInventoryItems(this.client, req);
							ConstructionHelperPlugin var10000;
							ItemComposition var10001;
							if (reqResults == null || reqResults.isEmpty())
							{
								var10000 = this.plugin;
								var10001 = this.client.getItemDefinition(req);
								var10000.stopPlugin("Missing requirement: " + var10001.getName());
								return;
							}

							Widget reqWidget = reqResults.get(0);
							if (reqWidget == null)
							{
								var10000 = this.plugin;
								var10001 = this.client.getItemDefinition(req);
								var10000.stopPlugin("Missing requirement (first): " + var10001.getName());
								return;
							}
						}

						List<Widget> results = InventoryHelper.getInventoryItems(this.client, (this.config.mode().getPlankId() + 1));
						if (results != null && !results.isEmpty())
						{
							Widget notedPlanks = results.get(0);
							if (notedPlanks == null)
							{
								this.plugin.stopPlugin("Out of noted planks.");
							}
							else if (notedPlanks.getItemQuantity() < this.config.mode().getPlankCost())
							{
								this.plugin.stopPlugin("Less noted planks than required for crafting target object.");
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
									this.plugin.stopPlugin("GP not found.");
								}
							}
						}
						else
						{
							this.plugin.stopPlugin("Out of noted planks. (first)");
						}
					}
				}
				else
				{
					this.plugin.stopPlugin("Hammer not found.");
				}
			}
		}
		else
		{
			this.plugin.stopPlugin("Saw not found.");
		}
	}
}
