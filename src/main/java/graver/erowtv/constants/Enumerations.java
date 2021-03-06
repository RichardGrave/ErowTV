package graver.erowtv.constants;

import org.bukkit.block.BlockFace;

public interface Enumerations {

//	private Enumerations(){}

	/**
	 * Enum used for determining checking the players direction.
	 */
	public static enum PlayerDirection {
		
		NORTH(ErowTVConstants.ENUM_PLAYER_DIRECTION_NORTH), NORTHEAST(ErowTVConstants.ENUM_PLAYER_DIRECTION_NORTHEAST),
		EAST(ErowTVConstants.ENUM_PLAYER_DIRECTION_EAST), SOUTHEAST(ErowTVConstants.ENUM_PLAYER_DIRECTION_SOUTHEAST),
		SOUTH(ErowTVConstants.ENUM_PLAYER_DIRECTION_SOUTH), SOUTHWEST(ErowTVConstants.ENUM_PLAYER_DIRECTION_SOUTHWEST),
		WEST(ErowTVConstants.ENUM_PLAYER_DIRECTION_WEST), NORTHWEST(ErowTVConstants.ENUM_PLAYER_DIRECTION_NORTHWEST),
		LOST(ErowTVConstants.ENUM_PLAYER_DIRECTION_LOST);

		private final String direction;

		private PlayerDirection(String direction) {
			this.direction = direction;
		}
		
		public String getDirection(){
			return this.direction;
		}
	}
	
	
	/**
	 * Use CustomRecipe final String in the Recipe as its DisplayName
	 * Use getCustomRecipe() to get the index for switch statements
	 */
	public static enum CustomItem {
		TWO_BY_TWO("Two by Two", "I command you to multiply"),
		COPY_FROM_BLOCK("Copy FROM block", "You also need a Copy TO block"),
		COPY_TO_BLOCK("Copy TO block",  "You also need a Copy FROM block"),
		PASTE_BLOCK("Paste block", "Paste whatever you want, how often you want."),
		SPECIAL_SIGN("Special sign", "The right tool for the job"),
		DESTROY_FROM_BLOCK("Destroy FROM block", "You also need a Destroy TO block"),
		DESTROY_TO_BLOCK("Destroy TO block",  "You also need a Destroy FROM block"),
		NO_RECIPE("No Recipe", "");
		
		private final String customItem;
		private final String lore;
		
		private CustomItem(String customItem, String lore) {
			this.customItem = customItem;
			this.lore = lore;
		}
		
		public String getCustomItemName() {
			return this.customItem;
		}
		
		public String getLore() {
			return this.lore;
		}
		
		public String getKey() {
			//Needed for NamespacedKey. Dont remove.
			return this.customItem.replaceAll(" ", "_");
		}
		
	    public static CustomItem getCustomItem(String recipeName){
	        try {
	        	//First Uppercase and then replace the whitespace with underscore for recipe names with whitespaces
	            return valueOf(recipeName.toUpperCase().replace(' ', '_'));
	        } catch (Exception ex) {
	            return NO_RECIPE;
	        }
	    } 
	}

	/**
	 * All the material that can have a direction.
	 */
	public static enum DirectionalMaterial {
		ORG_BUKKIT_MATERIAL_BANNER,
		ORG_BUKKIT_MATERIAL_BED,
		ORG_BUKKIT_MATERIAL_BUTTON,
		ORG_BUKKIT_MATERIAL_CHEST,
		ORG_BUKKIT_MATERIAL_COCOAPLANT,
		ORG_BUKKIT_MATERIAL_COMPARATOR,
		ORG_BUKKIT_MATERIAL_DIODE,
		ORG_BUKKIT_MATERIAL_DIRECTIONALCONTAINER,
		ORG_BUKKIT_MATERIAL_DISPENSER,
		ORG_BUKKIT_MATERIAL_DOOR,
		ORG_BUKKIT_MATERIAL_ENDERCHEST,
		ORG_BUKKIT_MATERIAL_FURNACE,
		ORG_BUKKIT_MATERIAL_FURNACEANDDISPENSER,
		ORG_BUKKIT_MATERIAL_GATE,
		ORG_BUKKIT_MATERIAL_HOPPER,
		ORG_BUKKIT_MATERIAL_LADDER,
		ORG_BUKKIT_MATERIAL_LEVER,
		ORG_BUKKIT_MATERIAL_OBSERVER,
		ORG_BUKKIT_MATERIAL_PISTONBASEMATERIAL,
		ORG_BUKKIT_MATERIAL_PISTONEXTENSIONMATERIAL,
		ORG_BUKKIT_MATERIAL_PUMPKIN,
		ORG_BUKKIT_MATERIAL_REDSTONETORCH,
		ORG_BUKKIT_MATERIAL_SIGN,
		ORG_BUKKIT_MATERIAL_SIMPLEATTACHABLEMATERIALDATA,
		ORG_BUKKIT_MATERIAL_SKULL,
		ORG_BUKKIT_MATERIAL_STAIRS,
		ORG_BUKKIT_MATERIAL_TORCH,
		ORG_BUKKIT_MATERIAL_TRAPDOOR,
		ORG_BUKKIT_MATERIAL_TRIPWIREHOOK,
		NO_ITEM;
				
	    public static DirectionalMaterial getDirectionMaterial(String material){
	        try {
	        	//First Uppercase and then replace the dots with underscore for material names with dots
	            return valueOf(material.toUpperCase().replace('.', '_'));
	        } catch (Exception ex) {
	            return NO_ITEM;
	        }
	    } 
	}

	/**
	 * Needed to find out the direction for blocks.
	 */
	public static enum DirectionalRotation {
		NORTH(1),
		NORTH_NORTH_EAST(2),
		NORTH_EAST(3),
		EAST_NORTH_EAST(4),
		EAST(5),
		EAST_SOUTH_EAST(6),
		SOUTH_EAST(7),
		SOUTH_SOUTH_EAST(8),
		SOUTH(9),
		SOUTH_SOUTH_WEST(10),
		SOUTH_WEST(11),
		WEST_SOUTH_WEST(12),
		WEST(13),
		WEST_NORTH_WEST(14),
		NORTH_WEST(15),
		NORTH_NORTH_WEST(16),
		NO_ROTATION(0);
		
		private int rotationValue;
		
		private DirectionalRotation(int rotationValue) {
			this.rotationValue = rotationValue;
		}
		
		public int getRotationValue() {
			return this.rotationValue;
		}
		
		public static BlockFace getBlockFaceByRotation(int rotation) {
			switch(rotation) {
			case ErowTVConstants.FACING_NORTH:
				return BlockFace.NORTH;
			case ErowTVConstants.FACING_NORTH_NORTH_EAST:
				return BlockFace.NORTH_NORTH_EAST;
			case ErowTVConstants.FACING_NORTH_EAST:
				return BlockFace.NORTH_EAST;
			case ErowTVConstants.FACING_EAST_NORTH_EAST:
				return BlockFace.EAST_NORTH_EAST;
			case ErowTVConstants.FACING_EAST:
				return BlockFace.EAST;
			case ErowTVConstants.FACING_EAST_SOUTH_EAST:
				return BlockFace.EAST_SOUTH_EAST;
			case ErowTVConstants.FACING_SOUTH_EAST:
				return BlockFace.SOUTH_EAST;
			case ErowTVConstants.FACING_SOUTH_SOUTH_EAST:
				return BlockFace.SOUTH_SOUTH_EAST;
			case ErowTVConstants.FACING_SOUTH:
				return BlockFace.SOUTH;
			case ErowTVConstants.FACING_SOUTH_SOUTH_WEST:
				return BlockFace.SOUTH_SOUTH_WEST;
			case ErowTVConstants.FACING_SOUTH_WEST:
				return BlockFace.SOUTH_WEST;
			case ErowTVConstants.FACING_WEST_SOUTH_WEST:
				return BlockFace.WEST_SOUTH_WEST;
			case ErowTVConstants.FACING_WEST:
				return BlockFace.WEST;
			case ErowTVConstants.FACING_WEST_NORTH_WEST:
				return BlockFace.WEST_NORTH_WEST;
			case ErowTVConstants.FACING_NORTH_WEST:
				return BlockFace.NORTH_WEST;
			case ErowTVConstants.FACING_NORTH_NORTH_WEST:
				return BlockFace.NORTH_NORTH_WEST;
			default:
				return BlockFace.SELF;	
			}
		}
	}
}
