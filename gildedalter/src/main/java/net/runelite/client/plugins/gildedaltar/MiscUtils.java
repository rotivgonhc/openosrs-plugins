package net.runelite.client.plugins.gildedaltar;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.queries.GameObjectQuery;

public class MiscUtils
{
	private static final List<Integer> regions = Arrays.asList(7513, 7514, 7769, 7770);

	public MiscUtils()
	{
	}

	public static boolean isInPOH(Client client)
	{
		IntStream var10000 = Arrays.stream(client.getMapRegions());
		List var10001 = regions;
		Objects.requireNonNull(var10001);
		return var10000.anyMatch(var10001::contains);
	}

	@Nullable
	public static GameObject findNearestGameObject(Client client, String name)
	{
		assert client.isClientThread();

		return client.getLocalPlayer() == null ? null : (new GameObjectQuery()).filter((o) -> o.getName().contains(name)).result(client).nearestTo(client.getLocalPlayer());
	}

	public static int getNotedBoneId(GildedAltarConfig config)
	{
		if (config.boneId() == ItemID.WYVERN_BONES)
		{
			return config.boneId() + 4;
		}
		else
		{
			return config.boneId() + 1;
		}
	}
}
