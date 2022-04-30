package net.runelite.client.plugins.nmzassist.tasks;

import java.util.List;

import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.nmzassist.*;

public class DrinkAbsorptionTask extends Task
{
	public DrinkAbsorptionTask(NMZAssistPlugin plugin, Client client, ClientThread clientThread, NMZAssistConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	@Override
	public boolean validate()
	{
		//fail if:

		//not in the nightmare zone
		if (!NMZUtils.isInNightmareZone(client))
		{
			return false;
		}

		//don't have absorptions
		List<Widget> items = InventoryHelper.getItems(client, NMZUtils.ABSORPTIONS);
		if (items == null || items.isEmpty())
		{
			return false;
		}

		Widget item = items.get(0);

		if (item == null)
		{
			return false;
		}

		if (client.getVarbitValue(Varbits.NMZ_ABSORPTION) >= 900)
		{
			return false;
		}

		return (client.getVarbitValue(Varbits.NMZ_ABSORPTION) < 200);
	}

	@Override
	public String getTaskDescription()
	{
		return "Drinking Absorption";
	}

	@Override
	public void onGameTick(GameTick event)
	{
		List<Widget> items = InventoryHelper.getItems(client, NMZUtils.ABSORPTIONS);
		if (items == null || items.isEmpty())
		{
			return;
		}

		Widget item = items.get(0);

		if (item == null)
		{
			return;
		}

		clientThread.invoke(() ->
				client.invokeMenuAction(
						"Drink",
						"<col=ff9040>",
						2,
						MenuAction.CC_OP.getId(),
						item.getIndex(),
						WidgetInfo.INVENTORY.getId()));
	}
}