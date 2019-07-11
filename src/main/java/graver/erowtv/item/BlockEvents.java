package graver.erowtv.item;

import graver.erowtv.constants.Enumerations;
import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.PasteBlockTool;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BlockEvents implements Listener, ErowTVConstants {

    /**
     * Check if a certain block is placed and handle the action that needs to be done.
     *
     * @param event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        try {
            //This checks for CustomItem block made by me.
            //Dont use for game blocks.
            BlockTools.blockPlaced(event.getPlayer(), event.getBlockPlaced());

            handlePlacedBlock(event.getPlayer(), event.getItemInHand(), event.getBlockPlaced());

        } catch (Exception ex) {
            event.getPlayer().sendMessage("[EventException]:[onBlockPlace]");
            ex.printStackTrace();
        }
    }

    /**
     * Handle the action that needs to be done for the placed block
     *
     * @param player
     * @param itemInHand
     * @param placedBlock
     */
    public void handlePlacedBlock(Player player, ItemStack itemInHand, Block placedBlock) {
        try {
            if (placedBlock.getType() == Material.REDSTONE_WALL_TORCH ||
                    placedBlock.getType() == Material.REDSTONE_TORCH) {

                SpecialBlockTools.redstoneTorchPlacedByPlayer(player, placedBlock);
            } else {
                if (itemInHand != null && itemInHand.getItemMeta() != null && itemInHand.getItemMeta().getDisplayName() != null) {

                    //TODO:RG for later use with more items
                    switch (Enumerations.CustomItem.getCustomItem(itemInHand.getItemMeta().getDisplayName())) {
                        case PASTE_BLOCK:
                            //If it's not a sign then use the copy action
                            //Start pasting
                            World.Environment environment = player.getWorld().getEnvironment();
                            int playersWorld = (environment == World.Environment.NETHER ? WORLD_NETHER : environment == World.Environment.NORMAL ? WORLD_NORMAL : WORLD_END);

                            List<String> fileNameCopy = (List<String>) ErowTV.readPlayerMemory(player, MEMORY_PASTE_BLOCK_ACTION);

                            //Has to have a filename else do nothing
                            if (fileNameCopy != null && !fileNameCopy.isEmpty()) {

                                List<Integer> position = Arrays.asList(playersWorld, placedBlock.getX(), placedBlock.getY(), placedBlock.getZ());
                                //Add correct dir to fileNameCopy.
                                PasteBlockTool.pasteBlocks(player, placedBlock, null, (DIR_COPY_BLOCKS+fileNameCopy.get(0)), position, MEMORY_PASTE_BLOCK_ACTION);
                            }

                            break;

                        case NO_RECIPE:
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            player.sendMessage("[EventException]:[handlePlacedBlock]");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            BlockTools.blockBreak(event.getPlayer(), event.getBlock());
        } catch (Exception ex) {
            event.getPlayer().sendMessage("[EventException]:[onBlockBreak]");
            ex.printStackTrace();
        }
    }
}