package graver.erowtv.item;

import graver.erowtv.constants.Constants;
import graver.erowtv.constants.Enumerations.CustomItem;
import graver.erowtv.main.ErowTV;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public final class ItemTools {

	// Dont instantiate or subclass the class
	private ItemTools() {
	}

	/**
	 * Handle right-click PlayerEvents
	 * 
	 * @param player
	 * @param itemStack
	 * @param clickedBlock
	 */

	@SuppressWarnings("incomplete-switch")
	public static void rightClickItemUse(Player player, ItemStack itemStack, Block clickedBlock) {
		if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {

			// When item is not found, then NO_RECIPE could handle Bukkit items
			switch (CustomItem.getCustomItem(itemStack.getItemMeta().getDisplayName())) {
				case TWO_BY_TWO:
					BlockTools.placeBlockByPlayerPosition(player, clickedBlock, null, Constants.APPLY_PHYSICS, 2, 2, 2);
				break;

				case PASTE_BLOCK:
					if(clickedBlock.getType() == Material.SPRUCE_SIGN || clickedBlock.getType() == Material.SPRUCE_WALL_SIGN) {
						String fileNameForPaste = ((Sign)clickedBlock.getState()).getLine(0);
						List<String> fileNameToCopy = Arrays.asList(fileNameForPaste);
						ErowTV.storePlayerMemory(player, Constants.MEMORY_PASTE_BLOCK_ACTION, fileNameToCopy);

					}
//					else{
//						//If it's not a sign then use the copy action
//						//Start pasting
//						World.Environment environment = player.getWorld().getEnvironment();
//						int playersWorld = (environment == World.Environment.NETHER ? Constants.WORLD_NETHER : environment == World.Environment.NORMAL ? Constants.WORLD_NORMAL : Constants.WORLD_END);
//						List<String> fileNameCopy = (List<String>)ErowTV.readPlayerMemory(player, Constants.MEMORY_PASTE_BLOCK_ACTION);
//						//Has to have a filename else do nothing
//						if(fileNameCopy != null && !fileNameCopy.isEmpty()) {
//							List<Integer> position = Arrays.asList(playersWorld, clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());
//							PasteBlockTool.pasteBlocks(player, clickedBlock, null, fileNameCopy.get(0), position);
//						}
//					}
				break;

				case NO_RECIPE:
				break;
			}

		}
	}

	/**
	 * Handle left-click PlayerEvents
	 * 
	 * @param player
	 * @param itemStack
	 * @param block
	 */
	@SuppressWarnings("incomplete-switch")
	public static void leftClickItemUse(Player player, ItemStack itemStack, Block block) {
		if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {
			
			// When item is not found, then NO_RECIPE could handle Bukkit items
			switch (CustomItem.getCustomItem(itemStack.getItemMeta().getDisplayName())) {
			case TWO_BY_TWO:
				BlockTools.placeBlockByPlayerPosition(player, block, Material.AIR, Constants.APPLY_PHYSICS, 2, 2, 2);
				break;
			case NO_RECIPE:
				break;
			}

		}
	}
		
}
