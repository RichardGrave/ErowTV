package graver.erowtv.item;

import graver.erowtv.constants.Constants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.CopyBlockTool;
import graver.erowtv.tools.PasteBlockTool;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.List;

public final class SignTools {

	// Dont instantiate or subclass the class
	private SignTools() {
	}
	
	/**
	 * Only for Wall Signs
	 * Check if there is something needed to be done when a wallsign is clicked
	 * 
	 * @param player
	 * @param clickedBlock
	 */
	public static void wallSignClickedByPlayer(Player player, Block clickedBlock) {
		//We already know its a wall sign. That was checked before this method was called.
		
		//Turn the block into a Sign
		Sign wallSign = (Sign)clickedBlock.getState();

		//Get MaterialData from sign for the attachedFace thats needed to get relative block
		org.bukkit.block.data.type.WallSign dataSign = (org.bukkit.block.data.type.WallSign)wallSign.getBlockData();
		
		Block blockBehindSign = clickedBlock.getRelative(dataSign.getFacing().getOppositeFace());
		
		handleWallSignClicked(player, clickedBlock, wallSign, blockBehindSign);
	}

	/**
	 * Only for Signs
	 * Check if there is something needed to be done when a wallsign is clicked
	 *
	 * @param player
	 * @param clickedBlock
	 */
	public static void signClickedByPlayer(Player player, Block clickedBlock) {
		//We already know its a sign. That was checked before this method was called.

		//Turn the block into a Sign
		Sign sign = (Sign)clickedBlock.getState();

		//Get MaterialData from sign
		org.bukkit.block.data.type.Sign dataSign = (org.bukkit.block.data.type.Sign)sign.getBlockData();

		Block blockBehindSign = clickedBlock.getRelative(dataSign.getRotation().getOppositeFace());

		handleWallSignClicked(player, clickedBlock, sign, blockBehindSign);
	}
	
	public static void handleWallSignClicked(Player player, Block clickedBlock, Sign sign, Block blockBehindSign) {
		if(BlockTools.isBlockPositionTheSame(blockBehindSign,(List<Integer>)ErowTV.readPlayerMemory(player, Constants.MEMORY_COPY_FROM_POSITION))) {
			//We also need the COPY_TO position
			if(ErowTV.doesPlayerHaveMemory(player, Constants.MEMORY_COPY_TO_POSITION)) {
				//Start the copy
				CopyBlockTool.copyFromAndToBlocks(player, clickedBlock, sign,
						(List<Integer>)ErowTV.readPlayerMemory(player, Constants.MEMORY_COPY_FROM_POSITION),
						(List<Integer>)ErowTV.readPlayerMemory(player, Constants.MEMORY_COPY_TO_POSITION));
			}else {
				player.sendMessage("A 'Copy to block' is needed");
			}
		}else if(BlockTools.isBlockPositionTheSame(blockBehindSign,(List<Integer>)ErowTV.readPlayerMemory(player, Constants.MEMORY_COPY_TO_POSITION))) {
			//We also need the COPY_FROM position
			if(ErowTV.doesPlayerHaveMemory(player, Constants.MEMORY_COPY_FROM_POSITION)) {
				//Start the copy
				CopyBlockTool.copyFromAndToBlocks(player, clickedBlock, sign,
						(List<Integer>)ErowTV.readPlayerMemory(player, Constants.MEMORY_COPY_FROM_POSITION),
						(List<Integer>)ErowTV.readPlayerMemory(player, Constants.MEMORY_COPY_TO_POSITION));
			}else {
				player.sendMessage("A 'Copy from block' is needed");
			}

			//Here we check clickedBlock, not blockBehindSign. So the sign itself.
		}else if(BlockTools.isBlockPositionTheSame(clickedBlock,(List<Integer>)ErowTV.readPlayerMemory(player, Constants.MEMORY_PASTE_POSITION))) {
			//Start pasting
			PasteBlockTool.pasteBlocks(player, clickedBlock, sign, (List<Integer>)ErowTV.readPlayerMemory(player, Constants.MEMORY_PASTE_POSITION));
		}
	}
}
