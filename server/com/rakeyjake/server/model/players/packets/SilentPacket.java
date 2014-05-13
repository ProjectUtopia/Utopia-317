package com.rakeyjake.server.model.players.packets;

import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.PacketType;

/**
 * Slient Packet
 **/
public class SilentPacket implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {

	}
}
