package com.afunproject.dawncraft.invasion;

import net.minecraft.util.StringRepresentable;
import net.minecraftforge.common.IExtensibleEnum;

import java.util.Locale;

public enum InvasionKey implements IExtensibleEnum, StringRepresentable{

	;

	private final String name;

	private InvasionKey(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	static InvasionKey create(String registry, String name) {
		throw new IllegalStateException("Enum not extended");
	}

	static InvasionKey getKey(String name) {
		for (InvasionKey key : values()) {
			if (key.getSerializedName().equals(name)) return key;
		}
		return valueOf(name.toUpperCase(Locale.US));
	}

	@Override
	public String getSerializedName() {
		return name;
	}

}
