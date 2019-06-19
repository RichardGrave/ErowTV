package graver.erowtv.player;

import graver.erowtv.constants.Messages;
import graver.erowtv.item.ButtonTools;
import graver.erowtv.item.ItemTools;
import graver.erowtv.item.SignTools;
import graver.erowtv.main.ErowTV;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.text.MessageFormat;

public class PlayerEvents implements Listener {

	//TODO:RG onPlayerLeave -> to clear the memory
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		try {
			ErowTV.addPlayerToMemory(event.getPlayer());
			event.setJoinMessage(MessageFormat.format(Messages.PLAYER_LOGIN_WELCOME_MESSAGE, event.getPlayer().getName()));
		} catch (Exception ex) {
			event.getPlayer().sendMessage("[EventException]:[onPlayerJoin]");
			ex.printStackTrace();
		}
	}

	/**
	 * First check if Player interacts with a WallSign or a Button.
	 * If not, then check for RightClick and LeftClick with MainHand.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
//		event.getPlayer().sendMessage(EmeraldValley.pluginFolder);
		try {
			//Handle sign clicks
			//!!! Use left-click then you dont have to switch to an empty hand. Right-click with same block replaces the block
			if(event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.SPRUCE_WALL_SIGN) {
				SignTools.leftClickWallSignByPlayer(event.getPlayer(), event.getClickedBlock());

			}else if(event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.SPRUCE_SIGN) {
				SignTools.leftClickSignByPlayer(event.getPlayer(), event.getClickedBlock());

			//Handle button clicks
			//!! Doesn't do anything yet
			}else if(event.getAction() == Action.RIGHT_CLICK_BLOCK &&
					(event.getClickedBlock().getType() == Material.ACACIA_BUTTON || event.getClickedBlock().getType() == Material.STONE_BUTTON)) {
				ButtonTools.buttonClickedByPlayer(event.getPlayer(), event.getClickedBlock());

			//Handle right clicks
			}else if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getHand() == EquipmentSlot.HAND)) {
				// Check which hand is used. Event is fired twice -> for Main Hand and Off Hand
				ItemTools.rightClickItemUse(event.getPlayer(), event.getItem(), event.getClickedBlock());
				
			//Handle left clicks
			}else if ((event.getAction() == Action.LEFT_CLICK_BLOCK) && (event.getHand() == EquipmentSlot.HAND)) {
				// Check which hand is used. Event is fired twice -> for Main Hand and Off Hand
				ItemTools.leftClickItemUse(event.getPlayer(), event.getItem(), event.getClickedBlock());
			}
		} catch (Exception ex) {
			event.getPlayer().sendMessage("[EventException]:[onPlayerInteract]");
			ex.printStackTrace();
		}
	}
}
