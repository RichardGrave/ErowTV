package graver.erowtv.tools;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Set;

public final class YmlFileTool implements ErowTVConstants {

	private YmlFileTool() {
	}
	
	/**
	 * Use this to save specific data (like for copying blocks) to a file with given name.
	 * 
	 * 
	 * @param fileName with correct dir Example: /copy_blocks/tree
	 * @param player used for sending a message
	 * @param values
	 * @return true if succeded, false if something went wrong
	 */
	public static boolean saveToYmlFile(String fileName, Player player, LinkedHashMap<String, String> values) {
		try {
			player.sendMessage("Path: "+ErowTV.fileSaveFolder);
			player.sendMessage("File: "+ fileName);
			File blockYml = new File(ErowTV.fileSaveFolder + fileName + FILE_EXTENSION_YML);
			if (blockYml.exists()) {
				// Never save to a existing file, just in case the data would be appended to te
				// rest of the existing data.

				//TODO:RG Danger that someone else can overwrite your file. Maybe save per player?
				//Or if you forgot about your file you would accidentally overwrite it.
				//Maybe stop saving if it exists with a warning and you'll have to overrule it with a second line
				//parameter on the sign? Or maybe a second click to confirm
				//Another idea is to check for filename for games that are in the jar and should not be overwritten.

				if(!blockYml.delete()) {
					player.sendMessage("File already exists and could not be deleted");
					return false;
				}
			}

			//TODO:RG if DIR does not exist, then create it.

			blockYml.createNewFile();

			//If file is created
			if (blockYml.exists()) {
				FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(blockYml);
				
				//Set data
				Set<String> keys = values.keySet();
				for(String key: keys) {
					blockConfig.set(key, values.get(key));
				}
				//Save all
				blockConfig.save(blockYml);
			}else {
				player.sendMessage("File '"+fileName + ".yml'could not be created");
				return false;
			}
		} catch (IOException e) {
			player.sendMessage("Could not write to yml file");
			e.printStackTrace();
			return false;
		}
		player.sendMessage("File '"+fileName + ".yml' saved");
		return true;
	}

	/**
	 * Checks if the given file exists. If yes, then return the file otherwise return NULL.
	 *
	 *
	 * @param fileName with correct dir Example: /copy_blocks/tree
	 * @return file if found
	 */
	public static File doesFileExist(String fileName) {
		//Add .yml if filename doesn't have it.
		if(!fileName.endsWith(FILE_EXTENSION_YML)){
			fileName = fileName + FILE_EXTENSION_YML;
		}
		File blockYml = new File(ErowTV.fileSaveFolder + fileName);
		//Only returns if found
		if(blockYml.exists()) {
			return blockYml;
		}else{
			return null;
		}
	}

	//!!! Important !!!
	//When developing a Game then STOP and START the server again. Or we get file write problems.
	/**
	 * Checks if game file exists. If not then it wil copy the file from the jar to the server plugins
	 * saved_files directory for games.
	 *
	 * @param player
	 * @param gameName used for filename exists check
	 * @param gameFile where is should create the file (contains dir + filename)
	 * @return file for the game
	 */
	public static File doesGameFileExist(Player player, String gameName, String gameFile){
		File file;

		if((file = YmlFileTool.doesFileExist((DIR_GAMES + gameName))) != null){
			return file;
		}else{
			try {
				if (ErowTV.isDebug) {
					player.sendMessage(ChatColor.DARK_AQUA+"Going to create the gamefile = "+gameFile);
				}

				ClassLoader classLoader = ErowTV.class.getClassLoader();

				File targetFile = new File(ErowTV.fileSaveFolder + DIR_GAMES + gameName + FILE_EXTENSION_YML);

				InputStream input = classLoader.getResourceAsStream(gameFile);
				FileUtils.copyInputStreamToFile(input, targetFile);

				//try to find the file again
				return YmlFileTool.doesFileExist((DIR_GAMES + gameName));
			}catch (Exception ex){
				player.sendMessage(ChatColor.DARK_RED+"[EventException]:[createDoubleOrNothing]");
				ex.printStackTrace();
			}
		}

		return null;
	}
}
