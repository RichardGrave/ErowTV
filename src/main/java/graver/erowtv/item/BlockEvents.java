package graver.erowtv.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvents implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		try {
			BlockTools.getDataForBlockType(event.getPlayer(),event.getBlockPlaced());
//			PasteBlockTool.changeDirectionForBlockType(event.getPlayer(),event.getBlockPlaced(), null, new String[] {});
			BlockTools.blockPlaced(event.getPlayer(), event.getBlockPlaced());
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