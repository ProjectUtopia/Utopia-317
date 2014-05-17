package com.rakeyjake.server.commands.player;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.Player;
import com.rakeyjake.server.model.players.PlayerHandler;
import com.rakeyjake.server.util.Misc;

/**
 * Yells the given message to everyone in the server
 * ::yell MESSAGE
 * @author André
 *
 */
public class Yell extends Command{

	@Override
	public String command() {
		return "yell";
	}


	@Override
	public void execute(Client c, String playerCommand) {
		String text = playerCommand.substring(5);
		String[] bad = { "chalreq", "duelreq", "tradereq", ". com", "com",
				"org", "net", "biz", ". net", ". org", ". biz", ". no-ip",
				"- ip", ".no-ip.biz", "no-ip.org", "servegame", ".com",
				".net", ".org", "no-ip", "****", "is gay", "****", "crap",
				"rubbish", ". com", ". serve", ". no-ip", ". net", ". biz" };
		for (int i = 0; i < bad.length; i++) {
			if (text.indexOf(bad[i]) >= 0) {
				return;
			}
		}
		for(Player p : PlayerHandler.players){
			Client c2 = (Client) p;
			if(c2 != null){
				if (c.playerRights != 0) {
					c2.sendMessage(""+Command.Type.values()[c.playerRights]+"] "
							+ Misc.optimizeText(c.playerName) + ": "
							+ Misc.optimizeText(playerCommand.substring(5))
							+ "");
				}else{
					c.sendMessage(Misc
							.optimizeText("@red@Please consider donating to use this feature"));
				}
			}
		}
	}

}
