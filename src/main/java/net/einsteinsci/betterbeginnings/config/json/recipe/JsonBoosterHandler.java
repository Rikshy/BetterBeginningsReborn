package net.einsteinsci.betterbeginnings.config.json.recipe;

import java.util.ArrayList;
import java.util.List;

public class JsonBoosterHandler
{
	private List<JsonBooster> boosters = new ArrayList<>();

	private List<String> includes = new ArrayList<>();
	private List<String> modDependencies = new ArrayList<>();
	
	private int version = 0;

	public List<JsonBooster> getBoosters()
	{
		return boosters;
	}

	public List<String> getIncludes()
	{
		return includes;
	}

	public List<String> getModDependencies()
	{
		return modDependencies;
	}

	public int getActualVersion() 
	{
		return version;
	}
	
	public void updateVersion(int newVersion)
	{
		version = newVersion;
	}
}
