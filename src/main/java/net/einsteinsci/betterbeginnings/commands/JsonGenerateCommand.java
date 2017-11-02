package net.einsteinsci.betterbeginnings.commands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.einsteinsci.betterbeginnings.config.BBConfigFolderLoader;
import net.einsteinsci.betterbeginnings.config.json.BrickOvenConfig;
import net.einsteinsci.betterbeginnings.config.json.KilnConfig;
import net.einsteinsci.betterbeginnings.config.json.SmelterConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class JsonGenerateCommand extends CommandBase
{
	public static final String SMELTER = "smelter";
	public static final String BRICKOVEN = "brickoven";
	public static final String KILN = "kiln";

	@Override
	public String getName()
	{
		return "jsongen";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		// Do not translate this, as "smelter", "brickoven", and "kiln" are hardcoded.
		return "jsongen <smelter | brickoven | kiln>";
	}

	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 0 || args.length > 1)
		{
			throw new WrongUsageException(getUsage(sender));
		}

		String code = args[0];
		if (code.equalsIgnoreCase(SMELTER))
		{
			SmelterConfig.INSTANCE.generateAutoConfig();
			BBConfigFolderLoader.saveAutoJson(SmelterConfig.INSTANCE);
		}
		else if (code.equalsIgnoreCase(BRICKOVEN))
		{
			BrickOvenConfig.INSTANCE.generateAutoConfig();
			BBConfigFolderLoader.saveAutoJson(BrickOvenConfig.INSTANCE);
		}
		else if (code.equalsIgnoreCase(KILN))
		{
			SmelterConfig.INSTANCE.generateAffectedInputs();
			BrickOvenConfig.INSTANCE.generateAffectedOutputs();

			KilnConfig.INSTANCE.generateAutoConfig();
			BBConfigFolderLoader.saveAutoJson(KilnConfig.INSTANCE);
		}
		else
		{
			throw new WrongUsageException(getUsage(sender));
		}

		sender.sendMessage(new TextComponentTranslation("command.jsongen.complete"));
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		List<String> res = new ArrayList<>();
		if (args == null || args.length == 0)
		{
			res.add(SMELTER);
			res.add(BRICKOVEN);
			res.add(KILN);
		}

		return res;
	}
}
