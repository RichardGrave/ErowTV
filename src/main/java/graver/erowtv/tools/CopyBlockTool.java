package graver.erowtv.tools;

import graver.erowtv.constants.Constants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

public final class CopyBlockTool {

	private static int ARRAY_PLACEMENT_POS_STARTX = 0;
	private static int ARRAY_PLACEMENT_POS_STARTY = 1;
	private static int ARRAY_PLACEMENT_POS_STARTZ = 2;
	private static int ARRAY_PLACEMENT_POS_DEPTH = 3;
	private static int ARRAY_PLACEMENT_POS_HEIGHT = 4;
	private static int ARRAY_PLACEMENT_POS_WIDTH = 5;
	private static int ARRAY_PLACEMENT_POS_XAS = 6;
	private static int ARRAY_PLACEMENT_POS_ZAS = 7;
	private static int ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH = 8;
	private static int ARRAY_CURRENT_FACING_DIRECTION = 9;

	private CopyBlockTool() {
	}
	
	/**
	 * Copy every blok from the COPY_FROM to the COPY_TO block
	 * Not the positions of the blocks them selfs.
	 * 
	 * @param player
	 * @param wallSign is needed to read a name to be used for a JSON file name to save it all
	 * @param fromBlock
	 * @param toBlock
	 */
	public static void copyFromAndToBlocks(Player player, Block clickedBlock, Sign wallSign, List<Integer> fromBlock, List<Integer> toBlock) {
		//We need to check if the blocks are in the same world.
		if(fromBlock.get(Constants.BLOCK_POS_WORLD) == toBlock.get(Constants.BLOCK_POS_WORLD)) {
			Block blockFrom = player.getWorld().getBlockAt(fromBlock.get(Constants.BLOCK_POS_X),
					fromBlock.get(Constants.BLOCK_POS_Y),fromBlock.get(Constants.BLOCK_POS_Z));
			
			Block blockTo = player.getWorld().getBlockAt(toBlock.get(Constants.BLOCK_POS_X),
					toBlock.get(Constants.BLOCK_POS_Y),toBlock.get(Constants.BLOCK_POS_Z));

//			org.bukkit.material.Sign dataSign = (org.bukkit.material.Sign)wallSign.getData();
			
			String fileName = wallSign.getLine(0).trim();
			
			if(fileName != null && !fileName.isEmpty()) {
				fileName = fileName.replace(' ', '_');
				int[] directions = BlockTools.getBlockDirectionsFromTo(fromBlock, toBlock, clickedBlock);
			
				//For debugging
				//player.sendMessage("DEPTH="+directions[ARRAY_PLACEMENT_POS_DEPTH] + " HEIGHT="+directions[ARRAY_PLACEMENT_POS_HEIGHT] + " WIDTH="+directions[ARRAY_PLACEMENT_POS_WIDTH]);
				//player.sendMessage("XAS="+directions[ARRAY_PLACEMENT_POS_XAS] + " ZAS="+directions[ARRAY_PLACEMENT_POS_ZAS] + " IS_NORTH_SOUTH="+directions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH]);
			
				if(directions.length != 0) {
					copyBlocksAtAllPositions(player, fileName, directions);
			
					//Set blocks and the sign to AIR
					blockFrom.setType(Material.AIR, Constants.DO_NOT_APPLY_PHYSICS);
					blockTo.setType(Material.AIR, Constants.DO_NOT_APPLY_PHYSICS);
					wallSign.getBlock().setType(Material.AIR);
		
					//Remove the memory after the copy
					ErowTV.removeMemoryFromPlayerMemory(player, Constants.MEMORY_COPY_FROM_POSITION);
					ErowTV.removeMemoryFromPlayerMemory(player, Constants.MEMORY_COPY_TO_POSITION);
				} else {
					player.sendMessage("Couldnt find the correct directions to copy from and to");
				}
			}else {
				player.sendMessage("Filename is needed. Please fill in a name on the first row");
			}
		} else {
			player.sendMessage("Blocks are not in the same world");
		}
	}
	
	/**
	 * Copy the blocks with help of calculated positions from BlockTools.getBlockDirections(fromBlock, toBlock, dataSign)
	 * 
	 * @param player
	 * @param positions
	 */
	@SuppressWarnings("deprecation")
	public static void copyBlocksAtAllPositions(Player player, String fileName, int[] positions) {		
//		startX, startY, startZ, depth, (height+1), width, xas, zas, isNorthSouth };
		boolean isNorthSouth = (positions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH] == Constants.IS_NORTH_SOUTH);
		int startX = positions[ARRAY_PLACEMENT_POS_STARTX];
		int startY = positions[ARRAY_PLACEMENT_POS_STARTY];
		int startZ = positions[ARRAY_PLACEMENT_POS_STARTZ];
		int xas = positions[ARRAY_PLACEMENT_POS_XAS];
		int zas = positions[ARRAY_PLACEMENT_POS_ZAS];
		int facingDirection = positions[ARRAY_CURRENT_FACING_DIRECTION];
		
		int depth = positions[ARRAY_PLACEMENT_POS_DEPTH];
		int height = positions[ARRAY_PLACEMENT_POS_HEIGHT];
		int width = positions[ARRAY_PLACEMENT_POS_WIDTH];
		
		//TODO:RG Write dept, width, height
		
		LinkedHashMap<String, String> blockRowData = new LinkedHashMap<>();
		int rowNum = 0;
		
		//first save depth, height and width
		blockRowData.put(Constants.YML_D_H_W_KEY, depth + Constants.SEP_D_H_W + height +
						 Constants.SEP_D_H_W + width + Constants.SEP_D_H_W + facingDirection);
		
		//Copy all the blocks that are found
		for (int iterH = 0; iterH < height; iterH++) {
			for (int iterW = 0; iterW < width; iterW++) {
				rowNum++;
				
				//Tmp values
				String rowData = "";
				String tmpBlockData = "";
				int blockCounter = 1;
				int totalCounter = 0;

				for (int iterD = 0; iterD < depth; iterD++) {
					int placeX, placeZ;

					//If equal to 1 then its direction is NorthSouth
					if (isNorthSouth) {
						placeX = startX + (iterW * xas);
						placeZ = startZ + (iterD * zas);
					}else {
						placeX = startX + (iterD * xas);
						placeZ = startZ + (iterW * zas);
					}

					Block block = player.getWorld().getBlockAt(placeX, (startY + iterH), placeZ);
					
					//TODO:RG Sign data -> Text + Checst, Furnace, etc. -> items it contains

					String[] directionalData = BlockTools.getDataForBlockType(player, block);
					
					//Deprecated but needed
					String blockData = block.getBlockData().getMaterial() + Constants.SEP_BLOCK_DATA + block.getState().getData().getData()
							+ Constants.SEP_BLOCK_DATA + (directionalData.length > 0 ? directionalData[0] : "") + Constants.SEP_BLOCK;
					
					if(!tmpBlockData.isEmpty()) {
						//Check if blocks are the same, if so then counter++ and go to the next block
						if(blockData.equalsIgnoreCase(tmpBlockData)) {
							blockCounter++;
						}else {
							//save correct number of blocks
							if(blockCounter > 1) {
								rowData += Constants.SEP_ROW_BLOCK_COUNT + blockCounter + Constants.SEP_BLOCK_DATA + tmpBlockData;
								//keep track of how many blocks have been saved. Needs to be the same as depth
								totalCounter += blockCounter;
							}else {
								//There is only one block, no counter needed
								rowData += Constants.SEP_BLOCK_DATA + tmpBlockData;
								totalCounter++;
							}
							//Next block
							tmpBlockData = blockData;
							//reset
							blockCounter = 1;
						}
					}else {
						//only firsttime
						tmpBlockData = blockData;
						continue;
					}
				}
				int diffTotalCounterDepth = depth - totalCounter;
				if(diffTotalCounterDepth > 1) {
					rowData += Constants.SEP_ROW_BLOCK_COUNT + blockCounter + Constants.SEP_BLOCK_DATA + tmpBlockData;
				}else {
					rowData += Constants.SEP_BLOCK_DATA + tmpBlockData;
				}
				
				//store rowNum (as key) with rowData
				blockRowData.put(rowNum+"", rowData);
			}
		}
		
		//When done, write data
		YmlFileTool.saveToYmlFile(fileName, player, blockRowData);
	}
	
}
