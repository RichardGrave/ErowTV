package graver.erowtv.item;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;

public final class ButtonTools {

	// Dont instantiate or subclass the class
	private ButtonTools() {
	}
	
	//TODO:RG afmaken
	
	/**
	 * Only for Wood and Stone buttons
	 * Check if there is something needed to be done when a button is clicked
	 * 
	 * @param player
	 * @param clickedBlock
	 */
	public static void buttonRightClickedByPlayer(Player player, Block clickedBlock) {
		//We already know its a Button. That was checked before this method was called.
		
		//Turn the block into a Button
		BlockState blockState = clickedBlock.getState();
		blockState.setType(clickedBlock.getType());
		Button button = (Button) blockState.getData();
		
		//Get the block the button is placed on
		Block blockBehindButton = clickedBlock.getRelative(button.getAttachedFace());
		
		//Handle the click
		handleButtonClicked(player, clickedBlock, blockBehindButton);
	}

	/**
	 * Only Levers
	 * Check if there is something needed to be done when a lever is clicked
	 *
	 * @param player
	 * @param clickedBlock
	 */
	public static void leverRightClickedByPlayer(Player player, Block clickedBlock) {
		//We already know its a Button. That was checked before this method was called.

		//Turn the block into a Button
		BlockState blockState = clickedBlock.getState();
		blockState.setType(clickedBlock.getType());
		Lever lever = (Lever) blockState.getData();

		//Get the block the button is placed on
		Block blockBehindButton = clickedBlock.getRelative(lever.getAttachedFace());

		//Handle the click
		handleLeverClicked(player, clickedBlock, blockBehindButton);
	}
	
	/**
	 * Check if the positions match the DESTROY_FROM and DESTROY_TO memory for the player.
	 * If so start destroying.
	 *
	 * @param player
	 * @param clickedBlock
	 * @param blockBehindButton
	 */
	public static void handleLeverClicked(Player player, Block clickedBlock, Block blockBehindButton) {
//		if(BlockTools.isBlockPositionTheSame(blockBehindButton,(List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION))) {
//			//We also need the DESTROY_TO position
//			if(ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION)) {
//				//Start destroying
//				DestroyBlockTool.destroyFromToBlocks(player, clickedBlock,
//						(List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION),
//						(List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION));
//			}else {
//				//If the TO block is missing
//				player.sendMessage("A 'Destroy TO block' is needed");
//			}
//		}else if(BlockTools.isBlockPositionTheSame(blockBehindButton,(List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION))) {
//			//We also nee the DESTROY_FROM position
//			if(ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION)) {
//				//Start destroying
//				DestroyBlockTool.destroyFromToBlocks(player, clickedBlock,
//						(List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION),
//						(List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION));
//			}else {
//				//If the FROM block is missing
//				player.sendMessage("A 'Destroy FROM block' is needed");
//			}
//		}
	}

	/**
	 * Reserverd for button clicks
	 * If so start destroying.
	 *
	 * @param player
	 * @param clickedBlock
	 * @param blockBehindButton
	 */
	public static void handleButtonClicked(Player player, Block clickedBlock, Block blockBehindButton) {

	}
}
