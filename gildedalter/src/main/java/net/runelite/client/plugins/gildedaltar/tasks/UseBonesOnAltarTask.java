package net.runelite.client.plugins.gildedaltar.tasks;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.QueryResults;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.gildedaltar.*;

import java.util.List;

public class UseBonesOnAltarTask extends Task
{
	public UseBonesOnAltarTask(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 0;
	}

	public boolean validate()
	{
		if (!MiscUtils.isInPOH(this.client))
		{
			return false;
		}
		else
		{
			List<Widget> items = InventoryHelper.getInventoryItems(this.client, this.config.boneId());

			if (items == null || items.isEmpty())
			{
				return false;
			}
			else
			{
				GameObject gameObject = MiscUtils.findNearestGameObject(this.client, "Altar");
				return gameObject != null;
			}
		}
	}

	public void onGameTick(GameTick event)
	{
		QueryResults<GameObject> gameObjects = (new GameObjectQuery()).filter((o) -> o.getName().contains("Altar")).result(this.client);
		if (gameObjects != null && !gameObjects.isEmpty())
		{
			GameObject altarObject = gameObjects.first();
			if (altarObject != null)
			{
				this.clientThread.invoke(() -> {
					List<Widget> items = InventoryHelper.getInventoryItems(this.client, this.config.boneId());
					if (items != null && !items.isEmpty())
					{
						Widget firstItem = items.get(0);
						if (firstItem != null)
						{
							GameObject object = MiscUtils.findNearestGameObject(this.client, "Altar");
							if (object != null)
							{
								this.clientThread.invoke(() -> {
									this.client.setSelectedSpellWidget(WidgetInfo.INVENTORY.getId());
									this.client.setSelectedSpellChildIndex(firstItem.getIndex());
									this.client.setSelectedSpellItemId(firstItem.getItemId());
									this.client.invokeMenuAction("", "", altarObject.getId(), MenuAction.WIDGET_TARGET_ON_GAME_OBJECT.getId(), altarObject.getSceneMinLocation().getX(), altarObject.getSceneMinLocation().getY());
								});
							}
						}
					}
				});
			}
		}
	}
}
