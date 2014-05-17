package com.rakeyjake.server.commands.player;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;

/**
 * Resets your kill death ratio
 * @author André
 *
 */
public class ResetKillDeathRatio extends Command{

	@Override
	public String command() {
		return "resetkdr";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		if (c.deaths != 0 && c.kills != 0) {
			c.deaths = 0;
			c.kills = 0;
			c.sendMessage("KDR successfully reset.");
		} else {
			c.sendMessage("Your KDR doesn't need to be reset.");
		}
	}

}
