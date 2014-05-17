package com.rakeyjake.server.commands.player;

import java.math.BigDecimal;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.util.Misc;

/**
 * ::kdr - Will message you about your kill death ratio ::kdr reset - Resets
 * your kill death ratio
 * 
 * @author André
 * 
 */
public class KillDeadRatio extends Command {

	@Override
	public String command() {
		return "kdr";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		String[] args = playerCommand.split(" ");
		switch (args.length) {
		case 1:
			switch (args[1]) {
			case "reset":
				if (c.deaths != 0 && c.kills != 0) {
					c.deaths = 0;
					c.kills = 0;
					c.sendMessage("KDR successfully reset.");
				} else {
					c.sendMessage("Your KDR doesn't need to be reset.");
				}
				break;
			default:
				break;
			}
			break;

		default:
			if (c.deaths != 0 && c.kills != 0) {
				double kdr = (double) (c.kills / c.deaths);
				c.sendMessage("You have: " + c.kills + " kills and " + c.deaths
						+ " deaths.");
				c.sendMessage("Your KDR is " + c.kills + ":" + c.deaths + "; "
						+ Math.round(kdr) + " kills for every death.");
				c.forcedChat("Your KDR is " + c.kills + ":" + c.deaths + "; "
						+ Misc.roundDouble(kdr, 2, BigDecimal.ROUND_HALF_UP)
						+ " kills for every death.");
			} else {
				c.sendMessage("You have not died yet. You have a total of "
						+ c.kills + " kills.");
			}
			break;
		}
	}
}
