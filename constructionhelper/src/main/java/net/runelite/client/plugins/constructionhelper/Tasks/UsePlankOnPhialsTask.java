package net.runelite.client.plugins.constructionhelper.Tasks;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.QueryResults;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.constructionhelper.*;

import java.util.List;

public class UsePlankOnPhialsTask extends Task
{
	public UsePlankOnPhialsTask(ConstructionHelperPlugin plugin, Client client, ClientThread clientThread, ConstructionHelperConfig config)
	{
		super(plugin, client, clientThread, config);
	}

	public int getDelay()
	{
		return 4;
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

				List<Widget> items = InventoryHelper.getInventoryItems(this.client, this.config.mode().getPlankId());

				if (items == null)
				{
					return false;
				}
				else if (items.size() >= this.config.mode().getPlankCost())
				{
					return false;
				}
				else
				{
					List<Widget> notedPlanksQueryResults = InventoryHelper.getInventoryItems(this.client, (this.config.mode().getPlankId() + 1));
					if (notedPlanksQueryResults != null && !notedPlanksQueryResults.isEmpty())
					{
						Widget dialogueWidget = this.client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS);
						return dialogueWidget == null;
					}
					else
					{
						return false;
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
			NPC phials = results.first();
			if (phials != null)
			{
				List<Widget> notedPlanksQueryResults = InventoryHelper.getInventoryItems(this.client, (this.config.mode().getPlankId() + 1));
				if (notedPlanksQueryResults != null && !notedPlanksQueryResults.isEmpty())
				{
					Widget notedPlanks = notedPlanksQueryResults.get(0);
					if (notedPlanks != null)
					{
						this.clientThread.invoke(() -> {
							this.client.setSelectedSpellWidget(WidgetInfo.INVENTORY.getId());
							this.client.setSelectedSpellChildIndex(notedPlanks.getIndex());
							this.client.setSelectedSpellItemId(notedPlanks.getItemId());
							this.client.invokeMenuAction("Use", "<col=00ffff>Oak plank -> Phials", phials.getIndex(), MenuAction.WIDGET_TARGET_ON_NPC.getId(), 0, 0);
						});
					}
				}
			}
		}
	}
}
