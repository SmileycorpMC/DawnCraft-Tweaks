package com.afunproject.dawncraft.dungeon;

import java.util.Locale;

public enum KeyColour {
	RED(0x7F0000),
	GREEN(0x2B6000),
	BLUE(0x275EA5),
	YELLOW(0xFFD800),
	ORANGE(0xD88808),
	PURPLE(0x7E00A8),
	SILVER(0xEFF9F9);

	private final int colour;

	private KeyColour(int colour) {
		this.colour = colour;
	}

	public String getName() {
		return super.name().toLowerCase(Locale.US);
	}

	public int getColour() {
		return colour;
	}
}
