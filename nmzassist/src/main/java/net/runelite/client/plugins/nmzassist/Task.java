package net.runelite.client.plugins.nmzassist;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;

import javax.inject.Inject;

public abstract class Task
{
	public Task(NMZAssistPlugin plugin, Client client, ClientThread clientThread, NMZAssistConfig config)
	{
		this.plugin = plugin;
		this.client = client;
		this.clientThread = clientThread;
		this.config = config;
	}

	@Inject
	public NMZAssistPlugin plugin;

	@Inject
	public Client client;

	@Inject
	public ClientThread clientThread;

	@Inject
	public NMZAssistConfig config;

	public abstract boolean validate();

	public String getTaskDescription()
	{
		return this.getClass().getSimpleName();
	}

	public void onGameTick(GameTick event)
	{
	}
}
