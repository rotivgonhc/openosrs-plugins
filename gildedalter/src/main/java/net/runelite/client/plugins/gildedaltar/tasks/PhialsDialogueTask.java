package net.runelite.client.plugins.gildedaltar.tasks;

import java.util.Arrays;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.QueryResults;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.gildedaltar.GildedAltarConfig;
import net.runelite.client.plugins.gildedaltar.GildedAltarPlugin;
import net.runelite.client.plugins.gildedaltar.MiscUtils;
import net.runelite.client.plugins.gildedaltar.Task;

public class PhialsDialogueTask extends Task
{
	public PhialsDialogueTask(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 0;
	}

	public boolean validate()
	{
		if (MiscUtils.isInPOH(this.client))
		{
			return false;
		}
		else
		{
			QueryResults<NPC> results = (new NPCQuery()).idEquals(new int[] {1614}).result(this.client);
			if (results != null && !results.isEmpty())
			{
				Widget dialogueWidget = this.client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS);
				if (dialogueWidget == null)
				{
					return false;
				}
				else
				{
					Widget[] children = dialogueWidget.getChildren();
					if (children == null)
					{
						return false;
					}
					else
					{
						return !Arrays.stream(children).noneMatch((w) -> {
							return w.getText().contains("Exchange All");
						});
					}
				}
			}
			else
			{
				return false;
			}
		}
	}

	public void onGameTick(GameTick event)
	{
		QueryResults<NPC> results = (new NPCQuery()).idEquals(new int[] {1614}).result(this.client);
		if (results != null && !results.isEmpty())
		{
			Widget dialogueWidget = this.client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS);
			if (dialogueWidget != null)
			{
				Widget[] children = dialogueWidget.getChildren();
				if (children != null && Arrays.stream(children).anyMatch((w) -> w.getText().contains("Exchange All")))
				{
					int index = 0;
					for (int i = 0; i < children.length; i++)
					{
						if (children[i].getText().contains("Exchange All"))
						{
							index = i;
							break;
						}
					}
					int finalIndex = index;
					this.clientThread.invoke(() -> {
						this.client.invokeMenuAction("Continue", "", 0, MenuAction.WIDGET_CONTINUE.getId(), children[finalIndex].getIndex(), children[finalIndex].getId());
					});
				}

			}
		}
	}
}