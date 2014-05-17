package com.rakeyjake.server.commands.donator;

import com.rakeyjake.server.commands.Command;
import com.rakeyjake.server.model.players.Client;

public class ItemSet extends Command{

	@Override
	public String command() {
		return "itemset";
	}

	@Override
	public void execute(Client c, String playerCommand) {
		String[] args = playerCommand.split(" ");
		Set itemset = Set.valueOf(args[1].toUpperCase());
		for (int i = 0; i< itemset.ids.length; i++) {
			c.getItems().addItem(itemset.ids[i], itemset.ammounts[i]);
		}
	}
	
	enum Set{
		VENG(new int[]{ 9075, 557, 560}, new int[]{1000,1000,1000}),
		BARRAGE(new int[]{555, 560, 565}, new int[]{6000,4000,2000}),
		POTS(new int[]{157, 145, 163}, new int[]{1,1,1});
		
		private int[] ids;
		private int[] ammounts;
		
		Set(int[] ids, int[] ammount){
			this.ids = ids;
			this.ammounts = ammount;
		}
		
		@Override 
		public String toString(){
			return name().toLowerCase();
		}
	}

}
