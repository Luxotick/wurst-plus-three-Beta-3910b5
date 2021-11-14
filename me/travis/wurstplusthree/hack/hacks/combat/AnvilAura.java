/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAnvil
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.Arrays;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import net.minecraft.block.BlockAnvil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Hack.Registration(name="Anvil Aura", description="drops anvils on people/urself", category=Hack.Category.COMBAT, isListening=false)
public class AnvilAura
extends Hack {
    EnumSetting mode = new EnumSetting("Mode", "Others", Arrays.asList("Self", "Others"), this);
    BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)false, (Hack)this);
    BooleanSetting airplace = new BooleanSetting("AirPlace", (Boolean)false, (Hack)this);
    IntSetting range = new IntSetting("Range", 3, 0, 6, this);
    IntSetting bpt = new IntSetting("BlocksPerTick", 4, 0, 10, this);
    IntSetting placeDelay = new IntSetting("PlaceDelay", 4, 0, 20, this);
    IntSetting layers = new IntSetting("Layers", 3, 1, 5, this);
    private int ticksPassed = 0;

    @Override
    public void onEnable() {
        if (InventoryUtil.findHotbarBlock(BlockAnvil.class) == -1 || PlayerUtil.findObiInHotbar() == -1) {
            this.disable();
            this.ticksPassed = 0;
        }
        if (this.mode.is("Self")) {
            this.placeAnvil(new BlockPos((Vec3i)AnvilAura.mc.player.getPosition().up(3)));
        }
    }

    @Override
    public void onTick() {
        EntityPlayerSP target = this.mode.is("Self") ? AnvilAura.mc.player : this.getTarget();
        int placedAmmount = 0;
        if (target == null) {
            ClientMessage.sendErrorMessage("Cannot find target");
            this.disable();
            return;
        }
        if (this.mode.is("Self") && this.airplace.getValue().booleanValue() && !target.isAirBorne) {
            this.placeAnvil(new BlockPos((Vec3i)EntityUtil.getFlooredPos((Entity)target)).up(this.range.getValue().intValue()));
        }
        if (this.mode.is("Self")) {
            if (this.airplace.getValue().booleanValue()) {
                this.placeAnvil(EntityUtil.getFlooredPos((Entity)target).up(this.range.getValue().intValue()));
            } else {
                if (!BlockUtil.canPlaceBlock(target.getPosition().up(3))) {
                    for (int i = 0; i < this.range.getValue() / 2; ++i) {
                        BlockPos pos = new BlockPos(target.posX - 1.0, target.posY - (double)i - 1.0, target.posZ);
                        if (AnvilAura.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) continue;
                        this.placeObi(pos);
                    }
                }
                if (this.ticksPassed == 0) {
                    if (this.airplace.getValue().booleanValue() || AnvilAura.mc.world.getBlockState(target.getPosition().up(3).south()).getBlock() != Blocks.AIR) {
                        this.placeAnvil(target.getPosition().up(3));
                    } else {
                        this.placeObi(target.getPosition().up(3).south());
                        this.placeAnvil(target.getPosition().up(3));
                    }
                    ++this.ticksPassed;
                } else {
                    this.ticksPassed = this.ticksPassed == this.placeDelay.getValue() ? 0 : ++this.ticksPassed;
                }
            }
        } else if (AnvilAura.mc.world.getBlockState(target.getPosition()).getBlock() == Blocks.ANVIL) {
            this.breakBlock(target.getPosition());
        } else {
            for (int i = 0; placedAmmount < this.bpt.getValue() && i <= this.layers.getValue(); ++i) {
                if (AnvilAura.mc.world.getBlockState(new BlockPos(target.posX - 1.0, target.posY + (double)i, target.posZ)).getBlock() == Blocks.AIR) {
                    this.placeObi(new BlockPos(target.posX - 1.0, target.posY + (double)i, target.posZ));
                    ++placedAmmount;
                }
                if (AnvilAura.mc.world.getBlockState(new BlockPos(target.posX, target.posY + (double)i, target.posZ - 1.0)).getBlock() == Blocks.AIR) {
                    this.placeObi(new BlockPos(target.posX, target.posY + (double)i, target.posZ - 1.0));
                    ++placedAmmount;
                }
                if (AnvilAura.mc.world.getBlockState(new BlockPos(target.posX + 1.0, target.posY + (double)i, target.posZ)).getBlock() == Blocks.AIR) {
                    this.placeObi(new BlockPos(target.posX + 1.0, target.posY + (double)i, target.posZ));
                    ++placedAmmount;
                }
                if (AnvilAura.mc.world.getBlockState(new BlockPos(target.posX, target.posY + (double)i, target.posZ + 1.0)).getBlock() != Blocks.AIR) continue;
                this.placeObi(new BlockPos(target.posX, target.posY + (double)i, target.posZ + 1.0));
                ++placedAmmount;
            }
            this.placeAnvil(target.getPosition().up(3));
        }
    }

    private void placeAnvil(BlockPos pos) {
        int old = AnvilAura.mc.player.inventory.currentItem;
        this.switchToSlot(InventoryUtil.findHotbarBlock(BlockAnvil.class));
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, (boolean)this.rotate.getValue(), true, false);
        this.switchToSlot(old);
    }

    private void placeObi(BlockPos pos) {
        int old = AnvilAura.mc.player.inventory.currentItem;
        this.switchToSlot(PlayerUtil.findObiInHotbar());
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, (boolean)this.rotate.getValue(), true, false);
        this.switchToSlot(old);
    }

    private void breakBlock(BlockPos pos) {
        int old = AnvilAura.mc.player.inventory.currentItem;
        for (int i = 0; i < 9; ++i) {
            if (!(AnvilAura.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemPickaxe)) continue;
            this.switchToSlot(i);
            AnvilAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.EAST));
            break;
        }
        this.switchToSlot(old);
    }

    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        double shortestRange = 10.0;
        for (EntityPlayer player : AnvilAura.mc.world.playerEntities) {
            double range = AnvilAura.mc.player.getDistance((Entity)player);
            if (range > (double)this.range.getValue().intValue() || !EntityUtil.isInHole((Entity)player) || !this.isValid(player) || !(range < shortestRange)) continue;
            target = player;
            shortestRange = range;
        }
        return target;
    }

    private boolean isValid(EntityPlayer player) {
        BlockPos pos = EntityUtil.getFlooredPos((Entity)player);
        if (AnvilAura.mc.world.getBlockState(pos.up(2)).getBlock() != Blocks.AIR || AnvilAura.mc.world.getBlockState(pos.up()).getBlock() != Blocks.AIR) {
            return false;
        }
        return AnvilAura.mc.world.getBlockState(pos.down()).getBlock() == Blocks.AIR;
    }

    private void switchToSlot(int slot) {
        AnvilAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
        AnvilAura.mc.player.inventory.currentItem = slot;
        AnvilAura.mc.playerController.updateController();
    }
}

