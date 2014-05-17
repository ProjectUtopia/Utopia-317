package com.rakeyjake.server.commands.owner;

import com.rakeyjake.server.Server;
import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;

/**
 * ::developer location - Gives the player current location.
 * ::developer dialogue NPCID - Starts a dialogue with the given npc id.
 * ::developer interface ID - Opens the interface with the given id.
 * ::developer dualg GFXID ANIMID - Dual anim the player, with gfx and anim.
 * ::developer gfx GFXID - Does the given gfx id.
 * ::developer anim ANIMID - Does the given animation id.
 * @author André
 *
 */
public class Developer extends Command {

	@Override
	public String command() {
		return "developer";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		String[] args = playerCommand.split(" ");
		switch (args[1]) {
		case "bank":
			c.getPA().openUpBank();
			break;
		case "reloadshops":
			Server.shopHandler = new com.rakeyjake.server.world.ShopHandler();
			Server.shopHandler.loadShops("shops.cfg");
			break;
		case "location":
			c.sendMessage("x:" + c.absX+", y:"+c.absY+", plane:"+c.heightLevel);
			break;
		case "dialogue":
			int npcType = 1552;
			c.getDH().sendDialogues(Integer.parseInt(args[2]), npcType);
			break;
		case "interface":
			c.getPA().showInterface(Integer.parseInt(args[2]));
			break;
		case "dualg":
			c.gfx0(Integer.parseInt(args[2]));
			c.startAnimation(Integer.parseInt(args[3]));
			break;	
		case "anim":
			c.startAnimation(Integer.parseInt(args[2]));
			c.getPA().requestUpdates();
			break;
		case "gfx":
			c.gfx0(Integer.parseInt(args[2]));
			break;
		}
	}

}
