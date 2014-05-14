package com.rakeyjake.server.util;

import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.PlayerHandler;

public class ShutDownHook extends Thread {

	@Override
	public void run() {
		System.out.println("Shutdown thread run.");
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				com.rakeyjake.server.model.players.PlayerSave.saveGame(c);
			}
		}
		System.out.println("Shutting down...");
	}

}