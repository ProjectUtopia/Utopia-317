package com.rakeyjake.server.commands.moderator;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.util.Misc;

/**
 * ::query USERNAME- Messages the ip from the given username.
 * @author André
 *
 */
public class Query extends Command {

	@Override
	public String command() {
		return "query";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		String[] args = playerCommand.split(" ");
		Client c2 = (Client) Misc.getPlayer(args[1]);
		if(c2 != null) c.sendMessage("IP: "+c2.connectedFrom);
	}

}
