package com.afunproject.dawncraft.integration.ironspellbooks;

import io.redspace.ironsspellbooks.capabilities.spell.SpellData;
import io.redspace.ironsspellbooks.capabilities.spellbook.SpellBookData;
import net.minecraft.world.item.ItemStack;

public class IronsSpellbooksCompat {
    
    public static boolean isSpellBook(ItemStack stack) {
        return SpellData.hasSpellData(stack) || SpellBookData.getSpellBookData(stack).getSpellCount() > 0;
    }
    
}
