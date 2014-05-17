package com.rakeyjake.server.commands;

import com.rakeyjake.server.model.players.Client;

public abstract class Command {
	public abstract String command();
	public abstract void execute(Client c, String playerCommand);
	
	public enum Type{
		PLAYER, DONATOR, ADMIN, OWNER;
		
		@Override
	    public String toString() {
	        return name().toLowerCase();
	    }
	}
}
