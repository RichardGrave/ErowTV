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
                    pasteBlocksAtAllPositions(player, copyFile, directions, blockFace, null);
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
                                                 HashMap<Material, List<Block>> materialBlocks) {

//		startX, startY, startZ, xas, zas, isNorthSouth };
        boolean isNorthSouth = (positions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH] == ErowTVConstants.IS_NORTH_SOUTH);
        int startX = positions[ARRAY_PLACEMENT_POS_STARTX];
        int startY = positions[ARRAY_PLACEMENT_POS_STARTY];
        int startZ = positions[ARRAY_PLACEMENT_POS_STARTZ];
        int xas = positions[ARRAY_PLACEMENT_POS_XAS];
        int zas = positions[ARRAY_PLACEMENT_POS_ZAS];
        int facingDirection = positions[ARRAY_CURRENT_FACING_DIRECTION];

        LinkedHashMap<Integer, String> blockIndex = new LinkedHashMap<>();

        FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(copyFile);

        if (!blockConfig.contains(ErowTVConstants.YML_D_H_W_KEY)) {
            player.sendMessage("Cant find depth, height and widht in yml file");
        }

        String[] dhw = blockConfig.get(ErowTVConstants.YML_D_H_W_KEY).toString().split(ErowTVConstants.SEP_D_H_W);
        if (dhw.length != 4) {
            player.sendMessage("String doesnt contain depth, height and widht in yml file");
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

        //TODO:RG Magic numbers, never use forbidden magic
        //Depth not needed, is calculated before placing blocks
        int height = Integer.parseInt(dhw[1]);
        int width = Integer.parseInt(dhw[2]);

        //Your facing direction when copying is needed to recalculate the new block directions if your looking in
        //a different direction.
        int wasFacing = Integer.parseInt(dhw[3]);
        int currentFacing = BlockTools.getCurrentBlockFaceRotation(blockFace);

        //getRotation for blocks to rotate them
        int faceRotation = BlockTools.getRotationDifference(player, wasFacing, currentFacing);

        if (ErowTV.isDebug) {
            player.sendMessage(ChatColor.DARK_AQUA + "FaceRotation=" + faceRotation +
                    " - WasFacing=" + wasFacing + " CurrentFacing=" + currentFacing);
        }

        //Paste block in the world with iteration
//        pasteBlocksInTheWorld(player, startX, startZ, startY, xas, zas, height, width, blockConfig, faceRotation,
//                isNorthSouth, blockIndex, materialBlocks);

        new BlockPaster(copyFile, width, height,
                player, startX, startZ, startY, xas, zas, faceRotation, isNorthSouth,
                blockIndex, materialBlocks).runTaskTimer(ErowTV.javaPluginErowTV, TIME_ONE_TICK, TIME_HALF_SECOND);
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
        block.getState().update(true);
    }

    //Exmperimental Thread paste.
    //When a copy is insanely large, then the server will crash during the paste function.
    //It can be just to big.
    private static class BlockPaster extends BukkitRunnable {


        FileConfiguration blockConfig;

        int rowNum = 1;
        int realDepth = 0;
        int finalWidth = 0;
        int finalHeight = 0;

        int tmpDepth = 0;
        int tmpWidht = 0;
        int tmpHeight = 0;
        LinkedHashMap<Integer, String> blockIndex;
        boolean isNorthSouth;
        int startX = 0;
        int startZ = 0;
        int xas = 0;
        int zas = 0;
        int startY = 0;
        Player player;
        int faceRotation;
        HashMap<Material, List<Block>> materialBlocks;
        String[] blockRow;

        public BlockPaster(File copyFile, int finalWidth, int finalHeight,
                           Player player, int startX, int startZ, int startY, int xas, int zas, int faceRotation, boolean isNorthSouth,
                           LinkedHashMap<Integer, String> blockIndex, HashMap<Material, List<Block>> materialBlocks) {
            //first time
            this.blockConfig = YamlConfiguration.loadConfiguration(copyFile);
            this.blockRow = (blockConfig.get(rowNum + "").toString()).split("\\" + ErowTVConstants.SEP_BLOCK);

            this.finalWidth = finalWidth;
            this.finalHeight = finalHeight;
            this.player = player;
            this.startX = startX;
            this.startY = startY;
            this.startZ = startZ;
            this.xas =xas;
            this.zas = zas;
            this.faceRotation = faceRotation;
            this.isNorthSouth = isNorthSouth;
            this.blockIndex = blockIndex;
            this.materialBlocks = materialBlocks;
        }


        @Override
        public void run() {
            try {
                for (int iterW = 0; iterW < finalWidth; iterW++) {
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
                            updateBlockDirections(player, block, entireBlock, faceRotation);

                            if (materialBlocks != null) {
                                //Store every block in a HashMap, so we can use it in the game
                                storeAllMaterials(player, block, placeX, placeY, placeZ, materialBlocks);
                            }

                            //++ the realDepth for next depth position
                            realDepth++;
                        }
                    }

                    realDepth = 0;
                    tmpWidht++;
                    rowNum++;
                    if (blockConfig.contains(rowNum + "")) {
                        blockRow = (blockConfig.get(rowNum + "").toString()).split("\\" + ErowTVConstants.SEP_BLOCK);
                    }
                }

                    tmpWidht = 0;
                    tmpHeight++;

                if (tmpHeight >= finalHeight) {
                    this.cancel();
                }

            } catch (Exception ex) {
                player.sendMessage(ChatColor.DARK_RED + "[DispensePrice-run][Exception][" + ex.getMessage() + "]");
            }

        }


    }
}
