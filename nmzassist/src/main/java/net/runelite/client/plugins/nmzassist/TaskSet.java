package net.runelite.client.plugins.nmzassist;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;

public class TaskSet
{
	public List<Task> taskList = new ArrayList<>();

	public TaskSet(Task... tasks)
	{
		taskList.addAll(Arrays.asList(tasks));
	}

	public void addAll(NMZAssistPlugin plugin, Client client, ClientThread clientThread, NMZAssistConfig config, List<Class<?>> taskClasses)
	{
		for (Class<?> taskClass : taskClasses)
		{
			try
			{
				Constructor<?> ctor = taskClass.getDeclaredConstructor(NMZAssistPlugin.class, Client.class, ClientThread.class, NMZAssistConfig.class);
				ctor.setAccessible(true);
				taskList.add((Task) ctor.newInstance(plugin, client, clientThread, config));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void clear()
	{
		taskList.clear();
	}

	/**
	 * Iterates through all the tasks in the set and returns
	 * the highest priority valid task.
	 *
	 * @return The first valid task from the task list or null if no valid task.
	 */
	public Task getValidTask()
	{
		for (Task task : this.taskList)
		{
			if (task.validate())
			{
				return task;
			}
		}
		return null;
	}
}