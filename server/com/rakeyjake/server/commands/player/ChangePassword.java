package com.rakeyjake.server.commands.player;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;

/**
 * Changes the user password.
 * 
 * @author André
 * 
 */
public class ChangePassword extends Command {

	@Override
	public String command() {
		return "changepassword";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		if (playerCommand.length() > 15) {
			String newPassword = playerCommand.substring(15);
			if(!c.playerPass.equals(newPassword)){
				c.playerPass = newPassword;
				c.sendMessage("Your password is now: " + c.playerPass);
				return;
			}
			c.sendMessage("Your new password is the same as the old one.");
		}
	}

}
