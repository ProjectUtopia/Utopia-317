package com.rakeyjake.server.model.players.packets;

import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.PacketType;

/**
 * 
 * Logs players other than admins out after they have been idle for a certain amount of time.
 */
public class IdleLogout implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		 if (!c.playerName.equalsIgnoreCase("Jake") || !c.playerName.equalsIgnoreCase("Pd"))
		 c.logout();
	}
}