package com.rakeyjake.server.commands.admin;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;

/**
 * ::misc copy PLAYERNAME - Copies the whole equipment of the given player name
 * @author André
 *
 */
public class Misc extends Command {

	@Override
	public String command() {
		return "misc";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		String[] args = playerCommand.split(" ");
		switch (args[1]) {
		case "copy":
			int[] arm = new int[14];
			Client c2 = (Client) com.rakeyjake.server.util.Misc.getPlayer(args[3]);
			if (c2 != null) {
				for (int q = 0; q < c2.playerEquipment.length; q++) {
					arm[q] = c2.playerEquipment[q];
					c.playerEquipment[q] = c2.playerEquipment[q];
				}
				for (int q = 0; q < arm.length; q++) {
					c.getItems().setEquipment(arm[q], 1, q);
				}
				c.sendMessage("You have successfully copied " + args[3] + ".");
			}
			break;
		}
	}
}
