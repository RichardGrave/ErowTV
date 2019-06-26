package graver.erowtv.item;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.special.CountDownTimer;
import graver.erowtv.special.YoutubeSubCounter;
import graver.erowtv.tools.CopyBlockTool;
import graver.erowtv.tools.PasteBlockTool;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public final class SignTools implements ErowTVConstants {

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
	public static void leftClickWallSignByPlayer(Player player, Block clickedBlock) {
		//We already know its a wall sign. That was checked before this method was called.
		
		//Turn the block into a Sign
		Sign wallSign = (Sign)clickedBlock.getState();

		//Get MaterialData from sign for the attachedFace thats needed to get relative block
		org.bukkit.block.data.type.WallSign dataSign = (org.bukkit.block.data.type.WallSign)wallSign.getBlockData();

		BlockFace blockFace = dataSign.getFacing().getOppositeFace();
		Block blockBehindSign = clickedBlock.getRelative(blockFace);
		
		handleWallSignClicked(player, clickedBlock, wallSign, blockBehindSign, blockFace, true);
	}

	/**
	 * Only for Signs
	 * Check if there is something needed to be done when a sign is clicked
	 *
	 * @param player
	 * @param clickedBlock
	 */
	public static void leftClickSignByPlayer(Player player, Block clickedBlock) {
		//We already know its a sign. That was checked before this method was called.

		//Turn the block into a Sign
		Sign sign = (Sign)clickedBlock.getState();

		//Get MaterialData from sign
		org.bukkit.block.data.type.Sign dataSign = (org.bukkit.block.data.type.Sign)sign.getBlockData();

		BlockFace blockFace = dataSign.getRotation().getOppositeFace();
		Block blockBehindSign = clickedBlock.getRelative(blockFace);

		handleWallSignClicked(player, clickedBlock, sign, blockBehindSign, blockFace, false);
	}


	/**
	 * Only for Signs
	 * Check if there is something needed to be done when a sign is clicked
	 *
	 * @param player
	 * @param clickedBlock
	 */
	public static void rightClickSignByPlayer(Player player, Block clickedBlock) {
		//We already know its a sign. That was checked before this method was called.

		//Turn the block into a Sign
		Sign sign = (Sign)clickedBlock.getState();

		//Get MaterialData from sign
		org.bukkit.block.data.type.Sign dataSign = (org.bukkit.block.data.type.Sign)sign.getBlockData();

		BlockFace blockFace = dataSign.getRotation().getOppositeFace();
		Block blockBehindSign = clickedBlock.getRelative(blockFace);

		handleWallSignClicked(player, clickedBlock, sign, blockBehindSign, blockFace, false);
	}

	/**
	 * Only for Signs
	 * Check if there is something needed to be done when a wallsign is clicked
	 *
	 * @param player
	 * @param clickedBlock
	 */
	public static void rightClickWallSignByPlayer(Player player, Block clickedBlock) {
		//We already know its a wall sign. That was checked before this method was called.

		//Turn the block into a Sign
		Sign wallSign = (Sign)clickedBlock.getState();

		//Get MaterialData from sign for the attachedFace thats needed to get relative block
		org.bukkit.block.data.type.WallSign dataSign = (org.bukkit.block.data.type.WallSign)wallSign.getBlockData();

		BlockFace blockFace = dataSign.getFacing().getOppositeFace();
		Block blockBehindSign = clickedBlock.getRelative(blockFace);

		handleWallSignClicked(player, clickedBlock, wallSign, blockBehindSign, blockFace, true);
	}


	/**
	 * Only for Signs
	 * Check if there is something needed to be done when a wallsign is clicked
	 *
	 * @param player
	 * @param clickedBlock
	 * @param blockBehindSign
	 * @param clickedBlock
	 * @param blockFace
	 */
	public static void handleWallSignClicked(Player player, Block clickedBlock, Sign sign, Block blockBehindSign, BlockFace blockFace, boolean isWallSign) {
		player.sendMessage("handleWallSignClicked");
		String uniqueMemory;

		if(BlockTools.isBlockPositionTheSame(blockBehindSign,(List<Integer>)ErowTV.readPlayerMemory(player, MEMORY_COPY_FROM_POSITION))) {
			//We also need the COPY_TO position
			if(ErowTV.doesPlayerHaveSpecificMemory(player, MEMORY_COPY_TO_POSITION)) {
				//Start the copy
				CopyBlockTool.copyFromAndToBlocks(player, clickedBlock, sign,
						(List<Integer>)ErowTV.readPlayerMemory(player, MEMORY_COPY_FROM_POSITION),
						(List<Integer>)ErowTV.readPlayerMemory(player, MEMORY_COPY_TO_POSITION));
			}else {
				player.sendMessage("A 'Copy to block' is needed");
			}
		}else if(BlockTools.isBlockPositionTheSame(blockBehindSign,(List<Integer>)ErowTV.readPlayerMemory(player, MEMORY_COPY_TO_POSITION))) {
			//We also need the COPY_FROM position
			if(ErowTV.doesPlayerHaveSpecificMemory(player, MEMORY_COPY_FROM_POSITION)) {
				//Start the copy
				CopyBlockTool.copyFromAndToBlocks(player, clickedBlock, sign,
						(List<Integer>)ErowTV.readPlayerMemory(player, MEMORY_COPY_FROM_POSITION),
						(List<Integer>)ErowTV.readPlayerMemory(player, MEMORY_COPY_TO_POSITION));
			}else {
				player.sendMessage("A 'Copy from block' is needed");
			}

			//Create memoryNameForSign and clicked block if it returns, then its a TOOL_SIGN
		}else if(ErowTV.doesPlayerHaveSpecificMemory(player, uniqueMemory = createMemoryName(player, clickedBlock, MEMORY_TOOL_SIGN_POSITION))) {
			player.sendMessage("doesPlayerHaveSpecificMemory");

			List<Integer> signPosition = (List<Integer>)ErowTV.readPlayerMemory(player, uniqueMemory);
			Sign toolSign = (Sign) player.getWorld().getBlockAt(signPosition.get(ErowTVConstants.BLOCK_POS_X),
					signPosition.get(ErowTVConstants.BLOCK_POS_Y), signPosition.get(ErowTVConstants.BLOCK_POS_Z)).getState();

			if(toolSign != null) {
				if(isDebug) {
					player.sendMessage("TOOL_SIGN = " + toolSign.getLine(0).toLowerCase());
				}

				//Read first line
				switch(toolSign.getLine(0).toLowerCase()){
					case TOOL_COUNTDOWN_TIMER:
						new CountDownTimer(player, blockFace, blockBehindSign, toolSign, isWallSign, uniqueMemory).runTaskTimer(ErowTV.getJavaPluginErowTV(), TIME_SECOND, TIME_SECOND);
						break;
					case TOOL_YOUTUBE_SUBS:
						new YoutubeSubCounter(player, blockBehindSign, blockFace, toolSign, isWallSign, uniqueMemory).runTaskTimer(ErowTV.getJavaPluginErowTV(), TIME_SECOND, TIME_SECOND);
						break;
					case TOOL_PASTE:
						PasteBlockTool.pasteBlocks(player, clickedBlock, sign, null, signPosition, uniqueMemory);
						break;
				}

			}
		}

	}

	/**
	 * Multiple signs can be placed, but they all need a unique name.
	 *
	 * @param player
	 * @param block
	 * @param signName
	 */
	public static String createMemoryName(Player player, Block block, String signName) {
		//Get world player is in. It is needed to store the position of the block
		World.Environment environment = player.getWorld().getEnvironment();
		int playersWorld = (environment == World.Environment.NETHER ? ErowTVConstants.WORLD_NETHER : environment == World.Environment.NORMAL ? ErowTVConstants.WORLD_NORMAL : ErowTVConstants.WORLD_END);

		//The unique memory name
		return signName+
				MEMORY_SIGN_NAME_SEPERATOR+
				playersWorld+
				MEMORY_SIGN_NAME_SEPERATOR+
				block.getX()+
				MEMORY_SIGN_NAME_SEPERATOR+
				block.getY()+
				MEMORY_SIGN_NAME_SEPERATOR+
				block.getZ();
	}

	/**
	 * Multiple signs can be placed with a unique memory name
	 *
	 * @param player
	 * @param block
	 * @param memoryName
	 */
	public static void thereCanBeMore(Player player, Block block, String memoryName) {
		//Get world player is in. It is needed to store the position of the block
		World.Environment environment = player.getWorld().getEnvironment();
		int playersWorld = (environment == World.Environment.NETHER ? ErowTVConstants.WORLD_NETHER : environment == World.Environment.NORMAL ? ErowTVConstants.WORLD_NORMAL : ErowTVConstants.WORLD_END);

		//Store it in players memory
		List<Integer> toPosition = Arrays.asList(playersWorld, block.getX(), block.getY(), block.getZ());
		ErowTV.storePlayerMemory(player, memoryName, toPosition);
	}


}
