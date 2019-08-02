package graver.erowtv.tools.copypaste;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.YmlFileTool;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;

public final class ChunkCopyBlockTool implements ErowTVConstants {


    private ChunkCopyBlockTool() {
    }


    public static void calculateChunkPositions(Player player, String fileName, int[] positions) {
        if (ErowTV.isDebug) {
            player.sendMessage("Chunk COPY");
        }

        boolean isNorthSouth = (positions[ARRAY_COPY_POS_IS_NORTH_SOUTH] == ErowTVConstants.IS_NORTH_SOUTH);
        int startX = positions[ARRAY_COPY_POS_STARTX];
        int startY = positions[ARRAY_COPY_POS_STARTY];
        int startZ = positions[ARRAY_COPY_POS_STARTZ];
        int xas = positions[ARRAY_COPY_POS_XAS];
        int zas = positions[ARRAY_COPY_POS_ZAS];

        int facingDirection = positions[ARRAY_COPY_FACING_DIRECTION];
        boolean isFromBlockYGreater = (positions[ARRAY_COPY_FROM_Y_GREATER] == 1 ? true : false);

        int depth = positions[ARRAY_COPY_POS_DEPTH];
        int height = positions[ARRAY_COPY_POS_HEIGHT];
        int width = positions[ARRAY_COPY_POS_WIDTH];

        //TODO:RG try to create a progress indicator

        //Should be faster then pasting. Do this every 5 ticks. (250ms)
        new ChunkCopier(player, fileName, startX, startY, startZ, xas, zas, depth, height, width,
                facingDirection, isFromBlockYGreater, isNorthSouth).
                runTaskTimer(ErowTV.javaPluginErowTV, TIME_ONE_TICK, (TIME_ONE_TICK * 5));
    }

    private static class ChunkCopier extends BukkitRunnable {

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
        private int facingDirection;
        private int height;
        private int width;
        private int depth;
        private boolean isNorthSouth;
        private boolean isFromBlockYGreater;

        public ChunkCopier(Player player, String fileName,
                           int startX, int startY, int startZ, int xas, int zas,
                           int depth, int height, int width, int facingDirection,
                           boolean isFromBlockYGreater, boolean isNorthSouth) {

            //first save depth, height and width
            blockFileData.put(ErowTVConstants.YML_D_H_W_KEY, depth + ErowTVConstants.SEP_D_H_W + height +
                    ErowTVConstants.SEP_D_H_W + width + ErowTVConstants.SEP_D_H_W + facingDirection);

            this.player = player;
            this.fileName = fileName;
            this.startX = startX;
            this.startZ = startZ;
            this.startY = startY;
            this.xas = xas;
            this.zas = zas;
            this.facingDirection = facingDirection;
            this.height = height;
            this.width = width;
            this.depth = depth;
            this.isNorthSouth = isNorthSouth;
            this.isFromBlockYGreater = isFromBlockYGreater;
        }

        int chunkIterH = 0;
        int chunkIterW = 0;
        int chunkIterD = 0;
        int chunkNum = 0;

        @Override
        public void run() {
            try {
                chunkNum++;
                int placeX, placeZ;

                //If equal to 1 then its direction is NorthSouth
                if (isNorthSouth) {
                    placeX = startX + (chunkIterW * xas);
                    placeZ = startZ + (chunkIterD * zas);
                } else {
                    placeX = startX + (chunkIterD * xas);
                    placeZ = startZ + (chunkIterW * zas);
                }

                int placeY = (isFromBlockYGreater ? (startY - chunkIterH) : (startY + chunkIterH));

                int calculatedHeight = (height - chunkIterH);
                int calculatedWidth = (width - chunkIterW);
                int calculatedDepth = (depth - chunkIterD);

                int chunkHeight = (calculatedHeight >= MAX_CHUNK_HEIGHT ? MAX_CHUNK_HEIGHT : calculatedHeight);
                int chunkWidth = (calculatedWidth >= MAX_CHUNK_WIDTH ? MAX_CHUNK_WIDTH : calculatedWidth);
                int chunkDepth = (calculatedDepth >= MAX_CHUNK_DEPT ? MAX_CHUNK_DEPT : calculatedDepth);

                copyBlocksAtAllPositions(player, chunkNum, placeX, placeY, placeZ, xas, zas,
                        chunkDepth, chunkHeight, chunkWidth, isFromBlockYGreater, isNorthSouth, blockFileData,
                        blockIndex, blockIndexReverse);

                chunkIterD += MAX_CHUNK_DEPT;

                if (chunkIterD >= depth) {
                    chunkIterD = 0;
                    chunkIterW += MAX_CHUNK_WIDTH;
                }

                if (chunkIterW >= width) {
                    chunkIterW = 0;
                    chunkIterH += MAX_CHUNK_HEIGHT;
                }
                if (chunkIterH >= height) {
                    //Combine blockIndex and all the copy blocks together so they get into one file.
                    blockIndex.putAll(blockFileData);

                    //When done, write data
                    YmlFileTool.saveToYmlFile(fileName, player, blockIndex);
                    this.cancel();
                }
            } catch (Exception ex) {
                player.sendMessage(ChatColor.DARK_RED + "[BlockPaster-run][Exception][" + ex.getMessage() + "]");
            }
        }
    }


    /**
     * Copy the blocks with help of calculated positions from BlockTools.getBlockDirections(fromBlock, toBlock, dataSign)
     *
     * @param player
     */
    public static void copyBlocksAtAllPositions(Player player, int chunkNum,
                                                int startX, int startY, int startZ, int xas, int zas,
                                                int depth, int height, int width,
                                                boolean isFromBlockYGreater, boolean isNorthSouth,
                                                LinkedHashMap<String, String> blockFileData,
                                                LinkedHashMap<String, String> blockIndex,
                                                LinkedHashMap<String, String> blockIndexReverse) {

        int rowNum = 0;
        int index = 0;

        //first save depth, height and width for chunk
        blockFileData.put(chunkNum+ErowTVConstants.YML_C_D_H_W_KEY, depth + ErowTVConstants.SEP_D_H_W + height +
                ErowTVConstants.SEP_D_H_W + width);

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
                    } else {
                        placeX = startX + (iterD * xas);
                        placeZ = startZ + (iterW * zas);
                    }

                    int placeY = (isFromBlockYGreater ? (startY - iterH) : (startY + iterH));

                    Block block = player.getWorld().getBlockAt(placeX, placeY, placeZ);
                    //Entire block data(Facing, activated, etc.) into one String.
                    String entireBlock = block.getBlockData().getAsString();

                    //Keep track of blocks in Reversed list.
                    if (blockIndexReverse.containsKey(entireBlock)) {
                        //Easy way of getting block key.
                        index = Integer.parseInt(blockIndexReverse.get(entireBlock).substring(1));
                    } else {
                        index = blockIndexReverse.size();
                        //Add "B" so we know when reading the YML file this is a block that is used
                        //in the rows (that also start with numbers)
                        blockIndexReverse.put(entireBlock, BLOCK_INDEX + index);
                        blockIndex.put(BLOCK_INDEX + index, entireBlock);
                    }

                    String blockData = index + ErowTVConstants.SEP_BLOCK;

                    if (!tmpBlockData.isEmpty()) {
                        //Check if blocks are the same, if so then counter++ and go to the next block
                        if (blockData.equalsIgnoreCase(tmpBlockData)) {
                            blockCounter++;
                        } else {
                            //save correct number of blocks
                            if (blockCounter > 1) {
                                rowData += ErowTVConstants.SEP_ROW_BLOCK_COUNT + blockCounter + ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
                                //keep track of how many blocks have been saved. Needs to be the same as depth
                                totalCounter += blockCounter;
                            } else {
                                //There is only one block, no counter needed
                                rowData += ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
                                totalCounter++;
                            }
                            //Next block
                            tmpBlockData = blockData;
                            //reset
                            blockCounter = 1;
                        }
                    } else {
                        //only firsttime
                        tmpBlockData = blockData;
                        continue;
                    }
                }
                //If more than one of the same blocks are found in sequence then the totalCounter is smaller
                //then the depth. And should be greater than 1.
                int diffTotalCounterDepth = depth - totalCounter;

                if (diffTotalCounterDepth > 1) {
                    rowData += ErowTVConstants.SEP_ROW_BLOCK_COUNT + blockCounter + ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
                } else {
                    rowData += ErowTVConstants.SEP_BLOCK_DATA + tmpBlockData;
                }

                //store rowNum (as key) with rowData
                blockFileData.put(chunkNum+CHUNK+rowNum, rowData);
            }
        }
    }
}