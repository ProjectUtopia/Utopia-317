package com.rakeyjakey.client.frame;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import com.rakeyjakey.client.Client;
import com.rakeyjakey.client.settings.Settings;
import com.rakeyjakey.client.xml.Xml$;

public class Jframe extends Client implements ActionListener {

	private static JMenuItem menuItem;
	private JFrame frame;

	public Jframe(String args[]) {
		super();
		try {
			com.rakeyjakey.client.sign.signlink.startpriv(InetAddress
					.getByName(server));
			initUI();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void close() {
		frame.dispose();
	}

	public void initUI() {
		try {
			// UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			frame = new JFrame(Settings.NAME + " v" + Settings.VERSION_NUMBER
					+ " (rev) " + Settings.REVISION_ID + ")");
			frame.setLayout(new BorderLayout());
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel gamePanel = new JPanel();

			gamePanel.setLayout(new BorderLayout());
			gamePanel.add(this);
			gamePanel.setPreferredSize(new Dimension(765, 503));

			JMenu fileMenu = new JMenu("File");

			String[] mainButtons = new String[] { "Change IP", "Change Port",
					"Item IDs", "Object IDs", "NPC IDs", "-", "Exit" };

			for (String name : mainButtons) {
				JMenuItem menuItem = new JMenuItem(name);
				if (name.equalsIgnoreCase("-")) {
					fileMenu.addSeparator();
				} else {
					menuItem.addActionListener(this);
					fileMenu.add(menuItem);
				}
			}

			JMenuBar menuBar = new JMenuBar();
			JMenuBar jmenubar = new JMenuBar();

			frame.add(jmenubar);
			menuBar.add(fileMenu);
			frame.getContentPane().add(menuBar, BorderLayout.NORTH);
			frame.getContentPane().add(gamePanel, BorderLayout.CENTER);

			// SET ICON
			/*
			 * Image icon = getImage("Utopia%20Logo.png"); if (icon != null)
			 * frame.setIconImage(icon);
			 */

			frame.pack();

			frame.setVisible(true); // can see the client
			frame.setResizable(false); // resizeable frame

			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Image getImage(String name) {
		String url = "file:///C:/Users/Jake/Dropbox/" + name;
		try {
			File f = new File("./Cache/" + name);
			if (f.exists())
				return ImageIO.read(f.toURI().toURL());
			Image img = ImageIO.read(new URL(url));
			if (img != null) {
				ImageIO.write((RenderedImage) img, "PNG", f);
				return img;
			}
		} catch (MalformedURLException e) {
			System.out.println("Error connecting to image URL: " + url);
		} catch (IOException e) {
			System.out.println("Error reading file: " + name);
		}
		return null;
	}

	@Override
	public URL getCodeBase() {
		try {
			return new URL("http://" + server + "/cache");
		} catch (Exception e) {
			return super.getCodeBase();
		}
	}

	@Override
	public URL getDocumentBase() {
		return getCodeBase();
	}

	public void loadError(String s) {
		System.out.println("loadError: " + s);
	}

	@Override
	public String getParameter(String key) {
		return "";
	}

	private static void openUpWebSite(String url) {
		Desktop d = Desktop.getDesktop();
		try {
			d.browse(new URI(url));
		} catch (Exception e) {
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		try {
			if (cmd != null) {
				if (cmd.equalsIgnoreCase("exit")) {
					System.exit(0);
				}
				if (cmd.equalsIgnoreCase("change ip")) {
					Client.server = JOptionPane
							.showInputDialog("Input server IP address:");
					System.out.println("IP Address set to: " + Client.server);
				}
				if (cmd.equalsIgnoreCase("change port")) {
					String temp = JOptionPane
							.showInputDialog("Input server port:");
					Client.port = Integer.parseInt(temp);
					System.out.println("Port set to: " + Client.port);
				}

				if (cmd.equalsIgnoreCase("item ids")) {
					// if (isApplet) {
					// new Xml$((new StringBuilder())
					// .append(findcachedir())
					// .append("Files/all_IDs/Items.xml")
					// .toString());
					// } else {
					new Xml$("cache/all_IDs/Items.xml");
					// }
				}

				if (cmd.equalsIgnoreCase("NPC IDs")) {
					if (isApplet) {

						new Xml$((new StringBuilder()).append(findcachedir())
								.append("Files/all_IDs/NPCs.xml").toString());
					} else {
						new Xml$("cache/all_IDs/NPCs.xml");
					}
				}
				if (cmd.equalsIgnoreCase("New Item IDs")) {
					if (isApplet) {

						new Xml$((new StringBuilder()).append(findcachedir())
								.append("Files/all_IDs/NewItems.xml")
								.toString());
					} else {
						new Xml$("cache/all_IDs/NewItems.xml");
					}
				}
				if (cmd.equalsIgnoreCase("Object IDs")) {
					if (isApplet) {

						new Xml$((new StringBuilder()).append(findcachedir())
								.append("Files/all_IDs/Objects.xml").toString());
					} else {
						new Xml$("cache/all_IDs/Objects.xml");
					}
				}
			}

		} catch (Exception e) {
		}
	}

	public static final String findcachedir() {
		try {
			String s = "./";
			String s1 = "cache";
			File file = new File(s1 + s);
			if (file.exists() || file.mkdir())
				return s1 + s + "/";
		} catch (Exception _ex) {
		}

		return null;
	}

	private static boolean isApplet = false;
}