package graver.erowtv.item;

import graver.erowtv.constants.Constants;
import graver.erowtv.constants.Enumerations;
import graver.erowtv.main.ErowTV;
import graver.erowtv.special.YoutubeSubCounter;
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

public class BlockEvents implements Listener {

	/**
	 * Check if a certain block is placed and handle the action that needs to be done.
	 * @param event
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		try {
			BlockTools.getDataForBlockType(event.getPlayer(),event.getBlockPlaced());
//			PasteBlockTool.changeDirectionForBlockType(event.getPlayer(),event.getBlockPlaced(), null, new String[] {});
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
	 * @param player
	 * @param itemInHand
	 * @param placedBlock
	 */
	public void handlePlacedBlock(Player player, ItemStack itemInHand, Block placedBlock) {
		try {
			if(placedBlock.getType() == Material.REDSTONE_WALL_TORCH ||
					placedBlock.getType() == Material.REDSTONE_TORCH) {

				new YoutubeSubCounter(player).runTaskTimer(ErowTV.getJavaPluginErowTV(), 20, (20 * 10));
				//TODO:RG weer aanzetten
				//SpecialBlockTools.redstoneTorchPlacedByPlayer(player, placedBlock);
			}else {
				if (itemInHand != null && itemInHand.getItemMeta() != null && itemInHand.getItemMeta().getDisplayName() != null) {

					//TODO:RG for later use with more items
					switch (Enumerations.CustomItem.getCustomItem(itemInHand.getItemMeta().getDisplayName())) {
						case PASTE_BLOCK:
							//If it's not a sign then use the copy action
							//Start pasting
							World.Environment environment = player.getWorld().getEnvironment();
							int playersWorld = (environment == World.Environment.NETHER ? Constants.WORLD_NETHER : environment == World.Environment.NORMAL ? Constants.WORLD_NORMAL : Constants.WORLD_END);
							List<String> fileNameCopy = (List<String>) ErowTV.readPlayerMemory(player, Constants.MEMORY_PASTE_BLOCK_ACTION);
							//Has to have a filename else do nothing
							if (fileNameCopy != null && !fileNameCopy.isEmpty()) {
								List<Integer> position = Arrays.asList(playersWorld, placedBlock.getX(), placedBlock.getY(), placedBlock.getZ());
								PasteBlockTool.pasteBlocks(player, placedBlock, null, fileNameCopy.get(0), position);
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


	//			if(event.getBlockPlaced().getType() == Material.REDSTONE_WALL_TORCH ||
//					event.getBlockPlaced().getType() == Material.REDSTONE_TORCH) {
//				event.getPlayer().sendMessage("Inside redstone_torch");
//				SpecialBlockTools.redstoneTorchPlacedByPlayer(event.getPlayer(), event.getBlockPlaced());
//
//				//TODO:RG moet nog beter
////				delay is per Spigot Ticks. 20 ticks is 1 seconde
//				//delay wachten tot het begint te runnen.
//				//period is tijd tussen de volgende run.
//				//TODO:RG Constanten van maken.
////				new YoutubeSubCounter(event.getPlayer()).runTaskTimer(ErowTV.getJavaPluginErowTV(), 40, 40);
//			}

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


// TODO:RG dit is voor test, moet niet zo blijven
//event.getPlayer().sendMessage(event.getBlock().getType().name() + " is placed");
//boolean isValid = PlayerTools.isDirectionValid(event.getPlayer());
//String isValidDirection = (isValid ? " is a valid direction" : " is NOT a valid direction");
//event.getPlayer().sendMessage(PlayerTools.getCardinalDirection(event.getPlayer(), false) + isValidDirection);
//event.getPlayer().sendMessage("Simple: " +PlayerTools.getCardinalDirection(event.getPlayer(), true));
//if (isValid) {
//	if(event.getBlock().getType() != Material.STONE) {
//		BlockTools.placeBlockByPlayerPosition(event.getPlayer(), event.getBlock().getType(), true, 8, 8, 8);
//	}else {
//		BlockTools.placeBlockByPlayerPosition(event.getPlayer(), Material.AIR, false, 8, 8, 8);
//	}
//	
//}