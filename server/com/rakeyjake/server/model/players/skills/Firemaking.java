package com.rakeyjake.server.model.players.skills;

import com.rakeyjake.server.Config;
import com.rakeyjake.server.Server;
import com.rakeyjake.server.event.CycleEvent;
import com.rakeyjake.server.event.CycleEventContainer;
import com.rakeyjake.server.event.CycleEventHandler;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.util.Misc;

/**
 * Firemaking.java
 *
 * @author Faris
 *
 **/ 
public class Firemaking {
	
	private Client c;
	
	private int[] logs = {1511,1521,1519,1517,1515,1513};
	private int[] exp = {1,3,4,5,7,8};
	private int[] level = {1,15,30,45,60,75};
	public long lastLight;
	private int DELAY = 1250;
	private int fireId = 2732;
	public int timer;
	public int timer2;
	public int multiplyFailChance;
	public int chanceToLight;
	public int chancetoFail;
	
	public boolean resetAnim = true;
	
	public Firemaking(Client c) {
		this.c = c;
	}
	
	public void checkLogType(int logType, int otherItem) {
		for (int j = 0; j < logs.length;j++) {
			if (logs[j] == logType || logs[j] == otherItem) {
				attemptLighting();
				return;
			}
		}	
	}
	
	public void lightFire(int slot) {
	
	final int x = c.getX();
	final int y = c.getY();
	
			
		if (c.playerLevel[c.playerFiremaking] >= level[slot]) {
			if (c.getItems().playerHasItem(590) && c.getItems().playerHasItem(logs[slot])) {
				if (System.currentTimeMillis() - lastLight > DELAY) {
					c.getItems().deleteItem(logs[slot], c.getItems().getItemSlot(logs[slot]), 1);
					c.getPA().addSkillXP(logs[slot] * Config.FIREMAKING_EXPERIENCE, c.playerFiremaking);
					c.getPA().checkObjectSpawn(fireId, x, y, 1, 10);
					
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			int timer = 1;
			@Override
			public void execute(CycleEventContainer container) {
			
			
			if (timer == 0) {
			container.stop();
			}
			timer--;
			
			
			}
			@Override
			public void stop() {
				c.getPA().checkObjectSpawn(-1, x, y, 1, 10);
				//c.sendMessage("Your fire has been extinguished.");
				Server.itemHandler.createGroundItem(c, 592, x, y, 1, c.playerId);
			}
		}, 45);
					c.sendMessage("You light the fire.");
					final int walk = Misc.random(3);
				if (walk == 0)
					c.getPA().walkTo(-1, 0);
				if (walk == 1)
					c.getPA().walkTo(1, 0);
				if (walk == 2)
					c.getPA().walkTo(0, 1);
				if (walk == 3)
					c.getPA().walkTo(0, -1);
					this.lastLight = System.currentTimeMillis();
					resetAnim = true;
				}
			}
		}	
	}
	
	public void attemptLighting() {
	if (c.duelStatus >= 5) {
			c.sendMessage("I can't do that here.");
			return;
		}
		
	final int x = c.getX();
	final int y = c.getY();
	final int levelChance = c.playerLevel[11];
		c.sendMessage("You attempt to light a fire...");
		c.inTrade = true;
		Server.itemHandler.createGroundItem(c, 1511, x, y, 1, c.playerId);
		c.startAnimation(733,0);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			int timer2 = 10;
			@Override
			public void execute(CycleEventContainer container) {
			double multiplyFailChance = Misc.random(levelChance)/10;
			int chanceToLight = Misc.random(levelChance)+1;
			double chanceToFail = Misc.random(9)+(multiplyFailChance);
			
			if (chanceToLight >= chanceToFail && timer2 != 0) {
			for (int j = 0; j < logs.length;j++) {
			lightFire(j);
			}
			container.stop();
			}
			
			if (timer2 == 0) {
				//c.sendMessage("You have failed to start a fire");
				container.stop();
				}
				
			timer2--;
			}
			@Override
			public void stop() {
				c.startAnimation(65535);
				Server.itemHandler.removeGroundItem(c, 1511, x, y, false);
				c.inTrade = false;
			}
		}, 3);
	}
	
}