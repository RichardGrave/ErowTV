package graver.erowtv.tools;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
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
			File blockYml = new File(ErowTV.fileSaveFolder + fileName + FILE_EXTENSION_YML);
			if (blockYml.exists()) {
				// Never save to a existing file, just in case the data would be appended to te
				// rest of the existing data.

				//TODO:RG Danger that someone else can overwrite your file. Maybe save per player?
				//Or if you forgot about your file you would accidentally overwrite it.
				//Maybe stop saving if it exists with a warning and you'll have to overrule it with a second line
				//parameter on the sign?
				if(!blockYml.delete()) {
					player.sendMessage("File already exists and could not be deleted");
					return false;
				}
			}

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

}
