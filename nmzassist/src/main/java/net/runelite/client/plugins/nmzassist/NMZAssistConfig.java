package net.runelite.client.plugins.nmzassist;

import net.runelite.client.config.Button;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("nmzassist")
public interface NMZAssistConfig extends Config
{
	@ConfigItem(
			keyName = "minPrayerLevel",
			name = "Minimum Prayer points to sip restore",
			description = "",
			position = 1
	)
	default int minPrayerLevel()
	{
		return 5;
	}

	@ConfigItem(
			keyName = "maxPrayerLevel",
			name = "Maximum Prayer points to sip restore",
			description = "",
			position = 2
	)
	default int maxPrayerLevel()
	{
		return 30;
	}

	@ConfigItem(keyName = "startButton",
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
