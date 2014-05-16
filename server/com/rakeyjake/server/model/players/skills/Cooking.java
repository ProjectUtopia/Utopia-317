package com.rakeyjake.server.model.players.skills;

import com.rakeyjake.server.Config;
import com.rakeyjake.server.event.Event;
import com.rakeyjake.server.event.EventContainer;
import com.rakeyjake.server.event.EventManager;
import com.rakeyjake.server.model.players.Client;

public class Cooking {

	Client c;
	CookingEnum cook;

	public Cooking(Client c) {
		this.c = c;
	}

	public void itemOnObject(int id) {
		cook = CookingEnum.checkIngredients(id);
		cookFish(cook.rawId, 1);
	}

	private void sendStatementTwo(String s) {
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}

	public void cookFish(final int id, final int slot) {
		if (c.getItems().playerHasItem(id, 1)) {
			if (c.playerLevel[c.playerCooking] >= cook.levelReq) {
				c.isCooking = true;
				c.startAnimation(883);
				c.getItems().deleteItem(id, c.getItems().getItemSlot(id), 1);
				c.getItems().addItem(cook.cookedId, 1);
				c.getPA().addSkillXP(cook.xpGained * Config.COOKING_EXPERIENCE,
						c.playerCooking);
				c.sendMessage("You successfully cook a" + cook.name + ".");
			} else {
				c.isCooking = false;
				sendStatementTwo("You need a cooking level of " + cook.levelReq
						+ " to cook this fish.");
				return;
			}
		} else {
			c.isCooking = false;
			return;
		}
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer container) {
				if (c.isCooking) {
					if (c.getItems().playerHasItem(id, 1)) {
						c.startAnimation(883);
						c.getItems().deleteItem(id,
								c.getItems().getItemSlot(id), 1);
						c.getItems().addItem(cook.cookedId, 1);
						c.getPA().addSkillXP(
								cook.xpGained * Config.COOKING_EXPERIENCE,
								c.playerCooking);
						c.sendMessage("You successfully cook a " + cook.name
								+ ".");
					} else {
						container.stop();
						c.isCooking = false;
						return;
					}
				} else {
					container.stop();
					c.isCooking = false;
					return;
				}
			}

		}, 1800);
	}
}

enum CookingEnum {
	SHRIMP("Shrimp", 317, 315, 7954, 30, 50), TROUT("Trout", 335, 333, 323,
			100, 20), SALMON("Salmon", 331, 329, 323, 150, 30), TUNA("Tuna",
			359, 361, 363, 175, 35), MONKFISH("Monkfish", 7944, 7946, 7948,
			300, 62), SHARK("Shark", 383, 385, 387, 500, 80), MANTA(
			"Manta Ray", 389, 391, 393, 700, 91);

	int rawId, cookedId, burntId, xpGained, levelReq;
	String name;

	static int[] rawIds = { 317, 335, 331, 359, 7944, 383, 389 };
	static CookingEnum[] enumArray = { SHRIMP, TROUT, SALMON, TUNA, MONKFISH,
			SHARK, MANTA };

	public static CookingEnum checkIngredients(int id) {
		for (int i = 0; i < rawIds.length; i++) {
			if (id == rawIds[i])
				return enumArray[i];
		}
		return null;
	}

	private CookingEnum(String name, int rawId, int cookedId, int burntId,
			int xpGained, int levelReq) {
		this.name = name;
		this.rawId = rawId;
		this.levelReq = levelReq;
		this.cookedId = cookedId;
		this.burntId = burntId;
		this.xpGained = xpGained;
		this.levelReq = levelReq;
	}

	private CookingEnum(int rawId) {
		this.rawId = rawId;
	};
}
