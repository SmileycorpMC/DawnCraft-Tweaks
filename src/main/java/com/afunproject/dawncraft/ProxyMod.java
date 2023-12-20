package com.afunproject.dawncraft;

import net.minecraftforge.fml.common.Mod;

@Mod(Constants.LEGACY_MODID)
public class ProxyMod {}//TODO: remove after quest giver removes dependency
//quest giver still hasn't removed dependency despite the mod not using anything
//doesn't look like it's going to now, so we're stuck loading a second mod with 0 content to make the game load
