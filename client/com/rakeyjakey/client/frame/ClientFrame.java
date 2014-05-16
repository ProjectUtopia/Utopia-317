package com.rakeyjakey.client.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.URI;

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
import javax.swing.WindowConstants;

import com.rakeyjakey.client.Client;
import com.rakeyjakey.client.settings.Settings;

/**
 * The main client frame and its contents that run on start. 
 * Was very basic and untidy when I acquired it but I have nearly completely rewritten it.
 * 
 * @authors Rakeyjakey, PatriqDesigns
 *
 */
@SuppressWarnings("serial")
public class ClientFrame extends Client implements ActionListener {

	// The main frame that will hold everything.
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

	/**
	 * Initializes the contents of the main frame.
	 */
	public void initUI() {
		try {
			frame = new JFrame(Settings.SERVER_NAME + " v"
					+ Settings.VERSION_NUMBER + " (rev" + Settings.REVISION_ID
					+ ")") {
				{
					setLayout(new BorderLayout());
					setResizable(false);
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				}
			};

			JPanel gamePanel = new JPanel() {
				{
					setLayout(new BorderLayout());
					setPreferredSize(new Dimension(765, 503));
					add(ClientFrame.this);
				}
			};

			final JMenu fileMenu = new JMenu("File") {
				{
					String[] mainButtons = new String[] { "Change IP",
							"Change Port", "Item IDs", "-", "About Utopia",
							"Exit" };

					for (String name : mainButtons) {
						JMenuItem menuItem = new JMenuItem(name);
						if (name.equalsIgnoreCase("-")) {
							addSeparator();
						} else {
							menuItem.addActionListener(ClientFrame.this);
							add(menuItem);
						}
					}

				}
			};

			JMenuBar menuBar = new JMenuBar() {
				{
					add(fileMenu);
				}
			};

			JMenuBar jmenubar = new JMenuBar();

			frame.add(jmenubar);
			frame.getContentPane().add(menuBar, BorderLayout.NORTH);
			frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);

			init();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadError(String s) {
		System.out.println("loadError: " + s);
	}

	@Override
	public String getParameter(String key) {
		return "";
	}

	/**
	 * Opens a url in the default browser.
	 * 
	 * @param url
	 *            The url that you wish to open in the default browser.
	 */
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
	 * Creates a frame showing information about ProjectUtopia
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