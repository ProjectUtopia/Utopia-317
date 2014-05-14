package com.rakeyjake.server.model.players.packets;

import com.rakeyjake.server.Server;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.PacketType;
import com.rakeyjake.server.util.Misc;

/**
 * Chat
 **/
public class ClanChat implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		String textSent = Misc.longToPlayerName2(c.getInStream().readQWord());
		textSent = textSent.replaceAll("_", " ");
		// c.sendMessage(textSent);
		Server.clanChat.handleClanChat(c, textSent);
	}
}
