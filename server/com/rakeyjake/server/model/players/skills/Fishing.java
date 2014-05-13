package com.rakeyjake.server.model.players.skills;

import java.util.HashMap;

import com.rakeyjake.server.Config;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.util.Misc;

/**
 * Fishing.java
 * <br><br>
 * Currently have: Shrimp, Trout, Lobster, Tuna, Swordfish, Monkfish and Sharks added.
 * @author Rakeyjake
 * @version 1.0
 **/

public class Fishing {

	private Client c;
	public boolean fishing = false;
	private int currentlyFishing;

	private static enum Fish {

		SHRIMP(317, 1, 10, 303, 621, "Shrimp"), TROUT(335, 20, 50, 309, 622,
				"Trout"), SALMON(331, 30, 70, 309, 622, "Salmon"), LOBSTER(377,
				40, 80, 301, 619, "Lobster"), TUNA(359, 35, 90, 311, 618,
				"Tuna"),

		SWORDFISH(371, 50, 100, 311, 618, "Swordfish"), MONKFISH(7944, 62, 120,
				303, 621, "Monkfish"), SHARK(383, 76, 110, 311, 418, "Shark");

		private int id;
		private int levelReq;
		private int xp;
		private int equipmentType;
		private int anim;
		private String name;

		private Fish(int id, int levelReq, int xp, int equipmentType, int anim,
				String name) {
			this.id = id;
			this.levelReq = levelReq;
			this.xp = xp;
			this.equipmentType = equipmentType;
			this.anim = anim;
			this.name = name;
		}

		/**
		 * 
		 * @return Returns the id of the fish.
		 */
		public int getId() {
			return id;
		}

		/**
		 * 
		 * @return Returns the Level Requirement of the fish.
		 */
		public int getLevelReq() {
			return levelReq;
		}

		/**
		 * 
		 * @return Returns the XP that the fish gives when fished.
		 */
		public int getXp() {
			return xp;
		}

		/**
		 * 
		 * @return Returns the id of the equipment needed for the fish.
		 */
		public int getEquipmentType() {
			return equipmentType;
		}

		/**
		 * 
		 * @return Returns the animation id of the animation performed when
		 *         fishing.
		 */
		public int getAnim() {
			return anim;
		}

		/**
		 * 
		 * @return Returns the name of the fish.
		 */
		public String getName() {
			return name;
		}

		public static HashMap<Integer, Fish> fish = new HashMap<Integer, Fish>();

		/*
		 * public static Fish forId(int id) { return fish.get(id); }
		 */

		static {
			for (Fish f : Fish.values())
				fish.put(f.getId(), f);
		}

	}

	public Fishing(Client c) {
		this.c = c;
	}

	/**
	 * Handles the initial setup of the fishing process.
	 * 
	 * @param id
	 *            The fish to be fished.
	 */

	public void setupFishing(int id) {

		Fish f = Fish.fish.get(id);

		if (c.getItems().playerHasItem(f.getEquipmentType())) {

			if (c.playerLevel[c.playerFishing] >= f.getLevelReq()) {

				if (c.getItems().freeSlots() > 0) {

					c.fishing = true;
					currentlyFishing = f.getId();
					c.startAnimation(f.getAnim());
					c.fishTimer = 3 + Misc.random(2);

				} else {
					c.sendMessage("Your inventory is full.");
					c.getDH().sendStatement("Your inventory is full.");
					resetFishing();

				}

			} else {
				c.getDH().sendStatement(
						"You need a fishing level of " + f.getLevelReq()
								+ " to fish here.");
				resetFishing();
			}

		} else {
			c.getDH()
					.sendStatement(
							"You do not have the correct equipment to use this fishing spot.");
			resetFishing();

		}
	}

	/**
	 * Handles the process of catching the fish.
	 */
	public void catchFish() {
		Fish f = Fish.fish.get(currentlyFishing);
		if (c.getItems().playerHasItem(f.getEquipmentType())) {
			if (c.playerLevel[c.playerFishing] >= f.getLevelReq()) {
				if (c.getItems().freeSlots() > 0) {
					if (canFishOther(f.getId())) {
						Fish ftemp = Fish.fish.get(currentlyFishing);
						c.getItems().addItem(ftemp.getId(), 1);
						c.getPA().addSkillXP(
								ftemp.getXp() * Config.FISHING_EXPERIENCE,
								c.playerFishing);
						c.sendMessage("You catch a " + ftemp.getName() + ".");
					} else {
						c.getItems().addItem(f.getId(), 1);
						c.getPA().addSkillXP(
								f.getXp() * Config.FISHING_EXPERIENCE,
								c.playerFishing);
						c.sendMessage("You catch a " + f.getName() + ".");
					}

					c.startAnimation(f.getAnim());
					c.fishTimer = 2 + Misc.random(2);
				} else {
					c.sendMessage("Your inventory is full.");
					c.getDH().sendStatement("Your inventory is full.");
					resetFishing();

				}
			} else {
				c.getDH().sendStatement(
						"You need a fishing level of " + f.getLevelReq()
								+ " to fish here.");
				resetFishing();
			}
		} else {
			c.getDH()
					.sendStatement(
							"You do not have the correct equipment to use this fishing spot.");
			resetFishing();
		}
	}

	private boolean canFishOther(int fishType) {
		if (fishType == 335 && c.playerLevel[c.playerFishing] >= 30) {
			currentlyFishing = 331;
			return true;
		}
		if (fishType == 359 && c.playerLevel[c.playerFishing] >= 50) {
			currentlyFishing = 371;
			return true;
		}
		return false;
	}

	public void resetFishing() {
		c.startAnimation(65535);
		currentlyFishing = -1;
		c.fishTimer = -1;
		c.fishing = false;
	}

}