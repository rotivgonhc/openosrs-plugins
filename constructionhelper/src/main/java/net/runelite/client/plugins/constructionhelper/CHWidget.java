package net.runelite.client.plugins.constructionhelper;

public enum CHWidget
{
	CRUDE_WOODEN_CHAIR(458, 4),
	WOODEN_CHAIR(458, 5),
	OAK_LARDER(458, 5),
	OAK_TABLE(458, 5),
	CARVED_OAK_TABLE(458, 6),
	MAHOGANY_TABLE(458, 9),
	MYTHICAL_CAPE(458, 7);

	private final int groupId;
	private final int childId;

	private CHWidget(int groupId, int childId)
	{
		this.groupId = groupId;
		this.childId = childId;
	}

	public int getGroupId()
	{
		return this.groupId;
	}

	public int getChildId()
	{
		return this.childId;
	}
}
