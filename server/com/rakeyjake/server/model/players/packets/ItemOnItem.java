package com.rakeyjake.server.model.players.packets;

/**
 * @author Ryan / Lmctruck30
 */

import com.rakeyjake.server.model.items.UseItem;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.PacketType;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int usedWithSlot = c.getInStream().readUnsignedWord();
		int itemUsedSlot = c.getInStream().readUnsignedWordA();
		int useWith = c.playerItems[usedWithSlot] - 1;
		int itemUsed = c.playerItems[itemUsedSlot] - 1;
		if (!c.getItems().playerHasItem(useWith, 1, usedWithSlot)
				|| !c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot)) {
			return;
		}
		UseItem.ItemonItem(c, itemUsed, useWith);
	}

}
