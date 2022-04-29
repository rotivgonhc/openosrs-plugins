package net.runelite.client.plugins.constructionhelper.Tasks;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.QueryResults;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperConfig;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperPlugin;
import net.runelite.client.plugins.constructionhelper.MiscUtils;
import net.runelite.client.plugins.constructionhelper.Task;

public class RemoveObjectTask extends Task
{
	public RemoveObjectTask(ConstructionHelperPlugin plugin, Client client, ClientThread clientThread, ConstructionHelperConfig config)
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
			QueryResults<GameObject> gameObjects = (new GameObjectQuery()).idEquals(new int[] {this.config.mode().getObjectId()}).result(this.client);
			return !gameObjects.isEmpty();
		}
	}

	public void onGameTick(GameTick event)
	{
		QueryResults<GameObject> gameObjects = (new GameObjectQuery()).idEquals(new int[] {this.config.mode().getObjectId()}).result(this.client);
		if (gameObjects != null && !gameObjects.isEmpty())
		{
			GameObject builtObject = gameObjects.first();
			if (builtObject != null)
			{
				this.clientThread.invoke(() -> {
					this.client.invokeMenuAction("Remove", "", this.config.mode().getObjectId(), MenuAction.GAME_OBJECT_FIFTH_OPTION.getId(), builtObject.getSceneMinLocation().getX(), builtObject.getSceneMinLocation().getY());
				});
			}
		}
	}
}
