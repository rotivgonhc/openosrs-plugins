package net.runelite.client.plugins.constructionhelper;

public enum CHMode
{
	CRUDE_WOODEN_CHAIR("Crude Wooden Chair", 960, 6752, 4516, 2, CHWidget.CRUDE_WOODEN_CHAIR, new int[]{1539}),
	WOODEN_CHAIR("Wooden Chair", 960, 6753, 4515, 3, CHWidget.WOODEN_CHAIR, new int[]{1539}),
	OAK_TABLE("Oak Table", 8778, 13294, 15298, 4, CHWidget.OAK_TABLE, new int[0]),
	CARVED_OAK_TABLE("Carved Oak Table", 8778, 13295, 15298, 6, CHWidget.CARVED_OAK_TABLE, new int[0]),
	OAK_LARDERS("Oak Larder", 8778, 13566, 15403, 8, CHWidget.OAK_LARDER, new int[0]),
	MAHOGANY_TABLE("Mahogany Table", 8782, 13298, 15298, 6, CHWidget.MAHOGANY_TABLE, new int[0]),
	MYTHICAL_CAPE("Mythical Cape", 8780, 31986, 15394, 3, CHWidget.MYTHICAL_CAPE, new int[]{22114});

	private final String name;
	private final int plankId;
	private final int objectId;
	private final int objectSpaceId;
	private final int plankCost;
	private final CHWidget widget;
	private final int[] otherReqs;

	private CHMode(String name, int plankId, int objectId, int objectSpaceId, int plankCost, CHWidget widget, int... otherReqs)
	{
		this.name = name;
		this.plankId = plankId;
		this.objectId = objectId;
		this.objectSpaceId = objectSpaceId;
		this.plankCost = plankCost;
		this.widget = widget;
		this.otherReqs = otherReqs;
	}

	public String getName()
	{
		return this.name;
	}

	public int getPlankId()
	{
		return this.plankId;
	}

	public int getObjectId()
	{
		return this.objectId;
	}

	public int getObjectSpaceId()
	{
		return this.objectSpaceId;
	}

	public int getPlankCost()
	{
		return this.plankCost;
	}

	public CHWidget getWidget()
	{
		return this.widget;
	}

	public String toString()
	{
		return this.name;
	}

	public int[] getOtherReqs()
	{
		return this.otherReqs;
	}
}
