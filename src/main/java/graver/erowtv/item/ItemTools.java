package graver.erowtv.item;

import graver.erowtv.constants.Constants;
import graver.erowtv.constants.Enumerations.CustomItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ItemTools {

	// Dont instantiate or subclass the class
	private ItemTools() {
	}

	/**
	 * Handle right-click PlayerEvents
	 * 
	 * @param player
	 * @param itemStack
	 * @param block
	 */

	@SuppressWarnings("incomplete-switch")
	public static void rightClickItemUse(Player player, ItemStack itemStack, Block block) {
		if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {

			// When item is not found, then NO_RECIPE could handle Bukkit items
			switch (CustomItem.getCustomItem(itemStack.getItemMeta().getDisplayName())) {
				case TWO_BY_TWO:
					BlockTools.placeBlockByPlayerPosition(player, block, null, Constants.APPLY_PHYSICS, 2, 2, 2);
				break;

				case PASTE_BLOCK:
					//TODO:RG Check of een sign is clicked. Zo ja, kijk of er een paste actie opstaat (hoeft geen paste block te zijn).
					//Dan die actie kopieren.
//					BlockTools.placeBlockByPlayerPosition(player, block, null, Constants.APPLY_PHYSICS, 2, 2, 2);
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
