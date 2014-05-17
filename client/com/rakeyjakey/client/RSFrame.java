package com.rakeyjakey.client;


import java.awt.Frame;
import java.awt.Graphics;

final class RSFrame extends Frame {

	public RSFrame(RSApplet RSApplet_, int i, int j) {
		rsApplet = RSApplet_;
		setTitle("");
		setResizable(false);
		setVisible(true);
		toFront();
		setSize(i + 8, j + 28);
		setResizable(true);
		setLocationRelativeTo(null);
	}

	@Override
	public Graphics getGraphics() {
		Graphics g = super.getGraphics();
		g.translate(4, 24);
		return g;
	}

	@Override
	public void update(Graphics g) {
		rsApplet.update(g);
	}

	@Override
	public void paint(Graphics g) {
		rsApplet.paint(g);
	}

	private final RSApplet rsApplet;
}
