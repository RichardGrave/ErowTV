package graver.erowtv.player;

import graver.erowtv.constants.Enumerations.PlayerDirection;
import graver.erowtv.constants.ErowTVConstants;
import org.bukkit.entity.Player;

public final class PlayerTools {

	// Dont instantiate or subclass the class
	private PlayerTools() {
	}

	/**
	 * Checks if the player is facing a valid direction NORTH, EAST, SOUTH or WEST
	 * For placing items/blocks
	 * 
	 * @param player
	 *            object
	 * @return rotation float
	 */
	public static float getPlayerRotation(Player player) {
		float rotation = (player.getLocation().getYaw() - ErowTVConstants.ROTATION_90_0_F) % ErowTVConstants.ROTATION_360_0_F;
		if (rotation < ErowTVConstants.ROTATION_0_0_F) {
			rotation += ErowTVConstants.ROTATION_360_0_F;
		}
		return rotation;
	}

	/**
	 * Checks if the player is facing a valid direction NORTH, EAST, SOUTH or WEST
	 * For placing items/blocks
	 * 
	 * @param player
	 *            object
	 * @return valid or not valid direction boolean
	 */
	public static boolean isDirectionValid(Player player) {
		float rotation = getPlayerRotation(player);

		if ((ErowTVConstants.ROTATION_67_5_F <= rotation && rotation < ErowTVConstants.ROTATION_112_5_F) || // NORTH
				(ErowTVConstants.ROTATION_157_5_F <= rotation && rotation < ErowTVConstants.ROTATION_202_5_F) || // EAST
				(ErowTVConstants.ROTATION_247_5_F <= rotation && rotation < ErowTVConstants.ROTATION_292_5_F) || // SOUTH
				(ErowTVConstants.ROTATION_337_5_F <= rotation && rotation < ErowTVConstants.ROTATION_360_0_F) || // WEST
				(ErowTVConstants.ROTATION_0_0_F <= rotation && rotation < ErowTVConstants.ROTATION_22_5_F)) { // ALSO WEST
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the cardinal direction for a player
	 * 
	 * @param player
	 *            object
	 * @param simpleDirection if you only want to know NORTH, EAST, SOUTH and WEST. (No North-East, South-West, etc.)
	 * @return direction String -> example PlayerDirection.WEST.getDirection()
	 */
	public static String getCardinalDirection(Player player, boolean simpleDirection) {
		if(simpleDirection) {
			return getSimplePlayerDirection(player).getDirection();
		}else {
			return getPlayerDirection(player).getDirection();
		}
	}
	
	/**
	 * Get the direction for a player
	 * 
	 * @param player
	 *            object
	 * @return PlayerDirection enum
	 */
	public static PlayerDirection getPlayerDirection(Player player) {
		float rotation = getPlayerRotation(player);

		if (ErowTVConstants.ROTATION_0_0_F <= rotation && rotation < ErowTVConstants.ROTATION_22_5_F) {
			return PlayerDirection.WEST;
		} else if (ErowTVConstants.ROTATION_22_5_F <= rotation && rotation < ErowTVConstants.ROTATION_67_5_F) {
			return PlayerDirection.NORTHWEST;
		} else if (ErowTVConstants.ROTATION_67_5_F <= rotation && rotation < ErowTVConstants.ROTATION_112_5_F) {
			return PlayerDirection.NORTH;
		} else if (ErowTVConstants.ROTATION_112_5_F <= rotation && rotation < ErowTVConstants.ROTATION_157_5_F) {
			return PlayerDirection.NORTHEAST;
		} else if (ErowTVConstants.ROTATION_157_5_F <= rotation && rotation < ErowTVConstants.ROTATION_202_5_F) {
			return PlayerDirection.EAST;
		} else if (ErowTVConstants.ROTATION_202_5_F <= rotation && rotation < ErowTVConstants.ROTATION_247_5_F) {
			return PlayerDirection.SOUTHEAST;
		} else if (ErowTVConstants.ROTATION_247_5_F <= rotation && rotation < ErowTVConstants.ROTATION_292_5_F) {
			return PlayerDirection.SOUTH;
		} else if (ErowTVConstants.ROTATION_292_5_F <= rotation && rotation < ErowTVConstants.ROTATION_337_5_F) {
			return PlayerDirection.SOUTHWEST;
		} else if (ErowTVConstants.ROTATION_337_5_F <= rotation && rotation < ErowTVConstants.ROTATION_360_0_F) {
			return PlayerDirection.WEST;
		} else {
			return PlayerDirection.LOST;
		}
	}
	
	/**
	 * Get the simple direction for a player.
	 * Meaning, only NORTH, EAST, SOUTH or WEST
	 * 
	 * @param player
	 *            object
	 * @return PlayerDirection enum
	 */
	public static PlayerDirection getSimplePlayerDirection(Player player) {
		float rotation = getPlayerRotation(player);

		//West starts at 0.0, so we need to check between 315.0 and 360.0 + 0.0 and 45.0 to get an angle of 90 degrees
		if (ErowTVConstants.SIMPLE_ROTATION_315_F <= rotation && rotation < ErowTVConstants.SIMPLE_ROTATION_360_F ||
				ErowTVConstants.SIMPLE_ROTATION_0_0_F <= rotation && rotation < ErowTVConstants.SIMPLE_ROTATION_45_F) {
			return PlayerDirection.WEST;
		} else if (ErowTVConstants.SIMPLE_ROTATION_45_F <= rotation && rotation < ErowTVConstants.SIMPLE_ROTATION_135_F) {
			return PlayerDirection.NORTH;
		} else if (ErowTVConstants.SIMPLE_ROTATION_135_F <= rotation && rotation < ErowTVConstants.SIMPLE_ROTATION_225_F) {
			return PlayerDirection.EAST;
		} else if (ErowTVConstants.SIMPLE_ROTATION_225_F <= rotation && rotation < ErowTVConstants.SIMPLE_ROTATION_315_F) {
			return PlayerDirection.SOUTH;
		} else {
			return PlayerDirection.LOST;
		}
	}
}