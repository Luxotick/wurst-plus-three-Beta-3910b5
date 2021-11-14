/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.BlockEvent;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.setting.type.KeySetting;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.RotationUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import me.travis.wurstplusthree.util.elements.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Hack.Registration(name="Speed Mine", description="break shit fast idfk", category=Hack.Category.MISC, priority=HackPriority.Highest)
public final class SpeedMine
extends Hack {
    private final ParentSetting packetMine = new ParentSetting("Packet Mine", this);
    private final BooleanSetting rangeCheck = new BooleanSetting("RangeCheck", (Boolean)true, this.packetMine);
    private final DoubleSetting range = new DoubleSetting("Range", (Double)12.0, (Double)1.0, (Double)60.0, this.packetMine, s -> this.rangeCheck.getValue());
    private final BooleanSetting swing = new BooleanSetting("Swing", (Boolean)true, this.packetMine);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)false, this.packetMine);
    private final EnumSetting rotateSetting = new EnumSetting("RotateSettings", "Break", Arrays.asList("Break", "Constant", "Hit"), this.packetMine, s -> this.rotate.getValue());
    private final BooleanSetting cancel = new BooleanSetting("CancelEvent", (Boolean)true, this.packetMine);
    private final BooleanSetting packetLoop = new BooleanSetting("PacketLoop", (Boolean)false, this.packetMine);
    private final IntSetting packets = new IntSetting("Packets", 1, 1, 25, this.packetMine, s -> this.packetLoop.getValue());
    private final BooleanSetting abortPacket = new BooleanSetting("AbortPacket", (Boolean)true, this.packetMine);
    private final BooleanSetting correctHit = new BooleanSetting("Correction Hit", (Boolean)true, this.packetMine);
    private final IntSetting tickSub = new IntSetting("Tick Sub", 10, 1, 20, this.packetMine, s -> this.rangeCheck.getValue() != false || this.correctHit.getValue() != false);
    private final BooleanSetting shouldLoop = new BooleanSetting("Should Loop", (Boolean)false, this.packetMine, s -> this.rangeCheck.getValue() != false || this.correctHit.getValue() != false);
    private final ParentSetting switch0 = new ParentSetting("Switch", this);
    private final EnumSetting switchMode = new EnumSetting("SwitchMode", "None", Arrays.asList("None", "Silent", "Normal"), this.switch0);
    private final BooleanSetting keyMode = new BooleanSetting("KeyOnly", (Boolean)false, this.switch0);
    private final KeySetting key = new KeySetting("SwitchKey", 0, this.switch0, v -> this.keyMode.getValue());
    private final BooleanSetting switchBack = new BooleanSetting("SwitchBack", (Boolean)false, this.switch0, v -> !this.switchMode.is("None"));
    private final BooleanSetting noDesync = new BooleanSetting("NoDesync", (Boolean)false, this.switch0, v -> this.switchMode.is("Silent"));
    private final ParentSetting parentInstant = new ParentSetting("Instant", this);
    private final BooleanSetting instant = new BooleanSetting("Instant Mine", (Boolean)false, this.parentInstant);
    private final IntSetting instantPacketLoop = new IntSetting("InstantPackets", 2, 2, 25, this.parentInstant, s -> this.instant.getValue());
    private final IntSetting instantDelay = new IntSetting("InstantDelay", 0, 0, 120, this.parentInstant, s -> this.instant.getValue());
    private final ParentSetting parentCombat = new ParentSetting("Combat", this);
    private final BooleanSetting packetCity = new BooleanSetting("Packet City", (Boolean)false, this.parentCombat);
    private final BooleanSetting packetBurrow = new BooleanSetting("Packet Burrow", (Boolean)false, this.parentCombat);
    private final IntSetting cityRange = new IntSetting("Combat Range", 5, 1, 15, this.parentCombat, s -> this.packetCity.getValue() != false || this.packetBurrow.getValue() != false);
    private final ParentSetting parentRender = new ParentSetting("Render", this);
    private final BooleanSetting render = new BooleanSetting("Render", (Boolean)true, this.parentRender);
    private final EnumSetting renderMode = new EnumSetting("Mode", "Both", Arrays.asList("Both", "Outline", "Fill", "Gradient"), this.parentRender, s -> this.render.getValue());
    private final EnumSetting fillMode = new EnumSetting("Animation", "Expand", Arrays.asList("Expand", "Fill", "Static"), this.parentRender, s -> this.render.getValue());
    private final ColourSetting instantColor = new ColourSetting("InstantColorOutline", new Colour(100, 0, 100), this.parentRender, s -> this.render.getValue() != false && this.instant.getValue() != false);
    private final ColourSetting instantColor0 = new ColourSetting("InstantColorOutlineTop", new Colour(10, 0, 100, 255), this.parentRender, s -> this.render.getValue() != false && this.instant.getValue() != false && this.renderMode.is("Gradient"));
    private final ColourSetting instantColorFill = new ColourSetting("InstantColorFill", new Colour(100, 0, 100, 40), this.parentRender, s -> this.render.getValue() != false && this.instant.getValue() != false);
    private final ColourSetting instantColorFill0 = new ColourSetting("InstantColorFillTop", new Colour(10, 0, 100, 40), this.parentRender, s -> this.render.getValue() != false && this.instant.getValue() != false && this.renderMode.is("Gradient"));
    private final ColourSetting breakColor = new ColourSetting("BreakColorOutline", new Colour(255, 0, 0), this.parentRender, s -> this.render.getValue());
    private final ColourSetting breakColor0 = new ColourSetting("BreakColorOutlineTop", new Colour(10, 0, 0, 255), this.parentRender, s -> this.render.getValue() != false && this.renderMode.is("Gradient"));
    private final ColourSetting doneColor = new ColourSetting("FinishedColorOutline", new Colour(0, 255, 0), this.parentRender, s -> this.render.getValue());
    private final ColourSetting doneColor0 = new ColourSetting("FinishedColorOutlineTop", new Colour(0, 10, 0, 255), this.parentRender, s -> this.render.getValue() != false && this.renderMode.is("Gradient"));
    private final ColourSetting breakColorFill = new ColourSetting("BreakColorFill", new Colour(255, 0, 0, 40), this.parentRender, s -> this.render.getValue());
    private final ColourSetting breakColorFill0 = new ColourSetting("BreakColorFillTop", new Colour(10, 0, 0, 40), this.parentRender, s -> this.render.getValue() != false && this.renderMode.is("Gradient"));
    private final ColourSetting doneColorFill = new ColourSetting("FinishedColorFill", new Colour(0, 255, 0, 40), this.parentRender, s -> this.render.getValue());
    private final ColourSetting doneColorFill0 = new ColourSetting("FinishedColorFillTop", new Colour(0, 10, 0, 40), this.parentRender, s -> this.render.getValue() != false && this.renderMode.is("Gradient"));
    private boolean isActive;
    private EnumFacing lastFace;
    private BlockPos lastPos;
    private BlockPos lastBreakPos;
    private EnumFacing lastBreakFace;
    private Block lastBlock;
    private double time = 0.0;
    private int tickCount = 0;
    private int switchedSlot;
    private boolean shouldInstant;
    private boolean firstPacket;
    private int delay;
    private int oldSlot = -1;
    private boolean loopStopPackets;

    @Override
    public void onEnable() {
        this.switchedSlot = -1;
        this.shouldInstant = false;
        this.firstPacket = true;
        this.delay = 0;
        this.oldSlot = -1;
        this.loopStopPackets = true;
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public final void onBlockDamage(@NotNull BlockEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (event.getStage() == 3 && SpeedMine.mc.playerController.curBlockDamageMP > 0.1f) {
            SpeedMine.mc.playerController.isHittingBlock = true;
        }
        if (event.getStage() == 4) {
            if (!this.canBreakBlockFromPos(event.pos)) {
                return;
            }
            if (event.pos != this.lastBreakPos) {
                this.shouldInstant = false;
            }
            if (this.swing.getValue().booleanValue()) {
                SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            if (event.pos != this.lastPos && this.correctHit.getValue().booleanValue() && this.lastPos != null) {
                this.isActive = false;
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFace));
                SpeedMine.mc.playerController.isHittingBlock = false;
                SpeedMine.mc.playerController.curBlockDamageMP = 0.0f;
            }
            if (!this.isActive) {
                event.setCancelled(this.cancel.getValue());
                this.setPacketPos(event.pos, event.facing);
            }
        }
    }

    @Override
    public void onUpdate() {
        double dis;
        if (this.nullCheck()) {
            return;
        }
        if (!this.isActive && (this.packetBurrow.getValue().booleanValue() || this.packetCity.getValue().booleanValue())) {
            if (this.packetCity.getValue().booleanValue()) {
                ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>> cityPos = PlayerUtil.GetPlayersReadyToBeCitied();
                BlockPos toCity = null;
                Iterator<Pair<EntityPlayer, ArrayList<BlockPos>>> iterator = cityPos.iterator();
                while (iterator.hasNext()) {
                    Pair<EntityPlayer, ArrayList<BlockPos>> p = iterator.next();
                    if (p.getKey() == SpeedMine.mc.player || SpeedMine.mc.player.getDistance((Entity)p.getKey()) > (float)this.cityRange.getValue().intValue()) continue;
                    for (BlockPos pos : p.getValue()) {
                        if (toCity == null) {
                            toCity = pos;
                            continue;
                        }
                        if (!(SpeedMine.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) < SpeedMine.mc.player.getDistance((double)toCity.getX(), (double)toCity.getY(), (double)toCity.getZ()))) continue;
                        toCity = pos;
                    }
                }
                if (toCity != null) {
                    this.setPacketPos(toCity, BlockUtil.getPlaceableSide(toCity));
                }
            }
            if (this.packetBurrow.getValue().booleanValue() && !this.isActive) {
                for (EntityPlayer entity : SpeedMine.mc.world.playerEntities.stream().filter(entityPlayer -> !WurstplusThree.FRIEND_MANAGER.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
                    if (entity == SpeedMine.mc.player || SpeedMine.mc.player.getDistance((Entity)entity) > (float)this.cityRange.getValue().intValue() || !this.isBurrowed(entity)) continue;
                    BlockPos burrowPos = new BlockPos(Math.floor(entity.posX), Math.floor(entity.posY), Math.floor(entity.posZ));
                    this.setPacketPos(burrowPos, BlockUtil.getPlaceableSide(burrowPos));
                }
            }
        }
        if (this.instant.getValue().booleanValue() && this.shouldInstant && !this.isActive && this.delay >= this.instantDelay.getValue()) {
            this.delay = 0;
            if (this.firstPacket) {
                this.firstPacket = false;
                for (int i = 0; i < this.instantPacketLoop.getValue(); ++i) {
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.lastBreakPos, this.lastBreakFace));
                }
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.lastBreakPos, this.lastBreakFace));
                if (this.abortPacket.getValue().booleanValue()) {
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastBreakPos, this.lastBreakFace));
                }
            } else if (SpeedMine.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE && SpeedMine.mc.world.getBlockState(this.lastBreakPos).getBlock() != Blocks.AIR) {
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.lastBreakPos, this.lastBreakFace));
            }
        }
        ++this.delay;
        if (this.shouldInstant && this.rangeCheck.getValue().booleanValue() && this.lastBreakPos != null) {
            double dis2 = SpeedMine.mc.player.getDistanceSq(this.lastBreakPos);
            this.shouldInstant = !(dis2 > this.range.getValue());
        }
        int subVal = 40;
        if (this.lastBlock == Blocks.OBSIDIAN && PlayerUtil.getBestItem(this.lastBlock) != null) {
            subVal = 146;
        } else if (this.lastBlock == Blocks.ENDER_CHEST && PlayerUtil.getBestItem(this.lastBlock) != null) {
            subVal = 66;
        }
        if (this.lastPos != null && this.lastBlock != null && this.isActive) {
            if ((this.rangeCheck.getValue().booleanValue() || this.correctHit.getValue().booleanValue()) && (double)this.tickCount > this.time - (double)subVal - (double)this.tickSub.getValue().intValue() && this.loopStopPackets) {
                if (this.packetLoop.getValue().booleanValue()) {
                    for (int i = 0; i < this.packets.getValue(); ++i) {
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.lastPos, this.lastFace));
                    }
                } else {
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.lastPos, this.lastFace));
                }
                this.loopStopPackets = this.shouldLoop.getValue();
            }
            if (this.rotate.getValue().booleanValue() && this.rotateSetting.is("Constant")) {
                RotationUtil.rotateHead(this.lastPos.getX(), this.lastPos.getY(), this.lastPos.getZ(), (EntityPlayer)SpeedMine.mc.player);
            }
            if (this.abortPacket.getValue().booleanValue()) {
                if (this.packetLoop.getValue().booleanValue()) {
                    for (int i = 0; i < this.packets.getValue(); ++i) {
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFace));
                    }
                } else {
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFace));
                }
                this.isActive = true;
            }
            SpeedMine.mc.playerController.blockHitDelay = 0;
            if (SpeedMine.mc.world.getBlockState(this.lastPos).getBlock() != this.lastBlock) {
                if (this.rotate.getValue().booleanValue() && this.rotateSetting.is("Break")) {
                    RotationUtil.rotateHead(this.lastPos.getX(), this.lastPos.getY(), this.lastPos.getZ(), (EntityPlayer)SpeedMine.mc.player);
                }
                this.shouldInstant = true;
                this.isActive = false;
                this.lastBreakPos = this.lastPos;
                this.lastBreakFace = this.lastFace;
                this.lastPos = null;
                this.lastFace = null;
                this.lastBlock = null;
            }
        }
        if (!this.switchMode.is("None") && this.switchedSlot == -1 && this.isActive && this.lastPos != null && (double)this.tickCount > this.time - (double)subVal && (!this.keyMode.getValue().booleanValue() || this.key.isDown())) {
            int slot = this.findBestTool(this.lastBlock.getBlockState().getBaseState());
            if (slot == -1) {
                return;
            }
            this.oldSlot = SpeedMine.mc.player.inventory.currentItem;
            if (this.switchMode.is("Silent")) {
                if (!(this.noDesync.getValue().booleanValue() && SpeedMine.mc.player.getHeldItemMainhand().getItem() instanceof ItemFood && SpeedMine.mc.gameSettings.keyBindUseItem.isKeyDown())) {
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
                    this.switchedSlot = slot;
                }
            } else {
                SpeedMine.mc.player.inventory.currentItem = slot;
                SpeedMine.mc.playerController.syncCurrentPlayItem();
                this.switchedSlot = slot;
            }
        }
        if (this.switchBack.getValue().booleanValue() && this.switchedSlot != -1 && (!this.isActive || this.keyMode.getValue().booleanValue() && !this.key.isDown())) {
            if (this.switchMode.is("Silent")) {
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(SpeedMine.mc.player.inventory.currentItem));
            } else if (SpeedMine.mc.player.inventory.currentItem == this.switchedSlot) {
                SpeedMine.mc.player.inventory.currentItem = this.oldSlot;
                SpeedMine.mc.playerController.syncCurrentPlayItem();
            }
            this.switchedSlot = -1;
        }
        if (this.isActive && this.rangeCheck.getValue().booleanValue() && Math.sqrt(dis = SpeedMine.mc.player.getDistanceSq(this.lastPos)) > this.range.getValue()) {
            SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFace));
            SpeedMine.mc.playerController.isHittingBlock = false;
            this.isActive = false;
            this.shouldInstant = false;
            this.firstPacket = true;
            this.lastPos = null;
            this.lastFace = null;
            this.lastBlock = null;
        }
        ++this.tickCount;
    }

    private int findBestTool(IBlockState blockState) {
        int bestSlot = -1;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            int eff;
            float f;
            ItemStack stack = SpeedMine.mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty) continue;
            float speed = stack.getDestroySpeed(blockState);
            if (!(f > 1.0f) || !((double)(speed = (float)((double)speed + ((eff = EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.EFFICIENCY, (ItemStack)stack)) > 0 ? Math.pow(eff, 2.0) + 1.0 : 0.0))) > max)) continue;
            max = speed;
            bestSlot = i;
        }
        return bestSlot;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        Colour c20;
        Colour c0;
        Colour c2;
        Colour c;
        if (!this.render.getValue().booleanValue()) {
            return;
        }
        if (this.isActive && this.lastPos != null) {
            c = this.breakColor.getValue();
            c2 = this.breakColorFill.getValue();
            c0 = this.breakColor0.getValue();
            c20 = this.breakColorFill0.getValue();
            int subVal = 40;
            if (this.lastBlock == Blocks.OBSIDIAN && PlayerUtil.getBestItem(this.lastBlock) != null) {
                subVal = 146;
            } else if (this.lastBlock == Blocks.ENDER_CHEST && PlayerUtil.getBestItem(this.lastBlock) != null) {
                subVal = 66;
            }
            if ((double)this.tickCount > this.time - (double)subVal) {
                c = this.doneColor.getValue();
                c2 = this.doneColorFill.getValue();
                c0 = this.doneColor0.getValue();
                c20 = this.doneColorFill0.getValue();
            }
            AxisAlignedBB bb = SpeedMine.mc.world.getBlockState(this.lastPos).getSelectedBoundingBox((World)SpeedMine.mc.world, this.lastPos);
            switch (this.fillMode.getValue()) {
                case "Expand": {
                    bb = bb.shrink(Math.max(Math.min(this.normalize(this.tickCount, this.time - (double)subVal, 0.0), 1.0), 0.0));
                    break;
                }
                case "Fill": {
                    bb = bb.setMaxY(bb.minY - 0.5 + Math.max(Math.min(this.normalize(this.tickCount * 2, this.time - (double)subVal, 0.0), 1.5), 0.0));
                    break;
                }
            }
            switch (this.renderMode.getValue()) {
                case "Both": {
                    RenderUtil.drawBBBox(bb, c2, c2.getAlpha());
                    RenderUtil.drawBlockOutlineBB(bb, new Color(c.getRed(), c.getGreen(), c.getBlue(), 255), 1.0f);
                    break;
                }
                case "Outline": {
                    RenderUtil.drawBlockOutlineBB(bb, c, 1.0f);
                    break;
                }
                case "Fill": {
                    RenderUtil.drawBBBox(bb, c2, c2.getAlpha());
                    break;
                }
                case "Gradient": {
                    Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
                    RenderUtil.drawGradientBlockOutline(bb.grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z), c0, c, 2.0f);
                    RenderUtil.drawOpenGradientBoxBB(bb, c2, c20, 0.0);
                }
            }
        }
        if (this.instant.getValue().booleanValue() && this.shouldInstant && this.lastBreakPos != null) {
            c = this.instantColor.getValue();
            c2 = this.instantColorFill.getValue();
            c0 = this.instantColor0.getValue();
            c20 = this.instantColorFill0.getValue();
            switch (this.renderMode.getValue()) {
                case "Both": {
                    RenderUtil.drawBlockOutline(this.lastBreakPos, new Color(c.getRed(), c.getGreen(), c.getBlue(), 255), 1.0f, true);
                    RenderUtil.drawBox(this.lastBreakPos, c2, true);
                    break;
                }
                case "Outline": {
                    RenderUtil.drawBlockOutline(this.lastBreakPos, c, 1.0f, true);
                    break;
                }
                case "Fill": {
                    RenderUtil.drawBox(this.lastBreakPos, c2, true);
                    break;
                }
                case "Gradient": {
                    RenderUtil.drawGradientBlockOutline(this.lastBreakPos, c0, c, 1.0f, 0.0);
                    RenderUtil.drawOpenGradientBox(this.lastBreakPos, c2, c20, 0.0);
                }
            }
        }
    }

    private boolean canBreakBlockFromPos(@NotNull BlockPos p) {
        IBlockState stateInterface = SpeedMine.mc.world.getBlockState(p);
        Block block = stateInterface.getBlock();
        return block.getBlockHardness(stateInterface, (World)SpeedMine.mc.world, p) != -1.0f;
    }

    private double normalize(double value, double max, double min) {
        return 0.5 * ((value - min) / (max - min)) + 0.5;
    }

    public void setPacketPos(BlockPos pos, EnumFacing facing) {
        if (!this.canBreakBlockFromPos(pos)) {
            return;
        }
        if (pos != this.lastBreakPos) {
            this.shouldInstant = false;
        }
        if (this.packetLoop.getValue().booleanValue()) {
            for (int i = 0; i < this.packets.getValue(); ++i) {
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
                if (this.rangeCheck.getValue().booleanValue() || this.correctHit.getValue().booleanValue()) continue;
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
            }
        } else {
            SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
            if (!this.rangeCheck.getValue().booleanValue() && !this.correctHit.getValue().booleanValue()) {
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
            }
        }
        this.isActive = true;
        this.lastFace = facing;
        this.lastPos = pos;
        this.lastBreakPos = pos;
        this.lastBreakFace = facing;
        this.firstPacket = true;
        this.switchedSlot = -1;
        this.loopStopPackets = true;
        this.lastBlock = SpeedMine.mc.world.getBlockState(this.lastPos).getBlock();
        ItemStack item = PlayerUtil.getItemStackFromItem(PlayerUtil.getBestItem(this.lastBlock)) != null ? PlayerUtil.getItemStackFromItem(PlayerUtil.getBestItem(this.lastBlock)) : SpeedMine.mc.player.getHeldItem(EnumHand.MAIN_HAND);
        if (item == null) {
            return;
        }
        this.time = BlockUtil.getMineTime(this.lastBlock, item);
        this.tickCount = 0;
        if (this.rotate.getValue().booleanValue() && this.rotateSetting.is("Hit")) {
            RotationUtil.rotateHead(this.lastPos.getX(), this.lastPos.getY(), this.lastPos.getZ(), (EntityPlayer)SpeedMine.mc.player);
        }
    }

    private boolean isBurrowed(EntityPlayer player) {
        BlockPos pos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY + 0.2), Math.floor(player.posZ));
        return SpeedMine.mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST || SpeedMine.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || SpeedMine.mc.world.getBlockState(pos).getBlock() == Blocks.CHEST;
    }
}

