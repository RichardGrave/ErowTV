package graver.erowtv.player;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.constants.Messages;
import graver.erowtv.item.ButtonTools;
import graver.erowtv.item.ItemTools;
import graver.erowtv.item.SignTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.text.MessageFormat;

public class PlayerEvents implements Listener, ErowTVConstants {

    //TODO:RG onPlayerLeave -> to clear the memory

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            ErowTV.addPlayerToMemory(event.getPlayer());
            event.setJoinMessage(MessageFormat.format(Messages.PLAYER_LOGIN_WELCOME_MESSAGE, event.getPlayer().getName()));
            event.getPlayer().discoverRecipes(ErowTV.getAllNamespacedKeysRecipes());
        } catch (Exception ex) {
            event.getPlayer().sendMessage("[EventException]:[onPlayerJoin]");
            ex.printStackTrace();
        }
    }

    /**
     * First check if Player interacts with RightClick or LeftClick.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            //Check which click is used and handle the event
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                playerLeftClickEvent(event);

            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                playerRightClickEvent(event);
            }

        } catch (Exception ex) {
            event.getPlayer().sendMessage("[EventException]:[onPlayerInteract]");
            ex.printStackTrace();
        }
    }

    /**
     * Only for left click events
     *
     * @param event
     */
    private void playerLeftClickEvent(PlayerInteractEvent event) {
        if(isDebug) {
            event.getPlayer().sendMessage("playerLeftClickEvent");
        }
        boolean specialItemUseHand = false;
        if (event.getHand() == EquipmentSlot.HAND) {
            // Check which hand is used. Event is fired twice -> for Main Hand and Off Hand
            specialItemUseHand = ItemTools.leftClickItemUse(event.getPlayer(), event.getItem(), event.getClickedBlock());
        }

        if(!specialItemUseHand) {
            //Do event by clicked block material
            switch (event.getClickedBlock().getType()) {
                case SPRUCE_WALL_SIGN:
                    SignTools.leftClickWallSignByPlayer(event.getPlayer(), event.getClickedBlock());
                    break;
                case SPRUCE_SIGN:
                    SignTools.leftClickSignByPlayer(event.getPlayer(), event.getClickedBlock());
                    break;
                case ACACIA_BUTTON:
                    break;
                case STONE_BUTTON:
                    break;
                default:
                    //nothing, yet
            }
        }
    }

    /**
     * Only for right click events
     *
     * @param event
     */
    private void playerRightClickEvent(PlayerInteractEvent event) {
        if(isDebug) {
            event.getPlayer().sendMessage("playerRightClickEvent");
        }
        boolean specialItemUseHand = false;
        if (event.getHand() == EquipmentSlot.HAND) {
            // Check which hand is used. Event is fired twice -> for Main Hand and Off Hand
            specialItemUseHand = ItemTools.rightClickItemUse(event.getPlayer(), event.getItem(), event.getClickedBlock());
        }
        if(!specialItemUseHand) {
            //Do event by clicked block material
            switch (event.getClickedBlock().getType()) {
                case SPRUCE_WALL_SIGN:
                    event.getPlayer().sendMessage("SPRUCE_WALL_SIGN");
                    SignTools.rightClickWallSignByPlayer(event.getPlayer(), event.getClickedBlock());
                    break;
                case SPRUCE_SIGN:
                    event.getPlayer().sendMessage("SPRUCE_SIGN");
                    SignTools.rightClickSignByPlayer(event.getPlayer(), event.getClickedBlock());
                    break;
                case ACACIA_BUTTON:
                    ButtonTools.buttonRightClickedByPlayer(event.getPlayer(), event.getClickedBlock());
                    break;
                case STONE_BUTTON:
                    ButtonTools.buttonRightClickedByPlayer(event.getPlayer(), event.getClickedBlock());
                    break;
                case LEVER:
                    ButtonTools.leverRightClickedByPlayer(event.getPlayer(), event.getClickedBlock());
                    break;
                default:
                    //nothing, yet
            }
        }
    }
}
