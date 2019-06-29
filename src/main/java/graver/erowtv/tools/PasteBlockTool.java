package graver.erowtv.tools;

import graver.erowtv.constants.Enumerations.DirectionalRotation;
import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;

import java.io.File;
import java.util.List;

public final class PasteBlockTool implements ErowTVConstants {

	private static int ARRAY_PLACEMENT_POS_STARTX = 0;
	private static int ARRAY_PLACEMENT_POS_STARTY = 1;
	private static int ARRAY_PLACEMENT_POS_STARTZ = 2;
	private static int ARRAY_PLACEMENT_POS_XAS = 3;
	private static int ARRAY_PLACEMENT_POS_ZAS = 4;
	private static int ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH = 5;
	private static int ARRAY_CURRENT_FACING_DIRECTION = 6;

	private PasteBlockTool() {
	}
	
	/**
	 * Paste every block from file
	 * 
	 * @param player
	 * @param sign is needed to read a name to be used for a JSON file name to paste from
	 * @param fileName if this is not empty then dont use the sign but this filename to paste from
	 * @param pasteBlock
	 */
	public static void pasteBlocks(Player player, Block clickedBlock, Sign sign, String fileName, List<Integer> pasteBlock, String memoryName) {
		//We need to check if the blocks are in the same world.
		Block blockTo = player.getWorld().getBlockAt(pasteBlock.get(ErowTVConstants.BLOCK_POS_X),
				pasteBlock.get(ErowTVConstants.BLOCK_POS_Y),pasteBlock.get(ErowTVConstants.BLOCK_POS_Z));

		//Add DIR_COPY_BLOCKS for SPECIAL_SIGN
		if(fileName == null || fileName.isEmpty()) {
			//Get file name if no filename is given
			//This only happens with SPECIAL_SIGN
			fileName = DIR_COPY_BLOCKS + sign.getLine(SPECIAL_SIGN_PARAMETER_1);
		}

		if(fileName != null && !fileName.isEmpty()) {
			//No whitespaces
			fileName = fileName.replace(' ', '_');

			File copyFile;
			//Check if file with name exists
			if((copyFile = YmlFileTool.doesFileExist(fileName)) != null) {

				//Get directions for pasting
				//If a sign is clicked then CustomBlockFace is NULL else it's the players facing direction
				int[] directions = (sign != null ? BlockTools.getBlockDirections(pasteBlock, clickedBlock, null)
									: BlockTools.getBlockDirections(pasteBlock, clickedBlock, player.getFacing()));
			
				//We can start pasting if array is filled
				if(directions.length != 0) {
					//Depth, height and width are in the file

					BlockFace blockFace;
					if(sign != null) {
						//This is for signs
						blockFace = ((org.bukkit.block.data.type.Sign) clickedBlock.getState().getBlockData()).getRotation().getOppositeFace();
						sign.getBlock().setType(Material.AIR);
					}else {
						//This is from placing on clicked blocks
						blockFace = player.getFacing();
					}

					pasteBlocksAtAllPositions(player, copyFile, directions, blockFace);
					//Set blocks and the sign to AIR
					blockTo.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);

		
					//Remove the memory after the copy
					//Only for SPECIAL_SIGN paste action. NOT for PASTE BLOCK
					if(!memoryName.equalsIgnoreCase(MEMORY_PASTE_BLOCK_ACTION)) {
						ErowTV.removeMemoryFromPlayerMemory(player, memoryName);
					}
				} else {
					player.sendMessage("Couldnt find the correct directions for pasting");
				}
			}else {
				player.sendMessage("Filename '"+fileName+"' does not exist");
			}
		}else {
			player.sendMessage("Filename is needed. Please fill in a name on the first row");
		}
	}

	/**
	 * Paste the blocks with help of calculated positions from BlockTools.getBlockDirections(fromBlock, toBlock, dataSign)
	 * 
	 * @param player
	 * @param positions
	 */
	@SuppressWarnings("deprecation")
	public static void pasteBlocksAtAllPositions(Player player, File copyFile, int[] positions, BlockFace blockFace) {
//		startX, startY, startZ, xas, zas, isNorthSouth };
		boolean isNorthSouth = (positions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH] == ErowTVConstants.IS_NORTH_SOUTH);
		int startX = positions[ARRAY_PLACEMENT_POS_STARTX];
		int startY = positions[ARRAY_PLACEMENT_POS_STARTY];
		int startZ = positions[ARRAY_PLACEMENT_POS_STARTZ];
		int xas = positions[ARRAY_PLACEMENT_POS_XAS];
		int zas = positions[ARRAY_PLACEMENT_POS_ZAS];
		int facingDirection = positions[ARRAY_CURRENT_FACING_DIRECTION];

		FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(copyFile);
		
		if(!blockConfig.contains(ErowTVConstants.YML_D_H_W_KEY)) {
			player.sendMessage("Cant find depth, height and widht in yml file");
			return;
		};
		
		String[] dhw = blockConfig.get(ErowTVConstants.YML_D_H_W_KEY).toString().split(ErowTVConstants.SEP_D_H_W);
		if(dhw.length != 4) {
			player.sendMessage("String doesnt contain depth, height and widht in yml file");
			return;
		}
		
		//Depth not needed, is calculated before placing blocks
		int height = Integer.parseInt(dhw[1]);
		int width = Integer.parseInt(dhw[2]);
		int wasFacing = Integer.parseInt(dhw[3]);
		int currentFacing = BlockTools.getCurrentBlockFaceRotation(player, blockFace);
		
		//getRotation for blocks to rotate them
		int faceRotation = BlockTools.getRotationDifference(player, wasFacing, currentFacing);
		
		//Key for next row in file
		int rowNum = 0;
		
		//Copy all the blocks that are found
		for (int iterH = 0; iterH < height; iterH++) {
			for (int iterW = 0; iterW < width; iterW++) {
				rowNum++;
				if(!blockConfig.contains(rowNum+"")) {
					player.sendMessage("Missing row to read from yml file");
					return;
				};
				//Use \\ with SEP_BLOCK to use correct split for '$'
				String[] blockRow = (blockConfig.get(rowNum+"").toString()).split("\\"+ ErowTVConstants.SEP_BLOCK);
				//Is needed for (iterD * zas) or (iterD * xas) in for loop down below
				int realDepth = 0;
								
				for (int iterD = 0; iterD < blockRow.length; iterD++) {
					int placeX, placeZ;
					
					String[] blockData = blockRow[iterD].split(ErowTVConstants.SEP_BLOCK_DATA);
					
					//Split makes first array position empty if there is no %number infront of the separator '&'
					//If isMulti then get number of blocks as Integer. Else its only 1 block.
					int numberOfBlocks = (!blockData[0].isEmpty() ? Integer.parseInt(blockData[0].substring(1)) : 1);
					//Parse needed blockData
//					int typeId = Integer.parseInt(blockData[1]);
					Material material = Material.getMaterial(blockData[1]);
//					byte data = Byte.parseByte(blockData[2]);
					
					for(int iterBlock = 0; iterBlock < numberOfBlocks; iterBlock++) {
						//If equal to 1 then its direction is NorthSouth
						if (isNorthSouth) {
							placeX = startX + (iterW * xas);
							placeZ = startZ + (realDepth * zas);
						}else {
							placeX = startX + (realDepth * xas);
							placeZ = startZ + (iterW * zas);
						}
	
						Block block = player.getWorld().getBlockAt(placeX, (startY + iterH), placeZ);
						//TODO:RG Sign data -> Text + Checst, Furnace, etc. -> items it contains
						//TODO:RG moet nog Deprecated but needed
//						block.setTypeId(typeId);
						block.setType(material);


						//Some Directionals handle there own update()
						if(block.getState().getData() instanceof Directional) {
							//if directional then blockData[3] should exist
							BlockFace directionalBlockFace = DirectionalRotation.getBlockFaceByRotation(Integer.parseInt(blockData[3]));
							player.sendMessage("Directional");
							//TODO:RG change to real saved Blockface
							BlockFace newBlockFaceDirection = BlockTools.getNewBlockFaceDirection(player, directionalBlockFace, faceRotation);
							BlockTools.changeDataForBlockType(player, block, newBlockFaceDirection, new String[] {});
						}
							
						//This one is still needed
						block.getState().update(true);
						
						//++ the realDepth for next depth position
						realDepth++;
					}
				}
			}
		}
	}
	
	
//		
//		if(block.getState().getData() instanceof Stairs) {
//			player.sendMessage("Stairs");
//			Stairs stairs = (Stairs)block.getState().getData();
////			org.bukkit.material.Sign dataSign = (org.bukkit.material.Sign)block.getData();
//			stairs.setFacingDirection((!stairs.isInverted() ? blockFace : blockFace.getOppositeFace()));
//			block.setData(stairs.getData());
//		}else if(block.getState().getData() instanceof Stairs) {
//			player.sendMessage("Stairs");
//			Furnace furnace = (Furnace)block.getState();
////			org.bukkit.material.Sign dataSign = (org.bukkit.material.Sign)block.getData();
//			org.bukkit.material.Furnace furn = ((org.bukkit.material.Furnace)furnace.getData());
//			furn.setFacingDirection(blockFace);
//			block.setData(furn.getData());
//		}
	
}
