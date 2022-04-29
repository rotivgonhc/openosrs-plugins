package net.runelite.client.plugins.constructionhelper.Tasks;

import java.util.Arrays;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperConfig;
import net.runelite.client.plugins.constructionhelper.ConstructionHelperPlugin;
import net.runelite.client.plugins.constructionhelper.MiscUtils;
import net.runelite.client.plugins.constructionhelper.Task;

public class RemoveDialogueTask extends Task
{
	public RemoveDialogueTask(ConstructionHelperPlugin plugin, Client client, ClientThread clientThread, ConstructionHelperConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 1;
	}

	public boolean validate()
	{
		if (MiscUtils.isInPOH(this.client))
		{
			Widget remove_dialogue_widget = this.client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS);
			if (remove_dialogue_widget != null)
			{
				Widget[] children = remove_dialogue_widget.getChildren();
				if (children != null && Arrays.stream(children).anyMatch((w) -> w.getText().contains("Really remove it?")))
				{
					return true;
				}
			}

		}
		return false;
	}

	public void onGameTick(GameTick event)
	{
		Widget remove_dialogue_widget = this.client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS);
		if (remove_dialogue_widget != null)
		{
			Widget[] children = remove_dialogue_widget.getChildren();
			if (children != null && Arrays.stream(children).anyMatch((w) -> w.getText().contains("Really remove it?")))
			{
				this.clientThread.invoke(() -> {
					this.client.invokeMenuAction("", "", 0, MenuAction.WIDGET_CONTINUE.getId(), children[1].getIndex(), children[1].getId());
				});
			}
		}

	}
}
