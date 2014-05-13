package com.rakeyjake.server.model.players.packets;

import com.rakeyjake.server.model.items.UseItem;
import com.rakeyjake.server.model.npcs.NPCHandler;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.PacketType;

public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		int i = c.getInStream().readSignedWordA();
		int slot = c.getInStream().readSignedWordBigEndian();
		int npcId = NPCHandler.npcs[i].npcType;
		if (!c.getItems().playerHasItem(itemId, 1, slot)) {
			return;
		}

		UseItem.ItemonNpc(c, itemId, npcId, slot);
	}
}
