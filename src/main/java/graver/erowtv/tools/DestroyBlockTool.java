package graver.erowtv.tools;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public final class DestroyBlockTool implements ErowTVConstants {


	private DestroyBlockTool() {
	}
	
	/**
	 * Destroy every blok from the COPY_FROM to the COPY_TO block
	 * Not the positions of the blocks them selfs.
	 * 
	 * @param player
	 * @param clickedBlock is needed to read a name to be used for a JSON file name to save it all
	 * @param fromBlock
	 * @param toBlock
	 */
	public static void destroyFromToBlocks(Player player, Block clickedBlock, List<Integer> fromBlock, List<Integer> toBlock) {
		//We need to check if the blocks are in the same world.
		if(fromBlock.get(ErowTVConstants.BLOCK_POS_WORLD) == toBlock.get(ErowTVConstants.BLOCK_POS_WORLD)) {
			Block blockFrom = player.getWorld().getBlockAt(fromBlock.get(ErowTVConstants.BLOCK_POS_X),
					fromBlock.get(ErowTVConstants.BLOCK_POS_Y),fromBlock.get(ErowTVConstants.BLOCK_POS_Z));
			
			Block blockTo = player.getWorld().getBlockAt(toBlock.get(ErowTVConstants.BLOCK_POS_X),
					toBlock.get(ErowTVConstants.BLOCK_POS_Y),toBlock.get(ErowTVConstants.BLOCK_POS_Z));

			//Destroy block has nothing to click on (no Sign), so use Player's Facing direction. ClickedBlock = NULL
			int[] directions = BlockTools.getBlockDirectionsFromTo(fromBlock, toBlock, null, player.getFacing());

			if(ErowTV.isDebug) {
				player.sendMessage(ChatColor.DARK_AQUA+"DEPTH="+directions[ARRAY_COPY_POS_DEPTH] + " HEIGHT="+directions[ARRAY_COPY_POS_HEIGHT] + " WIDTH="+directions[ARRAY_COPY_POS_WIDTH]);
				player.sendMessage(ChatColor.DARK_AQUA+"XAS="+directions[ARRAY_COPY_POS_XAS] + " ZAS="+directions[ARRAY_COPY_POS_ZAS] + " IS_NORTH_SOUTH="+directions[ARRAY_COPY_POS_IS_NORTH_SOUTH]);
			}

			if(directions.length != 0) {
				player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 1.0f);

				//2 seconds for Main Thread to keep up with us?
				new BlockDestroyer(player, directions).runTaskTimer(ErowTV.javaPluginErowTV, TIME_ONE_TICK, TIME_SECOND*2);

				//Set blocks and the sign to AIR
				blockFrom.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);
				blockTo.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);
				clickedBlock.setType(Material.AIR);

				//Remove the memory after the copy
				ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION);
				ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION);
			} else {
				player.sendMessage("Couldnt find the correct directions to destory blocks FROM and TO");
			}
		} else {
			//Blocks need to be in the same world
			player.sendMessage("Blocks are not in the same world");
		}
	}

	private static class BlockDestroyer extends BukkitRunnable {

		private Player player;
		private int startX;
		private int startZ;
		private int startY;
		private int xas;
		private int zas;
		private int height;
		private int width;
		private int depth;
		private boolean isNorthSouth;
		private boolean isFromBlockYGreater;

		public BlockDestroyer(Player player, int[] positions) {
			this.player = player;

			this.isNorthSouth = (positions[ARRAY_COPY_POS_IS_NORTH_SOUTH] == ErowTVConstants.IS_NORTH_SOUTH);
			this.startX = positions[ARRAY_COPY_POS_STARTX];
			this.startY = positions[ARRAY_COPY_POS_STARTY];
			this.startZ = positions[ARRAY_COPY_POS_STARTZ];
			this.xas = positions[ARRAY_COPY_POS_XAS];
			this.zas = positions[ARRAY_COPY_POS_ZAS];
			this.isFromBlockYGreater = (positions[ARRAY_COPY_FROM_Y_GREATER]==1 ? true : false);

			this.depth = positions[ARRAY_COPY_POS_DEPTH];
			this.height = positions[ARRAY_COPY_POS_HEIGHT];
			this.width = positions[ARRAY_COPY_POS_WIDTH];
		}

		//Tmp values
		int blocksDestroyed = 0;

		int iterH = 0;
		int iterW = 0;
		int iterD = 0;

		@Override
		public void run() {
			try {
				blocksDestroyed = 0;

				while (blocksDestroyed <= MAX_BLOCKS) {
					int placeX, placeZ;

					//If equal to 1 then its direction is NorthSouth
					if (isNorthSouth) {
						placeX = startX + (iterW * xas);
						placeZ = startZ + (iterD * zas);
					}else {
						placeX = startX + (iterD * xas);
						placeZ = startZ + (iterW * zas);
					}

					int placeY = (isFromBlockYGreater ? (startY - iterH) : (startY + iterH));

					Block block = player.getWorld().getBlockAt(placeX, placeY, placeZ);

					if(block.getType() != Material.AIR) {
						//Only show explosion with blocks that are not AIR
						block.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation(), 3);
						block.setType(Material.AIR, ErowTVConstants.DO_NOT_APPLY_PHYSICS);
					}


					blocksDestroyed++;
					iterD++;

					if (iterD >= depth) {
						iterD = 0;
						iterW++;
					}

					if (iterW >= width) {
						iterW = 0;
						iterH++;
					}

					if (iterH >= height) {
						this.cancel();
						player.sendMessage(ChatColor.GREEN + "Destroying is done.");
						//break loop or it will continue
						break;
					}
				}
			} catch (Exception ex) {
				player.sendMessage(ChatColor.DARK_RED + "[BlockDestroyer-run][Exception][" + ex.getMessage() + "]");
			}
		}
	}
}
