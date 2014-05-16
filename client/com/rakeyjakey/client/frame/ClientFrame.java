package com.rakeyjakey.client.frame;

import java.awt.BorderLayout;
import java.awt.Component;
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
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;

import com.rakeyjakey.client.Client;
import com.rakeyjakey.client.settings.Settings;

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
			frame = new JFrame(Settings.SERVER_NAME + " v"
					+ Settings.VERSION_NUMBER + " (rev" + Settings.REVISION_ID
					+ ")");
			frame.setLayout(new BorderLayout());
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel gamePanel = new JPanel();
			gamePanel.setLayout(new BorderLayout());
			gamePanel.add(this);
			gamePanel.setPreferredSize(new Dimension(765, 503));

			JMenu fileMenu = new JMenu("File");
			String[] mainButtons = new String[] { "Change IP", "Change Port",
					"Item IDs", "-", "About Utopia", "Exit" };

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
				createAboutProjectUtopiaFrame();
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
				openUpWebSite("http://itemdb.biz");
			}

		}

	}

	/**
	 * Creates a frame showing information about Project Utopia
	 * 
	 * @authors Rakeyjakey, PatriqDesigns
	 */
	private void createAboutProjectUtopiaFrame() {
		final JLabel versionLabel = new JLabel("<html><u>"
				+ Settings.SERVER_NAME + " v" + Settings.VERSION_NUMBER
				+ " (rev" + Settings.REVISION_ID + ")</u></html>");
		final JLabel runescapeLabel = new JLabel(
				"<html><font size=0>RuneScape is a trademark of Jagex Software 1999 - 2013 Jagex, Ltd.</font></html>");
		final JLabel jagexLabel = new JLabel(
				"<html><font size=0>Copyright � 2009-2014 Jagex Ltd. Jagex is a registered trademark of Jagex Ltd.</font></html>");
		final JLabel copyrightLabel = new JLabel(
				"<html><font size=0>Copyright \u00A9 2014 ProjectUtopia</font></html>");
		final JFrame frame = new JFrame("About ProjectUtopia") {
			{
				getContentPane().add(new JPanel() {
					{
						setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
								10));
						setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						add(versionLabel);
						add(Box.createRigidArea(new Dimension(0, 10)));
						add(runescapeLabel);
						add(jagexLabel);
						add(Box.createRigidArea(new Dimension(0, 20)));
						add(copyrightLabel);
					}
				}, BorderLayout.NORTH);
				getContentPane().add(new JPanel() {
					{
						add(new JButton("OK") {
							{
								addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										dispose();
									}
								});
								setAlignmentX(Component.RIGHT_ALIGNMENT);
							}
						});
					}
				}, BorderLayout.SOUTH);
				setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				setSize(345, 160);
				setResizable(false);
				setLocationRelativeTo(ClientFrame.this.frame);
				setVisible(true);
			}
		};
	}

}