package graver.erowtv.item;

import graver.erowtv.constants.Enumerations.CustomItem;
import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public final class ItemTools implements ErowTVConstants {

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
    public static boolean rightClickItemUse(Player player, ItemStack itemStack, Block clickedBlock) {
        boolean specialItemUseHand = false;
        if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {

            // When item is not found, then NO_RECIPE could handle Bukkit items
            switch (CustomItem.getCustomItem(itemStack.getItemMeta().getDisplayName())) {
                case TWO_BY_TWO:
                    specialItemUseHand = true;
                    BlockTools.placeBlockByPlayerPosition(player, clickedBlock, clickedBlock.getBlockData().getMaterial(), ErowTVConstants.APPLY_PHYSICS, 2, 2, 2);
                    break;

                case PASTE_BLOCK:
                    specialItemUseHand = true;
                    if (clickedBlock.getType() == Material.SPRUCE_SIGN || clickedBlock.getType() == Material.SPRUCE_WALL_SIGN) {
                        String fileNameForPaste = ((Sign) clickedBlock.getState()).getLine(PASTE_BLOCK_FILE_NAME);

                        List<String> fileNameToCopy = Arrays.asList(fileNameForPaste);
                        ErowTV.storePlayerMemory(player, ErowTVConstants.MEMORY_PASTE_BLOCK_ACTION, fileNameToCopy);
                    }
                    break;

                case NO_RECIPE:
                    break;

                default:
            }

        }

        return specialItemUseHand;
    }

    /**
     * Handle left-click PlayerEvents
     *
     * @param player
     * @param itemStack
     * @param block
     */
    public static boolean leftClickItemUse(Player player, ItemStack itemStack, Block block) {
        boolean specialItemUseHand = false;
        if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {

            // When item is not found, then NO_RECIPE could handle Bukkit items
            switch (CustomItem.getCustomItem(itemStack.getItemMeta().getDisplayName())) {
                case TWO_BY_TWO:
                    specialItemUseHand = true;
                    BlockTools.placeBlockByPlayerPosition(player, block, Material.AIR, ErowTVConstants.APPLY_PHYSICS, 2, 2, 2);
                    break;
                case NO_RECIPE:
                    break;

                default:
            }

        }
        return specialItemUseHand;
    }

}
