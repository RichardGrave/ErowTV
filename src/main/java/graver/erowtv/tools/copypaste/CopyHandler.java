package graver.erowtv.tools.copypaste;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.List;

public final class CopyHandler implements ErowTVConstants {

    private CopyHandler(){}


    public static void doCopy(Player player, Block clickedBlock, Sign wallSign,
                         List<Integer> copyFromPosition, List<Integer> copyToPosition){

        String fileName;
        //If filename is on the second sign row then we use Chunk Copy
        if(!wallSign.getLine(SPECIAL_SIGN_PARAMETER_1).trim().isEmpty()){
            fileName = wallSign.getLine(SPECIAL_SIGN_PARAMETER_1).trim();

            //Start the chunk copy
            copyFromAndToBlocks(player, clickedBlock, wallSign, copyFromPosition, copyToPosition,
                    DIR_COPY_CHUNKS, fileName);
        }else{
             fileName = wallSign.getLine(COPY_BLOCK_SIGN_FILE_NAME).trim();

            //Start the normal copy
            copyFromAndToBlocks(player, clickedBlock, wallSign, copyFromPosition, copyToPosition,
                    DIR_COPY_BLOCKS, fileName);
        }
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
    public static void copyFromAndToBlocks(Player player, Block clickedBlock, Sign wallSign, List<Integer> fromBlock,
                                           List<Integer> toBlock, String fileDir, String fileName) {
        //We need to check if the blocks are in the same world.
        if(fromBlock.get(ErowTVConstants.BLOCK_POS_WORLD) == toBlock.get(ErowTVConstants.BLOCK_POS_WORLD)) {
            Block blockFrom = player.getWorld().getBlockAt(fromBlock.get(ErowTVConstants.BLOCK_POS_X),
                    fromBlock.get(ErowTVConstants.BLOCK_POS_Y),fromBlock.get(ErowTVConstants.BLOCK_POS_Z));

            Block blockTo = player.getWorld().getBlockAt(toBlock.get(ErowTVConstants.BLOCK_POS_X),
                    toBlock.get(ErowTVConstants.BLOCK_POS_Y),toBlock.get(ErowTVConstants.BLOCK_POS_Z));

            if(fileName != null && !fileName.isEmpty()) {
                fileName = fileDir + fileName.replace(' ', '_');

                //Copy block has a sign to click on for directions, so CustomBlockFace = NULL
                int[] directions = BlockTools.getBlockDirectionsFromTo(fromBlock, toBlock, clickedBlock, null);

                if(ErowTV.isDebug) {
                    player.sendMessage(ChatColor.DARK_AQUA+"DEPTH=" + directions[ARRAY_COPY_POS_DEPTH] + " HEIGHT=" + directions[ARRAY_COPY_POS_HEIGHT] + " WIDTH=" + directions[ARRAY_COPY_POS_WIDTH]);
                    player.sendMessage(ChatColor.DARK_AQUA+"XAS=" + directions[ARRAY_COPY_POS_XAS] + " ZAS=" + directions[ARRAY_COPY_POS_ZAS] + " IS_NORTH_SOUTH=" + directions[ARRAY_COPY_POS_IS_NORTH_SOUTH]);
                }

                if(directions.length != 0) {
                    if(fileDir.equalsIgnoreCase(DIR_COPY_BLOCKS)) {
                        CopyBlockTool.copyBlocksAtAllPositions(player, fileName, directions);
                    }else{
                        ChunkCopyBlockTool.calculateChunkPositions(player, fileName, directions);
                    }

                    //Set blocks and the sign to AIR
                    blockFrom.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);
                    blockTo.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);
                    wallSign.getBlock().setType(Material.AIR);

                    //Remove the memory after the copy
                    ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_COPY_FROM_POSITION);
                    ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_COPY_TO_POSITION);
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

}
