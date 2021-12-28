package net.runelite.client.plugins.autofletchdarts;

import net.runelite.client.config.*;

@ConfigGroup("autofletchdarts")
public interface AutoFletchDartsConfig extends Config
{
	@ConfigTitle(
			position = 0,
			keyName = "controlsSection",
			name = "Controls",
			description = ""
	)
	String controlsSection = "controlsSection";

	@ConfigItem(keyName = "startButton",
			name = "Start",
			description = "",
			position = 0,
			title = "controlsSection"
	)
	default Button startButton()
	{
		return new Button();
	}

	@ConfigItem(keyName = "stopButton",
			name = "Stop",
			description = "",
			position = 1,
			title = "controlsSection"
	)
	default Button stopButton()
	{
		return new Button();
	}

}
