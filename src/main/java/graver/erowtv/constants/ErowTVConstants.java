package graver.erowtv.constants;

public interface ErowTVConstants {

	//Various Strings
	public final static String ENCHANTMENT_EROWTV = "ErowTV's Magic";

	//File saving
	public final static String FILE_EXTENSION_YML = ".yml";
	public final static String DIR_FILE_SAVE = "/saved_files/";
	public final static String DIR_COPY_BLOCKS = "/copy_blocks/";
	public final static String DIR_GENERAL = "/general/";

	//Sign TextLine
	public final static int SPECIAL_SIGN_ACTION = 0;
	public final static int SPECIAL_SIGN_PARAMETER_1 = 1;
	public final static int SPECIAL_SIGN_PARAMETER_2 = 2;
	public final static int SPECIAL_SIGN_PARAMETER_3 = 3;

	public final static int PASTE_BLOCK_FILE_NAME = 0;
	public final static int COPY_BLOCK_SIGN_FILE_NAME = 0;

	//Various ints
	public final static int MEMORY_NO_LIST_SIZE = -1;

	//Position index
	public final static int POSITION_SIZE = 4; //x, y, z
	public final static int BLOCK_POS_WORLD = 0;
	public final static int BLOCK_POS_X = 1;
	public final static int BLOCK_POS_Y = 2;
	public final static int BLOCK_POS_Z = 3;
	
	//Worlds
	public final static int WORLD_NETHER = 0;
	public final static int WORLD_NORMAL = 1;
	public final static int WORLD_END = 2;
	
	//Various booleans
	public final static boolean APPLY_PHYSICS = true;
	public final static boolean DO_NOT_APPLY_PHYSICS = false;
	
	//Commands to register
	public final static String TEST = "test";
	public final static String FACING = "facing";
	public final static String DEBUG_MESSAGES = "debug_messages";

	//Rotations to calculate the directions
	public static final float ROTATION_0_0_F = 0.0f;
	public static final float ROTATION_22_5_F = 22.5f;
	public static final float ROTATION_67_5_F = 67.5f;
	public static final float ROTATION_90_0_F = 90.0f;
	public static final float ROTATION_112_5_F = 112.5f;
	public static final float ROTATION_157_5_F = 157.5f;
	public static final float ROTATION_202_5_F = 202.5f;
	public static final float ROTATION_247_5_F = 247.5f;
	public static final float ROTATION_292_5_F = 292.5f;
	public static final float ROTATION_337_5_F = 337.5f;
	public static final float ROTATION_360_0_F = 360.0f;
	
	//Rotations for simple direction. So only NORTH, EAST, SOUTH and WEST.
	public static final float SIMPLE_ROTATION_0_0_F = 0.0f;
	public static final float SIMPLE_ROTATION_45_F = 45.0f;
	public static final float SIMPLE_ROTATION_135_F = 135.0f;
	public static final float SIMPLE_ROTATION_225_F = 225.0f;
	public static final float SIMPLE_ROTATION_315_F = 315.0f;
	public static final float SIMPLE_ROTATION_360_F = 360.0f;
	
	//Enums values PlayerDirection
	public static final String ENUM_PLAYER_DIRECTION_NORTH = "NORTH";
	public static final String ENUM_PLAYER_DIRECTION_NORTHEAST = "NORTHEAST";
	public static final String ENUM_PLAYER_DIRECTION_EAST = "EAST";
	public static final String ENUM_PLAYER_DIRECTION_SOUTHEAST = "SOUTHEAST";
	public static final String ENUM_PLAYER_DIRECTION_SOUTH = "SOUTH";
	public static final String ENUM_PLAYER_DIRECTION_SOUTHWEST = "SOUTHWEST";
	public static final String ENUM_PLAYER_DIRECTION_WEST = "WEST";
	public static final String ENUM_PLAYER_DIRECTION_NORTHWEST = "NORTHWEST";
	public static final String ENUM_PLAYER_DIRECTION_LOST = "LOST";
	
	//Facing directions	
	public static final int FACING_NORTH = 1;
	public static final int FACING_NORTH_NORTH_EAST = 2;
	public static final int FACING_NORTH_EAST = 3;
	public static final int FACING_EAST_NORTH_EAST = 4;
	public static final int FACING_EAST = 5;
	public static final int FACING_EAST_SOUTH_EAST = 6;
	public static final int FACING_SOUTH_EAST = 7;
	public static final int FACING_SOUTH_SOUTH_EAST = 8;
	public static final int FACING_SOUTH = 9;
	public static final int FACING_SOUTH_SOUTH_WEST = 10;
	public static final int FACING_SOUTH_WEST = 11;
	public static final int FACING_WEST_SOUTH_WEST = 12;
	public static final int FACING_WEST = 13;
	public static final int FACING_WEST_NORTH_WEST = 14;
	public static final int FACING_NORTH_WEST = 15;
	public static final int FACING_NORTH_NORTH_WEST = 16;
	public static final int FACING_NO_ROTATION = 0;
	
	//Various direction constants
	public static final int NOT_NORTH_SOUTH = 0;
	public static final int IS_NORTH_SOUTH = 1;
	public static final boolean DIRECTION_USE_SIMPLE = true;
	public static final boolean DIRECTION_DONT_USE_SIMPLE = false;
	
	//Memory names
	public static final String MEMORY_PASTE_BLOCK_ACTION = "PASTE_BLOCK_ACTION";
//	public static final String MEMORY_PASTE_SIGN_POSITION = "PASTE_SIGN_POSITION";
//	public static final String MEMORY_CLOCK_SIGN_POSITION = "CLOCK_SIGN_POSITION";
	public static final String MEMORY_SPECIAL_SIGN_POSITION = "SPECIAL_SIGN_POSITION";
	
	public static final String MEMORY_COPY_TO_POSITION = "COPY_TO_POSITION";
	public static final String MEMORY_COPY_FROM_POSITION = "COPY_FROM_POSITION";
	
	public static final String MEMORY_DESTROY_TO_POSITION = "DESTROY_TO_POSITION";
	public static final String MEMORY_DESTROY_FROM_POSITION = "DESTROY_FROM_POSITION";

	public static final String MEMORY_SIGN_NAME_SEPERATOR = "#";
	
	//Yml save
	//Always save depth, height and width with a copy
	public final static String YML_D_H_W_KEY = "D_H_W";
	public final static String SEP_D_H_W = "_";
	public final static String SEP_BLOCK_DATA = "&";
	public final static String SEP_BLOCK = "$";

	public final static String SEP_ROW_BLOCK_COUNT = "%";

	//20 game ticks is 1 seconde
	public final static int TIME_SECOND = 20;
	public final static int TIME_5_SECONDS = 100;
	public final static int TIME_10_SECONDS = 200;
	public final static int TIME_20_SECONDS = 400;
	public final static int TIME_30_SECONDS = 600;
	public final static int TIME_40_SECONDS = 800;
	public final static int TIME_50_SECONDS = 1000;

	public final static int TIME_1_MINUTE = 1200;
	public final static int TIME_5_MINUTES = 6000;
	public final static int TIME_10_MINUTES = 12000;
	public final static int TIME_15_MINUTES = 18000;
	public final static int TIME_20_MINUTES = 24000;

	public final static int TIME_1_HOUR = 72000;

	//Real seconds, not game ticks
	public final static int TIME_REAL_30_SECONDS = 30;

	//Timeformats
	public final static String TIME_FORMAT_HH_MM_SS = "HH:mm:ss";
	public final static String TIME_FORMAT_H_MM_SS = "H:mm:ss";
	public final static String TIME_FORMAT_MM_SS = "mm:ss";
	public final static String TIME_FORMAT_M_SS = "m:ss";
	public final static String TIME_FORMAT_SS = "ss";
	public final static String TIME_FORMAT_S = "s";

	//SpecialSign tools (Use lowercase)
	public final static String SPECIAL_COUNTDOWN_TIMER = "timer";
	public final static String SPECIAL_YOUTUBE_SUBS = "youtube";
	public final static String SPECIAL_PASTE = "paste";
}
