package net.einsteinsci.betterbeginnings.config.json.recipe;

import net.einsteinsci.betterbeginnings.config.BBConfigFolderLoader;
import net.einsteinsci.betterbeginnings.config.json.JsonLoadedItem;
import net.einsteinsci.betterbeginnings.config.json.JsonLoadedItemStack;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

public class JsonBrickOvenShapedRecipe
{
	private JsonLoadedItemStack output;
	private List<String> recipe = new ArrayList<>();
	private Map<Character, JsonLoadedItem> ingredients = new HashMap<>();

	public JsonBrickOvenShapedRecipe(ItemStack output, Object... params)
	{
		this.output = new JsonLoadedItemStack(output);

		char active = '\0';
		boolean recipeFinished = false;
		for (Object obj : params)
		{
			if (!recipeFinished)
			{
				if (obj instanceof String)
				{
					String str = (String)obj;
					recipe.add(str);
				}
				else if (obj instanceof Character)
				{
					active = (Character)obj;
					recipeFinished = true;
				}
				else
				{
					throw new IllegalArgumentException("Invalid type for first phase of recipe: " +
						obj.getClass().getName());
				}
			}
			else
			{
				if (obj instanceof Character)
				{
					char c = (Character)obj;

					if (c != ' ')
					{
						active = c;
					}
				}
				else if (obj instanceof ItemStack)
				{
					if (active == '\0')
					{
						continue;
					}

					ItemStack stack = (ItemStack)obj;
					JsonLoadedItem ing = new JsonLoadedItem(stack);
					ingredients.put(active, ing);
				}
				else if (obj instanceof Item)
				{
					if (active == '\0')
					{
						continue;
					}

					Item item = (Item)obj;
					JsonLoadedItem ing = new JsonLoadedItem(new ItemStack(item));
					ingredients.put(active, ing);
				}
				else if (obj instanceof Block)
				{
					if (active == '\0')
					{
						continue;
					}

					Block block = (Block)obj;
					JsonLoadedItem ing = new JsonLoadedItem(new ItemStack(block));
					ingredients.put(active, ing);
				}
				else if (obj instanceof String)
				{
					String ore = (String)obj;
					JsonLoadedItem ing = JsonLoadedItem.makeOreDictionary(ore);
					ingredients.put(active, ing);
				}
			}
		}
	}

	public JsonBrickOvenShapedRecipe(JsonLoadedItemStack output, List<String> recipe, Map<Character, JsonLoadedItem> ingredients)
	{
		this.output = output;
		this.recipe = recipe;
		this.ingredients = ingredients;
	}

	public void register()
	{
	    	if(!output.isValid())
	    	{
	    	    LogUtil.log(Level.ERROR, "No matching item found for brick oven recipe output '" + output.getItemName() + "'");
	    	    BBConfigFolderLoader.failLoading();
	    	    return;
	    	}
		List<Object> res = new ArrayList<>();
		for (String s : recipe)
		{
		    res.add(s);
		}
		
		for (Map.Entry<Character, JsonLoadedItem> entry : ingredients.entrySet())
		{
			res.add(entry.getKey());

			JsonLoadedItem jli = entry.getValue();
			if(!jli.isValid())
		    	{
		    	    LogUtil.log(Level.ERROR, "No matching item found for brick oven recipe input '" + jli.getItemName() + "'");
		    	    BBConfigFolderLoader.failLoading();
		    	    return;
		    	}
			if (jli.isOreDictionary())
			{
				res.add(jli.getItemName());
			}
			else
			{
				ItemStack stack = jli.getFirstItemStackOrNull();
				if (!stack.isEmpty())
				{
					res.add(stack);
				}
				else
				{
					ItemStack invalid = new ItemStack(Blocks.BARRIER);
					invalid.setStackDisplayName("ERROR IN LOADING JSON RECIPE. MISSING INGREDIENT.");
					res.add(invalid);
				}
			}
		}
		Object[] params = res.toArray();
		BrickOvenRecipeHandler.addShapedRecipe(output.getFirstItemStackOrNull(), params);
	}

	public JsonLoadedItemStack getOutput()
	{
		return output;
	}

	public List<String> getRecipe()
	{
		return recipe;
	}

	public Map<Character, JsonLoadedItem> getIngredients()
	{
		return ingredients;
	}
}
