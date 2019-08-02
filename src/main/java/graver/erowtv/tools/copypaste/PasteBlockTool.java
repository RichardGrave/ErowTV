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

public class PasteBlockTool implements ErowTVConstants {

    private static HashMap<Material, List<Block>> materialBlocks;
    private static boolean isDonePasting = false;

    /**
     * Paste the blocks with help of calculated positions from BlockTools.getBlockDirections(fromBlock, toBlock, dataSign)
     *
     * @param player
     * @param copyFile  file with blocks to paste
     * @param positions for getting starting positions
     * @param blockFace to get current facing direction
     * @return HashMap with all the asked materials their locations to use in Games
     */
    public static void pasteBlocksAtAllPositions(Player player, File copyFile, int[] positions, BlockFace blockFace,
                                                 boolean useMaterialBlocksList) {

        if (ErowTV.isDebug) {
            player.sendMessage("Normal PASTE");
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
        int facingDirection = positions[ARRAY_PASTE_FACING_DIRECTION];

        LinkedHashMap<Integer, String> blockIndex = new LinkedHashMap<>();

        FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(copyFile);

        if (!blockConfig.contains(ErowTVConstants.YML_D_H_W_KEY)) {
            player.sendMessage("Cant find depth, height and width in yml file");
        }

        String[] dhw = blockConfig.get(ErowTVConstants.YML_D_H_W_KEY).toString().split(ErowTVConstants.SEP_D_H_W);
        if (dhw.length != 4) {
            player.sendMessage("String doesnt contain depth, height and width in yml file");
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

        //Depth not needed, is calculated before placing blocks
        int height = Integer.parseInt(dhw[D_H_W_HEIGHT]);
        int width = Integer.parseInt(dhw[D_H_W_WIDHT]);

        //Your facing direction when copying is needed to recalculate the new block directions if your looking in
        //a different direction.
        int wasFacing = Integer.parseInt(dhw[D_H_W_FACE]);
        int currentFacing = BlockTools.getCurrentBlockFaceRotation(blockFace);

        //getRotation for blocks to rotate them
        int faceRotation = BlockTools.getRotationDifference(player, wasFacing, currentFacing);

        if (ErowTV.isDebug) {
            player.sendMessage(ChatColor.DARK_AQUA+"FaceRotation=" + faceRotation +
                    " - WasFacing=" + wasFacing + " CurrentFacing=" + currentFacing);
        }

        //TODO:RG calculate time for Timer depending on the number of blocks to paste?

        //2 seconds for Main Thread to keep up with us?
        new BlockPaster(copyFile, width, height,
                player, startX, startZ, startY, xas, zas, faceRotation, isNorthSouth,
                blockIndex).runTaskTimer(ErowTV.javaPluginErowTV, TIME_ONE_TICK, TIME_SECOND*2);
    }

    private static class BlockPaster extends BukkitRunnable {

        private FileConfiguration blockConfig;
        private String[] blockRow;
        private int finalWidht;
        private int finalHeight;
        private Player player;
        private int startX;
        private int startZ;
        private int startY;
        private int xas;
        private int zas;
        private int faceRotation;
        private boolean isNorthSouth;
        private LinkedHashMap<Integer, String> blockIndex;
        private String pasteFileName;

        public BlockPaster(File copyFile, int finalWidht, int finalHeight,
                           Player player, int startX, int startZ, int startY, int xas, int zas, int faceRotation, boolean isNorthSouth,
                           LinkedHashMap<Integer, String> blockIndex){

            this.pasteFileName = copyFile.getName();

            //first time
            blockConfig = YamlConfiguration.loadConfiguration(copyFile);
            blockRow = (blockConfig.get(rowNum + "").toString()).split("\\" + ErowTVConstants.SEP_BLOCK);

            this.finalWidht = finalWidht;
            this.finalHeight = finalHeight;
            this.player = player;
            this.startX = startX;
            this.startZ = startZ;
            this.startY = startY;
            this.xas = xas;
            this.zas = zas;
            this.faceRotation = faceRotation;
            this.isNorthSouth = isNorthSouth;
            this.blockIndex = blockIndex;

            isDonePasting = false;
        }

        int rowNum = 1;
        int realDepth = 0;

        int tmpDepth = 0;
        int tmpWidht = 0;
        int tmpHeight = 0;
        int blocksPasted = 0;

        int tmpIterBlock = 0;

        @Override
        public void run() {
            try {
                whileEnder:
                while (blockConfig.contains(rowNum + "")) {

                    int placeX, placeZ;

                    String[] blockData = blockRow[tmpDepth].split(ErowTVConstants.SEP_BLOCK_DATA);
                    //Split makes first array position empty if there is no %number infront of the separator '&'
                    //If isMulti then get number of blocks as Integer. Else its only 1 block.
                    int numberOfBlocks = (!blockData[0].isEmpty() ? Integer.parseInt(blockData[0].substring(1)) : 1);

                    //Get entire blockData by index number
                    String entireBlock = blockIndex.get(Integer.parseInt(blockData[1]));

                    //This is for blocks that have the same Material and positioned in a sequence
                    for (int iterBlock = tmpIterBlock; iterBlock < numberOfBlocks; iterBlock++) {
                        if(blocksPasted >= MAX_BLOCKS){
                            tmpIterBlock = iterBlock;
                            blocksPasted = 0;
                            break whileEnder;
                        }

                        //If equal to 1 then its direction is NorthSouth
                        if (isNorthSouth) {
                            placeX = startX + (tmpWidht * xas);
                            placeZ = startZ + (realDepth * zas);
                        } else {
                            placeX = startX + (realDepth * xas);
                            placeZ = startZ + (tmpWidht * zas);
                        }

                        //Block somewhere in the world
                        int placeY = (startY + tmpHeight);
                        Block block = player.getWorld().getBlockAt(placeX, placeY, placeZ);

                        //Start updating the block
                        PasteHandler.updateBlockDirections(player, block, entireBlock, faceRotation);

                        if (materialBlocks != null) {
                            //Store every block in a HashMap, so we can use it in the game
                            PasteHandler.storeAllMaterials(block, materialBlocks);
                        }
                        //++ the realDepth for next depth position
                        realDepth++;

                        blocksPasted++;
                    }

                    tmpIterBlock = 0;

                    tmpDepth++;

                    if (tmpDepth >= blockRow.length) {
                        tmpDepth = 0;
                        realDepth = 0;
                        tmpWidht++;
                        rowNum++;
                        if(blockConfig.contains(rowNum + "")) {
                            blockRow = (blockConfig.get(rowNum + "").toString()).split("\\" + ErowTVConstants.SEP_BLOCK);
                        }
                    }

                    if (tmpWidht >= finalWidht) {
                        tmpWidht = 0;
                        tmpHeight++;


                    }
                    if (tmpHeight >= finalHeight) {
                        this.cancel();
                        isDonePasting = true;
                        player.sendMessage(ChatColor.GREEN + "Pasting is done. File: "+pasteFileName);
                    }
                }
            } catch (Exception ex) {
                player.sendMessage(ChatColor.DARK_RED + "[BlockPaster-run][Exception][" + ex.getMessage() + "]");
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
