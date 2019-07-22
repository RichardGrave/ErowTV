package graver.erowtv.games;

import org.bukkit.event.player.PlayerInteractEvent;

public interface GameInterface  {

    public void handleMaterialBlocks();
    public void handlePlayerAction(PlayerInteractEvent event);
    public void welcomeMessage();
}
