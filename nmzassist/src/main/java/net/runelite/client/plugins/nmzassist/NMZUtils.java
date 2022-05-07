package net.runelite.client.plugins.nmzassist;

import net.runelite.api.Client;
import net.runelite.api.ItemID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class NMZUtils
{
	private static final int[] NMZ_MAP_REGION = {9033};
	public static final ArrayList<Integer> OVERLOADS = new ArrayList<>()
	{
		{
			add(ItemID.OVERLOAD_1);
			add(ItemID.OVERLOAD_2);
			add(ItemID.OVERLOAD_3);
			add(ItemID.OVERLOAD_4);
		}
	};
	public static final ArrayList<Integer> ABSORPTIONS = new ArrayList<>()
	{
		{
			add(ItemID.ABSORPTION_1);
			add(ItemID.ABSORPTION_2);
			add(ItemID.ABSORPTION_3);
			add(ItemID.ABSORPTION_4);
		}
	};
	public static final ArrayList<Integer> PRAYER_POTIONS = new ArrayList<>()
	{
		{
			add(ItemID.PRAYER_POTION1);
			add(ItemID.PRAYER_POTION2);
			add(ItemID.PRAYER_POTION3);
			add(ItemID.PRAYER_POTION4);
		}
	};
	public static final ArrayList<Integer> LOWER_HP = new ArrayList<>()
	{
		{
			add(ItemID.DWARVEN_ROCK_CAKE_7510);
			add(ItemID.LOCATOR_ORB);
		}
	};

	public static boolean isInNightmareZone(Client client)
	{
		if (client.getLocalPlayer() == null)
		{
			return false;
		}

		// NMZ and the KBD lair uses the same region ID but NMZ uses planes 1-3 and KBD uses plane 0
		return client.getLocalPlayer().getWorldLocation().getPlane() > 0 && Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
	}

	public static int getRandomIntBetweenRange(int min, int max)
	{
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

}
