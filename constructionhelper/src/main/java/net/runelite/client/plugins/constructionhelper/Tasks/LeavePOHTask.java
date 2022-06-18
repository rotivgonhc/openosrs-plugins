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

public class LeavePOHTask extends Task
{
	public LeavePOHTask(ConstructionHelperPlugin plugin, Client client, ClientThread clientThread, ConstructionHelperConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 2;
	}

	public boolean validate()
	{
		if (!MiscUtils.isInPOH(this.client))
		{
			return false;
		}
		else
		{
			List<Widget> items = InventoryHelper.getInventoryItems(this.client, this.config.mode().getPlankId());

			if (items == null)
			{
				return false;
			}
			else
			{
				return (items.size() < this.config.mode().getPlankCost());
			}
		}
	}

	public void onGameTick(GameTick event)
	{
		QueryResults<GameObject> results = (new GameObjectQuery()).nameEquals(new String[] {"Portal"}).result(this.client);
		if (results != null && !results.isEmpty())
		{
			GameObject portalObject = results.first();
			if (portalObject != null)
			{
				this.clientThread.invoke(() -> {
					this.client.invokeMenuAction("Enter", "<col=ffff>Portal", portalObject.getId(), MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), portalObject.getSceneMinLocation().getX(), portalObject.getSceneMinLocation().getY());
				});
			}
		}
	}
}
