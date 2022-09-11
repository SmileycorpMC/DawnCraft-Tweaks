package com.afunproject.dawncraft.invasion;

import net.minecraftforge.common.IExtensibleEnum;

public enum InvasionKey implements IExtensibleEnum {

	;

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	static InvasionKey create(String name) {
		throw new IllegalStateException("Enum not extended");
	}

	static InvasionKey getKey(String name) {
		return valueOf(name.toUpperCase());
	}

}
