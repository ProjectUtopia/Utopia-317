package com.rakeyjake.server.model.players.skills;

import com.rakeyjake.server.Config;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.util.Misc;

/**
 * Thieving.java
 * 
 * @author Sanity
 * 
 **/

public class Thieving {

	private Client c;

	public Thieving(Client c) {
		this.c = c;
	}

	public void stealFromNPC(int id) {
		if (System.currentTimeMillis() - c.lastThieve < 2000)
			return;
		for (int j = 0; j < npcThieving.length; j++) {
			if (npcThieving[j][0] == id) {
				if (c.playerLevel[c.playerThieving] >= npcThieving[j][1]) {
					if (Misc.random(c.playerLevel[c.playerThieving] + 2
							- npcThieving[j][1]) != 1) {
						c.getPA().addSkillXP(
								npcThieving[j][2] * Config.THIEVING_EXPERIENCE,
								c.playerThieving);
						c.getItems().addItem(995, npcThieving[j][3]);
						c.startAnimation(881);
						c.lastThieve = System.currentTimeMillis();
						c.sendMessage("You thieve some money...");
						break;
					} else {
						c.setHitDiff(npcThieving[j][4]);
						c.setHitUpdateRequired(true);
						c.playerLevel[3] -= npcThieving[j][4];
						c.getPA().refreshSkill(3);
						c.lastThieve = System.currentTimeMillis() + 2000;
						c.sendMessage("You fail to thieve the NPC.");
						break;
					}
				} else {
					c.sendMessage("You need a thieving level of "
							+ npcThieving[j][1] + " to thieve from this NPC.");
				}
			}
		}
	}

	public void stealFromStall(int id, int xp, int level) {
		if (System.currentTimeMillis() - c.lastThieve < 2500)
			return;
		if (Misc.random(100) == 0) {

			return;
		}
		if (c.playerLevel[c.playerThieving] >= level) {
			if (c.getItems().addItem(id, 1)) {
				c.startAnimation(832);
				c.getPA().addSkillXP(xp * Config.THIEVING_EXPERIENCE,
						c.playerThieving);
				c.lastThieve = System.currentTimeMillis();
				c.sendMessage("You steal a "
						+ com.rakeyjake.server.model.items.Item.getItemName(id) + ".");
			}
		} else {
			c.sendMessage("You must have a thieving level of " + level
					+ " to thieve from this stall.");
		}
	}

	public static int randomGem() {
		return RANDOM_GEM[(int) (Math.random() * RANDOM_GEM.length)];
	}

	/*public void stealFromSafe() {
		if (System.currentTimeMillis() - c.lastThieve < 4000)
			return;
		if (c.playerLevel[c.playerThieving] >= 90) {
			if (Misc.random(12) == 1) {
				c.sendMessage("You fail to pick the lock..");
				c.startAnimation(3170);
				if (c.playerLevel[3] <= 30) {
					appendHit(Misc.random(4), c);
				} else {
					appendHit(Misc.random(10), c);
					return;
				}
			}
			c.sendMessage("You attempt to pick the lock...");
			c.getItems().addItem(randomGem(), 1);
			c.startAnimation(2247);
			c.getPA().addSkillXP(400 * Config.THIEVING_EXPERIENCE,
					c.playerThieving);
			c.lastThieve = System.currentTimeMillis();
			c.sendMessage("You find a Gem, within the Wall Safe.");
		} else if (c.playerLevel[17] < 90) {
			c.sendMessage("You need a theiving level of 90 to attempt this pick-lock.");
		}
	}*/

	private static int[] RANDOM_GEM = { 1617, 1619, 1621, 1623, 1625, 1627,
			1629, 1631, 6571 };

	// npc, level, exp, coin amount
	public int[][] npcThieving = { { 1, 1, 8, 200, 1 }, { 18, 25, 26, 500, 1 },
			{ 9, 40, 47, 1000, 2 }, { 26, 55, 85, 1500, 3 },
			{ 20, 70, 152, 2000, 4 }, { 21, 80, 273, 3000, 5 } };

}