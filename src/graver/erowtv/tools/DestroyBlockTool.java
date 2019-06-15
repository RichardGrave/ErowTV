package graver.erowtv.tools;

import graver.erowtv.constants.Constants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public final class DestroyBlockTool {

	private static int ARRAY_PLACEMENT_POS_STARTX = 0;
	private static int ARRAY_PLACEMENT_POS_STARTY = 1;
	private static int ARRAY_PLACEMENT_POS_STARTZ = 2;
	private static int ARRAY_PLACEMENT_POS_DEPTH = 3;
	private static int ARRAY_PLACEMENT_POS_HEIGHT = 4;
	private static int ARRAY_PLACEMENT_POS_WIDTH = 5;
	private static int ARRAY_PLACEMENT_POS_XAS = 6;
	private static int ARRAY_PLACEMENT_POS_ZAS = 7;
	private static int ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH = 8;

	private DestroyBlockTool() {
	}
	
	/**
	 * Destroy every blok from the COPY_FROM to the COPY_TO block
	 * Not the positions of the blocks them selfs.
	 * 
	 * @param player
	 * @param wallSign is needed to read a name to be used for a JSON file name to save it all
	 * @param fromBlock
	 * @param toBlock
	 */
	public static void destroyFromToBlocks(Player player, Block clickedBlock, List<Integer> fromBlock, List<Integer> toBlock) {
		//We need to check if the blocks are in the same world.
		if(fromBlock.get(Constants.BLOCK_POS_WORLD) == toBlock.get(Constants.BLOCK_POS_WORLD)) {
			Block blockFrom = player.getWorld().getBlockAt(fromBlock.get(Constants.BLOCK_POS_X),
					fromBlock.get(Constants.BLOCK_POS_Y),fromBlock.get(Constants.BLOCK_POS_Z));
			
			Block blockTo = player.getWorld().getBlockAt(toBlock.get(Constants.BLOCK_POS_X),
					toBlock.get(Constants.BLOCK_POS_Y),toBlock.get(Constants.BLOCK_POS_Z));

			int[] directions = BlockTools.getBlockDirectionsFromTo(fromBlock, toBlock, clickedBlock);
			
			//Use for debug
//			player.sendMessage("DEPTH="+directions[ARRAY_PLACEMENT_POS_DEPTH] + " HEIGHT="+directions[ARRAY_PLACEMENT_POS_HEIGHT] + " WIDTH="+directions[ARRAY_PLACEMENT_POS_WIDTH]);
//			player.sendMessage("XAS="+directions[ARRAY_PLACEMENT_POS_XAS] + " ZAS="+directions[ARRAY_PLACEMENT_POS_ZAS] + " IS_NORTH_SOUTH="+directions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH]);
			
			if(directions.length != 0) {
				player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 1.0f);
				
				
				destroyBlocksAtAllPositions(player, directions);
				
				//Set blocks and the sign to AIR
				blockFrom.setType(Material.AIR, Constants.DO_NOT_APPLY_PHYSICS);
				blockTo.setType(Material.AIR, Constants.DO_NOT_APPLY_PHYSICS);
				clickedBlock.setType(Material.AIR);
				
				//Remove the memory after the copy
				ErowTV.removeMemoryFromPlayerMemory(player, Constants.MEMORY_DESTROY_FROM_POSITION);
				ErowTV.removeMemoryFromPlayerMemory(player, Constants.MEMORY_DESTROY_TO_POSITION);
			} else {
				player.sendMessage("Couldnt find the correct directions to destory blocks FROM and TO");
			}
		} else {
			//Blocks need to be in the same world
			player.sendMessage("Blocks are not in the same world");
		}
	}
	
	/**
	 * Destroy the blocks with help of calculated positions from BlockTools.getBlockDirections(fromBlock, toBlock, dataSign)
	 * 
	 * @param player
	 * @param positions
	 */
	public static void destroyBlocksAtAllPositions(Player player, int[] positions) {	
//		startX, startY, startZ, depth, (height+1), width, xas, zas, isNorthSouth };
		boolean isNorthSouth = (positions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH] == Constants.IS_NORTH_SOUTH);
		int startX = positions[ARRAY_PLACEMENT_POS_STARTX];
		int startY = positions[ARRAY_PLACEMENT_POS_STARTY];
		int startZ = positions[ARRAY_PLACEMENT_POS_STARTZ];
		int xas = positions[ARRAY_PLACEMENT_POS_XAS];
		int zas = positions[ARRAY_PLACEMENT_POS_ZAS];
		
		int depth = positions[ARRAY_PLACEMENT_POS_DEPTH];
		int height = positions[ARRAY_PLACEMENT_POS_HEIGHT];
		int width = positions[ARRAY_PLACEMENT_POS_WIDTH];
		
		//Copy all the blocks that are found
		for (int iterH = 0; iterH < height; iterH++) {
			for (int iterW = 0; iterW < width; iterW++) {
				for (int iterD = 0; iterD < depth; iterD++) {
					int placeX, placeZ;

					//If equal to 1 then its direction is NorthSouth
					if (isNorthSouth) {
						placeX = startX + (iterW * xas);
						placeZ = startZ + (iterD * zas);
					}else {
						placeX = startX + (iterD * xas);
						placeZ = startZ + (iterW * zas);
					}

					//Place a single block in the world at calculate position
					Block block = player.getWorld().getBlockAt(placeX, (startY + iterH), placeZ);
					if(block.getType() != Material.AIR) {
						//Only show explosion with blocks that are not AIR
						block.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation(), 3);
						block.setType(Material.AIR, Constants.DO_NOT_APPLY_PHYSICS);
					}
				}
			}
		}
	}
}
