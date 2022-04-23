package net.runelite.client.plugins.gildedaltar;

import net.runelite.client.config.Button;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("gildedaltar")
public interface GildedAltarConfig extends Config
{
	@ConfigItem(
			keyName = "hostName",
			name = "Host Name",
			description = "The username of the host",
			position = 1
	)
	default String hostName()
	{
		return "zezima";
	}

	@ConfigItem(
			keyName = "boneId",
			name = "Bone Item ID",
			description = "",
			position = 2
	)
	default int boneId()
	{
		return 536;
	}

	@ConfigItem(
			keyName = "minWaitTicks",
			name = "Min Wait Ticks",
			description = "Minimum EXTRA ticks to wait after click. Delays are already built in.",
			position = 3
	)
	default int minWaitTicks()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "maxWaitTicks",
			name = "Max Wait Ticks",
			description = "Maximum EXTRA ticks to wait after click. Delays are already built in.",
			position = 4
	)
	default int maxWaitTicks()
	{
		return 3;
	}

	@ConfigItem(
			keyName = "startButton",
			name = "Start",
			description = "",
			position = 12
	)
	default Button startButton()
	{
		return new Button();
	}

	@ConfigItem(
			keyName = "stopButton",
			name = "Stop",
			description = "",
			position = 13
	)
	default Button stopButton()
	{
		return new Button();
	}
}