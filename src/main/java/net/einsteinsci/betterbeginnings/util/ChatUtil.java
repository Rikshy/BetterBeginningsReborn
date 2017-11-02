package net.einsteinsci.betterbeginnings.util;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ChatUtil
{
	public static final String ESCAPE = "\u00a7"; // section symbol

	public static final String RESET = ESCAPE + "r";

	public static final String BOLD = ESCAPE + "l";
	public static final String ITALIC = ESCAPE + "o";
	public static final String UNDERLINE = ESCAPE + "n";
	public static final String STRIKETHROUGH = ESCAPE + "m";
	public static final String OBFUSCATED = ESCAPE + "k";

	public static final String BLACK = ESCAPE + "0";
	public static final String DARK_BLUE = ESCAPE + "1";
	public static final String DARK_GREEN = ESCAPE + "2";
	public static final String TEAL = ESCAPE + "3";
	public static final String DARK_RED = ESCAPE + "4";
	public static final String PURPLE = ESCAPE + "5";
	public static final String ORANGE = ESCAPE + "6";
	public static final String LIGHT_GRAY = ESCAPE + "7";
	public static final String DARK_GRAY = ESCAPE + "8";
	public static final String BLUE = ESCAPE + "9";
	public static final String LIME = ESCAPE + "a";
	public static final String CYAN = ESCAPE + "b";
	public static final String RED = ESCAPE + "c";
	public static final String PINK = ESCAPE + "d";
	public static final String YELLOW = ESCAPE + "e";
	public static final String WHITE = ESCAPE + "f";

	public static void sendModChatToPlayer(EntityPlayer player, String message)
	{
		sendChatToPlayer(player, ORANGE + "[" + ModMain.NAME + "] " + RESET + message);
	}

	public static void sendChatToPlayer(EntityPlayer player, String message)
	{
		player.sendMessage(new TextComponentString(message));
	}

	public static void sendChatToServer(String message)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		for (EntityPlayerMP aPlayerEntityList : server.getPlayerList().getPlayers())
		{
			EntityPlayerMP player = aPlayerEntityList;
			player.sendMessage(new TextComponentString(message));
		}
	}

	public static void sendModChatToServer(String message)
	{
		sendChatToServer(ORANGE + "[" + ModMain.NAME + "] " + RESET + message);
	}
}
