package net.runelite.client.plugins.constructionhelper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import net.runelite.api.Client;

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
}