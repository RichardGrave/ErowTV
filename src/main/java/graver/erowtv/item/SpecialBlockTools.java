package graver.erowtv.item;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.DestroyBlockTool;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.RedstoneTorch;

import java.util.List;

/*
 * This should do actions on special blocks made by me. So not any type of blocks.
 */
public class SpecialBlockTools {

    /**
     * Only for redstone torches
     * Check if there is something needed to be done when its placed on a special block
     *
     * @param player
     * @param placedBlock
     */
    public static void redstoneTorchPlacedByPlayer(Player player, Block placedBlock) {
        //We already know its a redstone torch. That was checked before this method was called.

        //Turn the block into a Button
        BlockState blockState = placedBlock.getState();
        blockState.setType(placedBlock.getType());
        RedstoneTorch redstoneTorch = (RedstoneTorch) blockState.getData();

        //Get the block the button is placed on
        Block blockBehindTorch = placedBlock.getRelative(redstoneTorch.getAttachedFace());

        //Handle the click
        handleRedstoneTorchPlacedByPlayer(player, placedBlock, blockBehindTorch);
    }

    /**
     * Check if the positions match the DESTROY_FROM and DESTROY_TO memory for the player.
     * If so start destroying.
     *
     * @param player
     * @param placedBlock
     * @param blockBehindTorch
     */
    public static void handleRedstoneTorchPlacedByPlayer(Player player, Block placedBlock, Block blockBehindTorch) {
        if(BlockTools.isBlockPositionTheSame(blockBehindTorch,(List<Integer>) ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION))) {
            //We also need the DESTROY_TO position
            if(ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION)) {
                //Start destroying
                DestroyBlockTool.destroyFromToBlocks(player, placedBlock,
                        (List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION),
                        (List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION));
            }else {
                //If the TO block is missing
                player.sendMessage("A 'Destroy TO block' is needed");
            }
        }else if(BlockTools.isBlockPositionTheSame(blockBehindTorch,(List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION))) {
            //We also nee the DESTROY_FROM position
            if(ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION)) {
                //Start destroying
                DestroyBlockTool.destroyFromToBlocks(player, placedBlock,
                        (List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION),
                        (List<Integer>)ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION));
            }else {
                //If the FROM block is missing
                player.sendMessage("A 'Destroy FROM block' is needed");
            }
        }
    }
}
