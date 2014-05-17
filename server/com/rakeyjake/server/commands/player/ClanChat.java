package com.rakeyjake.server.commands.player;
import com.rakeyjake.server.Server;
import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;

/**
 * Speaks using the clan chat, if in one.
 * @author André
 *
 */
public class ClanChat extends Command {

	@Override
	public String command() {
		return "/";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		if (playerCommand.length() > 0 && c.clanId >= 0) {
			System.out.println(playerCommand);
			playerCommand = playerCommand.substring(1);
			Server.clanChat.playerMessageToClan(c.playerId, playerCommand,
					c.clanId);
		} else {
			if (c.clanId != -1)
				c.clanId = -1;
			c.sendMessage("You are not in a clan.");
		}
	}

}
