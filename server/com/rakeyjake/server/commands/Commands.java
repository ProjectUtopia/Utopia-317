package com.rakeyjake.server.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.rakeyjake.server.Config;
import com.rakeyjake.server.Connection;
import com.rakeyjake.server.Server;
import com.rakeyjake.server.model.players.Client;
import com.rakeyjake.server.model.players.PacketType;
import com.rakeyjake.server.model.players.Player;
import com.rakeyjake.server.model.players.PlayerHandler;
import com.rakeyjake.server.model.players.PlayerSave;
import com.rakeyjake.server.util.Misc;

public class Commands implements PacketType {

	static {
		OWNER = load(Command.Type.OWNER);
		ADMIN = load(Command.Type.ADMIN);
		MODERATOR = load(Command.Type.MODERATOR);
		DONATOR = load(Command.Type.DONATOR);
		PLAYER = load(Command.Type.PLAYER);
	}

	private final static Command[] OWNER;
	private final static Command[] ADMIN;
	private final static Command[] MODERATOR;
	private final static Command[] DONATOR;
	private final static Command[] PLAYER;

	public static Command[] load(Command.Type type) {
		ArrayList<Command> commands = new ArrayList<>();
		File obtainDir = new File("server\\com\\rakeyjake\\server\\commands\\"
				+ type);
		for (File commandFile : obtainDir.listFiles()) {
			String className = "com.rakeyjake.server.commands." + type + "."
					+ commandFile.getName().replaceAll(".java", "");
			try {
				@SuppressWarnings("unchecked")
				Class<Command> commandClass = (Class<Command>) Class
						.forName(className);
				commands.add(commandClass.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return commands.toArray(new Command[commands.size()]);
	}

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		String playerCommand = c.getInStream().readString();
		Misc.println(c.playerName + " playerCommand: " + playerCommand);
		switch (c.playerRights) {
		case 4:
			ownerCommands(c, playerCommand);
		case 3:
			adminCommands(c, playerCommand);
		case 2:
			moderatorCommands(c, playerCommand);
		case 1:
			donatorCommands(c, playerCommand);
		default:
			playerCommands(c, playerCommand);
		}
	}

	public static void ownerCommands(Client c, String playerCommand) {
		/*
		 * Owner commands
		 */
		for (Command co : Commands.OWNER) {
			if (playerCommand.startsWith(co.command())) {
				co.execute(c, playerCommand);
				break;
			}
		}
	}

	public static void adminCommands(Client c, String playerCommand) {
		/*
		 * When a admin does a command it goes through all these commands to
		 * find a match
		 */

		if (playerCommand.startsWith("unmakenpc")) {
			String[] args = playerCommand.split(" ");
			String name = args[1];

			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				Player p = PlayerHandler.players[i];
				if (p != null && p.playerName.equalsIgnoreCase(name)) {
					Client c2 = (Client) p;
					c2.isNpc = false;
					c2.updateRequired = true;
					c2.appearanceUpdateRequired = true;
				}
			}
		}

		if (playerCommand.startsWith("makenpc")) {

			String[] args = playerCommand.split(" ");
			String name = args[1];
			String npcString = args[2];
			int npcId = Integer.parseInt(npcString);

			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name)) {
						Client c2 = (Client) PlayerHandler.players[i];

						if (npcId < 9999) {
							c2.npcId2 = npcId;
							c2.isNpc = true;
							c2.updateRequired = true;
							c2.appearanceUpdateRequired = true;

							c.sendMessage("You turned " + c2.playerName
									+ " into npc: " + npcId);
							c2.sendMessage(Misc.optimizeText(c.playerName)
									+ " turned you into npc: " + npcId);
						} else {
							c.sendMessage("Invalid NPC id");
						}
					}
				}
			}
		}

		if (playerCommand.startsWith("ban") && playerCommand.charAt(3) == ' ') {
			try {
				String playerToBan = playerCommand.substring(4);
				Connection.addNameToBanList(playerToBan);
				Connection.addNameToFile(playerToBan);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							PlayerHandler.players[i].disconnected = true;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("unmute")) {

			try {
				String playerToBan = playerCommand.substring(7);
				Connection.unMuteUser(playerToBan);
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("mute")) {

			try {
				String playerToBan = playerCommand.substring(5);
				Connection.addNameToMuteList(playerToBan);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been muted by: "
									+ c.playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("unban")) {

			try {
				String playerToBan = playerCommand.substring(6);
				Connection.removeNameFromBanList(playerToBan);
				c.sendMessage(playerToBan + " has been unbanned.");
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("ipmute")) {

			try {
				String playerToBan = playerCommand.substring(7);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Connection
									.addIpToMuteList(PlayerHandler.players[i].connectedFrom);
							c.sendMessage("You have IP Muted the user: "
									+ PlayerHandler.players[i].playerName);
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been muted by: "
									+ c.playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}

		if (playerCommand.startsWith("takeitem")) {

			try {
				String[] args = playerCommand.split(" ");
				int takenItemID = Integer.parseInt(args[1]);
				int takenItemAmount = Integer.parseInt(args[2]);
				String otherplayer = args[3];
				Client c2 = null;
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(otherplayer)) {
							c2 = (Client) PlayerHandler.players[i];
							break;
						}
					}
				}
				if (c2 == null) {
					c.sendMessage("Player doesn't exist.");
					return;
				}
				c.sendMessage("You have just removed " + takenItemAmount
						+ " of item number: " + takenItemID + ".");
				c2.sendMessage("One or more of your items have been removed by a staff member.");
				c2.getItems().deleteItem(takenItemID, takenItemAmount);
			} catch (Exception e) {
				c.sendMessage("Use as ::takeitem ID AMOUNT PLAYERNAME.");
			}
		}

		if (playerCommand.startsWith("item")) {
			try {
				String[] args = playerCommand.split(" ");
				if (args.length == 3) {
					int newItemID = Integer.parseInt(args[1]);
					int newItemAmount = Integer.parseInt(args[2]);
					if ((newItemID <= 25000) && (newItemID >= 0)) {
						c.getItems().addItem(newItemID, newItemAmount);
						System.out.println("Spawned: " + newItemID + " by: "
								+ c.playerName);
					} else {
						c.sendMessage("No such item.");
					}
				} else {
					c.sendMessage("Use as ::item 995 200");
				}
			} catch (Exception e) {
			}
		}
		// dbolts(e) - 9244
		// 3842 zam book

		if (playerCommand.equals("fillmeup")) {
			if (c.inWild())
				return;
			// c.getPA().spellTeleport(3087, 3500, 1);
			c.getItems().addItem(397, 28);
			// c.sendMessage("Spawned at home to take out camping.");
		}

		if (playerCommand.startsWith("ipban")) { // use as ::ipban name

			try {
				String playerToBan = playerCommand.substring(6);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							if (PlayerHandler.players[i].connectedFrom
									.equalsIgnoreCase("74.166.126.225")) {
								c.sendMessage("You have IP banned the user "
										+ PlayerHandler.players[i].playerName
										+ " with the host: 74.166.126.225");
								return;
							}
							if (c.duelStatus < 5
									&& PlayerHandler.players[i].duelStatus < 5) {
								if (PlayerHandler.players[i].playerRights < 1) {
									Connection
											.addIpToBanList(PlayerHandler.players[i].connectedFrom);
									Connection
											.addIpToFile(PlayerHandler.players[i].connectedFrom);

									c.sendMessage("You have IP banned the user: "
											+ PlayerHandler.players[i].playerName
											+ " with the host: "
											+ PlayerHandler.players[i].connectedFrom);
									PlayerHandler.players[i].disconnected = true;
								} else {
									c.sendMessage("You cannot ipban a moderator!");
								}
							}
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must be Online.");
			}
		}
		
		// Empties target player's inventory. FULLYWORKING.
		if (playerCommand.startsWith("invclear")) {

			try {
				String[] args = playerCommand.split(" ");
				String otherplayer = args[1];
				Client c2 = null;
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(otherplayer)) {
							c2 = (Client) PlayerHandler.players[i];
							break;
						}
					}
				}
				if (c2 == null) {
					c.sendMessage("Player doesn't exist.");
					return;
				}
				c2.getItems().removeAllItems();
				c2.sendMessage("Your inventory has been cleared by a staff member.");
				c.sendMessage("You cleared " + c2.playerName + "'s inventory.");
			} catch (Exception e) {
				c.sendMessage("Use as ::invclear PLAYERNAME.");
			}
		}

		// Gives target player specified item in their inventory. FULLYWORKING.
		if (playerCommand.startsWith("giveitem")) {

			try {
				String[] args = playerCommand.split(" ");
				int newItemID = Integer.parseInt(args[1]);
				int newItemAmount = Integer.parseInt(args[2]);
				String otherplayer = args[3];
				Client c2 = null;
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(otherplayer)) {
							c2 = (Client) PlayerHandler.players[i];
							break;
						}
					}
				}
				if (c2 == null) {
					c.sendMessage("Player doesn't exist.");
					return;
				}
				c.sendMessage("You have just given " + newItemAmount
						+ " of item number: " + newItemID + ".");
				c2.sendMessage("You have just been given item(s).");
				c2.getItems().addItem(newItemID, newItemAmount);
			} catch (Exception e) {
				c.sendMessage("Use as ::giveitem ID AMOUNT PLAYERNAME.");
			}
		}

		// Sets desired skill to desired level. Need a list of what skill is
		// what. FULLYWORKING.
		if (playerCommand.startsWith("setlevel")) {
			try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]);
				if (level > 99) {
					level = 99;
				} else if (level < 0) {
					level = 1;
				}
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.getPA().levelUp(skill);
			} catch (Exception e) {
			}
		}

		if (playerCommand.startsWith("jail")) {
			String targetUsr = playerCommand.substring(5);
			Client c2 = null;

			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(targetUsr)) {
						c2 = (Client) PlayerHandler.players[i];
						break;
					}
				}
			}
			if (c2 == null) {
				c.sendMessage("Player doesn't exist.");
				return;
			}
			c2.getPA().movePlayer(2933, 3285, 0);
			c2.teleBlockLength = 20000000;
			c2.jailed = true;
			c.sendMessage(Misc.optimizeText(targetUsr + " succesfully jailed."));
			c2.sendMessage(Misc.optimizeText("@red@You have been jailed by "
					+ c.playerName + "."));
			c2.sendMessage("@red@You will be contacted momentarily.");

			// x2933, y3285
		}

		// UNJAILS TARGET USER. FULLYWORKING.
		if (playerCommand.startsWith("unjail")) {
			String targetUsr = playerCommand.substring(7);
			Client c2 = null;
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(targetUsr)) {
						c2 = (Client) PlayerHandler.players[i];
						break;
					}
				}
			}
			if (c2 == null) {
				c.sendMessage("Player doesn't exist.");
				return;
			}
			c2.getPA().movePlayer(3087, 3500, 0);
			c2.teleBlockLength = 0;
			c2.jailed = false;
			c.sendMessage(targetUsr + " succesfully unjailed.");
			c2.sendMessage(Misc.optimizeText("@red@You have been unjailed by "
					+ c.playerName + "."));
			// x2933, y3285

		}

		if (playerCommand.startsWith("pnpc")) {
			int npc = Integer.parseInt(playerCommand.substring(5));
			if (npc < 9999) {
				c.npcId2 = npc;
				c.isNpc = true;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
			}
		}
		if (playerCommand.startsWith("unpc")) {
			c.isNpc = false;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
		}
	}

	public static void moderatorCommands(Client c, String playerCommand) {
		/*
		 * When a moderator does a comand it goes through all these commands to
		 * find a match
		 */
		for (Command co : Commands.MODERATOR) {
			if (playerCommand.startsWith(co.command())) {
				co.execute(c, playerCommand);
				break;
			}
		}
	}

	public static void donatorCommands(Client c, String playerCommand) {
		/*
		 * When a donator does a comand it goes through all these commands to
		 * find a match
		 */
		for (Command co : Commands.DONATOR) {
			if (playerCommand.startsWith(co.command())) {
				co.execute(c, playerCommand);
				break;
			}
		}

		// Changes spellbook between ancients and modern. FULLYWORKING.
		if (playerCommand.startsWith("spellbook")) {
			if (c.playerMagicBook == 0) {
				c.setSidebarInterface(6, 12855);
				c.playerMagicBook = 1;
			} else if (c.playerMagicBook == 1) {
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
			} else if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
			}
		}

	}

	public static void playerCommands(Client c, String playerCommand) {
		/*
		 * When a player does a command it goes through all these commands to
		 * find a match
		 */
		for (Command co : Commands.PLAYER) {
			if (playerCommand.startsWith(co.command())) {
				co.execute(c, playerCommand);
				break;
			}
		}
	}
}
