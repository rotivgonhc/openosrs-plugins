package net.runelite.client.plugins.gildedaltar;

import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;

public abstract class Task
{
	@Inject
	public GildedAltarPlugin plugin;
	@Inject
	public Client client;
	@Inject
	public ClientThread clientThread;
	@Inject
	public GildedAltarConfig config;

	public Task(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config)
	{
		this.plugin = plugin;
		this.client = client;
		this.clientThread = clientThread;
		this.config = config;
	}

	public abstract int getDelay();

	public abstract boolean validate();

	public String getTaskDescription()
	{
		return this.getClass().getSimpleName();
	}

	public void onGameTick(GameTick event)
	{
	}
}
