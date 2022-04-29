package net.runelite.client.plugins.constructionhelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ReflectBreakHandler
{
	private static final Logger log = LoggerFactory.getLogger(ReflectBreakHandler.class);
	@Inject
	private PluginManager pluginManager;
	private Object instance = null;
	private HashMap<String, Method> chinMethods = null;
	private boolean chinBreakHandlerInstalled = true;

	public ReflectBreakHandler()
	{
	}

	public void registerPlugin(Plugin p, boolean configure)
	{
		this.performReflection("registerPlugin2", p, configure);
	}

	public void registerPlugin(Plugin p)
	{
		this.performReflection("registerPlugin1", p);
	}

	public void unregisterPlugin(Plugin p)
	{
		this.performReflection("unregisterPlugin1", p);
	}

	public void startPlugin(Plugin p)
	{
		this.performReflection("startPlugin1", p);
	}

	public void stopPlugin(Plugin p)
	{
		this.performReflection("stopPlugin1", p);
	}

	public boolean isBreakActive(Plugin p)
	{
		Object o = this.performReflection("isBreakActive1", p);
		return o != null ? (Boolean) o : false;
	}

	public boolean shouldBreak(Plugin p)
	{
		Object o = this.performReflection("shouldBreak1", p);
		return o != null ? (Boolean) o : false;
	}

	public void startBreak(Plugin p)
	{
		this.performReflection("startBreak1", p);
	}

	private Object performReflection(String methodName, Object... args)
	{
		if (this.checkReflection() && this.chinMethods.containsKey(methodName = methodName.toLowerCase()))
		{
			try
			{
				return ((Method) this.chinMethods.get(methodName)).invoke(this.instance, args);
			}
			catch (InvocationTargetException | IllegalAccessException var4)
			{
				var4.printStackTrace();
			}
		}

		return null;
	}

	private boolean checkReflection()
	{
		if (!this.chinBreakHandlerInstalled)
		{
			return false;
		}
		else if (this.chinMethods != null && this.instance != null)
		{
			return true;
		}
		else
		{
			this.chinMethods = new HashMap();
			Iterator var1 = this.pluginManager.getPlugins().iterator();

			while (true)
			{
				Plugin p;
				do
				{
					if (!var1.hasNext())
					{
						this.chinBreakHandlerInstalled = false;
						return false;
					}

					p = (Plugin) var1.next();
				} while (!p.getClass().getSimpleName().toLowerCase().equals("chinbreakhandlerplugin"));

				Field[] var3 = p.getClass().getDeclaredFields();
				int var4 = var3.length;

				for (int var5 = 0; var5 < var4; ++var5)
				{
					Field f = var3[var5];
					if (f.getName().toLowerCase().equals("chinbreakhandler"))
					{
						f.setAccessible(true);

						try
						{
							this.instance = f.get(p);
							Method[] var7 = this.instance.getClass().getDeclaredMethods();
							int var8 = var7.length;

							for (int var9 = 0; var9 < var8; ++var9)
							{
								Method m = var7[var9];
								m.setAccessible(true);
								this.chinMethods.put(m.getName().toLowerCase() + m.getParameterCount(), m);
							}

							return true;
						}
						catch (IllegalAccessException var11)
						{
							var11.printStackTrace();
							return false;
						}
					}
				}
			}
		}
	}
}