package com.rakeyjake.server.model.players.skills;

import java.util.Random;

import com.rakeyjake.server.Config;
import com.rakeyjake.server.event.Event;
import com.rakeyjake.server.event.EventContainer;
import com.rakeyjake.server.event.EventManager;
import com.rakeyjake.server.model.players.Client;

/**
 * All done except for burning food. No food is burnt at the moment. A check
 * needs to be done to see if a) the food can be burnt at all, b) if cooking
 * gloves are worn. <br>
 * <br>
 * Also, a random burn rate needs to be added that will scale depending on your
 * level above the required.
 * 
 * @author Rakeyjakey
 * 
 */
public class Cooking {

	private Client c;
	private CookingEnum cook;

	public Cooking(Client c) {
		this.c = c;
	}

	/**
	 * Called when the item is used on the object. It checks which item has been used and assigns the correct data from the enum.
	 * @param id the id of the item used on the object
	 * @author Rakeyjakey
	 */
	public void itemOnObject(int id) {
		cook = CookingEnum.checkIngredients(id);
		cookFish(cook.rawId, 1);
	}

	/**
	 * Opens a frame with a continue button.
	 * @param s String to display on the frame.
	 * @author Rakeyjakey
	 */
	private void sendStatementTwo(String s) {
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}

	/**
	 * Evaluates the chance of burning by doing a simple calculation based on the level required to no longer burn the item. The closer you are to that, the less you will burn.
	 *
	 * @param cookingLevel the cooking level of the player
	 * @param neverFailLevel the level that the player will no longer burn the fish if not wearing gloves
	 * @param neverFailLevelWithGloves the level that the player will no longer burn the fish if wearing gloves
	 * @param gloves whether the player is wearing cooking gauntlets.
	 * 
	 * @return true if the calculation results in the item needing to be burnt.
	 *
	 * @author Rakeyjakey
	 */
	
	public boolean willBurn(int cookingLevel, int neverFailLevel,
			int neverFailLevelWithGloves, boolean gloves) {
		Random r = new Random();
		double chanceInPercent;

		if (gloves)
			chanceInPercent = ((double) cookingLevel / (double) neverFailLevelWithGloves) * 100;
		else
			chanceInPercent = ((double) cookingLevel / (double) neverFailLevel) * 100;

		return (double) r.nextInt(100) < (double) 100 - chanceInPercent;
	}

	public void cookFish(final int id, final int slot) {
		if (c.getItems().playerHasItem(id, 1)) {
			if (c.playerLevel[c.playerCooking] >= cook.levelReq) {
				if (willBurn(c.playerLevel[c.playerCooking], cook.levelStopBurningAtWithoutGloves,
						cook.levelStopBurningAtWithGloves, checkForCookingGauntlets())) {
					c.isCooking = true;
					c.startAnimation(883);
					c.getItems()
							.deleteItem(id, c.getItems().getItemSlot(id), 1);
					c.getItems().addItem(cook.burntId, 1);
					c.sendMessage("You burn a " + cook.name + ".");
				} else {
					c.isCooking = true;
					c.startAnimation(883);
					c.getItems()
							.deleteItem(id, c.getItems().getItemSlot(id), 1);
					c.getItems().addItem(cook.cookedId, 1);
					c.getPA().addSkillXP(
							cook.xpGained * Config.COOKING_EXPERIENCE,
							c.playerCooking);
					c.sendMessage("You successfully cook a " + cook.name + ".");
				}

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
						if (willBurn(c.playerLevel[c.playerCooking],
								cook.levelStopBurningAtWithoutGloves,
								cook.levelStopBurningAtWithGloves, checkForCookingGauntlets())) {
							c.isCooking = true;
							c.startAnimation(883);
							c.getItems().deleteItem(id,
									c.getItems().getItemSlot(id), 1);
							c.getItems().addItem(cook.burntId, 1);
							c.sendMessage("You burn a " + cook.name + ".");
						} else {
							c.isCooking = true;
							c.startAnimation(883);
							c.getItems().deleteItem(id,
									c.getItems().getItemSlot(id), 1);
							c.getItems().addItem(cook.cookedId, 1);
							c.getPA().addSkillXP(
									cook.xpGained * Config.COOKING_EXPERIENCE,
									c.playerCooking);
							c.sendMessage("You successfully cook a "
									+ cook.name + ".");
						}

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

	/**
	 * Checks if the player is wearing cooking gauntlets.
	 * @return true if the item that they player has equipped in the hand slot is of id 775 (Cooking Gauntlets).
	 * @author Rakeyjakey
	 */
	private boolean checkForCookingGauntlets() {
		return c.playerEquipment[c.playerHands] == 775;
	}
}

/**
 * An enum to hold data about the fish that can be cooked.
 * @author Rakeyjakey
 *
 */
enum CookingEnum {
	SHRIMP("Shrimp", 317, 315, 7954, 30, 1, 34, 34), 
	TROUT("Trout", 335, 333,323, 100, 20, 50, 48), 
	SALMON("Salmon", 331, 329, 323, 150, 30, 57,57), 
	TUNA("Tuna", 359, 361, 363, 175, 35, 65, 62), 
	LOBSTER("Lobster", 377, 379, 381, 200, 40, 74, 68),
	SWORDFISH("Swordfish", 371, 373, 375, 225, 50, 86, 81),
	MONKFISH("Monkfish", 7944, 7946, 7948, 300, 62, 92, 90), 
	SHARK("Shark", 383,385, 387, 500, 80, 100, 94), 
	SEA_TURTLE("Sea Turtle", 395, 397, 381, 800, 82, 100, 100),
	MANTA_RAY("Manta Ray", 389, 391, 393, 700,91, 100, 100);
	
	int rawId, cookedId, burntId, xpGained, levelReq,
			levelStopBurningAtWithoutGloves, levelStopBurningAtWithGloves;
	String name;

	

	/**
	 * 
	 * @param id the id of the fish used on the fire/range.
	 * @return an instance of CookingEnum relevant to the item used. Eg. if a shrimp is used it will return CookingEnum.SHRIMP;
	 * @author Rakeyjakey
	 */
	public static CookingEnum checkIngredients(int id) {
		
		int[] rawIds = { 317, 335, 331, 359, 377, 371, 7944, 383, 395, 389 };
		CookingEnum[] enumArray = { SHRIMP, TROUT, SALMON, TUNA, LOBSTER, SWORDFISH, MONKFISH,
					SHARK, SEA_TURTLE, MANTA_RAY };
			
		for (int i = 0; i < rawIds.length; i++) {
			if (id == rawIds[i])
				return enumArray[i];
		}
		
		return null;
	}

	private CookingEnum(String name, int rawId, int cookedId, int burntId,
			int xpGained, int levelReq, int levelStopBurningAtWithoutGloves,
			int levelStopBurningAtWithGloves) {
		
		this.name = name;
		this.rawId = rawId;
		this.levelReq = levelReq;
		this.cookedId = cookedId;
		this.burntId = burntId;
		this.xpGained = xpGained;
		this.levelReq = levelReq;
		this.levelStopBurningAtWithoutGloves = levelStopBurningAtWithoutGloves;
		this.levelStopBurningAtWithoutGloves = levelStopBurningAtWithGloves;
	}

}
