package net.runelite.client.plugins.nmzassist.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
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

public class DrinkPrayerTask extends Task
{

	protected Random randomValue;
	protected int nextRestoreValue = 0;

	public DrinkPrayerTask(NMZAssistPlugin plugin, Client client, ClientThread clientThread, NMZAssistConfig config)
	{
		super(plugin, client, clientThread, config);
		this.randomValue = new Random();
		this.nextRestoreValue = randomRestoreValue();
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

		//don't have prayer restore potions
		List<Widget> items = getPrayerRestorePotions();
		if (items == null || items.isEmpty())
		{
			return false;
		}

		Widget item = items.get(0);

		if (item == null)
		{
			return false;
		}

		// boost amount is greater than prayer level
		int currentPrayerPoints = client.getBoostedSkillLevel(Skill.PRAYER);
		int prayerLevel = client.getRealSkillLevel(Skill.PRAYER);
		int boostAmount = getBoostAmount(prayerLevel);

		if (currentPrayerPoints + boostAmount > prayerLevel)
		{
			return false;
		}

		return (currentPrayerPoints <= this.nextRestoreValue);
	}

	@Override
	public String getTaskDescription()
	{
		return "Restoring Prayer";
	}

	@Override
	public void onGameTick(GameTick event)
	{
		List<Widget> items = getPrayerRestorePotions();
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
						"<col=ff9040>Potion",
						2,
						MenuAction.CC_OP.getId(),
						item.getIndex(),
						WidgetInfo.INVENTORY.getId()
				)
		);
		this.nextRestoreValue = randomRestoreValue();
	}

	public int getBoostAmount(int prayerLevel)
	{
		return 7 + (int) Math.floor(prayerLevel * .25);
	}

	private int randomRestoreValue()
	{
		return randomValue.nextInt((config.maxPrayerLevel() - config.minPrayerLevel()) + 1) + config.minPrayerLevel();
	}

	public List<Widget> getPrayerRestorePotions()
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
				.filter(item -> Arrays.asList(ItemID.PRAYER_POTION1, ItemID.PRAYER_POTION2, ItemID.PRAYER_POTION3, ItemID.PRAYER_POTION4).contains(item.getItemId()))
				.collect(Collectors.toList());
	}
}
