package net.runelite.client.plugins.constructionhelper.Tasks;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.QueryResults;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.constructionhelper.*;

import java.util.List;

public class EnterPOHTask extends Task
{
	public EnterPOHTask(ConstructionHelperPlugin plugin, Client client, ClientThread clientThread, ConstructionHelperConfig config)
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
			List<Widget> items = InventoryHelper.getInventoryItems(this.client, this.config.mode().getPlankId());

			if (items == null || items.isEmpty())
			{
				return false;
			}
			else if (items.size() < this.config.mode().getPlankCost())
			{
				return false;
			}
			else
			{
				QueryResults<GameObject> objectQueryResults = (new GameObjectQuery()).nameEquals(new String[] {"Portal"}).result(this.client);
				if (objectQueryResults != null && !objectQueryResults.isEmpty())
				{
					GameObject portalObject = objectQueryResults.first();
					return portalObject != null;
				}
				else
				{
					return false;
				}
			}
		}
	}

	public void onGameTick(GameTick event)
	{
		QueryResults<GameObject> objectQueryResults = (new GameObjectQuery()).nameEquals(new String[] {"Portal"}).result(this.client);
		if (objectQueryResults != null && !objectQueryResults.isEmpty())
		{
			GameObject portalObject = objectQueryResults.first();
			if (portalObject != null)
			{
				this.clientThread.invoke(() -> {
					this.client.invokeMenuAction("Build mode", "<col=ffff>Portal", portalObject.getId(), MenuAction.GAME_OBJECT_THIRD_OPTION.getId(), portalObject.getSceneMinLocation().getX(), portalObject.getSceneMinLocation().getY());
				});
			}
		}
	}
}
