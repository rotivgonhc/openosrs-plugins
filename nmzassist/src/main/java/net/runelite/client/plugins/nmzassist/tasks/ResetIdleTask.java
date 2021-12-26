package net.runelite.client.plugins.nmzassist.tasks;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.nmzassist.NMZAssistConfig;
import net.runelite.client.plugins.nmzassist.NMZAssistPlugin;
import net.runelite.client.plugins.nmzassist.Task;

import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.Executors;

public class ResetIdleTask extends Task
{

	protected Random random;
	protected long randomDelay;

	public ResetIdleTask(NMZAssistPlugin plugin, Client client, ClientThread clientThread, NMZAssistConfig config)
	{
		super(plugin, client, clientThread, config);
		this.random = new Random();
		this.randomDelay = randomDelay();
	}

	@Override
	public boolean validate()
	{
		return getIdleTicks();
	}

	@Override
	public String getTaskDescription()
	{
		return "Resetting idle timer";
	}

	@Override
	public void onGameTick(GameTick gameTick)
	{
		if (getIdleTicks())
		{
			this.randomDelay = randomDelay();
			Executors.newSingleThreadExecutor()
					.submit(this::pressKey);
			client.setKeyboardIdleTicks(0);
			client.setMouseIdleTicks(0);
		}
	}

	private void pressKey()
	{
		KeyEvent keyPress = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
		this.client.getCanvas().dispatchEvent(keyPress);
		KeyEvent keyRelease = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
		this.client.getCanvas().dispatchEvent(keyRelease);
		KeyEvent keyTyped = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
		this.client.getCanvas().dispatchEvent(keyTyped);
	}

	private long randomDelay()
	{
		return (long) clamp(
				Math.round(random.nextGaussian() * 8000)
		);
	}

	private static double clamp(double val)
	{
		return Math.max(1, Math.min(13000, val));
	}

	private boolean getIdleTicks()
	{
		int idleClientTicks = client.getKeyboardIdleTicks();

		if (client.getMouseIdleTicks() > idleClientTicks)
		{
			idleClientTicks = client.getMouseIdleTicks();
		}

		return idleClientTicks >= randomDelay;
	}
}