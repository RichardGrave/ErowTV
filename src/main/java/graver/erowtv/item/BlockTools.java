package graver.erowtv.item;

import graver.erowtv.constants.Enumerations.CustomItem;
import graver.erowtv.constants.Enumerations.DirectionalMaterial;
import graver.erowtv.constants.Enumerations.DirectionalRotation;
import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.player.PlayerTools;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.material.Stairs;

import java.util.Arrays;
import java.util.List;


/*
 * Block methods that should be use for any type of block in the game.
 */
public final class BlockTools {

    //Dont instantiate or subclass the class
    private BlockTools() {
    }

    private static int ARRAY_PLACEMENT_POS_STARTX = 0;
    private static int ARRAY_PLACEMENT_POS_STARTY = 1;
    private static int ARRAY_PLACEMENT_POS_STARTZ = 2;
    private static int ARRAY_PLACEMENT_POS_XAS = 3;
    private static int ARRAY_PLACEMENT_POS_ZAS = 4;
    private static int ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH = 5;

    //TODO:!! RG dit moet ook voor Items

    /**
     * Place number of blocks in the world
     *
     * @param player       that triggers this method
     * @param clickedBlock the player has right or left clicked
     * @param material     of the block
     * @param applyPhysics should it use physics to let sand fall etc.
     * @param depth        how many block forward
     * @param width        how many blocks to the right
     * @param height       how many blocks up
     */
    @SuppressWarnings("deprecation")
    public static void placeBlockByPlayerPosition(Player player, Block clickedBlock, Material material, boolean applyPhysics, int depth, int width, int height) {
        int[] positions = getPlacementPositionByPlayer(player, clickedBlock, (material == Material.AIR));

        boolean isNorthSouth = (positions[ARRAY_PLACEMENT_POS_IS_NORTH_SOUTH] == ErowTVConstants.IS_NORTH_SOUTH);
        int startX = positions[ARRAY_PLACEMENT_POS_STARTX];
        int startY = positions[ARRAY_PLACEMENT_POS_STARTY];
        int startZ = positions[ARRAY_PLACEMENT_POS_STARTZ];
        int xas = positions[ARRAY_PLACEMENT_POS_XAS];
        int zas = positions[ARRAY_PLACEMENT_POS_ZAS];

        //Place blocks forward, to the right and up
        for (int iterH = 0; iterH < height; iterH++) {
            for (int iterW = 0; iterW < width; iterW++) {
                for (int iterD = 0; iterD < depth; iterD++) {
                    int placeX, placeZ;

                    //If equal to 1 then its direction is NorthSouth
                    if (isNorthSouth) {
                        placeX = startX + (iterW * xas);
                        placeZ = startZ + (iterD * zas);
                    } else {
                        placeX = startX + (iterD * xas);
                        placeZ = startZ + (iterW * zas);
                    }

                    //Place a single block in the world at calculate position
                    Block block = player.getWorld().getBlockAt(placeX, (startY + iterH), placeZ);
                    if (material != null) {
                        block.setType(material, applyPhysics);
                    }
                }
            }
        }
    }

    public static void placeBlockByItem(World world, Material material, boolean applyPhysics) {
        // Block block = world.getBlockAt(xas, yas, zas);
        // block.setType(material, applyPhysics);
        // Location location = block.getLocation();
    }

    public static int[] getPlacementPostionByItem(Item item) {
        // TODO:RG moet nog iets teruggeven
        return new int[]{};
    }

    /**
     * Handle block BlockEvents
     * If there can only be one, also check 'blockBreak' method
     *
     * @param player
     * @param block
     */
    @SuppressWarnings("incomplete-switch")
    public static void blockPlaced(Player player, Block block) {
        player.sendMessage("blockPlaced");
        //check if player isnt null. Maybe block place event is triggerd by something else??? EnderMan???
        if (player != null) {
            switch (CustomItem.getCustomItem(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName())) {
                case COPY_FROM_BLOCK:
                    thereCanBeOnlyOne(player, block, ErowTVConstants.MEMORY_COPY_FROM_POSITION);
                    break;
                case COPY_TO_BLOCK:
                    thereCanBeOnlyOne(player, block, ErowTVConstants.MEMORY_COPY_TO_POSITION);
                    break;
                case DESTROY_FROM_BLOCK:
                    thereCanBeOnlyOne(player, block, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION);
                    break;
                case DESTROY_TO_BLOCK:
                    thereCanBeOnlyOne(player, block, ErowTVConstants.MEMORY_DESTROY_TO_POSITION);
                    break;
                case TOOL_SIGN:
                    player.sendMessage("TOOL_SIGN PLACED");
                    //This is a sign, make it editable(false).
//                    ((Sign) block.getState()).setEditable(false); TODO:RG is Editable false needed?
                    String memoryName = SignTools.createMemoryName(player, block, ErowTVConstants.MEMORY_TOOL_SIGN_POSITION);
                    player.sendMessage("MEMORY="+memoryName);
                    SignTools.thereCanBeMore(player, block, memoryName);
                    break;

                case NO_RECIPE:
                    // TODO:RG if not found then check clickEvent from CraftBukkit
                    break;
            }
        }
    }

    /**
     * If a block breaks that is in the players memory then it should be removed from the memory.
     * So it doenst mess up other actions
     *
     * @param player
     * @param block
     */
    public static void blockBreak(Player player, Block block) {
        //Check if the broken block is COPY_FROM, if so then remove it from the memory
        if (ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_COPY_FROM_POSITION) &&
                isBlockPositionTheSame(block, (List<Integer>) ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_COPY_FROM_POSITION))) {
            ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_COPY_FROM_POSITION);

            //Check if the broken block is COPY_TO, if so then remove it from the memory
        } else if (ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_COPY_TO_POSITION) &&
                isBlockPositionTheSame(block, (List<Integer>) ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_COPY_TO_POSITION))) {
            ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_COPY_TO_POSITION);

            //Check if the broken block is DESTROY_FROM, if so then remove it from the memory
        } else if (ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION) &&
                isBlockPositionTheSame(block, (List<Integer>) ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION))) {
            ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_FROM_POSITION);

            //Check if the broken block is DESTROY_TO, if so then remove it from the memory
        } else if (ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION) &&
                isBlockPositionTheSame(block, (List<Integer>) ErowTV.readPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION))) {
            ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_DESTROY_TO_POSITION);

        } else if (ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_TOOL_SIGN_POSITION)){
            String memoryName = SignTools.createMemoryName(player, block, ErowTVConstants.MEMORY_TOOL_SIGN_POSITION);
            ErowTV.removeMemoryFromPlayerMemory(player, memoryName);
        }
    }

    /**
     * @param player       that triggers this method
     * @param clickedBlock the block the Player has clicked on
     * @param setAir       should blocks be AIR. Easy for cleaning up.
     * @return array startX, startY, startZ, xas(+1 or -1), zas(+1 or -1) and if isNorthSouth direction
     */
    public static int[] getPlacementPositionByPlayer(Player player, Block clickedBlock, boolean setAir) {
        int isNorthSouth = ErowTVConstants.NOT_NORTH_SOUTH;
        int zas = 1;
        int xas = 1;

        //If clickedBlock is null then the players position needs to be used
        int startX = clickedBlock != null ? clickedBlock.getLocation().getBlockX() : player.getLocation().getBlockX();
        int startZ = clickedBlock != null ? clickedBlock.getLocation().getBlockZ() : player.getLocation().getBlockZ();

        //If clickedBlock is used then BlockY + 1 (unless its AIR) to place it on top of the clickedBlock
        int startY = clickedBlock != null ?
                (setAir ? clickedBlock.getLocation().getBlockY() : clickedBlock.getLocation().getBlockY() + 1)
                : player.getLocation().getBlockY();

        //Even with clickedBlock we need to use the player direction
        switch (PlayerTools.getSimplePlayerDirection(player)) {
            case NORTH:
                zas = zas * -1;
                startZ = clickedBlock != null ? startZ : startZ - 1;
                isNorthSouth = ErowTVConstants.IS_NORTH_SOUTH;
                break;
            case EAST:
                startX = clickedBlock != null ? startX : startX + 1;
                break;
            case SOUTH:
                xas = xas * -1;
                startZ = clickedBlock != null ? startZ : startZ + 1;
                isNorthSouth = ErowTVConstants.IS_NORTH_SOUTH;
                break;
            case WEST:
                zas = zas * -1;
                xas = xas * -1;
                startX = clickedBlock != null ? startX : startX - 1;
                break;
            default:
                break;
        }

        return new int[]{startX, startY, startZ, xas, zas, isNorthSouth};
    }

    /**
     * Certain blocks can only exist on one place at a time.
     * If the block is placed then the same block at a other position needs to be removed.
     *
     * @param player
     * @param block
     */
    public static void thereCanBeOnlyOne(Player player, Block block, String memoryName) {
        //Get world player is in. It is needed to store the position of the block
        Environment environment = player.getWorld().getEnvironment();
        int playersWorld = (environment == Environment.NETHER ? ErowTVConstants.WORLD_NETHER : environment == Environment.NORMAL ? ErowTVConstants.WORLD_NORMAL : ErowTVConstants.WORLD_END);

        //Read players memory to get the position
        List<Integer> position = (List<Integer>) ErowTV.readPlayerMemory(player, memoryName);

        //Only if there is a memory present (meaning there already exits a block)
        if (position != null && position.size() == ErowTVConstants.POSITION_SIZE) {
            //Check if the world the player is in is the same as the memory's position world
            //To prevent removing a block in the wrong world that is needed
            //And if the position isnt the same. If you remove the block by hand and replace it on this position
            //it will immediately be replaced with AIR
            if (position.get(ErowTVConstants.BLOCK_POS_WORLD) == playersWorld && !isBlockPositionTheSame(block, position)) {
                Block worldBlock = player.getWorld().getBlockAt(position.get(ErowTVConstants.BLOCK_POS_X), position.get(ErowTVConstants.BLOCK_POS_Y), position.get(ErowTVConstants.BLOCK_POS_Z));
                worldBlock.setType(Material.AIR, ErowTVConstants.APPLY_PHYSICS);
            }
            //Old one needs to go
            ErowTV.removeMemoryFromPlayerMemory(player, memoryName);
        }

        //Store it in players memory
        List<Integer> toPosition = Arrays.asList(playersWorld, block.getX(), block.getY(), block.getZ());
        ErowTV.storePlayerMemory(player, memoryName, toPosition);
    }


    /**
     * Check if the blocks position is the same as the given position in its world
     *
     * @param block
     * @param position List
     * @return true if position is the same or false if not
     */
    public static boolean isBlockPositionTheSame(Block block, List<Integer> position) {
        //If null, then position can never be the same
        if (position != null && position.size() == ErowTVConstants.POSITION_SIZE) {
            // Get blocks environment (NETHER, NORMAL or END)
            Environment environment = block.getWorld().getEnvironment();
            int blocksWorld = (environment == Environment.NETHER ? ErowTVConstants.WORLD_NETHER : environment == Environment.NORMAL ?
                    ErowTVConstants.WORLD_NORMAL : ErowTVConstants.WORLD_END);

            // Check if position is the same
            if (blocksWorld == position.get(ErowTVConstants.BLOCK_POS_WORLD) && block.getX() == position.get(ErowTVConstants.BLOCK_POS_X) &&
                    block.getY() == position.get(ErowTVConstants.BLOCK_POS_Y) && block.getZ() == position.get(ErowTVConstants.BLOCK_POS_Z)) {
                return true;
            }
        }
        // if not
        return false;
    }

    public static boolean doesBlockExist(Player player, List<Integer> position) {
        //If null, then position can never be the same
        if (position != null && position.size() == ErowTVConstants.POSITION_SIZE) {
            // Get players environment (NETHER, NORMAL or END)
            Environment environment = player.getWorld().getEnvironment();
            int playerWorld = (environment == Environment.NETHER ? ErowTVConstants.WORLD_NETHER : environment == Environment.NORMAL ?
                    ErowTVConstants.WORLD_NORMAL : ErowTVConstants.WORLD_END);

            //If the world is the same, check the position
            if (playerWorld == position.get(ErowTVConstants.BLOCK_POS_WORLD)) {
                Block block = player.getWorld().getBlockAt(position.get(ErowTVConstants.BLOCK_POS_X), position.get(ErowTVConstants.BLOCK_POS_Y),
                        position.get(ErowTVConstants.BLOCK_POS_Z));

                return (block.getType() != Material.AIR);
            }
        }
        return false;
    }

    /**
     * Useful for pasting blocks without knowing the dept, height en widht.
     * Find out if xas needs to go with +1 or -1 for each position
     * Same for zas does it need +1 or -1 for each position
     * If its position is to NORTH of SOUTH
     * How far its from the other block (depth, height, width)
     *
     * @param block
     * @param clickedBlock
     * @param customBlockFace
     * @return startX, startY, startZ, depth, height, width and directions ints for xas (-1 or +1) en zas (-1 or +1)
     */
    public static int[] getBlockDirections(List<Integer> block, Block clickedBlock, BlockFace customBlockFace) {
        //Check if its BlockFace is facing to the North or to the South. It is importent for placing/copying the blocks
        int isNorthSouth = ErowTVConstants.NOT_NORTH_SOUTH;

        int startX = block.get(ErowTVConstants.BLOCK_POS_X);
        int startY = block.get(ErowTVConstants.BLOCK_POS_Y);
        int startZ = block.get(ErowTVConstants.BLOCK_POS_Z);

        int zas = 1;
        int xas = 1;

        int facingDirection = 0; //North = 1, East = 5, South = 9, West = 13;

        //TODO:RG Maybe better check for BlockFace if To block is behind you???
        //Maybe just a warning that the sign cannot be between both block??

        //If customBlockFace is null then search the BlockFace of the clickedBlock
        BlockFace blockFace = (customBlockFace != null ? customBlockFace : getBlockFaceClickedBlock(clickedBlock));
        if (blockFace == null) {
            //return empty int[]
            return new int[]{};
        }

        //Calculate the correct distances
        switch (blockFace) {
            case NORTH:
                zas = zas * -1;
                startZ = startZ - 1;
                isNorthSouth = ErowTVConstants.IS_NORTH_SOUTH;
                facingDirection = DirectionalRotation.NORTH.getRotationValue();
                break;
            case EAST:
                startX = startX + 1;
                facingDirection = DirectionalRotation.EAST.getRotationValue();
                break;
            case SOUTH:
                xas = xas * -1;
                startZ = startZ + 1;
                isNorthSouth = ErowTVConstants.IS_NORTH_SOUTH;
                facingDirection = DirectionalRotation.SOUTH.getRotationValue();
                break;
            case WEST:
                zas = zas * -1;
                xas = xas * -1;
                startX = startX - 1;
                facingDirection = DirectionalRotation.WEST.getRotationValue();
                break;
            default:
                //This is not good. return empty array
                return new int[]{};
        }

        //TODO:RG startX and startZ??
        //Height needs +1 because the height of the COPY_TO block its the actual height we are going to copy
        return new int[]{startX, startY, startZ, xas, zas, isNorthSouth, facingDirection};
    }


    /**
     * Useful for copying blocks etc.
     * Find out if xas needs to go with +1 or -1 for each position
     * Same for zas does it need +1 or -1 for each position
     * If its position is to NORTH of SOUTH
     * How far its from the other block (depth, height, width)
     * Facing direction when copying, to calculate a new facing direction when pasting
     *
     * @param fromBlock
     * @param toBlock
     * @param clickedBlock (wallsign or button)
     * @return startX, startY, startZ, depth, height, width, directions ints for xas (-1 or +1) en zas (-1 or +1) and facingDirection
     */
    public static int[] getBlockDirectionsFromTo(List<Integer> fromBlock, List<Integer> toBlock, Block clickedBlock, BlockFace customBlockFace) {
        //Check if its BlockFace is facing to the North or to the South. It is importent for placing/copying the blocks
        int isNorthSouth = ErowTVConstants.NOT_NORTH_SOUTH;

        //Height is always Yas
        int height = getDistanceBetween(fromBlock.get(ErowTVConstants.BLOCK_POS_Y), toBlock.get(ErowTVConstants.BLOCK_POS_Y));
        //get these in switch
        int depth = 0;
        int width = 0;
        int startX = fromBlock.get(ErowTVConstants.BLOCK_POS_X);
        int startY = fromBlock.get(ErowTVConstants.BLOCK_POS_Y);
        int startZ = fromBlock.get(ErowTVConstants.BLOCK_POS_Z);

        int fromBlockZ = fromBlock.get(ErowTVConstants.BLOCK_POS_Z);
        int toBlockZ = toBlock.get(ErowTVConstants.BLOCK_POS_Z);
        boolean fromBlockZGreater = (fromBlockZ > toBlockZ ? true : false);

        int fromBlockX = fromBlock.get(ErowTVConstants.BLOCK_POS_X);
        int toBlockX = toBlock.get(ErowTVConstants.BLOCK_POS_X);
        boolean fromBlockXGreater = (fromBlockX > toBlockX ? true : false);

        int zas = (fromBlockZGreater ? -1 : 1);
        int xas = (fromBlockXGreater ? -1 : 1);

        int facingDirection = 0; //North = 1, East = 5, South = 9, West = 13;

        //TODO:RG Maybe better check for BlockFace if To block is behind you???
        //Maybe just a warning that the sign cannot be between both block??

        //If customBlockFace is null then search the BlockFace of the clickedBlock
        BlockFace blockFace = (customBlockFace != null ? customBlockFace : getBlockFaceClickedBlock(clickedBlock));
        if (blockFace == null) {
            //return empty int[]
            return new int[]{};
        }

        //Calculate the correct distances
        switch (blockFace) {
            case NORTH:
                //Z = depth
                depth = getDistanceBetween((fromBlockZGreater ? fromBlockZ - 1 : fromBlockZ + 1), toBlockZ);
                width = getDistanceBetween(fromBlockX, (fromBlockXGreater ? toBlockX - 1 : toBlockX + 1));
                startZ = (fromBlockZGreater ? startZ - 1 : startZ + 1);
                isNorthSouth = ErowTVConstants.IS_NORTH_SOUTH;
                facingDirection = DirectionalRotation.NORTH.getRotationValue();
                break;
            case EAST:
                //X = depth
                depth = getDistanceBetween((fromBlockXGreater ? fromBlockX - 1 : fromBlockX + 1), toBlockX);
                width = getDistanceBetween(fromBlockZ, (fromBlockZGreater ? toBlockZ - 1 : toBlockZ + 1));
                startX = (fromBlockXGreater ? startX - 1 : startX + 1);
                facingDirection = DirectionalRotation.EAST.getRotationValue();
                break;
            case SOUTH:
                //Z = depth
                depth = getDistanceBetween((fromBlockZGreater ? fromBlockZ - 1 : fromBlockZ + 1), toBlockZ);
                width = getDistanceBetween(fromBlockX, (fromBlockXGreater ? toBlockX - 1 : toBlockX + 1));
                startZ = (fromBlockZGreater ? startZ - 1 : startZ + 1);
                isNorthSouth = ErowTVConstants.IS_NORTH_SOUTH;
                facingDirection = DirectionalRotation.SOUTH.getRotationValue();
                break;
            case WEST:
                //X = depth
                depth = getDistanceBetween((fromBlockXGreater ? fromBlockX - 1 : fromBlockX + 1), toBlockX);
                width = getDistanceBetween(fromBlockZ, (fromBlockZGreater ? toBlockZ - 1 : toBlockZ + 1));
                startX = (fromBlockXGreater ? startX - 1 : startX + 1);
                facingDirection = DirectionalRotation.WEST.getRotationValue();
                break;
            default:
                //This is not good. return empty array
                return new int[]{};
        }

        //TODO:RG startX and startZ??
        //Height needs +1 because the height of the COPY_TO block its the actual height we are going to copy
        return new int[]{startX, startY, startZ, depth, (height + 1), width, xas, zas, isNorthSouth, facingDirection};
    }

    //Either Sign, WallSign, Stone Button or a Lever
    //Only used when you need a Blockface after clicking a certain Item
    public static BlockFace getBlockFaceClickedBlock(Block clickedBlock) {
        if (clickedBlock.getType() == Material.SPRUCE_SIGN) {
            return ((org.bukkit.block.data.type.Sign) clickedBlock.getState().getBlockData()).getRotation().getOppositeFace();

        } else if (clickedBlock.getType() == Material.SPRUCE_WALL_SIGN) {
            return ((org.bukkit.block.data.type.WallSign) clickedBlock.getState().getBlockData()).getFacing().getOppositeFace();

        } else if (clickedBlock.getType() == Material.STONE_BUTTON) {
            return ((org.bukkit.material.Button) clickedBlock.getState().getData()).getAttachedFace();

        } else if (clickedBlock.getType() == Material.LEVER) {
            return ((org.bukkit.material.Lever) clickedBlock.getState().getData()).getAttachedFace();

        }

        return null;
    }

    /**
     * Calculate distance bewtween 2 (x, y or z) positions
     *
     * @param positionFrom
     * @param positionTo
     * @return int distance between positions
     */
    public static int getDistanceBetween(int positionFrom, int positionTo) {
        //If one of them is below zero
        if (positionFrom < 0 && positionTo >= 0) {
            positionFrom = positionFrom * -1;

            //if FROM = -6 and TO = 7
            // -6 = now +6 -> so 6 + 7 = 13 distance
            return positionFrom + positionTo;
        } else if (positionTo < 0 && positionFrom >= 0) {
            positionTo = positionTo * -1;
            //if FROM = 3 and TO = -5
            // -5 = now +5 -> so 3 + 5 = 8 distance
            return positionFrom + positionTo;
        } else {
            //in this case they are both below zero
            if (positionFrom < 0) {
                // * -1 to make them positive numbers
                positionFrom = positionFrom * -1;
                positionTo = positionTo * -1;
            }

            //Here they are zero or above zero
            if (positionFrom == positionTo) {
                //same position is NO distance
                //TODO:RG In this case there is a problem???, then there is nothing between the blocks???
                return 0;
            } else if (positionFrom > positionTo) {
                //if FROM = 8 and TO = 2
                //8 - 2 = 6 distance
                return positionFrom - positionTo;
            } else {
                //if FROM = 3 and TO = 7
                //7 - 3 = 4 distance
                return positionTo - positionFrom;
            }
        }
    }

    /**
     * Get the difference between rotations of blockface when sign was clicked during copy and
     * when sign was clicked now during paste.
     * That number can be used to calculate the new position of items like stairs, furnaces, etc.
     *
     * @param player
     * @param wasFacing
     * @param blockFacing
     * @return
     */
    public static int getRotationDifference(Player player, int wasFacing, int blockFacing) {
        //Get new blockface direction
        switch (wasFacing) {
            case ErowTVConstants.FACING_NORTH:
                return (blockFacing == DirectionalRotation.EAST.getRotationValue() ? 1 :
                        blockFacing == DirectionalRotation.SOUTH.getRotationValue() ? 2 :
                                blockFacing == DirectionalRotation.WEST.getRotationValue() ? 3 : 0);
            case ErowTVConstants.FACING_EAST:
                return (blockFacing == DirectionalRotation.NORTH.getRotationValue() ? 3 :
                        blockFacing == DirectionalRotation.SOUTH.getRotationValue() ? 1 :
                                blockFacing == DirectionalRotation.WEST.getRotationValue() ? 2 : 0);
            case ErowTVConstants.FACING_SOUTH:
                return (blockFacing == DirectionalRotation.EAST.getRotationValue() ? 3 :
                        blockFacing == DirectionalRotation.NORTH.getRotationValue() ? 2 :
                                blockFacing == DirectionalRotation.WEST.getRotationValue() ? 1 : 0);
            case ErowTVConstants.FACING_WEST:
                return (blockFacing == DirectionalRotation.EAST.getRotationValue() ? 2 :
                        blockFacing == DirectionalRotation.SOUTH.getRotationValue() ? 3 :
                                blockFacing == DirectionalRotation.NORTH.getRotationValue() ? 1 : 0);
            default:
                return 0;
        }
    }


    public static BlockFace getNewBlockFaceDirection(Player player, BlockFace blockFace, int rotate) {
        //Get new blockface direction
        switch (blockFace) {
            case NORTH:
                return (rotate == 1 ? BlockFace.EAST : rotate == 2 ? BlockFace.SOUTH : BlockFace.WEST);
            case NORTH_NORTH_EAST:
                return (rotate == 1 ? BlockFace.EAST_SOUTH_EAST : rotate == 2 ? BlockFace.SOUTH_SOUTH_WEST : BlockFace.WEST_NORTH_WEST);
            case NORTH_EAST:
                return (rotate == 1 ? BlockFace.SOUTH_EAST : rotate == 2 ? BlockFace.SOUTH_WEST : BlockFace.NORTH_WEST);
            case EAST_NORTH_EAST:
                return (rotate == 1 ? BlockFace.SOUTH_SOUTH_EAST : rotate == 2 ? BlockFace.WEST_SOUTH_WEST : BlockFace.NORTH_NORTH_WEST);
            case EAST:
                return (rotate == 1 ? BlockFace.SOUTH : rotate == 2 ? BlockFace.WEST : BlockFace.NORTH);
            case EAST_SOUTH_EAST:
                return (rotate == 1 ? BlockFace.SOUTH_SOUTH_WEST : rotate == 2 ? BlockFace.WEST_NORTH_WEST : BlockFace.NORTH_NORTH_EAST);
            case SOUTH_EAST:
                return (rotate == 1 ? BlockFace.SOUTH_WEST : rotate == 2 ? BlockFace.NORTH_WEST : BlockFace.NORTH_EAST);
            case SOUTH_SOUTH_EAST:
                return (rotate == 1 ? BlockFace.WEST_SOUTH_WEST : rotate == 2 ? BlockFace.NORTH_NORTH_WEST : BlockFace.EAST_NORTH_EAST);
            case SOUTH:
                return (rotate == 1 ? BlockFace.WEST : rotate == 2 ? BlockFace.NORTH : BlockFace.EAST);
            case SOUTH_SOUTH_WEST:
                return (rotate == 1 ? BlockFace.WEST_NORTH_WEST : rotate == 2 ? BlockFace.NORTH_NORTH_EAST : BlockFace.EAST_SOUTH_EAST);
            case SOUTH_WEST:
                return (rotate == 1 ? BlockFace.NORTH_WEST : rotate == 2 ? BlockFace.NORTH_EAST : BlockFace.SOUTH_EAST);
            case WEST_SOUTH_WEST:
                return (rotate == 1 ? BlockFace.NORTH_NORTH_WEST : rotate == 2 ? BlockFace.EAST_NORTH_EAST : BlockFace.SOUTH_SOUTH_EAST);
            case WEST:
                return (rotate == 1 ? BlockFace.NORTH : rotate == 2 ? BlockFace.EAST : BlockFace.SOUTH);
            case WEST_NORTH_WEST:
                return (rotate == 1 ? BlockFace.NORTH_NORTH_EAST : rotate == 2 ? BlockFace.EAST_SOUTH_EAST : BlockFace.SOUTH_SOUTH_WEST);
            case NORTH_WEST:
                return (rotate == 1 ? BlockFace.NORTH_EAST : rotate == 2 ? BlockFace.SOUTH_EAST : BlockFace.SOUTH_WEST);
            case NORTH_NORTH_WEST:
                return (rotate == 1 ? BlockFace.EAST_NORTH_EAST : rotate == 2 ? BlockFace.SOUTH_SOUTH_EAST : BlockFace.WEST_SOUTH_WEST);

            //Dont do anything with this (yet)
            case DOWN:
                break;
            case SELF:
                break;
            case UP:
                break;
            default:
                break;
        }

        return blockFace;
    }

    public static int getCurrentBlockFaceRotation(Player player, BlockFace blockFace) {
        //Get new blockface direction
        switch (blockFace) {
            case NORTH:
                return DirectionalRotation.NORTH.getRotationValue();
            case NORTH_NORTH_EAST:
                return DirectionalRotation.NORTH_NORTH_EAST.getRotationValue();
            case NORTH_EAST:
                return DirectionalRotation.NORTH_EAST.getRotationValue();
            case EAST_NORTH_EAST:
                return DirectionalRotation.EAST_NORTH_EAST.getRotationValue();
            case EAST:
                return DirectionalRotation.EAST.getRotationValue();
            case EAST_SOUTH_EAST:
                return DirectionalRotation.EAST_SOUTH_EAST.getRotationValue();
            case SOUTH_EAST:
                return DirectionalRotation.SOUTH_EAST.getRotationValue();
            case SOUTH_SOUTH_EAST:
                return DirectionalRotation.SOUTH_SOUTH_EAST.getRotationValue();
            case SOUTH:
                return DirectionalRotation.SOUTH.getRotationValue();
            case SOUTH_SOUTH_WEST:
                return DirectionalRotation.SOUTH_SOUTH_WEST.getRotationValue();
            case SOUTH_WEST:
                return DirectionalRotation.SOUTH_WEST.getRotationValue();
            case WEST_SOUTH_WEST:
                return DirectionalRotation.WEST_SOUTH_WEST.getRotationValue();
            case WEST:
                return DirectionalRotation.WEST.getRotationValue();
            case WEST_NORTH_WEST:
                return DirectionalRotation.WEST_NORTH_WEST.getRotationValue();
            case NORTH_WEST:
                return DirectionalRotation.NORTH_WEST.getRotationValue();
            case NORTH_NORTH_WEST:
                return DirectionalRotation.NORTH_NORTH_WEST.getRotationValue();

            //Dont do anything with this (yet)
            case DOWN:
                return DirectionalRotation.NO_ROTATION.getRotationValue();
            case SELF:
                return DirectionalRotation.NO_ROTATION.getRotationValue();
            case UP:
                return DirectionalRotation.NO_ROTATION.getRotationValue();
            default:
                return DirectionalRotation.NO_ROTATION.getRotationValue();
        }
    }

    /**
     * Only for items/blocks with a directional
     *
     * @param player
     * @param block
     * @param blockFace direction the block with sign clicked is facing
     */
    public static void changeDataForBlockType(Player player, Block block, BlockFace blockFace, String[] blockState) {
        //TODO:RG Need to save Directions en inverted -> calculate correct ones for pasting
//		player.sendMessage(block.getState().getData().getClass().getName());

        //!!Remember: Blocks are not copies of data. So if skull is a block then this:
        //Skull skull = (Skull)block.getState();
        //skull.setSkullType(SkullType.ZOMBIE);
        //No need to CAST to other Object, only with materials like Stairs
        //And it needs its own update call, like skull.update();

        switch (DirectionalMaterial.getDirectionMaterial(block.getState().getData().getClass().getName())) {
            case ORG_BUKKIT_MATERIAL_BANNER:
                break;
            case ORG_BUKKIT_MATERIAL_BED:
                break;
            case ORG_BUKKIT_MATERIAL_BUTTON:
                break;
            case ORG_BUKKIT_MATERIAL_CHEST:
                break;
            case ORG_BUKKIT_MATERIAL_COCOAPLANT:
                break;
            case ORG_BUKKIT_MATERIAL_COMPARATOR:
                break;
            case ORG_BUKKIT_MATERIAL_DIODE:
                break;
            case ORG_BUKKIT_MATERIAL_DIRECTIONALCONTAINER:
                break;
            case ORG_BUKKIT_MATERIAL_DISPENSER:
                break;
            case ORG_BUKKIT_MATERIAL_DOOR:
                break;
            case ORG_BUKKIT_MATERIAL_ENDERCHEST:
                break;
            case ORG_BUKKIT_MATERIAL_FURNACE:
                player.sendMessage("FURNACE");
                Furnace furnace = (Furnace) block.getState();
                //		org.bukkit.material.Sign dataSign = (org.bukkit.material.Sign)block.getData();
                org.bukkit.material.Furnace furn = ((org.bukkit.material.Furnace) furnace.getData());
                furn.setFacingDirection(blockFace);
                //TODO:RG moet nog
//			block.setData(furn.getData());
                break;
            case ORG_BUKKIT_MATERIAL_FURNACEANDDISPENSER:
                break;
            case ORG_BUKKIT_MATERIAL_GATE:
                break;
            case ORG_BUKKIT_MATERIAL_HOPPER:
                break;
            case ORG_BUKKIT_MATERIAL_LADDER:
                break;
            case ORG_BUKKIT_MATERIAL_LEVER:
                break;
            case ORG_BUKKIT_MATERIAL_OBSERVER:
                break;
            case ORG_BUKKIT_MATERIAL_PISTONBASEMATERIAL:
                break;
            case ORG_BUKKIT_MATERIAL_PISTONEXTENSIONMATERIAL:
                break;
            case ORG_BUKKIT_MATERIAL_PUMPKIN:
                break;
            case ORG_BUKKIT_MATERIAL_REDSTONETORCH:
                break;
            case ORG_BUKKIT_MATERIAL_SIGN:
                break;
            case ORG_BUKKIT_MATERIAL_SIMPLEATTACHABLEMATERIALDATA:
                break;
            case ORG_BUKKIT_MATERIAL_SKULL:
                //TODO:RG
                player.sendMessage("SKULL");
                Skull skull = (Skull) block.getState();
                skull.setSkullType(SkullType.ZOMBIE);
                skull.setRotation(blockFace);
                skull.update();
                break;
            case ORG_BUKKIT_MATERIAL_STAIRS:
                player.sendMessage("STAIRS");
                Stairs stairs = (Stairs) block.getState().getData();
//			org.bukkit.material.Sign dataSign = (org.bukkit.material.Sign)block.getData();
                stairs.setFacingDirection(blockFace.getOppositeFace());
                //TODO:RG moet nog
//			block.setData(stairs.getData());
                break;
            case ORG_BUKKIT_MATERIAL_TORCH:
                break;
            case ORG_BUKKIT_MATERIAL_TRAPDOOR:
                break;
            case ORG_BUKKIT_MATERIAL_TRIPWIREHOOK:
                break;
            case NO_ITEM:
            default:
                break;
        }
    }

    /**
     * Only for items/blocks with a directional
     *
     * @param player
     * @param block
     */
    public static String[] getDataForBlockType(Player player, Block block) {
        //TODO:RG FIRST!!!!! get all the stuff to copy
        //Need to save Directions en inverted -> calculate correct ones for pasting

//		player.sendMessage(block.getState().getData().getClass().getName());

        switch (DirectionalMaterial.getDirectionMaterial(block.getState().getData().getClass().getName())) {
            case ORG_BUKKIT_MATERIAL_BANNER:
                break;
            case ORG_BUKKIT_MATERIAL_BED:
                break;
            case ORG_BUKKIT_MATERIAL_BUTTON:
                break;
            case ORG_BUKKIT_MATERIAL_CHEST:
                break;
            case ORG_BUKKIT_MATERIAL_COCOAPLANT:
                break;
            case ORG_BUKKIT_MATERIAL_COMPARATOR:
                break;
            case ORG_BUKKIT_MATERIAL_DIODE:
                break;
            case ORG_BUKKIT_MATERIAL_DIRECTIONALCONTAINER:
                break;
            case ORG_BUKKIT_MATERIAL_DISPENSER:
                break;
            case ORG_BUKKIT_MATERIAL_DOOR:
                break;
            case ORG_BUKKIT_MATERIAL_ENDERCHEST:
                break;
            case ORG_BUKKIT_MATERIAL_FURNACE:
                player.sendMessage("FURNACE");
                Furnace furnace = (Furnace) block.getState();
                //		org.bukkit.material.Sign dataSign = (org.bukkit.material.Sign)block.getData();
                org.bukkit.material.Furnace newfurn = ((org.bukkit.material.Furnace) furnace.getData());
//			furn.setFacingDirection(blockFace);
                return new String[]{String.valueOf(getCurrentBlockFaceRotation(player, newfurn.getFacing()))};
            case ORG_BUKKIT_MATERIAL_FURNACEANDDISPENSER:
                break;
            case ORG_BUKKIT_MATERIAL_GATE:
                break;
            case ORG_BUKKIT_MATERIAL_HOPPER:
                break;
            case ORG_BUKKIT_MATERIAL_LADDER:
                break;
            case ORG_BUKKIT_MATERIAL_LEVER:
                break;
            case ORG_BUKKIT_MATERIAL_OBSERVER:
                break;
            case ORG_BUKKIT_MATERIAL_PISTONBASEMATERIAL:
                break;
            case ORG_BUKKIT_MATERIAL_PISTONEXTENSIONMATERIAL:
                break;
            case ORG_BUKKIT_MATERIAL_PUMPKIN:
                break;
            case ORG_BUKKIT_MATERIAL_REDSTONETORCH:
                break;
            case ORG_BUKKIT_MATERIAL_SIGN:
                break;
            case ORG_BUKKIT_MATERIAL_SIMPLEATTACHABLEMATERIALDATA:
                break;
            case ORG_BUKKIT_MATERIAL_SKULL:
                player.sendMessage("SKULL");

                Skull skull = (Skull) block.getState();
                org.bukkit.material.Skull newSkull = ((org.bukkit.material.Skull) skull.getData());

//			player.sendMessage(skull.getRotation().toString());
//			player.sendMessage(newSkull.getFacing().toString());
//			player.sendMessage(skull.getSkullType().toString());
//			player.sendMessage(newSkull.getItemType().toString());
//			player.sendMessage(block.getState().toString());
//			Skull skull = (Skull)block.getState();
//			org.bukkit.material.Skull newSkull = ((org.bukkit.material.Skull)skull.getData());
//			return new String[]{((Skull)block.getState()).getRotation().toString()};
                return new String[]{String.valueOf(getCurrentBlockFaceRotation(player, ((Skull) block.getState()).getRotation()))};
            case ORG_BUKKIT_MATERIAL_STAIRS:
                player.sendMessage("STAIRS");
                Stairs stairs = (Stairs) block.getState().getData();
                return new String[]{String.valueOf(getCurrentBlockFaceRotation(player, stairs.getFacing()))};
            case ORG_BUKKIT_MATERIAL_TORCH:
                break;
            case ORG_BUKKIT_MATERIAL_TRAPDOOR:
                break;
            case ORG_BUKKIT_MATERIAL_TRIPWIREHOOK:
                break;
            case NO_ITEM:
            default:
                break;
        }

        return new String[]{};
    }

}
