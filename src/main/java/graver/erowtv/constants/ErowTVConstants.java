package graver.erowtv.constants;

public interface ErowTVConstants {

	//Various Strings
	public final static String ENCHANTMENT_EROWTV = "ErowTV's Magic";

	//Direction ints array
	public final static int ARRAY_NORTH = 0;
	public final static int ARRAY_EAST  = 1;
	public final static int ARRAY_SOUTH = 2;
	public final static int ARRAY_WEST  = 3;
	public final static int ARRAY_NORTH_UP = 4;
	public final static int ARRAY_EAST_UP  = 5;
	public final static int ARRAY_SOUTH_UP = 6;
	public final static int ARRAY_WEST_UP  = 7;

	//File saving
	public final static String FILE_EXTENSION_YML = ".yml";
	public final static String DIR_FILE_SAVE = "/saved_files/";
	public final static String DIR_COPY_BLOCKS = "/copy_blocks/";
	public final static String DIR_GENERAL = "/general/";
	public final static String DIR_GAMES = "/games/";

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
	public final static float ROTATION_0_0_F = 0.0f;
	public final static float ROTATION_22_5_F = 22.5f;
	public final static float ROTATION_67_5_F = 67.5f;
	public final static float ROTATION_90_0_F = 90.0f;
	public final static float ROTATION_112_5_F = 112.5f;
	public final static float ROTATION_157_5_F = 157.5f;
	public final static float ROTATION_202_5_F = 202.5f;
	public final static float ROTATION_247_5_F = 247.5f;
	public final static float ROTATION_292_5_F = 292.5f;
	public final static float ROTATION_337_5_F = 337.5f;
	public final static float ROTATION_360_0_F = 360.0f;
	
	//Rotations for simple direction. So only NORTH, EAST, SOUTH and WEST.
	public final static float SIMPLE_ROTATION_0_0_F = 0.0f;
	public final static float SIMPLE_ROTATION_45_F = 45.0f;
	public final static float SIMPLE_ROTATION_135_F = 135.0f;
	public final static float SIMPLE_ROTATION_225_F = 225.0f;
	public final static float SIMPLE_ROTATION_315_F = 315.0f;
	public final static float SIMPLE_ROTATION_360_F = 360.0f;
	
	//Enums values PlayerDirection
	public final static String ENUM_PLAYER_DIRECTION_NORTH = "NORTH";
	public final static String ENUM_PLAYER_DIRECTION_NORTHEAST = "NORTHEAST";
	public final static String ENUM_PLAYER_DIRECTION_EAST = "EAST";
	public final static String ENUM_PLAYER_DIRECTION_SOUTHEAST = "SOUTHEAST";
	public final static String ENUM_PLAYER_DIRECTION_SOUTH = "SOUTH";
	public final static String ENUM_PLAYER_DIRECTION_SOUTHWEST = "SOUTHWEST";
	public final static String ENUM_PLAYER_DIRECTION_WEST = "WEST";
	public final static String ENUM_PLAYER_DIRECTION_NORTHWEST = "NORTHWEST";
	public final static String ENUM_PLAYER_DIRECTION_LOST = "LOST";
	
	//Facing directions	
	public final static int FACING_NORTH = 1;
	public final static int FACING_NORTH_NORTH_EAST = 2;
	public final static int FACING_NORTH_EAST = 3;
	public final static int FACING_EAST_NORTH_EAST = 4;
	public final static int FACING_EAST = 5;
	public final static int FACING_EAST_SOUTH_EAST = 6;
	public final static int FACING_SOUTH_EAST = 7;
	public final static int FACING_SOUTH_SOUTH_EAST = 8;
	public final static int FACING_SOUTH = 9;
	public final static int FACING_SOUTH_SOUTH_WEST = 10;
	public final static int FACING_SOUTH_WEST = 11;
	public final static int FACING_WEST_SOUTH_WEST = 12;
	public final static int FACING_WEST = 13;
	public final static int FACING_WEST_NORTH_WEST = 14;
	public final static int FACING_NORTH_WEST = 15;
	public final static int FACING_NORTH_NORTH_WEST = 16;
	public final static int FACING_NO_ROTATION = 0;
	
	//Various direction constants
	public final static int NOT_NORTH_SOUTH = 0;
	public final static int IS_NORTH_SOUTH = 1;
	public final static boolean DIRECTION_USE_SIMPLE = true;
	public final static boolean DIRECTION_DONT_USE_SIMPLE = false;
	
	//Memory names
	public final static String MEMORY_PASTE_BLOCK_ACTION = "PASTE_BLOCK_ACTION";
//	public final static String MEMORY_PASTE_SIGN_POSITION = "PASTE_SIGN_POSITION";
//	public final static String MEMORY_CLOCK_SIGN_POSITION = "CLOCK_SIGN_POSITION";
	public final static String MEMORY_SPECIAL_SIGN_POSITION = "SPECIAL_SIGN_POSITION";
	public final static String MEMORY_GAME_SIGN_POSITION = "GAME_SIGN_POSITION";

	public final static String MEMORY_COPY_TO_POSITION = "COPY_TO_POSITION";
	public final static String MEMORY_COPY_FROM_POSITION = "COPY_FROM_POSITION";
	
	public final static String MEMORY_DESTROY_TO_POSITION = "DESTROY_TO_POSITION";
	public final static String MEMORY_DESTROY_FROM_POSITION = "DESTROY_FROM_POSITION";

	public final static String MEMORY_SIGN_NAME_SEPERATOR = "#";

	//Yml save
	//Always save depth, height, width and facing with a copy
	public final static String YML_TOTAL_D_H_W_F_KEY = "TOTAL_D_H_W_F";
	public final static String SEP_D_H_W = "_";


	public final static String YML_C_D_H_W_KEY = "C_D_H_W";
	public final static int D_H_W_DEPTH = 0;
	public final static int D_H_W_HEIGHT = 1;
	public final static int D_H_W_WIDHT = 2;
	public final static int D_H_W_FACE = 3;

	public final static String SEP_BLOCK_DATA = "&";
	public final static String SEP_BLOCK = "$";
	public final static String BLOCK_INDEX = "B";
	public final static String CHUNK = "C";

	public final static String SEP_ROW_BLOCK_COUNT = "%";

	//25 (Should be a good number to paste)
	//Dont use numbers to small or to big. Somehow they cause the server to run behind
	public final static int MAX_CHUNK_HEIGHT = 20;
	public final static int MAX_CHUNK_WIDTH = 20;
	public final static int MAX_CHUNK_DEPT = 50;

	//20 game ticks is 1 seconde
	public final static int TIME_ONE_TICK = 1;
	public final static int TIME_HALF_SECOND = 10;
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
	public final static String SPECIAL_GAME = "game";

	public final static int ARRAY_PLACEMENT_POS_STARTX = 0;
	public final static int ARRAY_PLACEMENT_POS_STARTY = 1;
	public final static int ARRAY_PLACEMENT_POS_STARTZ = 2;
	public final static int ARRAY_PLACEMENT_POS_DEPTH = 3;
	public final static int ARRAY_PLACEMENT_POS_HEIGHT = 4;
	public final static int ARRAY_PLACEMENT_POS_WIDTH = 5;
	public final static int ARRAY_PLACEMENT_POS_XAS = 6;
	public final static int ARRAY_PLACEMENT_POS_ZAS = 7;
	public final static int ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH = 8;
	public final static int ARRAY_CURRENT_FACING_DIRECTION = 9;
	public final static int ARRAY_PLACEMENT_FROM_Y_GREATER = 10;


	public final static String GAME_DOUBLE_OR_NOTHING = "DoubleOrNothing";
}
