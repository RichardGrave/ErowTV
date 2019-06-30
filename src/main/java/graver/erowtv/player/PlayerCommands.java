package graver.erowtv.player;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlayerCommands implements CommandExecutor{

	private String action;

	public PlayerCommands(String action){
		this.action = action;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(action.equalsIgnoreCase(ErowTVConstants.FACING)) {
			sender.sendMessage(PlayerTools.getCardinalDirection(sender.getServer().getPlayer(sender.getName()), true));
		}else if(action.equalsIgnoreCase(ErowTVConstants.DEBUG_MESSAGES)){
			//switch it on or off
			ErowTV.isDebug = !ErowTV.isDebug;
			sender.sendMessage("Debugging is " + (ErowTV.isDebug ? "ON" : "OFF"));
		}
		return true;
	}

}
