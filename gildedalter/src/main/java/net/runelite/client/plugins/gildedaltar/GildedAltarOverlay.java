package net.runelite.client.plugins.gildedaltar;

import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;

import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class GildedAltarOverlay extends Overlay
{
	private final GildedAltarPlugin plugin;
	private final PanelComponent panelComponent = new PanelComponent();

	@Inject
	public GildedAltarOverlay(GildedAltarPlugin plugin)
	{
		this.plugin = plugin;
		this.setPriority(OverlayPriority.HIGHEST);
		this.setPosition(OverlayPosition.BOTTOM_LEFT);
		this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Gilded Altar Overlay"));
	}

	public Dimension render(Graphics2D graphics)
	{
		if (this.plugin == null)
		{
			return null;
		}
		else if (!this.plugin.pluginStarted)
		{
			return null;
		}
		else
		{
			this.panelComponent.getChildren().clear();
			TableComponent tableComponent = new TableComponent();
			tableComponent.setColumnAlignments(new TableAlignment[] {TableAlignment.LEFT});
			tableComponent.setDefaultColor(Color.ORANGE);
			tableComponent.addRow(new String[] {"Gilded Altar"});
			tableComponent.addRow(new String[] {this.plugin.status});
			tableComponent.addRow(new String[] {"Delay: " + this.plugin.delay});
			if (!tableComponent.isEmpty())
			{
				this.panelComponent.getChildren().add(tableComponent);
			}

			this.panelComponent.setPreferredSize(new Dimension(175, 100));
			this.panelComponent.setBackgroundColor(Color.BLACK);
			return this.panelComponent.render(graphics);
		}
	}
}