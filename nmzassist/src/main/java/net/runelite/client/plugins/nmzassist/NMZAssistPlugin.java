package net.runelite.client.plugins.nmzassist;

import com.google.inject.Provides;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.nmzassist.tasks.*;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
@Extension
@PluginDescriptor(
		name = "NMZ Assist",
		description = "Drinks potions in NMZ",
		tags = {"combat", "potion", "overload", "nmz", "nightmare", "zone", "assist"},
		enabledByDefault = false
)
public class NMZAssistPlugin extends Plugin
{
	static List<Class<?>> prayerPotionClassList = new ArrayList<>();

	static
	{
		prayerPotionClassList.add(DrinkOverloadTask.class);
		prayerPotionClassList.add(DrinkPrayerTask.class);
		prayerPotionClassList.add(ResetIdleTask.class);
	}

	static List<Class<?>> absorptionClassList = new ArrayList<>();

	static
	{
		absorptionClassList.add(DrinkOverloadTask.class);
		absorptionClassList.add(DrinkAbsorptionTask.class);
		absorptionClassList.add(DrinkPrayerTask.class);
		absorptionClassList.add(LowerHpTask.class);
		absorptionClassList.add(ResetIdleTask.class);
	}

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private NMZAssistConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private NMZAssistOverlay overlay;

	@Inject
	private ChatMessageManager chatMessageManager;

	boolean pluginStarted;
	int timeout;

	@Provides
	NMZAssistConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(NMZAssistConfig.class);
	}

	public String status = "initializing...";

	private final TaskSet tasks = new TaskSet();

	@Override
	protected void startUp() throws Exception
	{
		pluginStarted = false;
		overlayManager.add(overlay);
		status = "initializing...";
		configureTasks();
		timeout = 0;
	}

	private void configureTasks()
	{
		tasks.clear();
		if (this.config.mode() == NMZMode.PRAYER_POTIONS)
		{
			tasks.addAll(this, client, clientThread, config, prayerPotionClassList);
		}
		else if (this.config.mode() == NMZMode.ABSORPTIONS)
		{
			tasks.addAll(this, client, clientThread, config, absorptionClassList);
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		pluginStarted = false;
		overlayManager.remove(overlay);
		tasks.clear();
		timeout = 0;
	}

	@Subscribe
	public void onConfigButtonClicked(ConfigButtonClicked event)
	{
		if (!event.getGroup().equals(NMZAssistConfig.class.getAnnotation(ConfigGroup.class).value()))
		{
			return;
		}

		if (event.getKey().equals("startButton"))
		{
			pluginStarted = true;
			configureTasks();
			timeout = 0;
		}
		else if (event.getKey().equals("stopButton"))
		{
			pluginStarted = false;
			tasks.clear();
			timeout = 0;
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("nmzassist"))
		{
			return;
		}
		configureTasks();
		timeout = 0;
	}

	@Subscribe
	private void onChatMessage(ChatMessage event)
	{
		if (!pluginStarted)
		{
			return;
		}

		String msg = Text.removeTags(event.getMessage()); //remove color

		switch (event.getType())
		{
			case GAMEMESSAGE:
				if (msg.contains("This barrel is empty.")
						|| msg.contains("There is no ammo left in your quiver.")
						|| msg.contains("Your blowpipe has run out of scales and darts.")
						|| msg.contains("Your blowpipe has run out of darts.")
						|| msg.contains("Your blowpipe needs to be charged with Zulrah's scales."))
				{
					stopPlugin("Received game message: " + msg);
				}
				break;
			case SPAM:
				if (msg.contains("You drink some of your overload potion."))
				{
					timeout = 12 + (getRandomIntBetweenRange(1, 4));
				}
			default:
				break;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!pluginStarted)
		{
			return;
		}

		if (client.getGameState() != GameState.LOGGED_IN)
		{
			pluginStarted = false;
			return;
		}

		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

		if (inventoryWidget == null)
		{
			pluginStarted = false;
			return;
		}

		if (!NMZUtils.isInNightmareZone(client))
		{
			stopPlugin("You need to be in a Nightmare Zone instance");
			return;
		}

		if (timeout <= 0)
		{
			timeout = 0;
		}
		else
		{
			timeout--;
			return;
		}
		Task task = tasks.getValidTask();

		if (task != null)
		{
			status = task.getTaskDescription();
			task.onGameTick(event);
		}
		else
		{
			status = "Waiting to perform a task...";
		}
	}

	private void sendGameMessage(String message)
	{
		chatMessageManager
				.queue(QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(
								new ChatMessageBuilder()
										.append(ChatColorType.HIGHLIGHT)
										.append(message)
										.build())
						.build());
	}

	public void stopPlugin()
	{
		stopPlugin("");
	}

	public void stopPlugin(String reason)
	{
		pluginStarted = false;

		if (reason != null && !reason.isEmpty())
		{
			sendGameMessage("NMZAssist Stopped: " + reason);
		}
	}

	private int getRandomIntBetweenRange(int min, int max)
	{
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
}
