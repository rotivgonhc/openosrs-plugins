package net.runelite.client.plugins.gildedaltar;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;

public class TaskSet
{
	public List<Task> taskList = new ArrayList();

	public TaskSet(Task... tasks)
	{
		this.taskList.addAll(Arrays.asList(tasks));
	}

	public void addAll(GildedAltarPlugin plugin, Client client, ClientThread clientThread, GildedAltarConfig config, List<Class<?>> taskClazzes)
	{
		Iterator var6 = taskClazzes.iterator();

		while (var6.hasNext())
		{
			Class taskClass = (Class) var6.next();

			try
			{
				Constructor ctor = taskClass.getDeclaredConstructor(GildedAltarPlugin.class, Client.class, ClientThread.class, GildedAltarConfig.class);
				ctor.setAccessible(true);
				this.taskList.add((Task) ctor.newInstance(plugin, client, clientThread, config));
			}
			catch (Exception var9)
			{
				var9.printStackTrace();
			}
		}

	}

	public void clear()
	{
		this.taskList.clear();
	}

	public Task getValidTask()
	{
		Iterator var1 = this.taskList.iterator();

		Task task;
		do
		{
			if (!var1.hasNext())
			{
				return null;
			}

			task = (Task) var1.next();
		}
		while (!task.validate());

		return task;
	}
}
