package com.rakeyjake.server.commands.admin;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.util.Misc;

/**
 * ::teleport TRAVELERNAME player LOCATIONNAME
 * ::teleport TRAVELERNAME location X Y (optional)PLANE
 * @author André
 *
 */
public class Teleport extends Command {

	@Override
	public String command() {
		return "teleport";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		String[] args = playerCommand.split(" ");
		Client t = null;
		switch(args[2]){
		case "player":
			t = (Client) Misc.getPlayer(args[1]);
			Client l = (Client) Misc.getPlayer(args[3]);
			if(t != null && l != null){
				t.getPA().movePlayer(l.absX, l.absY, l.heightLevel);
			}
			break;
		case "location":
			t = (Client) Misc.getPlayer(args[1]);
			if(t != null){
				t.getPA().movePlayer(Integer.parseInt(args[3]), 
						Integer.parseInt(args[4]),
						args.length > 4 ? Integer.parseInt(args[5]) : t.heightLevel);
			}
			break;
		}
	}

}
