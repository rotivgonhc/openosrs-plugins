package net.runelite.client.plugins.gildedaltar.tasks;

import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.gildedaltar.*;

import java.util.List;

public class UseBonesOnPhialsTask extends Task
{
	public UseBonesOnPhialsTask(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
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
				List<Widget> items = InventoryHelper.getInventoryItems(this.client, this.config.boneId());

				if (items != null && !items.isEmpty())
				{
					return false;
				}
				else
				{
					List<Widget> notedItems = InventoryHelper.getInventoryItems(this.client, MiscUtils.getNotedBoneId(config));
					if (notedItems != null && !notedItems.isEmpty())
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
				List<Widget> notedItems = InventoryHelper.getInventoryItems(this.client, MiscUtils.getNotedBoneId(config));
				if (notedItems != null && !notedItems.isEmpty())
				{
					Widget notedBones = notedItems.get(0);
					if (notedBones != null)
					{
						this.clientThread.invoke(() -> {
							this.client.setSelectedSpellWidget(WidgetInfo.INVENTORY.getId());
							this.client.setSelectedSpellChildIndex(notedBones.getIndex());
							this.client.setSelectedSpellItemId(notedBones.getItemId());
							this.client.invokeMenuAction("Use", "<col=00ffff>Bones -> Phials", phials.getIndex(), MenuAction.WIDGET_TARGET_ON_NPC.getId(), 0, 0);
						});
					}
				}
			}
		}
	}
}