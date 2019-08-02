package graver.erowtv.tools.copypaste;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.YmlFileTool;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;

public final class CopyBlockTool implements ErowTVConstants {



	private CopyBlockTool() {
	}


	/**
	 * Copy the blocks with help of calculated positions from BlockTools.getBlockDirections(fromBlock, toBlock, dataSign)
	 *
	 * @param player
	 * @param positions
	 */
	public static void copyBlocksAtAllPositions(Player player, String fileName, int[] positions) {
		if (ErowTV.isDebug) {
			player.sendMessage("Normal COPY");
		}

//		startX, startY, startZ, depth, (height+1), width, xas, zas, isNorthSouth };
		boolean isNorthSouth = (positions[ARRAY_COPY_POS_IS_NORTH_SOUTH] == ErowTVConstants.IS_NORTH_SOUTH);
		int startX = positions[ARRAY_COPY_POS_STARTX];
		int startY = positions[ARRAY_COPY_POS_STARTY];
		int startZ = positions[ARRAY_COPY_POS_STARTZ];
		int xas = positions[ARRAY_COPY_POS_XAS];
		int zas = positions[ARRAY_COPY_POS_ZAS];

		int facingDirection = positions[ARRAY_COPY_FACING_DIRECTION];
		boolean isFromBlockYGreater = (positions[ARRAY_COPY_FROM_Y_GREATER]==1 ? true : false);

		int depth = positions[ARRAY_COPY_POS_DEPTH];
		int height = positions[ARRAY_COPY_POS_HEIGHT];
		int width = positions[ARRAY_COPY_POS_WIDTH];

		int rowNum = 0;
		int index = 0;
		int tmpIndexNum = 0;

		//Should be faster then pasting. Do this every 5 ticks. (250ms)
		new BlockCopier(player, fileName, startX, startY, startZ, xas, zas, depth, height, width, facingDirection,
				isFromBlockYGreater, isNorthSouth).runTaskTimer(ErowTV.javaPluginErowTV, TIME_ONE_TICK, TIME_SECOND);
	}

	private static class BlockCopier extends BukkitRunnable {

		//Easy way to get the key for the value. Needed when reading YML file.
		//Then it's easier to loop through the file and get the correct blockIndex when tryint to paste.
		private LinkedHashMap<String, String> blockIndexReverse = new LinkedHashMap<>();
		private LinkedHashMap<String, String> blockIndex = new LinkedHashMap<>();
		private LinkedHashMap<String, String> blockFileData = new LinkedHashMap<>();

		private Player player;
		private String fileName;
		private int startX;
		private int startZ;
		private int startY;
		private int xas;
		private int zas;
		private int height;
		private int width;
		private int depth;
		private boolean isNorthSouth;
		private boolean isFromBlockYGreater;

		public BlockCopier(Player player, String fileName,
						   int startX, int startY, int startZ, int xas, int zas,
						   int depth, int height, int width, int facingDirection,
						   boolean isFromBlockYGreater, boolean isNorthSouth) {

			//first save depth, height and width
			blockIndex.put(ErowTVConstants.YML_D_H_W_KEY, depth + ErowTVConstants.SEP_D_H_W + height +
					ErowTVConstants.SEP_D_H_W + width + ErowTVConstants.SEP_D_H_W + facingDirection);

			player.sendMessage("depth="+depth);
			player.sendMessage("width="+width);
			player.sendMessage("height="+height);

			this.player = player;
			this.fileName = fileName;
			this.startX = startX;
			this.startZ = startZ;
			this.startY = startY;
			this.xas = xas;
			this.zas = zas;
			this.height = height;
			this.width = width;
			this.depth = depth;
			this.isNorthSouth = isNorthSouth;
			this.isFromBlockYGreater = isFromBlockYGreater;
		}

		//Tmp values
		String rowData = "";
		String tmpBlockData = "";
		int blockCounter = 1;
		int totalCounter = 0;
		int blocksCopied = 0;

		int index = 0;
		int rowNum = 0;

		int iterH = 0;
		int iterW = 0;
		int iterD = 0;

		//Copy should be faster then pasting
		int MAX_BLOCKS_COPY = MAX_BLOCKS * 4;

		@Override
		public void run() {
			try {
				blocksCopied = 0;

				while (blocksCopied <= MAX_BLOCKS_COPY) {
					int placeX, placeZ;

					//If equal to 1 then its direction is NorthSouth
					if (isNorthSouth) {
						placeX = startX + (iterW * xas);
						placeZ = startZ + (iterD * zas);
					}else {
						placeX = startX + (iterD * xas);
						placeZ = startZ + (iterW * zas);
					}

					int placeY = (isFromBlockYGreater ? (startY - iterH) : (startY + iterH));

					Block block = player.getWorld().getBlockAt(placeX, placeY, placeZ);
					//Entire block data(Facing, activated, etc.) into one String.
					String entireBlock = block.getBlockData().getAsString();

					//Keep track of blocks in Reversed list.
					if(blockIndexReverse.containsKey(entireBlock)){
						//Easy way of getting block key.
						index = Integer.parseInt(blockIndexReverse.get(entireBlock).substring(1));
					}else{
						index = blockIndexReverse.size();
						//Add "B" so we know when reading the YML file this is a block that is used
						//in the rows (that also start with numbers)
						blockIndexReverse.put(entireBlock, BLOCK_INDEX+index);
						blockIndex.put(BLOCK_INDEX+index, entireBlock);
					}

					//Deprecated but needed
					String blockData = index + ErowTVConstants.SEP_BLOCK;

					if(!tmpBlockData.isEmpty()) {
						//Check if blocks are the same, if so then counter++ and go to the next block
						if(blockData.equalsIgnoreCase(tmpBlockData)) {
							blockCounter++;
						}else {
							//save correct number of blocks
							if(blockCounter > 1) {
								rowData += ErowTVConstants.SEP_ROW_BLOCK_COUNT + blockCounter + ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
								//keep track of how many blocks have been saved. Needs to be the same as depth
								totalCounter += blockCounter;
							}else {
								//There is only one block, no counter needed
								rowData += ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
								totalCounter++;
							}
							//Next block
							tmpBlockData = blockData;
							//reset
							blockCounter = 1;
						}
					}else {
						//only firsttime
						iterD++;
						blocksCopied++;
						tmpBlockData = blockData;
						continue;
					}

					blocksCopied++;
					iterD++;

					if (iterD >= depth) {
						iterD = 0;
						iterW++;

						rowNum++;

						//If more than one of the same blocks are found in sequence then the totalCounter is smaller
						//then the depth. And should be greater than 1.
						int diffTotalCounterDepth = depth - totalCounter;
						if(diffTotalCounterDepth > 1) {
							rowData += ErowTVConstants.SEP_ROW_BLOCK_COUNT + blockCounter + ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
						}else {
							rowData += ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
						}
						blockFileData.put(rowNum+"", rowData);

						//reset
						rowData = "";
						tmpBlockData = "";
						blockCounter = 1;
						totalCounter = 0;
					}

					if (iterW >= width) {
						iterW = 0;
						iterH++;
					}

					if (iterH >= height) {
						this.cancel();

						//Combine blockIndex and all the copy blocks together so they get into one file.
						blockIndex.putAll(blockFileData);

						//When done, write data
						YmlFileTool.saveToYmlFile(fileName, player, blockIndex);

						//break loop or it will continue
						break;
					}
				}
			} catch (Exception ex) {
				player.sendMessage(ChatColor.DARK_RED + "[BlockPaster-run][Exception][" + ex.getMessage() + "]");
			}
		}
	}
}
