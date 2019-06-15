package graver.erowtv.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlayerCommands implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(PlayerTools.getCardinalDirection(sender.getServer().getPlayer(sender.getName()), true));
		return true;
	}

}
