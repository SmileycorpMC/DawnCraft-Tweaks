package com.afunproject.dawncraft.effects;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;

public class FrogformEffect extends DCEffect {

	private final String HEALTH_MOD_UUID = "f89b1456-dfb0-42a2-a4dd-f0c9320feae6";

	public static final AABB FROG_AABB = new AABB(-0.25, 0, -0.25, 0.25, 0.5, 0.25);
	public static final float FROG_EYE_HEIGHT = 0.35f;

	protected FrogformEffect() {
		super( 0xFF598423);
		addAttributeModifier(Attributes.MAX_HEALTH, HEALTH_MOD_UUID, -0.5, Operation.MULTIPLY_BASE);
	}

	@Override
	public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
		return modifier.getAmount();
	}

}
