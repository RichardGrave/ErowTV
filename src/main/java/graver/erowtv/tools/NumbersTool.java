package graver.erowtv.tools;

import graver.erowtv.player.PlayerTools;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public final class NumbersTool {

	private NumbersTool() {
	}
	
//	private static int numHeight = 5;
//	private static int numWidth = 5;

	// First of every String is 0 (AIR) so numbers can be place next to each other
	// with space between them
	private static String[] numZero = new String[] { "0xxxx", "0x00x", "0x00x", "0x00x", "0xxxx" };
	private static String[] numOne = new String[] { "00xxx", "000x0", "000x0", "00xx0", "000x0" };
	private static String[] numTwo = new String[] { "0xxxx", "0x000", "0xxxx", "0000x", "0xxxx" };
	
	private static String[] numThree = new String[] { "0xxxx", "0000x", "00xxx", "0000x", "0xxxx" };
	private static String[] numFour = new String[] { "0000x", "0000x", "0xxxx", "0x00x", "0x00x" };
	private static String[] numFive = new String[] { "0xxxx", "0000x", "0xxxx", "0x000", "0xxxx" };
	private static String[] numSix = new String[] { "0xxxx", "0x00x", "0xxxx", "0x000", "0xxxx" };
	private static String[] numSeven = new String[] { "000x0", "000x0", "00xxx", "000x0", "0xxx0" };
	private static String[] numEight = new String[] { "0xxxx", "0x00x", "0xxxx", "0x00x", "0xxxx" };
	private static String[] numNine = new String[] { "0xxxx", "0000x", "0xxxx", "0x00x", "0xxxx" };
	private static String[] numClear = new String[] { "00000", "00000", "00000", "00000", "00000" };
	
	public static void rgNumberTimer(World world, int number, int x, int y, int z) {
//		int counter = 1;
		// var buildNumber = function(){
		// rgMakeBigNumber(world, counter+'', x, y, z, direction)
		// //stop at given number
		// if(counter == number){
		// clearInterval(timerId)
		// var clearString = ''
		// for(var cli = 0; cli < number.length; cli++){
		// clearString = clearString + ' '
		// }
		// rgMakeBigNumber(world, clearString, x, y, z, direction)
		// }
		// counter = counter + 1
		// }
		// //Set timer
		// var timerId = setInterval(buildNumber, 100)

	}

	public static void rgMakeBigNumber(World world, int number, int x, int y, int z) {
		// var isNorthSouth = (direction == rgDirections.NORTH || direction ==
		// rgDirections.SOUTH ? true : false)
		// var startNum = (number.length - 1)
		//
		// for(var i = startNum; i >= 0 ; i--){
		//
		// switch (direction) {
		// case rgDirections.WEST:
		// z = z + numWidth
		// break
		// case rgDirections.EAST:
		// z = z - numWidth
		// break
		// case rgDirections.NORTH:
		// x = x - numWidth;
		// break
		// case rgDirections.SOUTH:
		// x = x + numWidth;
		// break
		// }
		//
		// rgMakeNumber(world, number[i], x, y, z, direction)
		// }

	}

	/**
	 * Get the right pattern to build the number
	 * 
	 * @param player
	 * @param number
	 * @param clickedBlock
	 */
	public static void chooseNumber(Player player, int number, Block clickedBlock) {
		//Yas + 1 to place on top of the block
		int yas = clickedBlock.getY() + 1;
		int xas = clickedBlock.getX();
		int zas = clickedBlock.getZ();
		
		player.sendMessage("Number: " + number);
		
		switch (number) {
		case 0:
			buildNumber(player, numZero, xas, yas, zas);
			break;
		case 1:
			buildNumber(player, numOne, xas, yas, zas);
			break;
		case 2:
			buildNumber(player, numTwo, xas, yas, zas);
			break;
		case 3:
			buildNumber(player, numThree, xas, yas, zas);
			break;
		case 4:
			buildNumber(player, numFour, xas, yas, zas);
			break;
		case 5:
			buildNumber(player, numFive, xas, yas, zas);
			break;
		case 6:
			buildNumber(player, numSix, xas, yas, zas);
			break;
		case 7:
			buildNumber(player, numSeven, xas, yas, zas);
			break;
		case 8:
			buildNumber(player, numEight, xas, yas, zas);
			break;
		case 9:
			buildNumber(player, numNine, xas, yas, zas);
			break;
		default:
			buildNumber(player, numClear, xas, yas, zas);
			break;
		}
	}

	/**
	 * Use the pattern to build a given number on the position
	 * 
	 * @param player
	 * @param numberPattern
	 * @param xas
	 * @param yas
	 * @param zas
	 */
	public static void buildNumber(Player player, String[] numberPattern, int xas, int yas, int zas) {
		int tmpYas = 0;
		
		for (String pattern : numberPattern) {
			int tmpZas = 0;
			int tmpXas = 0;
			int placeY = yas - tmpYas;
			
			player.sendMessage("Pattern: " + pattern);
			
			for (int iter = 0; iter < pattern.length(); iter++) {
				int placeZ = zas;
				int placeX = xas;
				int isNorthSouth = 0;

				// Even with clickedBlock we need to use the player direction
				switch (PlayerTools.getPlayerDirection(player)) {
				case NORTH:
					placeX = xas + tmpXas;
					isNorthSouth = 1;
					break;
				case EAST:
					placeZ = zas + tmpZas;
					break;
				case SOUTH:
					placeX = xas - tmpXas;
					isNorthSouth = 1;
					break;
				case WEST:
					placeZ = zas - tmpZas;
					break;
				case NORTHEAST:
					break;
				case NORTHWEST:
					break;
				case SOUTHEAST:
					break;
				case SOUTHWEST:
					break;
				case LOST:
					break;
				default:
					break;
				}

				Block block = player.getWorld().getBlockAt(placeX, placeY, placeZ);

				if (pattern.charAt(iter) == 'x') {
					block.setType(Material.WHITE_WOOL, false);
				} else {
					block.setType(Material.AIR, false);
				}

				tmpZas = tmpZas + (isNorthSouth == 1 ? 0 : 1);
				tmpXas = tmpXas + (isNorthSouth == 1 ? 1 : 0);
			}
			yas = yas + 1;
		}
	}
}
