package com.rakeyjake.server.commands.owner;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.Player;
import com.rakeyjake.server.model.players.PlayerHandler;
import com.rakeyjake.server.util.Misc;

/**
 * rank promote rank - Promotes a player to a given rank rank demote rank -
 * Demotes a player to a given rank
 * 
 * @author André
 * 
 */
public class Rank extends Command {

	@Override
	public String command() {
		return "rank";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		String[] args = playerCommand.split(" ");

		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}

		Client c2 = (Client) Misc.getPlayer(args[2]);

		if (c2 != null) {
			if ((c2.playerRights < c.playerRights
					&& Command.Type.valueOf(args[3].toUpperCase()).ordinal() < c.playerRights) || c.playerRights == 4) {

				c2.logout();
				c2.playerRights = Command.Type.valueOf(args[3].toUpperCase())
						.ordinal();
			}
		}
	}
}
