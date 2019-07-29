package graver.erowtv.tools;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class ExperimentalPasteBlockTool implements ErowTVConstants {

    private static int ARRAY_PLACEMENT_POS_STARTX = 0;
    private static int ARRAY_PLACEMENT_POS_STARTY = 1;
    private static int ARRAY_PLACEMENT_POS_STARTZ = 2;
    private static int ARRAY_PLACEMENT_POS_XAS = 3;
    private static int ARRAY_PLACEMENT_POS_ZAS = 4;
    private static int ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH = 5;
    private static int ARRAY_CURRENT_FACING_DIRECTION = 6;

    private ExperimentalPasteBlockTool() {
    }

    /**
     * Paste every block from file
     *
     * @param player
     * @param sign       is needed to read a name to be used for a JSON file name to paste from
     * @param fileName   if this is not empty then dont use the sign but this filename to paste from
     * @param pasteBlock
     */
    public static void pasteBlocks(Player player, Block clickedBlock, Sign sign, String fileName, List<Integer> pasteBlock) {
        //We need to check if the blocks are in the same world.
        Block blockTo = player.getWorld().getBlockAt(pasteBlock.get(ErowTVConstants.BLOCK_POS_X),
                pasteBlock.get(ErowTVConstants.BLOCK_POS_Y), pasteBlock.get(ErowTVConstants.BLOCK_POS_Z));

        //Add DIR_COPY_BLOCKS for SPECIAL_SIGN
        if (fileName == null || fileName.isEmpty()) {
            //Get file name if no filename is given
            //This only happens with SPECIAL_SIGN
            fileName = DIR_COPY_BLOCKS + sign.getLine(SPECIAL_SIGN_PARAMETER_1);
        }

        if (fileName != null && !fileName.isEmpty()) {
            //No whitespaces
            fileName = fileName.replace(' ', '_');

            File copyFile;
            //Check if file with name exists
            if ((copyFile = YmlFileTool.doesFileExist(fileName)) != null) {

                //Get directions for pasting
                //If a sign is clicked then CustomBlockFace is NULL else it's the players facing direction
                int[] directions = (sign != null ? BlockTools.getBlockDirections(pasteBlock, clickedBlock, null)
                        : BlockTools.getBlockDirections(pasteBlock, clickedBlock, player.getFacing()));

                //We can start pasting if array is filled
                if (directions.length != 0) {
                    //Depth, height and width are in the file

                    BlockFace blockFace;
                    if (sign != null) {
                        //This is for signs
                        blockFace = ((org.bukkit.block.data.type.Sign) clickedBlock.getState().getBlockData()).getRotation().getOppositeFace();
                        sign.getBlock().setType(Material.AIR);
                    } else {
                        //This is from placing on clicked blocks
                        blockFace = player.getFacing();
                    }

                    //No blocks need to be added to a materialBlocks HashMap
                    pasteChunkPositions(player, copyFile, directions, blockFace, null);
                    //Set blocks and the sign to AIR
                    blockTo.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);

                } else {
                    player.sendMessage("Couldnt find the correct directions for pasting");
                }
            } else {
                player.sendMessage("Filename '" + fileName + "' does not exist");
            }
        } else {
            player.sendMessage("Filename is needed. Please fill in a name on the first row");
        }
    }


    public static void pasteChunkPositions(Player player, File copyFile, int[] positions, BlockFace blockFace,
                                           HashMap<Material, List<Block>> materialBlocks) {


        //		startX, startY, startZ, xas, zas, isNorthSouth };
        boolean isNorthSouth = (positions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH] == ErowTVConstants.IS_NORTH_SOUTH);
        int startX = positions[ARRAY_PLACEMENT_POS_STARTX];
        int startY = positions[ARRAY_PLACEMENT_POS_STARTY];
        int startZ = positions[ARRAY_PLACEMENT_POS_STARTZ];
        int xas = positions[ARRAY_PLACEMENT_POS_XAS];
        int zas = positions[ARRAY_PLACEMENT_POS_ZAS];

        FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(copyFile);
        LinkedHashMap<Integer, String> blockIndex = new LinkedHashMap<>();

        if (!blockConfig.contains(ErowTVConstants.YML_TOTAL_D_H_W_F_KEY)) {
            player.sendMessage("Cant find depth, height and widht in yml file");
        }

        String[] dhw = blockConfig.get(ErowTVConstants.YML_TOTAL_D_H_W_F_KEY).toString().split(ErowTVConstants.SEP_D_H_W);
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
                runTaskTimer(ErowTV.javaPluginErowTV, TIME_ONE_TICK, (TIME_SECOND*2));
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
//                        rowNum++;
//                        if(blockConfig.contains(rowNum + "")) {
//                            blockRow = (blockConfig.get(rowNum + "").toString()).split("\\" + ErowTVConstants.SEP_BLOCK);
//                        }
                    }

                    if (chunkIterW >= width) {
                        chunkIterW = 0;
                        chunkIterH += MAX_CHUNK_HEIGHT;
                    }
                    if (chunkIterH >= height) {
                        this.cancel();
                        player.sendMessage(ChatColor.GREEN + "Pasting is done.");
//                        isDonePasting = true;
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


        //TODO:RG Magic numbers, never use forbidden magic
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

    private static void
    pasteBlocksInTheWorld(Player player, int startX, int startZ, int startY, int xas, int zas, int height, int width,
                          int chunkNum, FileConfiguration blockConfig, int faceRotation, boolean isNorthSouth,
                          LinkedHashMap<Integer, String> blockIndex, HashMap<Material, List<Block>> materialBlocks) {

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
                        updateBlockDirections(player, block, entireBlock, faceRotation);

                        if (materialBlocks != null) {
                            //Store every block in a HashMap, so we can use it in the game
                            storeAllMaterials(player, block, placeX, placeY, placeZ, materialBlocks);
                        }

                        //++ the realDepth for next depth position
                        realDepth++;
                    }
                }
            }
        }
    }

    private static void storeAllMaterials(Player player, Block block, int x, int y, int z,
                                          HashMap<Material, List<Block>> materialBlocks) {

        Material blockMaterial = block.getBlockData().getMaterial();

        if (materialBlocks.containsKey(blockMaterial)) {
            //if it does exist, then add block to list in materials
            materialBlocks.get(blockMaterial).add(block);
        } else {
            //If it does not exist, create new new List to store the blocks
            List<Block> blockList = new ArrayList<>();
            blockList.add(block);

            //Put into HashMap with Material and blockLocation
            materialBlocks.put(blockMaterial, blockList);
        }
    }

    private static void updateBlockDirections(Player player, Block block, String entireBlock, int faceRotation) {

        //Blocks with facing need a new recalculated facing (by BlockFace direction from Copy sign)
        if (entireBlock.contains("facing=")) {
            //Get new BlockFace direction by using old BlockFace and the ratation
            BlockFace blockFaceFromSavedBlock = BlockTools.getBlockFaceByString(player, entireBlock);
            BlockFace newBlockFaceDirection = BlockTools.getNewBlockFaceDirection(player, blockFaceFromSavedBlock, faceRotation);

            //Replace old 'facing=' with the new 'facing='
            entireBlock = BlockTools.replaceBlockFaceDirection(player, entireBlock, newBlockFaceDirection);
        }

        //Some blocks don't have facing but 'axis='
        if (entireBlock.contains("axis=")) {
            entireBlock = BlockTools.replaceAxisDirection(entireBlock, faceRotation);
        }

        //Glass has boolean directions for north, east, south and west.
        if (entireBlock.contains("north=") || entireBlock.contains("east=") ||
                entireBlock.contains("south=") || entireBlock.contains("west=")) {

            //This if for Redstone that uses 'side, up and none'. Example: north=up, south=side, east=none, west=up
            if (entireBlock.contains("power=")) {
                entireBlock = BlockTools.replaceTernaryDirections(entireBlock, faceRotation);
            } else {
                //This if for blocks that use true or false. Example: north=true, south=false, east=false, west=false
                entireBlock = BlockTools.replaceBooleanDirections(entireBlock, faceRotation);
            }
        }


        block.setBlockData(Bukkit.getServer().createBlockData(entireBlock));
    }
}
