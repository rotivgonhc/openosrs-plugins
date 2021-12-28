package net.runelite.client.plugins.autofletchdarts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;

import net.runelite.api.Client;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;

import net.runelite.client.ui.overlay.Overlay;

import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;
import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;

public class AutoFletchDartsOverlay extends Overlay
{

	private final Client client;
	private final AutoFletchDartsPlugin plugin;
	private final AutoFletchDartsConfig config;
	private final PanelComponent panelComponent = new PanelComponent();

	@Inject
	private AutoFletchDartsOverlay(Client client, AutoFletchDartsPlugin plugin, AutoFletchDartsConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		this.setPriority(OverlayPriority.HIGHEST);
		this.setPosition(OverlayPosition.BOTTOM_LEFT);
		this.getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Auto Fletch Darts Overlay"));
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (plugin == null)
		{
			return null;
		}

		if (!plugin.pluginStarted)
		{
			return null;
		}

		panelComponent.getChildren().clear();

		TableComponent tableComponent = new TableComponent();
		tableComponent.setColumnAlignments(TableAlignment.LEFT);
		tableComponent.setDefaultColor(Color.ORANGE);

		tableComponent.addRow("Auto Fletch Darts");
		tableComponent.addRow(plugin.status);

		if (!tableComponent.isEmpty())
		{
			panelComponent.getChildren().add(tableComponent);
		}

		panelComponent.setPreferredSize(new Dimension(175, 100));
		panelComponent.setBackgroundColor(Color.BLACK);

		return panelComponent.render(graphics);
	}
}
