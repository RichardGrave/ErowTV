package graver.erowtv.games;

import graver.erowtv.constants.ErowTVConstants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class GameEvents implements Listener, ErowTVConstants {

    /**
     * First check if Player interacts with RightClick or LeftClick.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            //Check which click is used and handle the event
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                GameHandler.handlePlayerGameEvents(event);
            }
        } catch (Exception ex) {
            event.getPlayer().sendMessage(ChatColor.DARK_RED+"[EventException]:[onPlayerInteract]");
            ex.printStackTrace();
        }
    }

    /**
     * This checks if the block that somebody is trying to break is a game blocks from
     * a game that is currently running
     *
     * @param event this is a BlockBreakEvent
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            //First check if there is a game running
            if(GameHandler.isAGameRunning()) {
                //Get block material so we can search in the game blocks
                Material blockMaterial = event.getBlock().getBlockData().getMaterial();
                List<Block> gameMaterialBlocks = GameHandler.getGameMaterialBlocks().get(blockMaterial);

                if (gameMaterialBlocks != null &&
                        GameHandler.getGameMaterialBlocks().get(blockMaterial).contains(event.getBlock())) {
                    //If block is from the game then cancel the breaking
                    event.setCancelled(true);
                }
            }
        } catch (Exception ex) {
            event.getPlayer().sendMessage(ChatColor.DARK_RED+"[EventException]:[onBlockBreak]");
            ex.printStackTrace();
        }
    }

    /**
     * Handles every Redstone event. REDSTONE_LAMP that is going off or on.
     *
     * @param event this is a BlockBreakEvent
     */
    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (GameHandler.getGameBlocks(Material.REDSTONE_LAMP).contains(event.getBlock())){
            event.setNewCurrent(5);
        }
    }
}