package net.runelite.client.plugins.nmzassist;

import net.runelite.api.Client;

import java.util.Arrays;

public class NMZUtils
{
	private static final int[] NMZ_MAP_REGION = {9033};

	public static boolean isInNightmareZone(Client client)
	{
		if (client.getLocalPlayer() == null)
		{
			return false;
		}

		// NMZ and the KBD lair uses the same region ID but NMZ uses planes 1-3 and KBD uses plane 0
		return client.getLocalPlayer().getWorldLocation().getPlane() > 0 && Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
	}
}
