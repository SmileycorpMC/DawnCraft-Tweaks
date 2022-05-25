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

package com.afunproject.packtweaks.silverminer.structuregen.data;

import org.jetbrains.annotations.NotNull;

import com.afunproject.packtweaks.PackTweaks;

import net.minecraft.data.info.WorldgenRegistryDumpReport;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = PackTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Events {
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
		StructureSets.bootstrap();
	}

	@SubscribeEvent
	public static void onGatherDataEvent(@NotNull GatherDataEvent event) {
		event.getGenerator().addProvider(new BiomeTagProvider(event.getGenerator(), event.getExistingFileHelper()));
		event.getGenerator().addProvider(new WorldgenRegistryDumpReport(event.getGenerator()));
	}
}
