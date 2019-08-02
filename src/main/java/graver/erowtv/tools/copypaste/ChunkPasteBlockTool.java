package graver.erowtv.tools.copypaste;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ChunkPasteBlockTool implements ErowTVConstants {

    private static HashMap<Material, List<Block>> materialBlocks;
    private static boolean isDonePasting = false;


    public static void pasteChunkPositions(Player player, File copyFile, int[] positions, BlockFace blockFace,
                                           boolean useMaterialBlocksList) {

        if (ErowTV.isDebug) {
            player.sendMessage("Chunk PASTE");
        }

        //Only fill this if we say that we want it to be filled (for games, etc.)
        if(useMaterialBlocksList) {
            materialBlocks = new HashMap<>();
        }

        boolean isNorthSouth = (positions[ARRAY_PASTE_POS_IS_NORTH_SOUTH] == ErowTVConstants.IS_NORTH_SOUTH);
        int startX = positions[ARRAY_PASTE_POS_STARTX];
        int startY = positions[ARRAY_PASTE_POS_STARTY];
        int startZ = positions[ARRAY_PASTE_POS_STARTZ];
        int xas = positions[ARRAY_PASTE_POS_XAS];
        int zas = positions[ARRAY_PASTE_POS_ZAS];

        FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(copyFile);
        LinkedHashMap<Integer, String> blockIndex = new LinkedHashMap<>();

        if (!blockConfig.contains(ErowTVConstants.YML_D_H_W_KEY)) {
            player.sendMessage("Cant find depth, height and widht in yml file");
        }

        String[] dhw = blockConfig.get(ErowTVConstants.YML_D_H_W_KEY).toString().split(ErowTVConstants.SEP_D_H_W);
        if (dhw.length != 4) {
            player.sendMessage("String doesnt contain depth, height width and facing in yml file");
        }

        int depth = Integer.parseInt(dhw[D_H_W_DEPTH]);
        int height = Integer.parseInt(dhw[D_H_W_HEIGHT]);
        int width = Integer.parseInt(dhw[D_H_W_WIDHT]);

        //Your facing direction when copying is needed to recalculate the new block directions if your looking in
        //a different direction.
        int wasFacing = Integer.parseInt(dhw[D_H_W_FACE]);
        int currentFacing = BlockTools.getCurrentBlockFaceRotation(blockFace);

        //getRotation for blocks to rotate them
        int faceRotation = BlockTools.getRotationDifference(player, wasFacing, currentFacing);

        if (ErowTV.isDebug) {
            player.sendMessage(ChatColor.DARK_AQUA + "FaceRotation=" + faceRotation +
                    " - WasFacing=" + wasFacing + " CurrentFacing=" + currentFacing);
        }

        //Index all the blockData, so that we can use the index numbers to get entire blockdata when reading the rows
        //So we dont need to read the YML over and over.
        int index = 0;
        while (true) {
            if (blockConfig.contains(BLOCK_INDEX + index)) {
                String blockData = blockConfig.get("B" + index).toString();
                blockIndex.put(index, blockData);
                index++;
            } else {
                //When no more blocks with indexes are found.
                break;
            }
        }

        new ChunkPaster(player, blockFace, startX, startZ, startY, xas, zas, isNorthSouth,
                blockConfig, blockIndex, wasFacing, materialBlocks, height, width, depth).
                runTaskTimer(ErowTV.javaPluginErowTV, TIME_ONE_TICK, (TIME_SECOND * 3));
    }

    private static class ChunkPaster extends BukkitRunnable {

        private Player player;
        private BlockFace blockFace;
        private int startX;
        private int startZ;
        private int startY;
        private int xas;
        private int zas;
        private boolean isNorthSouth;
        private FileConfiguration blockConfig;
        private LinkedHashMap<Integer, String> blockIndex;
        private int wasFacing;
        private HashMap<Material, List<Block>> materialBlocks;
        private int height;
        private int width;
        private int depth;

        public ChunkPaster(Player player, BlockFace blockFace, int startX, int startZ, int startY,
                           int xas, int zas, boolean isNorthSouth, FileConfiguration blockConfig,
                           LinkedHashMap<Integer, String> blockIndex, int wasFacing,
                           HashMap<Material, List<Block>> materialBlocks, int height, int width, int depth) {

            this.player = player;
            this.blockFace = blockFace;
            this.startX = startX;
            this.startZ = startZ;
            this.startY = startY;
            this.xas = xas;
            this.zas = zas;
            this.isNorthSouth = isNorthSouth;
            this.blockConfig = blockConfig;
            this.blockIndex = blockIndex;
            this.wasFacing = wasFacing;
            this.materialBlocks = materialBlocks;
            this.height = height;
            this.width = width;
            this.depth = depth;

            isDonePasting = false;
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

                //Block somewhere in the world
                int placeY = (startY + chunkIterH);

                pasteBlocksAtAllPositions(player, blockFace, placeX, placeZ, placeY, xas, zas, isNorthSouth,
                        blockConfig, blockIndex, chunkNum, wasFacing, materialBlocks);

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
                    isDonePasting = true;
                    this.cancel();
                    player.sendMessage(ChatColor.GREEN + "Pasting is done.");
                }
            } catch (Exception ex) {
                player.sendMessage(ChatColor.DARK_RED + "[BlockPaster-run][Exception][" + ex.getMessage() + "]");
            }
        }
    }

    /**
     * Paste the blocks with help of calculated positions from BlockTools.getBlockDirections(fromBlock, toBlock, dataSign)
     *
     * @param player
     * @param blockFace to get current facing direction
     * @return HashMap with all the asked materials their locations to use in Games
     */
    public static void pasteBlocksAtAllPositions(Player player, BlockFace blockFace, int startX, int startZ, int startY,
                                                 int xas, int zas, boolean isNorthSouth, FileConfiguration blockConfig,
                                                 LinkedHashMap<Integer, String> blockIndex, int chunkNum, int wasFacing,
                                                 HashMap<Material, List<Block>> materialBlocks) {

        if (!blockConfig.contains(chunkNum + ErowTVConstants.YML_C_D_H_W_KEY)) {
            player.sendMessage("Cant find depth, height and widht in yml file");
        }

        String[] dhw = blockConfig.get(chunkNum + ErowTVConstants.YML_C_D_H_W_KEY).toString().split(ErowTVConstants.SEP_D_H_W);
        if (dhw.length != 3) {
            player.sendMessage("String doesnt contain depth, height width and facing in yml file");
        }

        //Depth not needed, is calculated before placing blocks
        int height = Integer.parseInt(dhw[D_H_W_HEIGHT]);
        int width = Integer.parseInt(dhw[D_H_W_WIDHT]);

        //Your facing direction when copying is needed to recalculate the new block directions if your looking in
        //a different direction.
        int currentFacing = BlockTools.getCurrentBlockFaceRotation(blockFace);

        //getRotation for blocks to rotate them
        int faceRotation = BlockTools.getRotationDifference(player, wasFacing, currentFacing);

        if (ErowTV.isDebug) {
            player.sendMessage(ChatColor.DARK_AQUA + "FaceRotation=" + faceRotation +
                    " - WasFacing=" + wasFacing + " CurrentFacing=" + currentFacing);
        }

        //Paste block in the world with iteration
        pasteBlocksInTheWorld(player, startX, startZ, startY, xas, zas, height, width, chunkNum, blockConfig,
                faceRotation, isNorthSouth, blockIndex, materialBlocks);
    }

    private static void pasteBlocksInTheWorld(Player player, int startX, int startZ, int startY, int xas,
                                              int zas, int height, int width, int chunkNum, FileConfiguration blockConfig,
                                              int faceRotation, boolean isNorthSouth, LinkedHashMap<Integer, String> blockIndex,
                                              HashMap<Material, List<Block>> materialBlocks) {

        //Key for next row in file
        int rowNum = 0;

        //Paste all the blocks that are found
        for (int iterH = 0; iterH < height; iterH++) {
            for (int iterW = 0; iterW < width; iterW++) {
                rowNum++;
                if (!blockConfig.contains(chunkNum + CHUNK + rowNum)) {
                    player.sendMessage("Missing row to read from yml file");
                }

                //Use \\ with SEP_BLOCK to use correct split for '$'
                String[] blockRow = (blockConfig.get(chunkNum + CHUNK + rowNum).toString()).split("\\" + ErowTVConstants.SEP_BLOCK);
                //Is needed for (iterD * zas) or (iterD * xas) in for loop down below
                int realDepth = 0;

                for (int iterD = 0; iterD < blockRow.length; iterD++) {
                    int placeX, placeZ;

                    String[] blockData = blockRow[iterD].split(ErowTVConstants.SEP_BLOCK_DATA);
                    //Split makes first array position empty if there is no %number infront of the separator '&'
                    //If isMulti then get number of blocks as Integer. Else its only 1 block.
                    int numberOfBlocks = (!blockData[0].isEmpty() ? Integer.parseInt(blockData[0].substring(1)) : 1);

                    //Get entire blockData by index number
                    String entireBlock = blockIndex.get(Integer.parseInt(blockData[1]));

                    for (int iterBlock = 0; iterBlock < numberOfBlocks; iterBlock++) {
                        //If equal to 1 then its direction is NorthSouth
                        if (isNorthSouth) {
                            placeX = startX + (iterW * xas);
                            placeZ = startZ + (realDepth * zas);
                        } else {
                            placeX = startX + (realDepth * xas);
                            placeZ = startZ + (iterW * zas);
                        }

                        //Block somewhere in the world
                        int placeY = (startY + iterH);
                        Block block = player.getWorld().getBlockAt(placeX, placeY, placeZ);

                        //Start updating the block
                        PasteHandler.updateBlockDirections(player, block, entireBlock, faceRotation);

                        if (materialBlocks != null) {
                            //Store every block in a HashMap, so we can use it in the game
                            PasteHandler.storeAllMaterials(block, materialBlocks);
                        }

                        //++ the realDepth for next depth position
                        realDepth++;
                    }
                }
            }
        }
    }

    public static HashMap<Material, List<Block>> getMaterialBlocks(){
        return materialBlocks;
    }

    public static boolean isDonePasting(){
        return isDonePasting;
    }

}
