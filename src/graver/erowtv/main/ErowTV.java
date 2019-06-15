package graver.erowtv.main;

import graver.erowtv.commands.TestCommand;
import graver.erowtv.constants.Constants;
import graver.erowtv.constants.Enumerations;
import graver.erowtv.constants.Messages;
import graver.erowtv.item.BlockEvents;
import graver.erowtv.player.PlayerCommands;
import graver.erowtv.player.PlayerEvents;
import graver.erowtv.recipes.BuildingBlocksRecipes;
import graver.erowtv.recipes.MiscellaneousRecipes;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErowTV extends JavaPlugin implements Enumerations {
	// TODO:RG alles wat maar een String is overzetten naar een constanten/message
	
	public static Map<String, List<?>> serverMemory; //TODO:RG Check if this is really needed
	public static Map<String, Map<String, List<?>>> playerMemory;
	
	public static String pluginFolder;
	
	@Override
	public void onEnable() {
		try {
			playerMemory = new HashMap<String, Map<String,List<?>>>();
			serverMemory = new HashMap<String, List<?>>();
			pluginFolder = getDataFolder().getParentFile().getAbsolutePath() + "/copy_blocks/";
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

	// Register all events
	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
		getServer().getPluginManager().registerEvents(new BlockEvents(), this);

		getLogger().info(Messages.EROWTV_EVENT_REGISTRATION_COMPLETE);
	}

	// Register all the commands
	public void registerCommands() {
		getCommand(Constants.TEST).setExecutor(new TestCommand());
		getCommand(Constants.PLAYER_DIRECTION).setExecutor(new PlayerCommands());

		getLogger().info(Messages.EROWTV_COMMAND_REGISTRATION_COMPLETE);
	}

	// Register all the commands
	public void registerRecipes() {
		//Each NamespacedKey needs the have a different key name, so use the CustomRecipe.RECIPE.getKey()
		getServer().addRecipe(MiscellaneousRecipes.createTwoByTwo(new NamespacedKey(this, CustomItem.TWO_BY_TWO.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createCopyFromBlock(new NamespacedKey(this, CustomItem.COPY_FROM_BLOCK.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createCopyToBlock(new NamespacedKey(this, CustomItem.COPY_TO_BLOCK.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createDestroyFromBlock(new NamespacedKey(this, CustomItem.DESTROY_FROM_BLOCK.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createDestroyToBlock(new NamespacedKey(this, CustomItem.DESTROY_TO_BLOCK.getKey())));
		getServer().addRecipe(BuildingBlocksRecipes.createPasteBlock(new NamespacedKey(this, CustomItem.PASTE_BLOCK.getKey())));
		
		getLogger().info(Messages.EROWTV_RECIPE_REGISTRATION_COMPLETE);
	}
	
	/**
	 * Add a player to ErowTV memory
	 * 
	 * @param player
	 */
	public static void addPlayerToMemory(Player player) {
		player.sendMessage("AddPlayerToMemory");
		if(!playerMemory.containsKey(player.getUniqueId().toString())) {
			playerMemory.put(player.getUniqueId().toString(), new HashMap<String, List<?>>());
			player.sendMessage("PlayerAddedToMemory");
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
		if(!playerMemory.containsKey(player.getUniqueId().toString())) {
			//If by any chance the Player hasnt got a playerMemory, make a new one.
			addPlayerToMemory(player);
		}
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
//		player.sendMessage("ReadMemory");
		if(!playerMemory.containsKey(player.getUniqueId().toString())) {
//			player.sendMessage("NoPlayerInMemory");
			//If by any chance the Player hasnt got a playerMemory, make a new one.
			addPlayerToMemory(player);
		}
		
		return playerMemory.get(player.getUniqueId().toString()).get(memoryName);
	}
	
	/**
	 * Read a specific memory
	 * 
	 * @param player
	 * @param memoryName
	 * @return a list with the stored values for the player with a specific memoryName
	 */
	public static boolean doesPlayerHaveMemory(Player player, String memoryName){
		if(!playerMemory.containsKey(player.getUniqueId().toString())) {
			return false;
		}
		
		return playerMemory.get(player.getUniqueId().toString()).containsKey(memoryName);
	}
}
