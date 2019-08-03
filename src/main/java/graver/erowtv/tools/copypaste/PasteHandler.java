package graver.erowtv.tools.copypaste;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.tools.YmlFileTool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class PasteHandler implements ErowTVConstants {

    public static void doPaste(Player player, Block clickedBlock, Sign sign, boolean chunkPaste){

        List<Integer> signPosition = Arrays.asList(null, sign.getX(), sign.getY(), sign.getZ());

        if(chunkPaste){
            //Start the chunk paste
            pasteBlocks(player, clickedBlock, sign, null, signPosition, DIR_COPY_CHUNKS);
        }else{
            //Start the normal paste
            pasteBlocks(player, clickedBlock, sign, null, signPosition, DIR_COPY_BLOCKS);
        }
    }

    /**
     * Paste every block from file
     *
     * @param player
     * @param sign       is needed to read a name to be used for a JSON file name to paste from
     * @param fileName   if this is not empty then dont use the sign but this filename to paste from
     * @param pasteBlock
     */
    public static void pasteBlocks(Player player, Block clickedBlock, Sign sign, String fileName, List<Integer> pasteBlock,
                                   String fileDir) {
        //We need to check if the blocks are in the same world.
        Block blockTo = player.getWorld().getBlockAt(pasteBlock.get(ErowTVConstants.BLOCK_POS_X),
                pasteBlock.get(ErowTVConstants.BLOCK_POS_Y), pasteBlock.get(ErowTVConstants.BLOCK_POS_Z));

        //Add DIR_COPY_BLOCKS for SPECIAL_SIGN
        if (fileName == null || fileName.isEmpty()) {
            //Get file name if no filename is given
            //This only happens with SPECIAL_SIGN
            fileName = fileDir + sign.getLine(SPECIAL_SIGN_PARAMETER_1);
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

                    if(fileDir.equalsIgnoreCase(DIR_COPY_BLOCKS)) {
                        //No blocks need to be added to a materialBlocks HashMap
                        PasteBlockTool.pasteBlocksAtAllPositions(player, copyFile, directions, blockFace, false);
                    }else{
                        ChunkPasteBlockTool.pasteChunkPositions(player, copyFile, directions, blockFace, false);
                    }

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

    public static void storeAllMaterials(Block block, HashMap<Material, List<Block>> materialBlocks) {

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

    public static void updateBlockDirections(Player player, Block block, String entireBlock, int faceRotation) {

        //Only if the block contains '[' because extra properties are between '[' and ']'
        if(entireBlock.contains("[")) {
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
        }

        //Only change if the block is completely different
        if(!block.getBlockData().getAsString().equalsIgnoreCase(entireBlock)) {
            block.setBlockData(Bukkit.getServer().createBlockData(entireBlock));
        }
    }


}
