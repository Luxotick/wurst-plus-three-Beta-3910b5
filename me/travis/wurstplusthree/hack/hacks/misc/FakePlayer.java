/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.GameType
 *  net.minecraft.world.World
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

@Hack.Registration(name="Fake Player", description="spawns a dripped out fake player", category=Hack.Category.MISC, priority=HackPriority.Lowest)
public class FakePlayer
extends Hack {
    private final ItemStack[] armour = new ItemStack[]{new ItemStack((Item)Items.GOLDEN_BOOTS), new ItemStack((Item)Items.GOLDEN_LEGGINGS), new ItemStack((Item)Items.GOLDEN_CHESTPLATE), new ItemStack((Item)Items.GOLDEN_HELMET)};

    @Override
    public void onEnable() {
        if (this.nullCheck()) {
            this.disable();
            return;
        }
        EntityOtherPlayerMP clonedPlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString("dbc45ea7-e8bd-4a3e-8660-ac064ce58216"), "travis"));
        clonedPlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        clonedPlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
        clonedPlayer.rotationYaw = FakePlayer.mc.player.rotationYaw;
        clonedPlayer.rotationPitch = FakePlayer.mc.player.rotationPitch;
        clonedPlayer.setGameType(GameType.SURVIVAL);
        clonedPlayer.setHealth(20.0f);
        FakePlayer.mc.world.addEntityToWorld(-1337, (Entity)clonedPlayer);
        for (int i = 0; i < 4; ++i) {
            ItemStack item = this.armour[i];
            item.addEnchantment(Enchantments.BLAST_PROTECTION, 4);
            clonedPlayer.inventory.armorInventory.set(i, (Object)item);
        }
        clonedPlayer.onLivingUpdate();
    }

    @Override
    public void onDisable() {
        if (!this.nullCheck()) {
            FakePlayer.mc.world.removeEntityFromWorld(-1337);
        }
    }
}

