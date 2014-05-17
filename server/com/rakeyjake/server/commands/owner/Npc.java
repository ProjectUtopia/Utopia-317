package com.rakeyjake.server.commands.owner;

import com.rakeyjake.server.Server;
import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.npcs.NPC;
import com.rakeyjake.server.model.npcs.NPCHandler;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.Player;
import com.rakeyjake.server.model.players.PlayerHandler;
import com.rakeyjake.server.util.Misc;

/**
 * ::npc reset - Kills all npc's and then respawn them after 30 seconds
 * 
 * @author André
 * 
 */
public class Npc extends Command {

	@Override
	public String command() {
		return "npc";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		String[] args = playerCommand.split(" ");
		switch (args[1]) {
		case "reset":
			for (NPC npc : NPCHandler.npcs) {
				if (npc != null) {
					npc.isDead = true;
					npc.actionTimer = 0;
				}
			}
			for (Player p : PlayerHandler.players) {
				if (p != null) {
					Client c2 = (Client) p;
					c2.sendMessage("[Server] NPCs reset by "
							+ Misc.optimizeText(c.playerName) + ".");
				}
			}
			break;
		case "spawn":
			Server.npcHandler.spawnNpc(c, Integer.parseInt(args[2]), c.absX,
					c.absY, c.heightLevel, 0, 120, 7, 70, 70, false, false);
			c.sendMessage("You spawn a Npc.");
			break;

		}
	}
}
