package net.runelite.client.plugins.nmzassist;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.runelite.api.Client;

public class InventoryHelper
{
	public static List<Widget> getItems(Client client, Collection<Integer> itemIds)
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
				.filter(item -> itemIds.contains(item.getItemId()))
				.collect(Collectors.toList());
	}

	public static boolean hasItems(Client client, Collection<Integer> itemIds)
	{
		return getItems(client, itemIds).size() > 0;
	}
}
