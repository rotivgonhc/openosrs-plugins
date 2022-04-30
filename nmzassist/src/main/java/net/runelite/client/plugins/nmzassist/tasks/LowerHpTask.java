package net.runelite.client.plugins.nmzassist.tasks;

import java.util.List;

import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.nmzassist.*;

public class LowerHpTask extends Task
{
	public LowerHpTask(NMZAssistPlugin plugin, Client client, ClientThread clientThread, NMZAssistConfig config)
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

		//don't have items to lower hp
		List<Widget> items = InventoryHelper.getItems(client, NMZUtils.LOWER_HP);
		if (items == null || items.isEmpty())
		{
			return false;
		}

		Widget item = items.get(0);

		if (item == null)
		{
			return false;
		}

		if (client.getVarbitValue(3955) == 0 && InventoryHelper.hasItems(client, NMZUtils.OVERLOADS))
		{
			return false;
		}

		return (client.getBoostedSkillLevel(Skill.HITPOINTS) >= 2);
	}

	@Override
	public String getTaskDescription()
	{
		return "Lowering HP";
	}

	@Override
	public void onGameTick(GameTick event)
	{
		List<Widget> items = InventoryHelper.getItems(client, NMZUtils.LOWER_HP);
		if (items == null || items.isEmpty())
		{
			return;
		}

		Widget item = items.get(0);

		if (item == null)
		{
			return;
		}

		String option = "Guzzle";
		int id = 4;
		if (item.getItemId() == ItemID.LOCATOR_ORB)
		{
			option = "Feel";
			id = 2;
		}
		String finalOption = option;
		int finalId = id;
		clientThread.invoke(() ->
				client.invokeMenuAction(
						finalOption,
						"",
						finalId,
						MenuAction.CC_OP.getId(),
						item.getIndex(),
						WidgetInfo.INVENTORY.getId()
				)
		);
	}
}