package graver.erowtv.tools;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public final class CharacterTool implements ErowTVConstants {

    private CharacterTool() {
    }

    //TODO:RG create this for letter, just like with the numbers

    private static int numHeight = 5;
    private static int numWidth = 5;
    //Needs to be 2 because every other char has already got a starting AIR block.
    //Else you will get one AIR block on one side to many
    private static int dotWidth = 2;

    // First of every String is 0 (AIR) so numbers can be place next to each other
    // with space between them
    private static String[] numZero = new String[]{"0xxxx", "0x00x", "0x00x", "0x00x", "0xxxx"};
    private static String[] numOne = new String[]{"00xxx", "000x0", "000x0", "00xx0", "000x0"};
    private static String[] numTwo = new String[]{"0xxxx", "0x000", "0xxxx", "0000x", "0xxxx"};
    private static String[] numThree = new String[]{"0xxxx", "0000x", "00xxx", "0000x", "0xxxx"};
    private static String[] numFour = new String[]{"0000x", "0000x", "0xxxx", "0x00x", "0x00x"};
    private static String[] numFive = new String[]{"0xxxx", "0000x", "0xxxx", "0x000", "0xxxx"};
    private static String[] numSix = new String[]{"0xxxx", "0x00x", "0xxxx", "0x000", "0xxxx"};
    private static String[] numSeven = new String[]{"000x0", "000x0", "00xxx", "000x0", "0xxx0"};
    private static String[] numEight = new String[]{"0xxxx", "0x00x", "0xxxx", "0x00x", "0xxxx"};
    private static String[] numNine = new String[]{"0xxxx", "0000x", "0xxxx", "0x00x", "0xxxx"};
    private static String[] numClear = new String[]{"00000", "00000", "00000", "00000", "00000"};
    private static String[] dotChar = new String[]{"0000x", "00000", "00000", "00000", "00000"};
    private static String[] doubleDotChar = new String[]{"00000", "0000x", "00000", "0000x", "00000"};

    /**
     * Build entire number
     *
     * @param player
     * @param bigNumber
     * @param block
     * @param blockFace
     */
    public static void buildEntireNumber(Player player, String bigNumber, Block block, BlockFace blockFace) {
        if(ErowTV.isDebug) {
            player.sendMessage(ChatColor.DARK_AQUA+"BigNumber: " + bigNumber);
        }

        char[] numberPieces = bigNumber.toCharArray();
        Material material = block.getBlockData().getMaterial();

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
                    int numWithForNext = getPatternForNumberAndBuild(player, numberPieces[reverseIter], startingBlockPostionForNumber, blockFace, material);

                    startingBlockPostionForNumber = calculateCorrectStartingPosition(player, startingBlockPostionForNumber, blockFace, numWithForNext);
                } catch (Exception ex) {
                    player.sendMessage("[NumbersTool][buildEntireNumber][Cant build char]");
                }
            }
        }
    }

    public static Block calculateCorrectStartingPosition(Player player, Block startingBlockPostionForNumber, BlockFace blockFace, int numWithForNext){
        int yas = startingBlockPostionForNumber.getY();
        int xas = startingBlockPostionForNumber.getX();
        int zas = startingBlockPostionForNumber.getZ();

        //Use blockFace to determine the direction
        switch (blockFace) {
            case NORTH:
                xas = xas - numWithForNext;
                break;
            case EAST:
                zas = zas - numWithForNext;
                break;
            case SOUTH:
                xas = xas + numWithForNext;
                break;
            case WEST:
                zas = zas + numWithForNext;
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
    public static int getPatternForNumberAndBuild(Player player, char number, Block block, BlockFace blockFace, Material material) {
        //Yas + 1 to place on top of the block
        int yas = block.getY() + 1;
        int xas = block.getX();
        int zas = block.getZ();

        if(ErowTV.isDebug) {
            player.sendMessage(ChatColor.DARK_AQUA+"Number: " + number);
        }

        switch (number) {
            case '0':
                buildSingleNumber(player, block, numZero, xas, yas, zas, blockFace, material);
                return numWidth;
            case '1':
                buildSingleNumber(player, block, numOne, xas, yas, zas, blockFace, material);
                return numWidth;
            case '2':
                buildSingleNumber(player, block, numTwo, xas, yas, zas, blockFace, material);
                return numWidth;
            case '3':
                buildSingleNumber(player, block, numThree, xas, yas, zas, blockFace, material);
                return numWidth;
            case '4':
                buildSingleNumber(player, block, numFour, xas, yas, zas, blockFace, material);
                return numWidth;
            case '5':
                buildSingleNumber(player, block, numFive, xas, yas, zas, blockFace, material);
                return numWidth;
            case '6':
                buildSingleNumber(player, block, numSix, xas, yas, zas, blockFace, material);
                return numWidth;
            case '7':
                buildSingleNumber(player, block, numSeven, xas, yas, zas, blockFace, material);
                return numWidth;
            case '8':
                buildSingleNumber(player, block, numEight, xas, yas, zas, blockFace, material);
                return numWidth;
            case '9':
                buildSingleNumber(player, block, numNine, xas, yas, zas, blockFace, material);
                return numWidth;
            case '.':
                buildSingleNumber(player, block, dotChar, xas, yas, zas, blockFace, material);
                return dotWidth;
            case ':':
                buildSingleNumber(player, block, doubleDotChar, xas, yas, zas, blockFace, material);
                return dotWidth;

                //Does a AIR number, so you'l see nothing
            default:
                buildSingleNumber(player, block, numClear, xas, yas, zas, blockFace, material);
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
    public static void buildSingleNumber(Player player, Block block, String[] numberPattern, int xas, int yas, int zas, BlockFace blockFace, Material material) {
        int tmpYas = 0;

        for (String pattern : numberPattern) {
            int tmpZas = 0;
            int tmpXas = 0;
            int placeY = yas - tmpYas;

            if(ErowTV.isDebug) {
                player.sendMessage(ChatColor.DARK_AQUA+"Pattern: " + pattern);
            }

            for (int iter = 0; iter < pattern.length(); iter++) {
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

                if (pattern.charAt(iter) == 'x') {
                    blockAt.setType(material, false);
                } else {
                    blockAt.setType(Material.AIR, false);
                }

                tmpZas = tmpZas + (isNorthSouth == 1 ? 0 : 1);
                tmpXas = tmpXas + (isNorthSouth == 1 ? 1 : 0);
            }
            yas = yas + 1;
        }
    }
}
