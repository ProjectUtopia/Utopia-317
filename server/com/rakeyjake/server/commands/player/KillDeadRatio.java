package com.rakeyjake.server.commands.player;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;

/**
 * Will message you about your kill death ratio
 * @author André
 *	
 */
public class KillDeadRatio extends Command{

	@Override
	public String command() {
		return "kdr";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		if (c.deaths != 0 && c.kills != 0) {
			double kdr = (double) (c.kills / c.deaths);
			c.sendMessage("You have: " + c.kills + " kills and " + c.deaths
					+ " deaths.");
			c.sendMessage("Your KDR is " + c.kills + ":" + c.deaths + "; "
					+ Math.round(kdr)
					+ " kills for every death.");
		} else {
			c.sendMessage("You have not died yet. You have a total of "
					+ c.kills + " kills.");
		}
	}

}
