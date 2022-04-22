package net.runelite.client.plugins.nmzassist.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.nmzassist.NMZUtils;
import net.runelite.client.plugins.nmzassist.NMZAssistConfig;
import net.runelite.client.plugins.nmzassist.NMZAssistPlugin;
import net.runelite.client.plugins.nmzassist.Task;

public class DrinkOverloadTask extends Task
{

	protected int delay = 0;

	public DrinkOverloadTask(NMZAssistPlugin plugin, Client client, ClientThread clientThread, NMZAssistConfig config)
	{
		super(plugin, client, clientThread, config);
		this.delay = getRandomWait();
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
		//if (client.getVar(NMZ_OVERLOAD) != 0)
		if (client.getVarbitValue(3955) != 0)
			return false;

		//don't have overloads
		List<Widget> items = getOverloadPotions();
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
		List<Widget> items = getOverloadPotions();

		if (items == null || items.isEmpty())
		{
			return;
		}

		Widget item = items.get(0);

		if (item == null)
		{
			return;
		}

		if (this.delay > 0)
		{
			delay--;
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
		delay = getRandomWait();
	}

	public List<Widget> getOverloadPotions()
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

		if (inventoryWidget == null)
		{
			return null;
		}

		Widget[] inventoryItems = inventoryWidget.getDynamicChildren();

		if (inventoryItems == null)
		{
			return null;
		}

		List<Widget> items = Arrays.asList(inventoryItems);

		return items.stream()
				.filter(item -> Arrays.asList(ItemID.OVERLOAD_1, ItemID.OVERLOAD_2, ItemID.OVERLOAD_3, ItemID.OVERLOAD_4).contains(item.getItemId()))
				.collect(Collectors.toList());
	}

	private int getRandomWait()
	{
		return (int) ((Math.random() * (120 - 0)));
	}
}