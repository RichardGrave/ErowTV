package graver.erowtv.games;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.List;

public abstract class Game implements GameInterface {

    private Player player;
    private Block startingBlock;
    private String gameUniqueName;
    private String gameName;
    private boolean stopGameThreads = false;

    //Going to contain all the blocks that are use in the game
    public HashMap<Material, List<Block>> materialBlocks = new HashMap<>();

    public Game(Player player, Block startingBlock, String gameUniqueName, String gameName) {
        this.player = player;
        this.startingBlock = startingBlock;
        this.gameUniqueName = gameUniqueName;
        this.gameName = gameName;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Block getStartingBlock() {
        return this.startingBlock;
    }

    public String getGameUniqueName() {
        return this.gameUniqueName;
    }

    public String getGameName(){
        return this.gameName;
    }

    public void stopGameThreads(){
        this.stopGameThreads = true;
    }

    public boolean isStopGameThreads(){
        return this.stopGameThreads;
    }

    public HashMap<Material, List<Block>> getMaterialBlocks() {
        return materialBlocks;
    }

    public void setMaterialBlocks(HashMap<Material, List<Block>> materialBlocks) {
        this.materialBlocks = materialBlocks;
    }

    //This is what every game needs
    public abstract void welcomeMessage();
    public abstract void handlePlayerAction(PlayerInteractEvent event);
    public abstract void handleMaterialBlocks();

}
