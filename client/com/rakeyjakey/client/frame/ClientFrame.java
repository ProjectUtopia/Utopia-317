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
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.jgoodies.forms.layout.CellConstraints.Alignment;
import com.rakeyjakey.client.Client;
import com.rakeyjakey.client.settings.Settings;
import com.rakeyjakey.client.xml.Xml;

@SuppressWarnings("serial")
public class ClientFrame extends Client implements ActionListener {

	private JFrame frame;

	public ClientFrame(String args[]) {
		super();
		try {
			com.rakeyjakey.client.sign.SignLink.startpriv(InetAddress
					.getByName(server));
			initUI();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void initUI() {
		try {
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			frame = new JFrame(Settings.NAME + " v" + Settings.VERSION_NUMBER
					+ " (rev" + Settings.REVISION_ID + ")");
			frame.setLayout(new BorderLayout());
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel gamePanel = new JPanel();
			gamePanel.setLayout(new BorderLayout());
			gamePanel.add(this);
			gamePanel.setPreferredSize(new Dimension(765, 503));

			JMenu fileMenu = new JMenu("File");
			String[] mainButtons = new String[] { "Change IP", "Change Port",
					"Item IDs", "Object IDs", "NPC IDs", "-", "About Utopia",
					"Exit" };

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

			Image icon = getImage("L2bSkVW.png");
			if (icon != null)
				frame.setIconImage(icon);

			frame.pack();

			frame.setVisible(true);

			init();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Image getImage(String url) {
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
		if (cmd != null) {
			if (cmd.equalsIgnoreCase("exit")) {
				System.exit(0);
			}
			if (cmd.equalsIgnoreCase("about utopia")) {
				createAboutUtopiaFrame();
			}
			if (cmd.equalsIgnoreCase("change ip")) {
				Client.server = JOptionPane
						.showInputDialog("Input server IP address:");
				System.out.println("IP Address set to: " + Client.server);
			}
			if (cmd.equalsIgnoreCase("change port")) {
				String temp = JOptionPane.showInputDialog("Input server port:");
				Client.port = Integer.parseInt(temp);
				System.out.println("Port set to: " + Client.port);
			}

			if (cmd.equalsIgnoreCase("item ids")) {
				new Xml("cache/all_IDs/Items.xml");
			}

			if (cmd.equalsIgnoreCase("NPC IDs")) {
				new Xml("cache/all_IDs/NPCs.xml");
			}
			if (cmd.equalsIgnoreCase("New Item IDs")) {
				new Xml("cache/all_IDs/NewItems.xml");
			}
			if (cmd.equalsIgnoreCase("Object IDs")) {
				new Xml("cache/all_IDs/Objects.xml");
			}
		}

	}

	/**
	 * Creates the frame to display information about the server. Used
	 * windowbuilder for ease so it's messy.
	 * 
	 * @author Rakeyjakey
	 */
	private void createAboutUtopiaFrame() {
		JLabel lblRakeyjakey = new JLabel("Copyright \u00A9 2014 Rakeyjakey");
		JLabel lblUtopiaVersion = new JLabel(Settings.NAME + " v"
				+ Settings.VERSION_NUMBER + " (rev" + Settings.REVISION_ID
				+ ")");
		JFrame frame = new JFrame("About Utopia");

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING)
														.addComponent(
																lblUtopiaVersion,
																GroupLayout.PREFERRED_SIZE,
																304,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblRakeyjakey,
																GroupLayout.PREFERRED_SIZE,
																285,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap(193, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblUtopiaVersion,
								GroupLayout.PREFERRED_SIZE, 59,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblRakeyjakey,
								GroupLayout.PREFERRED_SIZE, 59,
								GroupLayout.PREFERRED_SIZE).addGap(290)));

		frame.setLayout(groupLayout);
		frame.setSize(345, 160);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setAutoRequestFocus(true);
		frame.setLocationRelativeTo(frame);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
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

}