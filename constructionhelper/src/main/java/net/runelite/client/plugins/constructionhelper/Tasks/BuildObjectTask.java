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

public class BuildObjectTask extends Task
{
	public BuildObjectTask(ConstructionHelperPlugin plugin, Client client, ClientThread clientThread, ConstructionHelperConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 1;
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
				QueryResults<GameObject> gameObjects = (new GameObjectQuery()).idEquals(new int[] {this.config.mode().getObjectSpaceId()}).result(this.client);
				return gameObjects != null && !gameObjects.isEmpty();
			}
		}
	}

	public void onGameTick(GameTick event)
	{
		QueryResults<GameObject> gameObjects = (new GameObjectQuery()).idEquals(new int[] {this.config.mode().getObjectSpaceId()}).result(this.client);
		if (gameObjects != null && !gameObjects.isEmpty())
		{
			GameObject larderSpaceObject = gameObjects.first();
			if (larderSpaceObject != null)
			{
				this.clientThread.invoke(() -> {
					this.client.invokeMenuAction("Build", "", this.config.mode().getObjectSpaceId(), MenuAction.GAME_OBJECT_FIFTH_OPTION.getId(), larderSpaceObject.getSceneMinLocation().getX(), larderSpaceObject.getSceneMinLocation().getY());
				});
			}
		}
	}
}
