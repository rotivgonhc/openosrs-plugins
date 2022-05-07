package net.runelite.client.plugins.nmzassist.tasks;

import java.util.List;

import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.nmzassist.*;

import static net.runelite.client.plugins.nmzassist.NMZUtils.getRandomIntBetweenRange;

public class DrinkOverloadTask extends Task
{
	private int timer;

	public DrinkOverloadTask(NMZAssistPlugin plugin, Client client, ClientThread clientThread, NMZAssistConfig config)
	{
		super(plugin, client, clientThread, config);
		timer = getRandomIntBetweenRange(1, 120);
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
		//already overloaded
		if (client.getVarbitValue(3955) != 0)
			return false;

		//don't have overloads
		List<Widget> items = InventoryHelper.getItems(client, NMZUtils.OVERLOADS);
		if (items == null || items.isEmpty())
		{
			return false;
		}

		Widget item = items.get(0);

		if (item == null)
		{
			return false;
		}

		//less than 50 hp
		return client.getBoostedSkillLevel(Skill.HITPOINTS) > 50;
	}

	@Override
	public String getTaskDescription()
	{
		return "Drinking Overload";
	}

	@Override
	public void onGameTick(GameTick event)
	{
		List<Widget> items = InventoryHelper.getItems(client, NMZUtils.OVERLOADS);

		if (items == null || items.isEmpty())
		{
			return;
		}

		Widget item = items.get(0);

		if (item == null)
		{
			return;
		}

		if (timer <= 0)
		{
			timer = getRandomIntBetweenRange(1, 100);
			clientThread.invoke(() ->
					client.invokeMenuAction(
							"Drink",
							"<col=ff9040>",
							2,
							MenuAction.CC_OP.getId(),
							item.getIndex(),
							WidgetInfo.INVENTORY.getId()));
		}
		else
		{
			timer--;
		}
	}
}