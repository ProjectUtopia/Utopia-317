package com.rakeyjake.server.world.map;

import java.util.Hashtable;
import java.util.Map;

public class VirtualWorld {

	public VirtualWorld() {
	}

	public static final void main(String args[]) {
		init();
	}

	public static final void init() {
		for (int i = 0; i < 10331; i++) {
			I[i] = null;
			I[i] = new Hashtable<Object, Object>();
		}

		com.rakeyjake.server.world.map.I.Iz(true);
		for (int j = 0; j < 0x989680; j++)
			I(0, 3222, 3242, 3223, 3243, 0);

	}

	public static final boolean I(int height, int currentX, int currentY,
			int futureX, int futureY, int a) {
		if (height % 4 == 0)
			height = 0;
		if (currentX != futureX && futureY != currentY) {
			return com.rakeyjake.server.world.map.I.Iz(height, currentX, currentY, currentX,
					futureY, a)
					&& com.rakeyjake.server.world.map.I.Iz(height, currentX, currentY,
							futureX, currentY, a)
					&& com.rakeyjake.server.world.map.I.Iz(height, currentX, currentY,
							futureX, futureY, a);
		}
		return com.rakeyjake.server.world.map.I.Iz(height, currentX, currentY, futureX,
				futureY, a);
	}

	@SuppressWarnings("rawtypes")
	public static Map I[] = new Hashtable[10331];

}
