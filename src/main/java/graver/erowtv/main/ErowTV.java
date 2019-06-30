package graver.erowtv.main;

import graver.erowtv.commands.TestCommand;
import graver.erowtv.constants.Enumerations;
import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.constants.Messages;
import graver.erowtv.item.BlockEvents;
import graver.erowtv.player.PlayerCommands;
import graver.erowtv.player.PlayerEvents;
import graver.erowtv.recipes.BuildingBlocksRecipes;
import graver.erowtv.recipes.MiscellaneousRecipes;
import graver.erowtv.recipes.ToolsRecipes;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ErowTV extends JavaPlugin implements Enumerations, ErowTVConstants {

	//TODO:RG ServerMemory is going to be a prermanent memory. So write to a file. And load at startup
	public static Map<String, List<?>> serverMemory;
	public static Map<String, Map<String, List<?>>> playerMemory;
	public static Collection<NamespacedKey> namespacedKeysRecipes = new ArrayList<NamespacedKey>();

	//Set isDebug on true if player messages with info is neededs
	public static boolean isDebug = false;
	public static String fileSaveFolder;

	//Reference to this plugin.
	//For use with BukkitRunnable's that need the plugin to start.
	public static JavaPlugin javaPluginErowTV;

	/**
	 * Starting point of the plugin
	 */
	@Override
	public void onEnable() {
		try {
			this.javaPluginErowTV = this;
			playerMemory = new HashMap<String, Map<String,List<?>>>();
			serverMemory = new HashMap<String, List<?>>();

			//!! Make sure that you have a 'saved_files' folder inside the Spigot 'plugins' folder
			//Just in case the file system doesn't create the needed folder
			fileSaveFolder = getDataFolder().getParentFile().getAbsolutePath() + DIR_FILE_SAVE;

			// register Events, Commands and Recipes
			registerEvents();
			registerCommands();
			registerRecipes();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		getLogger().info(Messages.EROWTV_IS_ACTIVE);
	}

	@Override
	public void onDisable() {
//		TODO:RG do Actions depending on filled playerMemory???
		//Clear playerMemory for ErowTV
		playerMemory = null;
		serverMemory = null;
		getLogger().info(Messages.EROWTV_MEMORY_CLEARED);
	}

	/**
	 * Register all the events
	 */
	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
		getServer().getPluginManager().registerEvents(new BlockEvents(), this);
		getLogger().info(Messages.EROWTV_EVENT_REGISTRATION_COMPLETE);
	}


	/**
	 * Register all the commands
	 */
	public void registerCommands() {
		getCommand(ErowTVConstants.TEST).setExecutor(new TestCommand());
		getCommand(ErowTVConstants.FACING).setExecutor(new PlayerCommands(ErowTVConstants.FACING));
		getCommand(ErowTVConstants.DEBUG_MESSAGES).setExecutor(new PlayerCommands(ErowTVConstants.DEBUG_MESSAGES));

		getLogger().info(Messages.EROWTV_COMMAND_REGISTRATION_COMPLETE);
	}

	/**
	 * Register all the recipes
	 */
	public void registerRecipes() {
		//Each NamespacedKey needs the have a different key name, so use the CustomRecipe.RECIPE.getKey()
		getServer().addRecipe(MiscellaneousRecipes.createTwoByTwo(new NamespacedKey(this, CustomItem.TWO_BY_TWO.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createCopyFromBlock(new NamespacedKey(this, CustomItem.COPY_FROM_BLOCK.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createCopyToBlock(new NamespacedKey(this, CustomItem.COPY_TO_BLOCK.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createDestroyFromBlock(new NamespacedKey(this, CustomItem.DESTROY_FROM_BLOCK.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createDestroyToBlock(new NamespacedKey(this, CustomItem.DESTROY_TO_BLOCK.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createPasteBlock(new NamespacedKey(this, CustomItem.PASTE_BLOCK.getKey())));

		getServer().addRecipe(ToolsRecipes.createToolSign(new NamespacedKey(this, CustomItem.SPECIAL_SIGN.getKey())));


		getLogger().info(Messages.EROWTV_RECIPE_REGISTRATION_COMPLETE);
	}

	public static void addnamespacedKeyRecipe(NamespacedKey namespacedKeyRecipe){
		namespacedKeysRecipes.add(namespacedKeyRecipe);
	}

	public static Collection<NamespacedKey> getAllNamespacedKeysRecipes(){
		return namespacedKeysRecipes;
	}

	/**
	 * Add a player to ErowTV memory
	 * 
	 * @param player
	 */
	public static void addPlayerToMemory(Player player) {
		if(isDebug) {
			player.sendMessage("AddPlayerToMemory");
		}
		if(!playerMemory.containsKey(player.getUniqueId().toString())) {
			playerMemory.put(player.getUniqueId().toString(), new HashMap<String, List<?>>());
			if(isDebug) {
				player.sendMessage("PlayerAddedToMemory");
			}
		}
	}
	
	/**
	 * Remove a player (and his memory's) from ErowTVs memory
	 * 
	 * @param player
	 */
	public static void clearPlayerFromMemory(Player player) {
		playerMemory.remove(player.getUniqueId().toString());
	}
	
	/**
	 * Remove a specific memory for a player
	 * 
	 * @param player
	 * @param memoryName
	 */
	public static void removeMemoryFromPlayerMemory(Player player, String memoryName) {
		playerMemory.get(player.getUniqueId().toString()).remove(memoryName);
	}
	
	/**
	 * Store specific memory for a player
	 * 
	 * @param player
	 * @param memoryName
	 * @param memoryValues a List with values (Integer or String or whatever)
	 */
	public static void storePlayerMemory(Player player, String memoryName, List<?> memoryValues) {
		//First check if the PLAYER is IN the memory
		if(!playerMemory.containsKey(player.getUniqueId().toString())) {
			//If by any chance the Player hasnt got a playerMemory, make a new one.
			addPlayerToMemory(player);
		}
		//Write the memory for the player
		playerMemory.get(player.getUniqueId().toString()).put(memoryName, memoryValues);
	}
	
	/**
	 * Read a specific memory
	 * 
	 * @param player
	 * @param memoryName
	 * @return a list with the stored values for the player with a specific memoryName
	 */
	public static List<?> readPlayerMemory(Player player, String memoryName){
		if(isDebug) {
			player.sendMessage("ReadMemory");
		}
		//First check if the PLAYER is IN the memory
		if(!playerMemory.containsKey(player.getUniqueId().toString())) {
			if(isDebug) {
				player.sendMessage("NoPlayerInMemory");
			}

			//If by any chance the Player hasn't got a playerMemory, make a new one.
			addPlayerToMemory(player);
			//Just let it continue
		}

		//Fetch the memory and return it
		return playerMemory.get(player.getUniqueId().toString()).get(memoryName);
	}
	
	/**
	 * Read a specific memory
	 * 
	 * @param player
	 * @param memoryName
	 * @return a list with the stored values for the player with a specific memoryName
	 */
	public static boolean doesPlayerHaveSpecificMemory(Player player, String memoryName){
		//First check if the PLAYER is IN the memory
		if(!playerMemory.containsKey(player.getUniqueId().toString())) {
			return false;
		}

		//Now check if the player HAS the memory
		return playerMemory.get(player.getUniqueId().toString()).containsKey(memoryName);
	}
}
