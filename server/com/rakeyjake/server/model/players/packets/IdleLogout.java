package com.rakeyjake.server.model.players.packets;

import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.PacketType;

public class IdleLogout implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		// if (!c.playerName.equalsIgnoreCase("Sanity"))
		// c.logout();
	}
}