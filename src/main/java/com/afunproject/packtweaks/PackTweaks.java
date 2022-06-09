/*
 * Copyright © 2022 AFunProject
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software
 * and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.afunproject.packtweaks;

import org.slf4j.Logger;

import com.afunproject.packtweaks.capability.CapabilitiesRegister;
import com.afunproject.packtweaks.quest.task.FollowTask;
import com.afunproject.packtweaks.silverminer.structuregen.structure.StructureRegistration;
import com.feywild.quest_giver.quest.task.TaskTypes;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PackTweaks.MODID)
public class PackTweaks {
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final String MODID = "pack_tweaks";
	public static String VERSION = "N/A";

	public PackTweaks() {
		ModList.get().getModContainerById(PackTweaks.MODID)
		.ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
		LOGGER.info("Pack Tweaks " + VERSION + " initialized");
		StructureRegistration.STRUCTURE_FEATURE_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		MinecraftForge.EVENT_BUS.register(new CapabilitiesRegister());
		MinecraftForge.EVENT_BUS.register(new EventListener());
		TaskTypes.register(location("follow_quest"), FollowTask.INSTANCE);
	}

	public static ResourceLocation location(String path) {
		return new ResourceLocation(MODID, path);
	}
}