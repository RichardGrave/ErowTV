package graver.erowtv.tools;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.LinkedHashMap;
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
     * @param sign       is needed to read a name to be used for a JSON file name to paste from
     * @param fileName   if this is not empty then dont use the sign but this filename to paste from
     * @param pasteBlock
     * @param memoryName so we now the name of the memory to remove from playersmemory when done pasting.
     */
    public static void pasteBlocks(Player player, Block clickedBlock, Sign sign, String fileName, List<Integer> pasteBlock, String memoryName) {
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

                    pasteBlocksAtAllPositions(player, copyFile, directions, blockFace);
                    //Set blocks and the sign to AIR
                    blockTo.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);

                    //Remove the memory after the copy
                    //Only for SPECIAL_SIGN paste action. NOT for PASTE BLOCK
                    if (!memoryName.equalsIgnoreCase(MEMORY_PASTE_BLOCK_ACTION)) {
                        ErowTV.removeMemoryFromPlayerMemory(player, memoryName);
                    }
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

        LinkedHashMap<Integer, String> blockIndex = new LinkedHashMap<>();

        FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(copyFile);

        if (!blockConfig.contains(ErowTVConstants.YML_D_H_W_KEY)) {
            player.sendMessage("Cant find depth, height and widht in yml file");
            return;
        }

        String[] dhw = blockConfig.get(ErowTVConstants.YML_D_H_W_KEY).toString().split(ErowTVConstants.SEP_D_H_W);
        if (dhw.length != 4) {
            player.sendMessage("String doesnt contain depth, height and widht in yml file");
            return;
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
            player.sendMessage("FaceRotation="+faceRotation +
                    " - WasFacing="+wasFacing + " CurrentFacing="+currentFacing);
        }

        //Key for next row in file
        int rowNum = 0;

        //Copy all the blocks that are found
        for (int iterH = 0; iterH < height; iterH++) {
            for (int iterW = 0; iterW < width; iterW++) {
                rowNum++;
                if (!blockConfig.contains(rowNum + "")) {
                    player.sendMessage("Missing row to read from yml file");
                    return;
                }
                //Use \\ with SEP_BLOCK to use correct split for '$'
                String[] blockRow = (blockConfig.get(rowNum + "").toString()).split("\\" + ErowTVConstants.SEP_BLOCK);
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
                        Block block = player.getWorld().getBlockAt(placeX, (startY + iterH), placeZ);

                        //Start updating the block
                        updateBlockDirections(player, block, entireBlock, faceRotation);

                        //++ the realDepth for next depth position
                        realDepth++;
                    }
                }
            }
        }
    }

    private static void updateBlockDirections(Player player, Block block, String entireBlock, int faceRotation){

        //Blocks with facing need a new recalculated facing (by BlockFace direction from Copy sign)
        if (entireBlock.contains("facing=")) {
            //Get new BlockFace direction by using old BlockFace and the ratation
            BlockFace blockFaceFromSavedBlock = BlockTools.getBlockFaceByString(player, entireBlock);
            BlockFace newBlockFaceDirection = BlockTools.getNewBlockFaceDirection(player, blockFaceFromSavedBlock, faceRotation);

            //Replace old 'facing=' with the new 'facing='
            entireBlock = BlockTools.replaceBlockFaceDirection(player, entireBlock, newBlockFaceDirection);
        }

        //Some blocks don't have facing but 'axis='
        if(entireBlock.contains("axis=")){
            entireBlock = BlockTools.replaceAxisDirection(entireBlock, faceRotation);
        }

        //Glass has boolean directions for north, east, south and west.
        if(entireBlock.contains("north=") || entireBlock.contains("east=") ||
                entireBlock.contains("south=") || entireBlock.contains("west=")){
            entireBlock = BlockTools.replaceGlassDirections(entireBlock, faceRotation);
        }

        block.setBlockData(Bukkit.getServer().createBlockData(entireBlock));
    }
}
