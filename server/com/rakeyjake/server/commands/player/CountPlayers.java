package com.rakeyjake.server.commands.player;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.PlayerHandler;
/**
 * Tell how many players are online to the client.
 * ::players
 * @author André
 *
 */
public class CountPlayers extends Command {

	@Override
	public String command() {
		return "players";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		c.sendMessage("There are currently "
				+ PlayerHandler.getPlayerCount() + " players online.");
	}

}
