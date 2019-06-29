package graver.erowtv.tools;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.BlockTools;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;

public final class NumbersTool implements ErowTVConstants {

    private int numHeight = 5;
    private int numWidth = 5;
    //Needs to be 2 because every other char has already got a starting AIR block.
    //Else you will get one AIR block on one side to many
    private int dotWidth = 2;

    // First of every String is 0 (AIR) so numbers can be place next to each other
    // with space between them
    private List<String> numZero = List.of("ppppp", "0xxxx", "0x00x", "0x00x", "0x00x", "0xxxx");
    private List<String> numOne = List.of("ppppp", "00xxx", "000x0", "000x0", "00xx0", "000x0");
    private List<String> numTwo = List.of("ppppp", "0xxxx", "0x000", "0xxxx", "0000x", "0xxxx");
    private List<String> numThree = List.of("ppppp", "0xxxx", "0000x", "00xxx", "0000x", "0xxxx");
    private List<String> numFour = List.of("ppppp", "0000x", "0000x", "0xxxx", "0x00x", "0x00x");
    private List<String> numFive = List.of("ppppp", "0xxxx", "0000x", "0xxxx", "0x000", "0xxxx");
    private List<String> numSix = List.of("ppppp", "0xxxx", "0x00x", "0xxxx", "0x000", "0xxxx");
    private List<String> numSeven = List.of("ppppp", "000x0", "000x0", "00xxx", "000x0", "0xxx0");
    private List<String> numEight = List.of("ppppp", "0xxxx", "0x00x", "0xxxx", "0x00x", "0xxxx");
    private List<String> numNine = List.of("ppppp", "0xxxx", "0000x", "0xxxx", "0x00x", "0xxxx");
    private List<String> dotChar = List.of("ppppp", "0000x", "00000", "00000", "00000", "00000");
    private List<String> doubleDotChar = List.of("ppppp", "00000", "0000x", "00000", "0000x", "00000");
    //also no platform
    private List<String> numClear = List.of("00000", "00000", "00000", "00000", "00000", "00000");

    Material materialOnTop;
    Material material;
    Player player;
    Block block;
    BlockFace blockFace;

    public NumbersTool(Player player, Block block, BlockFace blockFace){
        this.player = player;
        this.blockFace = blockFace;
        //First get materials from block and on top of the block
        this.materialOnTop = BlockTools.getMaterialOnTopOfBlock(player, block);
        this.material = block.getBlockData().getMaterial();

        //Now do Y - 1 to start on the ground, so -1 to get clicked block position
        this.block = player.getWorld().getBlockAt(block.getLocation().getBlockX(),
                block.getLocation().getBlockY()-1,block.getLocation().getBlockZ());
    }

    /**
     * Build entire number
     *
     * @param bigNumber
     */
    public void buildEntireNumber(String bigNumber) {
        if(isDebug) {
            player.sendMessage("BigNumber: " + bigNumber);
        }

        char[] numberPieces = bigNumber.toCharArray();

        player.sendMessage("MATERIALONTOP: " + materialOnTop);

        //TODO:RG nog checken voor correct width?

        //Check first char for width and -1 because of the starting AIR block
        String firstNum = bigNumber.substring(bigNumber.length());
        int firstWidth = (firstNum.equalsIgnoreCase(":") || firstNum.equalsIgnoreCase(".") ?
                dotWidth : numWidth) - 1;

        //Calculate correct starting point
        Block startingBlockPostionForNumber = calculateCorrectStartingPosition(player, block, blockFace, firstWidth);

        //Has to contain numbers else skip
        if (numberPieces.length > 0) {
            for (int reverseIter = (numberPieces.length - 1); reverseIter >= 0; reverseIter--) {
                try {
                    int numWidthForNext = getPatternForNumberAndBuild(player, numberPieces[reverseIter],
                            startingBlockPostionForNumber, blockFace, material, materialOnTop);

                    startingBlockPostionForNumber = calculateCorrectStartingPosition(player, startingBlockPostionForNumber,
                            blockFace, numWidthForNext);
                } catch (Exception ex) {
                    player.sendMessage("[NumbersTool][buildEntireNumber][Cant build char]");
                }
            }
        }
    }


    public static Block calculateCorrectStartingPosition(Player player, Block startingBlockPostionForNumber,
                                                         BlockFace blockFace, int numWidthForNext){
        int yas = startingBlockPostionForNumber.getY();
        int xas = startingBlockPostionForNumber.getX();
        int zas = startingBlockPostionForNumber.getZ();

        //Use blockFace to determine the direction
        switch (blockFace) {
            case NORTH:
                xas = xas - numWidthForNext;
                break;
            case EAST:
                zas = zas - numWidthForNext;
                break;
            case SOUTH:
                xas = xas + numWidthForNext;
                break;
            case WEST:
                zas = zas + numWidthForNext;
                break;
            default:
                break;
        }

        //Position for next number
        return player.getWorld().getBlockAt(xas, yas, zas);
    }


    /**
     * Get the right pattern and build the number
     *
     * @param player
     * @param number
     * @param block
     * @param blockFace
     */
    public int getPatternForNumberAndBuild(Player player, char number, Block block, BlockFace blockFace,
                                           Material material, Material materialOnTop) {
        //Yas + 1 to place on top of the block
        int yas = block.getY() + 1;
        int xas = block.getX();
        int zas = block.getZ();

        if(isDebug) {
         player.sendMessage("Number: " + number);
        }

        switch (number) {
            case '0':
                buildSingleNumber(player, block, numZero, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '1':
                buildSingleNumber(player, block, numOne, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '2':
                buildSingleNumber(player, block, numTwo, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '3':
                buildSingleNumber(player, block, numThree, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '4':
                buildSingleNumber(player, block, numFour, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '5':
                buildSingleNumber(player, block, numFive, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '6':
                buildSingleNumber(player, block, numSix, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '7':
                buildSingleNumber(player, block, numSeven, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '8':
                buildSingleNumber(player, block, numEight, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '9':
                buildSingleNumber(player, block, numNine, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
            case '.':
                buildSingleNumber(player, block, dotChar, xas, yas, zas, blockFace, material, materialOnTop);
                return dotWidth;
            case ',':
                buildSingleNumber(player, block, dotChar, xas, yas, zas, blockFace, material, materialOnTop);
                return dotWidth;
            case ':':
                buildSingleNumber(player, block, doubleDotChar, xas, yas, zas, blockFace, material, materialOnTop);
                return dotWidth;

                //Does a AIR number, so you'l see nothing
            default:
                buildSingleNumber(player, block, numClear, xas, yas, zas, blockFace, material, materialOnTop);
                return numWidth;
        }
    }

    /**
     * Use the pattern to build a given number on the position
     *
     * @param player
     * @param block
     * @param numberPattern
     * @param xas
     * @param yas
     * @param zas
     * @param blockFace
     */
    public void buildSingleNumber(Player player, Block block, List<String> numberPattern, int xas, int yas, int zas,
                                         BlockFace blockFace, Material material, Material materialOnTop) {

        int tmpYas = 0;

        //If material on top is not AIR then use that material as a platform
        for (String pattern: numberPattern) {
            int tmpZas = 0;
            int tmpXas = 0;
            //- tmpYas to keep all next numbers on the same block heigth.
            //Or else every next block will go into the ground further and further.
            int placeY = yas;

            if(isDebug) {
                player.sendMessage("Pattern: " + pattern);
            }

            for (int index = 0; index < pattern.length(); index++) {
                int placeZ = zas;
                int placeX = xas;
                int isNorthSouth = 0;

                switch (blockFace) {
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
                    default:
                        break;
                }

                Block blockAt = player.getWorld().getBlockAt(placeX, placeY, placeZ);

                //Check if it is platform OR block that get material or air
                if (pattern.charAt(index) == 'x') {
                    //If numberPattern size == 6 then it has the platform pattern in it
                    blockAt.setType(material, false);
                } else if(pattern.charAt(index) == 'p') {
                    blockAt.setType(materialOnTop, false);
                }else {
                    blockAt.setType(Material.AIR, false);
                }

                tmpZas = tmpZas + (isNorthSouth == 1 ? 0 : 1);
                tmpXas = tmpXas + (isNorthSouth == 1 ? 1 : 0);
            }
            yas = yas + 1;
        }
    }
}
