package net.runelite.client.plugins.constructionhelper;

import net.runelite.client.config.Button;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("constructionhelper")
public interface ConstructionHelperConfig extends Config
{
	@ConfigItem(
			keyName = "mode",
			name = "Mode",
			description = "",
			position = 0
	)
	default CHMode mode()
	{
		return CHMode.OAK_LARDERS;
	}

	@ConfigItem(
			keyName = "minWaitTicks",
			name = "Min Wait Ticks",
			description = "Minimum EXTRA ticks to wait after click. Delays are already built in.",
			position = 1
	)
	default int minWaitTicks()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "maxWaitTicks",
			name = "Max Wait Ticks",
			description = "Maximum EXTRA ticks to wait after click. Delays are already built in.",
			position = 2
	)
	default int maxWaitTicks()
	{
		return 3;
	}

	@ConfigItem(
			keyName = "startButton",
			name = "Start",
			description = "",
			position = 3
	)
	default Button startButton()
	{
		return new Button();
	}

	@ConfigItem(
			keyName = "stopButton",
			name = "Stop",
			description = "",
			position = 4
	)
	default Button stopButton()
	{
		return new Button();
	}
}
