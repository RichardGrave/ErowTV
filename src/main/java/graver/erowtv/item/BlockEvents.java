package graver.erowtv.item;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

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

			if(event.getBlockPlaced().getType() == Material.REDSTONE_WALL_TORCH ||
					event.getBlockPlaced().getType() == Material.REDSTONE_TORCH) {
				event.getPlayer().sendMessage("Inside redstone_torch");
				SpecialBlockTools.redstoneTorchPlacedByPlayer(event.getPlayer(), event.getBlockPlaced());

				//TODO:RG moet nog beter
//				delay is per Spigot Ticks. 20 ticks is 1 seconde
				//delay wachten tot het begint te runnen.
				//period is tijd tussen de volgende run.
				//TODO:RG Constanten van maken.
//				new YoutubeSubCounter(event.getPlayer()).runTaskTimer(ErowTV.getJavaPluginErowTV(), 40, 40);
			}
		} catch (Exception ex) {
			event.getPlayer().sendMessage("[EventException]:[onBlockPlace]");
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