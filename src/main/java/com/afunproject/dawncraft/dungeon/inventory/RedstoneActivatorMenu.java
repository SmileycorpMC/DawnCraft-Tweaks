package com.afunproject.dawncraft.dungeon.inventory;

import com.afunproject.dawncraft.dungeon.block.entity.RedstoneActivatorBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class RedstoneActivatorMenu extends AbstractContainerMenu {

	protected RedstoneActivatorBlockEntity blockEntity = null;

	/*public RedstoneActivatorMenu(int id, RedstoneActivatorBlockEntity entity) {
		this(id);
	}*/

	protected RedstoneActivatorMenu(MenuType<?> p_38851_, int p_38852_) {
		super(p_38851_, p_38852_);
	}

	/*public RedstoneActivatorMenu(int id) {
		this(DungeonMenus.REDSTONE_ACTIVATOR_MENU.get(), id);
	}*/

	@Override
	public boolean stillValid(Player player) {
		return player.isCreative() && blockEntity != null;
	}

}
