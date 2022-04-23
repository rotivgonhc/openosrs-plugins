package net.runelite.client.plugins.gildedaltar.tasks;

import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.gildedaltar.*;

import java.util.List;

public class EnterPOHTask extends Task
{
	public EnterPOHTask(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 3;
	}

	public boolean validate()
	{
		if (MiscUtils.isInPOH(this.client))
		{
			return false;
		}
		else
		{
			Widget widget = this.client.getWidget(WidgetInfo.CHATBOX_TITLE);
			if (widget != null && !widget.isHidden() && widget.getText().equals("Enter name:"))
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
					QueryResults<GameObject> objectQueryResults = ((GameObjectQuery) (new GameObjectQuery()).nameEquals(new String[] {"Portal"})).result(this.client);
					if (objectQueryResults != null && !objectQueryResults.isEmpty())
					{
						GameObject portalObject = (GameObject) objectQueryResults.first();
						return portalObject != null;
					}
					else
					{
						return false;
					}
				}
			}
		}
	}

	public void onGameTick(GameTick event)
	{
		QueryResults<GameObject> objectQueryResults = ((GameObjectQuery) (new GameObjectQuery()).nameEquals(new String[] {"Portal"})).result(this.client);
		if (objectQueryResults != null && !objectQueryResults.isEmpty())
		{
			GameObject portalObject = (GameObject) objectQueryResults.first();
			if (portalObject != null)
			{
				this.clientThread.invoke(() -> {
					this.client.invokeMenuAction("Build mode", "<col=ffff>Portal", portalObject.getId(), MenuAction.GAME_OBJECT_FOURTH_OPTION.getId(), portalObject.getSceneMinLocation().getX(), portalObject.getSceneMinLocation().getY());
				});
			}
		}
	}
}