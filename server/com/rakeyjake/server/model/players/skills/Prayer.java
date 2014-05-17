package com.rakeyjake.server.model.players.skills;

import com.rakeyjake.server.model.players.Client;

	/**
	 * Class Prayer
	 * Handles Prayer
	 * @author 2012
	 * 23:56 29/12/2010
	 */
 
public class Prayer {

	Client c;
	public Prayer(Client c){
		this.c = c;
	}
	private static final int PRAYER_XP = 20;

	private static int[][] BONES = {
		{526, 5}, 	//	NPC BONES
		{528, 5}, 	//	BURNT BONES
		{530, 5}, 	//	BAT BONES
		{2859, 5},	//	WOLF BONES
		{3179, 5},	//	MONKEY BONES
		{3180, 5}, 	//	MONKEY BONES
		{3181, 5}, 	//	MONKEY BONES
		{3182, 5},	//	MONKEY BONES
		{3183, 5}, 	//	MONKEY BONES
		{3185, 5}, 	//	MONKEY BONES
		{3186, 5},	//	MONKEY BONES
		{3187, 5}, 	//	MONKEY BONES
		{532, 15},	//	BIG BONES
		{534, 30}, 	//	BABY DRAGON BONES
		{536, 72},	//	DRAGON BONES
		{2530, 5}, 	//	PLAYER BONES
		{3123, 25}, 	//	SHAIKAHAN BONES
		{3125, 23}, 	//	JOGRE BONES
		{3127, 25}, 	//	BURNT JOGRE BONES
		{4812, 82}, 	//	ZOGRE BONES
		{4830, 84}, 	//	FAYGR BONES
		{4832, 96},	//	RAURG BONES
		{4834, 140}, 	//	OURG BONES
		{6729, 125},	//	DAGANNOTH BONES
		{6812, 50}, 	//	WYVERN BONES
		{10976, 1000}, //	LONG BONE
		{10977, 1250}, //	CURVED BONE
		{11337, 2000}, //	MANGLED BONE
		{11338, 2500}, //	CHEWED BONE
	};

	public static boolean playerBones(Client c, int item) {
		for(int i = 0; i < BONES.length; i++) {
			if(item == BONES[i][0]) {
				return true;
			}
		}
		return false;
	}

	private static void handleBones(Client c, int i, boolean altar, int slot) {
		for(int l = 0; l < BONES.length; l++) {
			if(i == BONES[l][0]) {
				if(System.currentTimeMillis() - c.buryDelay > 800) {
					c.getItems().deleteItem(BONES[l][0], slot, 1);
					c.getPA().addSkillXP((altar ? BONES[l][1] * 2 : BONES[l][1]) * PRAYER_XP, 5);
					c.buryDelay = System.currentTimeMillis();
					c.startAnimation((altar ? 896 : 827));
					c.sendMessage(altar ? "The gods are pleased with your offerings." : "You bury the bones.");
					if(altar) {
						c.getPA().createPlayersStillGfx(624, c.objectX, c.objectY, 0, 0);
					}
				}	
			}
		}
	}

	public static void buryBones(Client c, int i, int slot) {
		handleBones(c, i, false, slot);
	}

	public static void useBonesOnAltar(Client c, int l, int i) {
		handleBones(c, i, true, c.getItems().getItemSlot(i));
	}
}