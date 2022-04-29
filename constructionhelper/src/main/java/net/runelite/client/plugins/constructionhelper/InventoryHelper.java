package net.runelite.client.plugins.constructionhelper;

import net.runelite.api.VarClientInt;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.Client;

import java.util.ArrayList;
import java.util.List;

public class InventoryHelper
{
	public static List<Widget> getInventoryItems(Client client, int itemId)
	{
		if (client.getVar(VarClientInt.INVENTORY_TAB) != 3)
		{
			client.runScript(915, 3);
		}

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

		List<Widget> items = new ArrayList<>();
		for (Widget item: inventoryItems)
		{
			if (itemId == item.getItemId())
			{
				items.add(item);
			}
		}

		return items;
	}

}
