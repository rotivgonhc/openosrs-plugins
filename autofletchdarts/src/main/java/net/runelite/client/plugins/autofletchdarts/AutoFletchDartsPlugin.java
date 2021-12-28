package net.runelite.client.plugins.autofletchdarts;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
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
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Extension
@PluginDescriptor(
		name = "Auto Fletch Darts",
		description = "Automatically fletches the dart tips in your inventory",
		tags = {"fletch", "darts"},
		enabledByDefault = false
)
public class AutoFletchDartsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private AutoFletchDartsConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AutoFletchDartsOverlay overlay;

	@Inject
	public ReflectBreakHandler chinBreakHandler;

	@Inject
	private ChatMessageManager chatMessageManager;

	boolean pluginStarted = false;

	@Provides
	AutoFletchDartsConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(AutoFletchDartsConfig.class);
	}

	public String status = "initializing...";


	@Override
	protected void startUp() throws Exception
	{
		chinBreakHandler.registerPlugin(this, false);
		overlayManager.add(overlay);
		status = "initializing...";
	}

	@Override
	protected void shutDown() throws Exception
	{
		chinBreakHandler.unregisterPlugin(this);
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onConfigButtonClicked(ConfigButtonClicked event)
	{
		if (!event.getGroup().equals(AutoFletchDartsConfig.class.getAnnotation(ConfigGroup.class).value()))
		{
			return;
		}

		if (event.getKey().equals("startButton"))
		{
			chinBreakHandler.startPlugin(this);
			pluginStarted = true;
		}
		else if (event.getKey().equals("stopButton"))
		{
			chinBreakHandler.stopPlugin(this);
			pluginStarted = false;
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("autofletchdarts"))
		{
			return;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!pluginStarted || chinBreakHandler.isBreakActive(this))
		{
			return;
		}

		if (chinBreakHandler.shouldBreak(this))
		{
			status = "Taking a break";
			chinBreakHandler.startBreak(this);
		}

		if (client.getGameState() != GameState.LOGGED_IN)
		{
			pluginStarted = false;
			return;
		}

		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

		if (inventoryWidget == null)
		{
			stopPlugin("Did not find components to create darts in inventory");
			return;
		}

		List<WidgetItem> dartTips = getDartTips();
		if (dartTips.isEmpty() || dartTips == null)
		{
			stopPlugin("No dart tips in inventory");
			return;
		}

		List<WidgetItem> feathers = getFeathers();
		if (feathers.isEmpty() || feathers == null)
		{
			stopPlugin("No feathers in inventory");
			return;
		}
		WidgetItem dartTip = dartTips.get(0);
		WidgetItem feather = feathers.get(0);

		if (feather == null || dartTip == null)
		{
			stopPlugin("No feathers or dart tips were found");
			return;
		}

		clientThread.invoke(() -> {
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(dartTip.getIndex());
			client.setSelectedItemID(dartTip.getId());
			status = "Fletching darts...";
			client.invokeMenuAction(
					"Use",
					"<col=ff9040>dart tip -> Feather",
					feather.getId(),
					MenuAction.ITEM_USE_ON_WIDGET_ITEM.getId(),
					feather.getIndex(),
					WidgetInfo.INVENTORY.getId()
			);
		});
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
		chinBreakHandler.stopPlugin(this);
		pluginStarted = false;

		if (reason != null && !reason.isEmpty())
		{
			sendGameMessage("Auto Fletch Darts Stopped: " + reason);
		}
	}

	private List<WidgetItem> getDartTips()
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

		if (inventoryWidget == null)
		{
			return null;
		}
		return inventoryWidget.getWidgetItems()
				.stream()
				.filter(item -> Arrays.asList(ItemID.BRONZE_DART_TIP, ItemID.IRON_DART_TIP,
						ItemID.STEEL_DART_TIP, ItemID.MITHRIL_DART_TIP, ItemID.ADAMANT_DART_TIP,
						ItemID.RUNE_DART_TIP, ItemID.AMETHYST_DART_TIP, ItemID.DRAGON_DART_TIP).contains(item.getId()))
				.collect(Collectors.toList());
	}

	private List<WidgetItem> getFeathers()
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

		if (inventoryWidget == null)
		{
			return null;
		}
		return inventoryWidget.getWidgetItems()
				.stream()
				.filter(item -> Arrays.asList(ItemID.FEATHER).contains(item.getId()))
				.collect(Collectors.toList());
	}

}
