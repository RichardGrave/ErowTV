package graver.erowtv.games;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.item.SignTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This Class if for handling Games
 */
public class GameHandler implements ErowTVConstants {

    private static HashMap<Material, List<Block>> gameMaterialBlocks = new HashMap<>();

    //Keep track of what game a Player plays. Can only play one game at a time.
    private static Map<String, Game> playersGame = new HashMap<>();
    //Keep track if the game is already being played by a other player. (Checks sign location)
    private static Map<String, String> gameWatcher = new HashMap<>();

    /**
     * Create a game for the player
     *
     * @param player
     * @param sign  contains the real name of the game
     * @param block locations will be used for unique game name
     */
    public static void createGameForPlayer(Player player, Sign sign, Block block) {
        String gameName = sign.getLine(SPECIAL_SIGN_PARAMETER_1);

        if (ErowTV.isDebug) {
            player.sendMessage(ChatColor.DARK_AQUA+"GameHandler-Game="+ gameName);
        }

        //Only proceed if the game exists
        if (!gameName.isEmpty() && gameExists(gameName)) {
            if (ErowTV.isDebug) {
                player.sendMessage(ChatColor.DARK_AQUA+"GameExists");
            }

            //unique name to track games being played
            String gameUniqueName = SignTools.createMemoryName(player, block, ErowTVConstants.MEMORY_GAME_SIGN_POSITION);

            if (ErowTV.isDebug) {
                player.sendMessage(ChatColor.DARK_AQUA+"UniqueName="+gameUniqueName);
            }

            //If game (sign location) does not exist yet. Meaning that the game is not played by another player.
            //And this player is not playing a other game.
            if (!gameWatcher.containsKey(gameUniqueName) && !playersGame.containsKey(player.getUniqueId().toString())) {

                if (ErowTV.isDebug) {
                    player.sendMessage(ChatColor.DARK_AQUA+"Player+GameWatcher is Empty");
                }

                //Connect player to the game
                playersGame.put(player.getUniqueId().toString(), createNewGame(player, gameName, gameUniqueName, block));
                //track game so it can only be started once.
                gameWatcher.put(gameUniqueName, player.getUniqueId().toString());

                if (ErowTV.isDebug) {
                    player.sendMessage(ChatColor.DARK_AQUA+"Player:[" + player.getUniqueId().toString() + "] plays " + gameName);
                }
            }else{
                //If the player that plays the game clicks the sign a second time then the game will stop
                if(gameWatcher.containsKey(gameUniqueName) &&
                        gameWatcher.get(gameUniqueName).equalsIgnoreCase(player.getUniqueId().toString())){
                    stopGameForPlayer(player);
                }
            }
        }
    }

    /**
     * Stops the game for the player.
     * Either by a game that ends or if player pressed the game sign a second time.
     * Removes game from gameWatcher so other players can play the game now
     *
     * @param player
     */
    public static void stopGameForPlayer(Player player) {
        //Get the game to find out what the gameUniqueName is
        Game game = playersGame.get(player.getUniqueId().toString());
        //Stop any threads the game has running
        game.stopGameThreads();

        //remove from gameWatcher. The game is free and can be used again
        gameWatcher.remove(game.getGameUniqueName());
        //remove from playersGame
        playersGame.remove(player.getUniqueId().toString());
        removeAllGameBlocks(game.getMaterialBlocks());

        player.sendMessage(ChatColor.RED+"Game '"+game.getGameName()+"' has stopped");
    }

    /**
     * Stops the game for the player.
     * Either by a game that ends or if player pressed the game sign a second time.
     *
     * @param player
     * @param gameName  contains the real name of the game
     * @param gameUniqueName is used to track if the game is already running so no other player can play it.
     * @param block locations will be used for unique game name
     * @return Game that is going to be played
     */
    private static Game createNewGame(Player player, String gameName, String gameUniqueName, Block block) {
        //More games to come
        switch (gameName) {
            case GAME_DOUBLE_OR_NOTHING:
                return new DoubleOrNothing(player, block, gameUniqueName, gameName);
        }

        return null;
    }

    /**
     * This is only a list of Games that can be played. If it's not on the list, then it's not a game
     *
     * @param game name to check if it exists
     * @return true if game exists
     */
    private static boolean gameExists(String game) {
        switch (game) {
            case GAME_DOUBLE_OR_NOTHING:
                return true;

                //more games
            default:
                return false;
        }
    }

    /**
     * All the GameBlocks from a game
     * This can be helpful when we need to stop blockBreaking
     *
     * @param gameMaterialBlocks contains all the material blocks from a game
     */
    public static void addAllGameBlocks(HashMap<Material, List<Block>> gameMaterialBlocks){
        for(Material material: gameMaterialBlocks.keySet()){
            addGameBlocks(material, gameMaterialBlocks.get(material));
        }
    }

    /**
     * GameBlocks that need to be tracked can be added here.
     * For exmaple REDSTONE_LAMP so Events can use this list to do something with the blocks.
     *
     * @param material
     * @param gameBlocks list with specific material blocks
     */
    public static void addGameBlocks(Material material, List<Block> gameBlocks){
        //Check if it already exists
        if(gameMaterialBlocks.containsKey(material)){
            //if it does exist, then add block to list in materials
            gameMaterialBlocks.get(material).addAll(gameBlocks);
        }else{
            //If it does not exist, create new new List to store the blocks
            gameMaterialBlocks.put(material, gameBlocks);
        }
    }

    /**
     * All the game blocks from a game that need to be removed from tracking
     *
     * @param oldGameMaterialBlocks contains all the material blocks from a game
     */
    public static void removeAllGameBlocks(HashMap<Material, List<Block>> oldGameMaterialBlocks){
        for(Material material: oldGameMaterialBlocks.keySet()){
            removeGameBlocks(material, oldGameMaterialBlocks.get(material));
        }
    }

    /**
     * Remove unused gameblocks from the list so they aren't tracked anymore.
     * Events can't use them anymore. This can be done if a game ends.
     *
     * @param material
     * @param oldGameBlocks  contains the real name of the game
     */
    public static void removeGameBlocks(Material material, List<Block> oldGameBlocks){
        //Check if it exists
        if(gameMaterialBlocks.containsKey(material)){
            //if it does exist, then remove blocks from the list in materials
            gameMaterialBlocks.get(material).removeAll(oldGameBlocks);
        }
    }

    /**
     * Get all the blocks from the list that are the same as the given material
     *
     * @param material
     * @return List with blocks
     */
    public static List<Block> getGameBlocks(Material material){
        //Get all from all games. Good for handling Redstone Lights
        if(gameMaterialBlocks.containsKey(material)) {
            return gameMaterialBlocks.get(material);
        }
        //Just return empty list. Dont want to check for NULL
        return new ArrayList<>();
    }

    /**
     * Checks if the player is playing a game and then handles the
     * game events like hitting a game button.
     *
     * @param event this is a PlayerInteractEvent
     */
    public static void handlePlayerGameEvents(PlayerInteractEvent event){
        //First check if the player is playing a game.
        if(playersGame.containsKey(event.getPlayer().getUniqueId().toString())){
            //If so, then pass through the event and do more handling there
            playersGame.get(event.getPlayer().getUniqueId().toString()).handlePlayerAction(event);
        }
    }

    public static HashMap<Material, List<Block>> getGameMaterialBlocks(){
        return gameMaterialBlocks;
    }

    public static boolean isAGameRunning(){
        //If not empty then a game is running
        return !gameWatcher.isEmpty();
    }
}
