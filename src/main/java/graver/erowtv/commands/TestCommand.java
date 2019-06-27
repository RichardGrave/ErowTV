package graver.erowtv.commands;

import graver.erowtv.constants.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor{

	//Like the name says, just for test.

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(Messages.EROWTV_TEST_COMMAND_MESSAGE);
		return true;
	}
}
