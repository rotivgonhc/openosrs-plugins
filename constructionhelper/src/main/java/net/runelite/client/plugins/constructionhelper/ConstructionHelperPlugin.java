package net.runelite.client.plugins.constructionhelper;

import com.google.inject.Provides;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.constructionhelper.Tasks.BreakTask;
import net.runelite.client.plugins.constructionhelper.Tasks.BuildObjectTask;
import net.runelite.client.plugins.constructionhelper.Tasks.CraftObjectTask;
import net.runelite.client.plugins.constructionhelper.Tasks.EnterPOHTask;
import net.runelite.client.plugins.constructionhelper.Tasks.LeavePOHTask;
import net.runelite.client.plugins.constructionhelper.Tasks.PhialsDialogueTask;
import net.runelite.client.plugins.constructionhelper.Tasks.RemoveDialogueTask;
import net.runelite.client.plugins.constructionhelper.Tasks.RemoveObjectTask;
import net.runelite.client.plugins.constructionhelper.Tasks.ResetIdleTask;
import net.runelite.client.plugins.constructionhelper.Tasks.StopConditionTask;
import net.runelite.client.plugins.constructionhelper.Tasks.ToggleRunTask;
import net.runelite.client.plugins.constructionhelper.Tasks.UsePlankOnPhialsTask;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Singleton
@Extension
@PluginDescriptor(
		name = "Construction Helper",
		description = "Automated construction (via phials unnoting)",
		tags = {"construction", "helper", "ben93riggs", "con"},
		enabledByDefault = false
)
public class ConstructionHelperPlugin extends Plugin
{
	static List<Class<?>> taskClassList = new ArrayList();
	@Inject
	public Client client;
	@Inject
	public ConstructionHelperConfig config;
	@Inject
	public ClientThread clientThread;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ConstructionHelperOverlay overlay;
	@Inject
	private ChatMessageManager chatMessageManager;
	@Inject
	public ReflectBreakHandler chinBreakHandler;
	boolean pluginStarted = false;
	public String status = "initializing...";
	private final TaskSet tasks = new TaskSet(new Task[0]);
	public int delay = 0;

	public ConstructionHelperPlugin()
	{
	}

	@Provides
	ConstructionHelperConfig provideConfig(ConfigManager configManager)
	{
		return (ConstructionHelperConfig) configManager.getConfig(ConstructionHelperConfig.class);
	}

	protected void startUp() throws Exception
	{
		this.pluginStarted = false;
		this.chinBreakHandler.registerPlugin(this);
		this.status = "initializing...";
		this.overlayManager.add(this.overlay);
		this.tasks.clear();
		this.tasks.addAll(this, this.client, this.clientThread, this.config, taskClassList);
	}

	protected void shutDown() throws Exception
	{
		this.pluginStarted = false;
		this.chinBreakHandler.unregisterPlugin(this);
		this.overlayManager.remove(this.overlay);
		this.tasks.clear();
	}

	@Subscribe
	public void onConfigButtonClicked(ConfigButtonClicked event)
	{
		if (event.getGroup().equals(((ConfigGroup) ConstructionHelperConfig.class.getAnnotation(ConfigGroup.class)).value()))
		{
			if (event.getKey().equals("startButton"))
			{
				this.pluginStarted = true;
				this.chinBreakHandler.startPlugin(this);
			}
			else if (event.getKey().equals("stopButton"))
			{
				this.pluginStarted = false;
				this.chinBreakHandler.stopPlugin(this);
			}

		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (this.pluginStarted)
		{
			if (!this.chinBreakHandler.isBreakActive(this))
			{
				if (this.client.getGameState() == GameState.LOGGED_IN)
				{
					if (this.delay > 0)
					{
						--this.delay;
					}
					else
					{
						Task task = this.tasks.getValidTask();
						if (task != null)
						{
							this.status = task.getTaskDescription();
							task.onGameTick(event);
							this.delay = task.getDelay() + this.getRandomWait();
						}

					}
				}
			}
		}
	}

	public int getRandomWait()
	{
		return (int) (Math.random() * (double) (this.config.maxWaitTicks() - this.config.minWaitTicks()) + (double) this.config.minWaitTicks());
	}

	private void sendGameMessage(String message)
	{
		this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage((new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append(message).build()).build());
	}

	public void stopPlugin()
	{
		this.stopPlugin("");
	}

	public void stopPlugin(String reason)
	{
		this.pluginStarted = false;
		this.chinBreakHandler.stopPlugin(this);
		if (reason != null && !reason.isEmpty())
		{
			this.sendGameMessage("ConstructionHelper Stopped: " + reason);
		}

	}

	static
	{
		taskClassList.add(ResetIdleTask.class);
		taskClassList.add(BreakTask.class);
		taskClassList.add(StopConditionTask.class);
		taskClassList.add(ToggleRunTask.class);
		taskClassList.add(RemoveDialogueTask.class);
		taskClassList.add(CraftObjectTask.class);
		taskClassList.add(RemoveObjectTask.class);
		taskClassList.add(BuildObjectTask.class);
		taskClassList.add(LeavePOHTask.class);
		taskClassList.add(UsePlankOnPhialsTask.class);
		taskClassList.add(EnterPOHTask.class);
		taskClassList.add(PhialsDialogueTask.class);
	}
}
