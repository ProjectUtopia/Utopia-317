package com.rakeyjake.server.commands.owner;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.Player;
import com.rakeyjake.server.model.players.PlayerHandler;
import com.rakeyjake.server.model.players.PlayerSave;

/**
 * ::server stop - Stops the server
 * @author André
 * 
 */
public class Server extends Command {

	@Override
	public String command() {
		return "server";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		String[] args = playerCommand.split(" ");
		switch (args[1]) {
		case "stop":
			saveData();
			System.exit(0);
			break;
		case "save":
			saveData();
			break;
		}
	}
	
	public void saveData(){
		for (Player p : PlayerHandler.players) {
			if (p != null) PlayerSave.saveGame((Client) p);
		}
	}

}
