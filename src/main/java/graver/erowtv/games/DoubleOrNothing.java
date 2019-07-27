package graver.erowtv.games;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.BlockTools;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.ExperimentalPasteBlockTool;
import graver.erowtv.tools.YmlFileTool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class DoubleOrNothing extends Game implements ErowTVConstants {

    private final String GAME_NAME = "DoubleOrNothing";
    private final String GAME_FILE = "games/" + GAME_NAME + ".yml";

    private final int NUMBER_OF_TOTAL_BUTTONS = 21;
    private final int NUMBER_OF_BAD_BUTTONS = 2;
    private final int POINTS_NEEDED_TO_WIN = 7;

    private List<ItemStack> itemsFromChest = new ArrayList<>();
    private List<Block> redstoneLamps;
    private List<Block> stoneButtons;
    private Block betChest;
    private Block winningsDispenser;

    private List<Block> badButtons = new ArrayList<>();
    private BlockFace blockFaceSign;

    private int points = 0;
    private boolean putItemsInChest = false;

    /**
     * Paste the blocks with help of calculated positions from BlockTools.getBlockDirections(fromBlock, toBlock, dataSign)
     *
     * @param player
     * @param startingBlock should be the WallSign
     * @param gameUniqueName unique gameName
     * @param gameName to get current facing direction
     * @return HashMap with all the asked materials their locations to use in Games
     */
    public DoubleOrNothing(Player player, Block startingBlock, String gameUniqueName, String gameName) {
        super(player, startingBlock, gameUniqueName, gameName);

        welcomeMessage();
        createDoubleOrNothing();
        createRandomButtons();
        //Every 2 seconds
        new GameLights().runTaskTimer(ErowTV.javaPluginErowTV, TIME_SECOND, TIME_SECOND);
    }

    public void welcomeMessage() {
        getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Welcome to Double or Nothing");
        getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Place one or more items in the chest before you start");
        getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "If you hit 7 correct buttons then your items will be doubled");
        getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "If you hit a wrong button then you'll lose the items");
    }

    /**
     * Creating the game by reading the YML file and pasting the blocks.
     * All game blocks are collected into a HashMap that can be used for Events and other blocks handling.
     */
    private void createDoubleOrNothing() {
        try {
            File gameFile;

            if ((gameFile = YmlFileTool.doesGameFileExist(getPlayer(), GAME_NAME, GAME_FILE)) != null) {

                List<Integer> signPosition = Arrays.asList(null, getStartingBlock().getX(),
                        getStartingBlock().getY(), getStartingBlock().getZ());

                //First check if it's a WallSign or normalSign
                if (getStartingBlock().getState().getBlockData() instanceof org.bukkit.block.data.type.WallSign) {
                    blockFaceSign = ((org.bukkit.block.data.type.WallSign) getStartingBlock().getState().
                            getBlockData()).getFacing().getOppositeFace();
                } else {
                    blockFaceSign = ((org.bukkit.block.data.type.Sign) getStartingBlock().getState().
                            getBlockData()).getRotation().getOppositeFace();
                }

                //Get directions for pasting the blocks
                int[] directions = BlockTools.getBlockDirections(signPosition, getStartingBlock(), null);

                ExperimentalPasteBlockTool.pasteChunkPositions(getPlayer(), gameFile, directions,
                        blockFaceSign, materialBlocks);

                handleMaterialBlocks();
            } else {
                getPlayer().sendMessage("Game '" + GAME_NAME + "' cannot be created");
            }
        } catch (Exception ex) {
            getPlayer().sendMessage(ChatColor.DARK_RED+"[EventException]:[createDoubleOrNothing]");
            ex.printStackTrace();
        }
    }

    /**
     * Get all the blocks from saved materialsBlocks HashMap that are needed to interact with in the game.
     */
    public void handleMaterialBlocks() {
        if (ErowTV.isDebug) {
            getPlayer().sendMessage(ChatColor.DARK_AQUA+"MaterialsBlocks size= " + materialBlocks.size());
        }

        //First give ALL the blocks to the GameHandler for stopping blocks with BlockBreaking events.
        GameHandler.addAllGameBlocks(materialBlocks);

        //Get all redstone blocks for the GameLights
        redstoneLamps = materialBlocks.get(Material.REDSTONE_LAMP);
        //Get all the buttons that need to be watched when playing the game
        stoneButtons = materialBlocks.get(Material.STONE_BUTTON);

        //There should always be one chest in this game
        betChest = materialBlocks.get(Material.CHEST).get(0);
        //There should always be one dispenser in this game
        winningsDispenser = materialBlocks.get(Material.DISPENSER).get(0);

        //Clear dispenser to make sure it's empty
        org.bukkit.block.Dispenser dispenser = (org.bukkit.block.Dispenser) winningsDispenser.getState();
        Inventory dispenserInventory = dispenser.getInventory();
        dispenserInventory.clear();

        if (ErowTV.isDebug) {
            getPlayer().sendMessage(ChatColor.DARK_AQUA+"redstoneLamps size= " + redstoneLamps.size());
            getPlayer().sendMessage(ChatColor.DARK_AQUA+"stoneButtons size= " + stoneButtons.size());
        }
    }

    /**
     * Here we check which button is clicked and if that button is the correct or wrong one.
     * If correct add points till we win.
     * Else stop the game and the player loses.
     *
     * @param event is from the players interaction. Like hitting a button.
     */
    public void handlePlayerAction(PlayerInteractEvent event) {
        //Check if the clicked button is a stone_button and that the button is in the button list.
        if (event.getClickedBlock().getType() == Material.STONE_BUTTON) {

            if (!putItemsInChest) {
                //We need item bets.
                getItemsFromChest();

                //Check again if 'getItemsFromChest()' has found items. Else stop and return
                if (!putItemsInChest) {
                    return;
                }
            }

            //Buttons in this list will let you lose the game
            if (badButtons.contains(event.getClickedBlock())) {
                //No need to remove the button from the list, it's game over anyway
                //Give the block behind the button a red color
                setGoodOrWrongBlock(event.getClickedBlock(), Material.RED_WOOL);
                //Play bad sound
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);

                stopGameThreads();
                //Remove the lamps. Dont track them anymore
                GameHandler.removeAllGameBlocks(materialBlocks);
                //Inform player and stop the game
                getPlayer().sendMessage(ChatColor.RED + "Sorry, you lost and also all your items :(");
                GameHandler.stopGameForPlayer(getPlayer());

                //Only buttons in this list can give points
            } else if (stoneButtons.contains(event.getClickedBlock())) {
                //remove button from list so it can't be clicked for more points
                stoneButtons.remove(event.getClickedBlock());
                //Give the block behind the button a green color
                setGoodOrWrongBlock(event.getClickedBlock(), Material.GREEN_WOOL);

                points++;
                if (points == POINTS_NEEDED_TO_WIN) {
                    playerWins();
                } else {
                    getPlayer().sendMessage(ChatColor.GREEN + "You have " + points + " correct");
                }
            }
        }
    }

    /**
     * Checks if the player has put something in the chest that he wants bet with.
     * If the chest is empty then the game wont start.
     * If player does place items in the chest they will be put into a list for later use (if players wins)
     * Then when the game starts the chest will be cleared, so the player cannot grab back his items while
     * trying to win.
     */
    private void getItemsFromChest() {
        org.bukkit.block.Chest chest = (org.bukkit.block.Chest) betChest.getState();
        Inventory chestInventory = chest.getBlockInventory();

        //Get all items from chest
        for (ItemStack items : chestInventory.getContents()) {
            if (items != null) {
                itemsFromChest.add(items);
            }
        }

        if (ErowTV.isDebug) {
            getPlayer().sendMessage(ChatColor.DARK_AQUA+"CHEST size= " + itemsFromChest.size());
        }

        //check if chest is was not empty
        if (!itemsFromChest.isEmpty()) {
            //So we dont have to check this again
            putItemsInChest = true;

            //Clear chest so player can't get items back while playing
            chestInventory.clear();
        } else {
            //No items? then inform player to put something in the chest
            getPlayer().sendMessage(ChatColor.RED + "If you want to play the game then you'll need to put items in the chest");
        }
    }

    /**
     * Player wins, therefor he gets double his items back (that he had put into the chest before the game started)
     *
     */
    private void playerWins() {
        getPlayer().sendMessage(ChatColor.YELLOW + "You have won. Here is your price");

        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 1.0f);

        //Use a Thread to dispense. If we dont do this and you won to much then the game freezes
        //for a couple of seconds because of the for loops.
        new DispensePrice().runTaskTimer(ErowTV.javaPluginErowTV, TIME_SECOND, TIME_HALF_SECOND);
    }

    /**
     * Changes the block behind the button to the given material.
     * Correct button gets Green block material
     * Wrong button gets Red block material
     *
     * @param clickedBlock is the button that's been hit by player
     * @param material for the block behind the button
     */
    private void setGoodOrWrongBlock(Block clickedBlock, Material material) {
        //Change the block color to show if you guessed good or wrong
        org.bukkit.block.data.type.Switch button = (org.bukkit.block.data.type.Switch) clickedBlock.getBlockData();
        Block blockBehindButton = clickedBlock.getRelative(button.getFacing().getOppositeFace());
        blockBehindButton.setType(material);
    }

    /**
     * Gets a couple of random numbers that are used for Bad buttons.
     * So the Bad buttons are always placed an other block every time we start a new game.
     */
    private void createRandomButtons() {
        if (stoneButtons != null && stoneButtons.size() == NUMBER_OF_TOTAL_BUTTONS) {
            Random rn = new Random();

            //Use while loop, because we dont know how often the same random number will come up
            while (badButtons.size() < NUMBER_OF_BAD_BUTTONS) {
                int randomNum = rn.nextInt(NUMBER_OF_TOTAL_BUTTONS);
                if (ErowTV.isDebug) {
                    getPlayer().sendMessage(ChatColor.DARK_AQUA+"Randomnumber=" + randomNum);
                }

                Block randomStoneButton;
                if (!badButtons.contains(randomStoneButton = stoneButtons.get(randomNum))) {
                    badButtons.add(randomStoneButton);
                }
            }
        } else {
            getPlayer().sendMessage(ChatColor.DARK_RED+"Problem: List<stoneButtons> size is not 21");
        }
    }

    private class DispensePrice extends BukkitRunnable {
        @Override
        public void run() {
            try {
                if (itemsFromChest.size() > 0) {
                    org.bukkit.block.Dispenser dispenser = (org.bukkit.block.Dispenser) winningsDispenser.getState();
                    Inventory dispenserInventory = dispenser.getInventory();

                    ItemStack items = itemsFromChest.get(0);
                    //Items X2 because player has won :)
                    dispenserInventory.addItem(items);
                    dispenserInventory.addItem(items);

                    for (int iter = 0; iter < (items.getAmount() * 2); iter++) {
                        dispenser.dispense();
                    }

                    itemsFromChest.remove(items);

                    if (ErowTV.isDebug) {
                        getPlayer().sendMessage(ChatColor.DARK_AQUA+
                                "DispensePrice itemsFromChest size="+itemsFromChest.size());
                    }
                } else {
                    if (ErowTV.isDebug) {
                        getPlayer().sendMessage(ChatColor.DARK_AQUA+"DispensePrice has ended");
                    }

                    this.cancel();

                    //Stop the game after winning
                    GameHandler.stopGameForPlayer(getPlayer());
                }

            } catch (Exception ex) {
                getPlayer().sendMessage(ChatColor.DARK_RED + "[DispensePrice-run][Exception][" + ex.getMessage() + "]");
            }
        }
    }

    //
    /**
     * Just a simple GameLight mechanic to make the game more fun to see.
     */
    private class GameLights extends BukkitRunnable {
        private final int PATTERN_ONE = 1;
        private final int PATTERN_TWO = 2;
        private final int PATTERN_THREE = 3;
        private final int SWITCH_LIGHTS_AFTER_SECONDS = 5; //seconds

        private boolean isXaxis = (blockFaceSign == BlockFace.NORTH || blockFaceSign == BlockFace.SOUTH ? true : false);
        private boolean switchTheLights = false;
        private int switchCounter = 1;
        private int switchPattern = 1;

        @Override
        public void run() {
            try {
                //Start Thread only if redstoneLamps is not NULL.
                if (redstoneLamps != null) {
                    if (switchPattern == PATTERN_THREE) {
                        doSwitchPatternThree();
                    } else if (switchPattern == PATTERN_TWO) {
                        doSwitchPatternTwo();
                    } else {
                        doSwitchPatternOne();
                    }

                    switchCounter++;
                    switchTheLights = (switchTheLights ? false : true);

                    if (switchCounter > SWITCH_LIGHTS_AFTER_SECONDS) {
                        switchCounter = 0;
                        Random rn = new Random();
                        //Just a number, doesnt really matter
                        int randomNum = rn.nextInt(100);

                        if (randomNum % PATTERN_THREE == 0) {
                            //If its not 3 yet, then 3 else 2
                            switchPattern = switchPattern != PATTERN_THREE ? PATTERN_THREE : PATTERN_TWO;
                        } else if (randomNum % PATTERN_THREE == 0) {
                            //If its not 2 yet, then 2 else 1
                            switchPattern = switchPattern != PATTERN_TWO ? PATTERN_TWO : PATTERN_ONE;
                        } else {
                            //If its not 1 yet, then 1 else 3
                            switchPattern = switchPattern != PATTERN_ONE ? PATTERN_ONE : PATTERN_THREE;
                        }
                    }
                    if (isStopGameThreads()) {
                        turnOffAllLights();
                        this.cancel();
                    }
                }
            } catch (Exception ex) {
                getPlayer().sendMessage(ChatColor.DARK_RED+"[GameLights-run][Exception][" + ex.getMessage() + "]");
            }
        }

        //If the game ends or stops by player then turn off all the lights.
        private void turnOffAllLights() {
            for (Block lamp : redstoneLamps) {
                String lampLit = lamp.getBlockData().getAsString().
                        replace("lit=true", "lit=false");
                lamp.setBlockData(Bukkit.getServer().createBlockData(lampLit));
            }
        }

        private void doSwitchPatternOne() {
            for (Block lamp : redstoneLamps) {
                if (lamp.getY() % 2 == 0) {
                    setLight(lamp, true);
                } else {
                    setLight(lamp, false);
                }

            }
        }

        private void doSwitchPatternTwo() {
            for (Block lamp : redstoneLamps) {
                if (isXaxis ? (lamp.getX() % 2 == 0) : (lamp.getZ() % 2 == 0)) {
                    setLight(lamp, true);
                } else {
                    setLight(lamp, false);
                }
            }
        }

        private void doSwitchPatternThree() {
            for (Block lamp : redstoneLamps) {
                if (lamp.getY() % 2 == 0) {
                    if (isXaxis ? (lamp.getX() % 2 == 0) : (lamp.getZ() % 2 == 0)) {
                        setLight(lamp, true);
                    } else {
                        setLight(lamp, false);
                    }

                } else {
                    //reverse
                    if (isXaxis ? (lamp.getX() % 2 == 0) : (lamp.getZ() % 2 == 0)) {
                        setLight(lamp, false);
                    } else {
                        setLight(lamp, true);
                    }
                }
            }
        }

        //Set one lamp to a specific condition
        private void setLight(Block lamp, boolean correctCondition) {
            if (correctCondition) {
                String lampLit = lamp.getBlockData().getAsString().
                        replace((switchTheLights ? "lit=false" : "lit=true"),
                                (switchTheLights ? "lit=true" : "lit=false"));
                lamp.setBlockData(Bukkit.getServer().createBlockData(lampLit));
            } else {
                String lampLit = lamp.getBlockData().getAsString().
                        replace((switchTheLights ? "lit=true" : "lit=false"),
                                (switchTheLights ? "lit=false" : "lit=true"));
                lamp.setBlockData(Bukkit.getServer().createBlockData(lampLit));
            }
        }
    }
}
