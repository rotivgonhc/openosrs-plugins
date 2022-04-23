package net.runelite.client.plugins.gildedaltar.tasks;

import net.runelite.api.Client;
import net.runelite.api.VarClientInt;
import net.runelite.api.VarClientStr;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.gildedaltar.GildedAltarConfig;
import net.runelite.client.plugins.gildedaltar.GildedAltarPlugin;
import net.runelite.client.plugins.gildedaltar.MiscUtils;
import net.runelite.client.plugins.gildedaltar.Task;

public class EnterUsernameTask extends Task
{
	public EnterUsernameTask(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
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
			Widget widget = this.client.getWidget(WidgetInfo.CHATBOX_TITLE);
			if (widget != null && !widget.isHidden())
			{
				return widget.getText().equals("Enter name:");
			}
			else
			{
				return false;
			}
		}
	}

	public String getTaskDescription()
	{
		return "Enter host name";
	}

	public void onGameTick(GameTick event)
	{
		this.client.setVar(VarClientInt.INPUT_TYPE, 8);
		this.client.setVar(VarClientStr.INPUT_TEXT, String.valueOf(this.config.hostName()));
		this.client.runScript(new Object[] {681});
	}
}