package net.runelite.client.plugins.constructionhelper.Tasks;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperConfig;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperPlugin;
import net.runelite.client.plugins.constructionhelper.MiscUtils;
import net.runelite.client.plugins.constructionhelper.Task;

public class CraftObjectTask extends Task
{
	public CraftObjectTask(ConstructionHelperPlugin plugin, Client client, ClientThread clientThread, ConstructionHelperConfig config)
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
			Widget craftObjectWidget = this.client.getWidget(this.config.mode().getWidget().getGroupId(), this.config.mode().getWidget().getChildId());
			return craftObjectWidget != null;
		}
	}

	public void onGameTick(GameTick event)
	{
		Widget craftObjectWidget = this.client.getWidget(this.config.mode().getWidget().getGroupId(), this.config.mode().getWidget().getChildId());
		if (craftObjectWidget != null)
		{
			this.clientThread.invoke(() -> {
				this.client.invokeMenuAction("Continue", "", 1, MenuAction.CC_OP.getId(), craftObjectWidget.getIndex(), craftObjectWidget.getId());
			});
		}

	}
}
