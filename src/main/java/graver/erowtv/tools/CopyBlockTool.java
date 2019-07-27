//package graver.erowtv.tools;
//
//import graver.erowtv.constants.ErowTVConstants;
//import graver.erowtv.item.BlockTools;
//import graver.erowtv.main.ErowTV;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.block.Sign;
//import org.bukkit.entity.Player;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//
//public final class CopyBlockTool implements ErowTVConstants {
//
//
//
//	private CopyBlockTool() {
//	}
//
//	/**
//	 * Copy every blok from the COPY_FROM to the COPY_TO block
//	 * Not the positions of the blocks them selfs.
//	 *
//	 * @param player
//	 * @param wallSign is needed to read a name to be used for a JSON file name to save it all
//	 * @param fromBlock
//	 * @param toBlock
//	 */
//	public static void copyFromAndToBlocks(Player player, Block clickedBlock, Sign wallSign, List<Integer> fromBlock, List<Integer> toBlock) {
//		//We need to check if the blocks are in the same world.
//		if(fromBlock.get(ErowTVConstants.BLOCK_POS_WORLD) == toBlock.get(ErowTVConstants.BLOCK_POS_WORLD)) {
//			Block blockFrom = player.getWorld().getBlockAt(fromBlock.get(ErowTVConstants.BLOCK_POS_X),
//					fromBlock.get(ErowTVConstants.BLOCK_POS_Y),fromBlock.get(ErowTVConstants.BLOCK_POS_Z));
//
//			Block blockTo = player.getWorld().getBlockAt(toBlock.get(ErowTVConstants.BLOCK_POS_X),
//					toBlock.get(ErowTVConstants.BLOCK_POS_Y),toBlock.get(ErowTVConstants.BLOCK_POS_Z));
//
////			org.bukkit.material.Sign dataSign = (org.bukkit.material.Sign)wallSign.getData();
//
//			String fileName = wallSign.getLine(COPY_BLOCK_SIGN_FILE_NAME).trim();
//
//			if(fileName != null && !fileName.isEmpty()) {
//				fileName = DIR_COPY_BLOCKS + fileName.replace(' ', '_');
//
//				//Copy block has a sign to click on for directions, so CustomBlockFace = NULL
//				int[] directions = BlockTools.getBlockDirectionsFromTo(fromBlock, toBlock, clickedBlock, null);
//
//				if(ErowTV.isDebug) {
//					player.sendMessage(ChatColor.DARK_AQUA+"DEPTH=" + directions[ARRAY_PLACEMENT_POS_DEPTH] + " HEIGHT=" + directions[ARRAY_PLACEMENT_POS_HEIGHT] + " WIDTH=" + directions[ARRAY_PLACEMENT_POS_WIDTH]);
//					player.sendMessage(ChatColor.DARK_AQUA+"XAS=" + directions[ARRAY_PLACEMENT_POS_XAS] + " ZAS=" + directions[ARRAY_PLACEMENT_POS_ZAS] + " IS_NORTH_SOUTH=" + directions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH]);
//				}
//
//				if(directions.length != 0) {
//					copyBlocksAtAllPositions(player, fileName, directions);
//
//					//Set blocks and the sign to AIR
//					blockFrom.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);
//					blockTo.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);
//					wallSign.getBlock().setType(Material.AIR);
//
//					//Remove the memory after the copy
//					ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_COPY_FROM_POSITION);
//					ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_COPY_TO_POSITION);
//				} else {
//					player.sendMessage("Couldnt find the correct directions to copy from and to");
//				}
//			}else {
//				player.sendMessage("Filename is needed. Please fill in a name on the first row");
//			}
//		} else {
//			player.sendMessage("Blocks are not in the same world");
//		}
//	}
//
//	/**
//	 * Copy the blocks with help of calculated positions from BlockTools.getBlockDirections(fromBlock, toBlock, dataSign)
//	 *
//	 * @param player
//	 * @param positions
//	 */
//	@SuppressWarnings("deprecation")
//	public static void copyBlocksAtAllPositions(Player player, String fileName, int[] positions) {
////		startX, startY, startZ, depth, (height+1), width, xas, zas, isNorthSouth };
//		boolean isNorthSouth = (positions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH] == ErowTVConstants.IS_NORTH_SOUTH);
//		int startX = positions[ARRAY_PLACEMENT_POS_STARTX];
//		int startY = positions[ARRAY_PLACEMENT_POS_STARTY];
//		int startZ = positions[ARRAY_PLACEMENT_POS_STARTZ];
//		int xas = positions[ARRAY_PLACEMENT_POS_XAS];
//		int zas = positions[ARRAY_PLACEMENT_POS_ZAS];
//
//		int facingDirection = positions[ARRAY_CURRENT_FACING_DIRECTION];
//		boolean isFromBlockYGreater = (positions[ARRAY_PLACEMENT_FROM_Y_GREATER]==1 ? true : false);
//
//		int depth = positions[ARRAY_PLACEMENT_POS_DEPTH];
//		int height = positions[ARRAY_PLACEMENT_POS_HEIGHT];
//		int width = positions[ARRAY_PLACEMENT_POS_WIDTH];
//
//		LinkedHashMap<String, String> blockFileData = new LinkedHashMap<>();
//		LinkedHashMap<String, String> blockIndex = new LinkedHashMap<>();
//
//		//Easy way to get the key for the value. Needed when reading YML file.
//		//Then it's easier to loop through the file and get the correct blockIndex when tryint to paste.
//		LinkedHashMap<String, String> blockIndexReverse = new LinkedHashMap<>();
//
//		int rowNum = 0;
//		int index = 0;
//		int tmpIndexNum = 0;
//
//		//first save depth, height and width
//		blockIndex.put(ErowTVConstants.YML_D_H_W_KEY, depth + ErowTVConstants.SEP_D_H_W + height +
//						 ErowTVConstants.SEP_D_H_W + width + ErowTVConstants.SEP_D_H_W + facingDirection);
//
//		//Copy all the blocks that are found
//		for (int iterH = 0; iterH < height; iterH++) {
//			for (int iterW = 0; iterW < width; iterW++) {
//				rowNum++;
//
//				//Tmp values
//				String rowData = "";
//				String tmpBlockData = "";
//				int blockCounter = 1;
//				int totalCounter = 0;
//
//				for (int iterD = 0; iterD < depth; iterD++) {
//					int placeX, placeZ;
//
//					//If equal to 1 then its direction is NorthSouth
//					if (isNorthSouth) {
//						placeX = startX + (iterW * xas);
//						placeZ = startZ + (iterD * zas);
//					}else {
//						placeX = startX + (iterD * xas);
//						placeZ = startZ + (iterW * zas);
//					}
//
//					int placeY = (isFromBlockYGreater ? (startY - iterH) : (startY + iterH));
//
//					Block block = player.getWorld().getBlockAt(placeX, placeY, placeZ);
//					//Entire block data(Facing, activated, etc.) into one String.
//					String entireBlock = block.getBlockData().getAsString();
//
//					//Keep track of blocks in Reversed list.
//					if(blockIndexReverse.containsKey(entireBlock)){
//						//Easy way of getting block key.
//						index = Integer.parseInt(blockIndexReverse.get(entireBlock).substring(1));
//					}else{
//						index = tmpIndexNum;
//						//Add "B" so we know when reading the YML file this is a block that is used
//						//in the rows (that also start with numbers)
//						blockIndexReverse.put(entireBlock, BLOCK_INDEX+index);
//						blockIndex.put(BLOCK_INDEX+index, entireBlock);
//						tmpIndexNum++;
//					}
//
//					//Deprecated but needed
//					String blockData = index + ErowTVConstants.SEP_BLOCK;
//
//					if(!tmpBlockData.isEmpty()) {
//						//Check if blocks are the same, if so then counter++ and go to the next block
//						if(blockData.equalsIgnoreCase(tmpBlockData)) {
//							blockCounter++;
//						}else {
//							//save correct number of blocks
//							if(blockCounter > 1) {
//								rowData += ErowTVConstants.SEP_ROW_BLOCK_COUNT + blockCounter + ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
//								//keep track of how many blocks have been saved. Needs to be the same as depth
//								totalCounter += blockCounter;
//							}else {
//								//There is only one block, no counter needed
//								rowData += ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
//								totalCounter++;
//							}
//							//Next block
//							tmpBlockData = blockData;
//							//reset
//							blockCounter = 1;
//						}
//					}else {
//						//only firsttime
//						tmpBlockData = blockData;
//						continue;
//					}
//				}
//				//If more than one of the same blocks are found in sequence then the totalCounter is smaller
//				//then the depth. And should be greater than 1.
//				int diffTotalCounterDepth = depth - totalCounter;
//				if(diffTotalCounterDepth > 1) {
//					rowData += ErowTVConstants.SEP_ROW_BLOCK_COUNT + blockCounter + ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
//				}else {
//					rowData += ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
//				}
//
//				//store rowNum (as key) with rowData
//				blockFileData.put(rowNum+"", rowData);
//			}
//		}
//
//		//Combine blockIndex and all the copy blocks together so they get into one file.
//		blockIndex.putAll(blockFileData);
//
//		//When done, write data
//		YmlFileTool.saveToYmlFile(fileName, player, blockIndex);
//	}
//
//}
