/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockHopper
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.client.gui.GuiHopper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.ContainerHopper
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketCloseWindow
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import me.travis.wurstplusthree.event.events.PacketEvent;
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
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.MathsUtil;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Hack.Registration(name="Auto 32K", description="places some blocks and pulls out a sword", category=Hack.Category.COMBAT, priority=HackPriority.High)
public class Auto32k
extends Hack {
    private BlockPos pos;
    private BlockPos hopperPos;
    private BlockPos redstonePos;
    private int hopperSlot;
    private int redstoneSlot;
    private int shulkerSlot;
    private int ticksPast;
    private boolean failed;
    private boolean didHopper;
    private int offsetStep;
    CPacketCloseWindow packet;
    private int[] rot;
    private boolean setup;
    EnumSetting placeMode = new EnumSetting("Mode", "Dispenser", Arrays.asList("Dispenser", "Hopper"), this);
    IntSetting delay = new IntSetting("Delay", 4, 0, 10, this);
    BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)false, (Hack)this);
    BooleanSetting encase = new BooleanSetting("Encase", (Boolean)false, (Hack)this);
    EnumSetting swing = new EnumSetting("Swing", "Mainhand", Arrays.asList("Mainhand", "Offhand", "None"), this);
    IntSetting slot = new IntSetting("Slot", 0, 0, 9, this);
    DoubleSetting hopperRange = new DoubleSetting("HopperRange", (Double)6.0, (Double)0.0, (Double)10.0, this);
    ParentSetting parentSetting = new ParentSetting("Gui", this);
    BooleanSetting secretClose = new BooleanSetting("PacketClose", (Boolean)false, this.parentSetting);
    BooleanSetting closeGui = new BooleanSetting("CloseGui", (Boolean)false, this.parentSetting, v -> this.secretClose.getValue());
    ColourSetting color = new ColourSetting("RenderColor", new Colour(0, 0, 200, 200), (Hack)this);
    private final Vec3d[] offsetsHopper = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 1.0, 0.0), new Vec3d(0.0, -1.0, 0.0)};

    @Override
    public void onEnable() {
        this.ticksPast = 0;
        this.setup = false;
        this.failed = true;
        this.packet = null;
        this.hopperPos = null;
        this.didHopper = false;
        this.offsetStep = 0;
        this.hopperSlot = -1;
        int dispenserSlot = -1;
        this.redstoneSlot = -1;
        this.shulkerSlot = -1;
        int blockSlot = -1;
        for (int i = 0; i < 9; ++i) {
            Item item = Auto32k.mc.player.inventory.getStackInSlot(i).getItem();
            if (item == Item.getItemFromBlock((Block)Blocks.HOPPER)) {
                this.hopperSlot = i;
                continue;
            }
            if (item == Item.getItemFromBlock((Block)Blocks.DISPENSER)) {
                dispenserSlot = i;
                continue;
            }
            if (item == Item.getItemFromBlock((Block)Blocks.REDSTONE_BLOCK)) {
                this.redstoneSlot = i;
                continue;
            }
            if (item instanceof ItemShulkerBox) {
                this.shulkerSlot = i;
                continue;
            }
            if (!(item instanceof ItemBlock)) continue;
            blockSlot = i;
        }
        if (!(this.hopperSlot != -1 && dispenserSlot != -1 && this.redstoneSlot != -1 && this.shulkerSlot != -1 && blockSlot != -1 || this.placeMode.getValue().equals("Hopper"))) {
            ClientMessage.sendErrorMessage("missing item");
            this.disable();
            return;
        }
        if (this.hopperSlot == -1 || this.shulkerSlot == -1) {
            ClientMessage.sendErrorMessage("missing item");
            this.disable();
            return;
        }
        if (this.placeMode.getValue().equals("Dispenser")) {
            for (int x = -2; x <= 2; ++x) {
                for (int y = -1; y <= 3; ++y) {
                    for (int z = -2; z <= 2; ++z) {
                        int[] arrn;
                        if (Math.abs(x) > Math.abs(z)) {
                            if (x > 0) {
                                int[] arrn2 = new int[2];
                                arrn2[0] = -1;
                                arrn = arrn2;
                                arrn2[1] = 0;
                            } else {
                                int[] arrn3 = new int[2];
                                arrn3[0] = 1;
                                arrn = arrn3;
                                arrn3[1] = 0;
                            }
                        } else if (z > 0) {
                            int[] arrn4 = new int[2];
                            arrn4[0] = 0;
                            arrn = arrn4;
                            arrn4[1] = -1;
                        } else {
                            int[] arrn5 = new int[2];
                            arrn5[0] = 0;
                            arrn = arrn5;
                            arrn5[1] = 1;
                        }
                        this.rot = arrn;
                        this.pos = Auto32k.mc.player.getPosition().add(x, y, z);
                        this.redstonePos = null;
                        if (BlockUtil.isBlockEmpty(this.pos.add(1, 1, 0)) && this.pos.getX() > Auto32k.mc.player.getPosition().getX()) {
                            this.redstonePos = this.pos.add(1, 1, 0);
                        } else if (BlockUtil.isBlockEmpty(this.pos.add(-1, 1, 0)) && this.pos.getX() < Auto32k.mc.player.getPosition().getX()) {
                            this.redstonePos = this.pos.add(-1, 1, 0);
                        } else if (BlockUtil.isBlockEmpty(this.pos.add(0, 1, 1)) && this.pos.getZ() > Auto32k.mc.player.getPosition().getZ()) {
                            this.redstonePos = this.pos.add(0, 1, 1);
                        } else if (BlockUtil.isBlockEmpty(this.pos.add(0, 1, -1)) && this.pos.getZ() < Auto32k.mc.player.getPosition().getZ()) {
                            this.redstonePos = this.pos.add(0, 1, -1);
                        } else if (BlockUtil.isBlockEmpty(this.pos.add(0, 2, 0))) {
                            this.redstonePos = this.pos.add(0, 2, 0);
                        }
                        if (!(Auto32k.mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(Auto32k.mc.player.getPositionVector().add((double)((float)x - (float)this.rot[0] / 2.0f), (double)y + 0.5, (double)((float)z + (float)this.rot[1] / 2.0f))) <= 4.5) || !(Auto32k.mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(Auto32k.mc.player.getPositionVector().add((double)x + 0.5, (double)y + 2.5, (double)z + 0.5)) <= 4.5)) continue;
                        if (y > 0) {
                            if (!BlockUtil.isBlockEmpty(this.pos) || !BlockUtil.canPlaceBlock(this.pos.down()) || !BlockUtil.canPlaceBlock(this.pos.up())) continue;
                            this.redstonePos = null;
                            if (BlockUtil.isBlockEmpty(this.pos.add(1, 1, 0))) {
                                this.redstonePos = this.pos.add(1, 1, 0);
                            } else if (BlockUtil.isBlockEmpty(this.pos.add(-1, 1, 0))) {
                                this.redstonePos = this.pos.add(-1, 1, 0);
                            } else if (BlockUtil.isBlockEmpty(this.pos.add(0, 1, 1))) {
                                this.redstonePos = this.pos.add(0, 1, 1);
                            } else if (BlockUtil.isBlockEmpty(this.pos.add(0, 1, -1))) {
                                this.redstonePos = this.pos.add(0, 1, -1);
                            }
                            if (this.redstonePos == null) continue;
                            BlockUtil.rotatePacket((double)this.pos.add(-this.rot[0], 1, -this.rot[1]).getX() + 0.5, this.pos.getY() + 1, (double)this.pos.add(-this.rot[0], 1, -this.rot[1]).getZ() + 0.5);
                            BlockUtil.placeBlock(this.pos.up(), dispenserSlot, (boolean)this.rotate.getValue(), false, this.swing);
                            BlockUtil.openBlock(this.pos.up());
                            this.hopperPos = this.pos.down();
                            this.setup = true;
                            return;
                        }
                        if (!BlockUtil.isBlockEmpty(this.pos.add(this.rot[0], 0, this.rot[1])) || EntityUtil.isBothHole(this.pos.add(this.rot[0], 0, this.rot[1])) || !BlockUtil.isBlockEmpty(this.pos.add(0, 1, 0)) || this.redstonePos == null || !BlockUtil.isBlockEmpty(this.pos.add(this.rot[0], 1, this.rot[1])) || !BlockUtil.canPlaceBlock(this.pos) && BlockUtil.isBlockEmpty(this.pos)) continue;
                        this.hopperPos = this.pos.add(this.rot[0], 0, this.rot[1]);
                        if (BlockUtil.isBlockEmpty(this.pos) && !BlockUtil.canPlaceBlock(this.pos.up()) || !BlockUtil.canPlaceBlock(this.hopperPos)) {
                            BlockUtil.placeBlock(this.pos, blockSlot, (boolean)this.rotate.getValue(), false, this.swing);
                        }
                        BlockUtil.rotatePacket((double)this.pos.add(-this.rot[0], 1, -this.rot[1]).getX() + 0.5, this.pos.getY() + 1, (double)this.pos.add(-this.rot[0], 1, -this.rot[1]).getZ() + 0.5);
                        BlockUtil.placeBlock(this.pos.up(), dispenserSlot, (boolean)this.rotate.getValue(), false, this.swing);
                        BlockUtil.openBlock(this.pos.up());
                        this.setup = true;
                        return;
                    }
                }
            }
            ClientMessage.sendErrorMessage("unable to place");
            this.disable();
        } else {
            for (int z = -2; z <= 2; ++z) {
                for (int y = -1; y <= 2; ++y) {
                    for (int x = -2; x <= 2; ++x) {
                        if (z == 0 && y == 0 && x == 0 || z == 0 && y == 1 && x == 0 || !BlockUtil.isBlockEmpty(Auto32k.mc.player.getPosition().add(z, y, x)) || !(Auto32k.mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(Auto32k.mc.player.getPositionVector().add((double)z + 0.5, (double)y + 0.5, (double)x + 0.5)) < 4.5) || !BlockUtil.isBlockEmpty(Auto32k.mc.player.getPosition().add(z, y + 1, x)) || !(Auto32k.mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(Auto32k.mc.player.getPositionVector().add((double)z + 0.5, (double)y + 1.5, (double)x + 0.5)) < 4.5)) continue;
                        this.hopperPos = Auto32k.mc.player.getPosition().add(z, y, x);
                        BlockUtil.placeBlock(Auto32k.mc.player.getPosition().add(z, y, x), this.hopperSlot, (boolean)this.rotate.getValue(), false, this.swing);
                        BlockUtil.placeBlock(Auto32k.mc.player.getPosition().add(z, y + 1, x), this.shulkerSlot, (boolean)this.rotate.getValue(), false, this.swing);
                        BlockUtil.openBlock(Auto32k.mc.player.getPosition().add(z, y, x));
                        this.pos = Auto32k.mc.player.getPosition().add(z, y, x);
                        this.setup = true;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (this.packet != null) {
            Auto32k.mc.player.connection.sendPacket((Packet)new CPacketCloseWindow());
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.hopperPos != null && !this.failed) {
            RenderUtil.drawBoxESP(this.hopperPos, this.color.getColor(), this.color.getColor(), 2.0f, true, true, false);
            RenderUtil.drawCircle(this.hopperPos.getX(), this.hopperPos.getY(), this.hopperPos.getZ(), this.hopperRange.getValue().floatValue(), this.color.getValue());
        }
    }

    @Override
    public void onUpdate() {
        if (this.setup && this.ticksPast > this.delay.getValue()) {
            if (this.ticksPast > 50 && this.failed && !(Auto32k.mc.currentScreen instanceof GuiHopper)) {
                ClientMessage.sendErrorMessage("Failed disabling now");
                this.disable();
                return;
            }
            if (this.hopperPos != null) {
                if (Auto32k.mc.player.getDistanceSqToCenter(this.hopperPos) >= MathsUtil.square(this.hopperRange.getValue().floatValue())) {
                    ClientMessage.sendErrorMessage("Out of range disabling..");
                    this.disable();
                    return;
                }
                if (!(Auto32k.mc.world.getBlockState(this.hopperPos).getBlock() instanceof BlockHopper || this.failed || Auto32k.mc.currentScreen instanceof GuiHopper)) {
                    ClientMessage.sendErrorMessage("Hopper Got blown up xDD");
                    this.disable();
                    return;
                }
            }
            if (!this.placeMode.getValue().equals("Hopper") && !this.didHopper) {
                try {
                    Auto32k.mc.playerController.windowClick(Auto32k.mc.player.openContainer.windowId, 36 + this.shulkerSlot, 0, ClickType.QUICK_MOVE, (EntityPlayer)Auto32k.mc.player);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                BlockUtil.placeBlock(this.hopperPos, this.hopperSlot, (boolean)this.rotate.getValue(), false, this.swing);
                BlockUtil.openBlock(this.hopperPos);
                BlockUtil.placeBlock(this.redstonePos, this.redstoneSlot, (boolean)this.rotate.getValue(), false, this.swing);
                this.didHopper = true;
                return;
            }
            if (Auto32k.mc.currentScreen instanceof GuiHopper) {
                if (!this.isEnabled()) {
                    return;
                }
                if (Auto32k.mc.player.openContainer instanceof ContainerHopper) {
                    if (!EntityUtil.holding32k((EntityPlayer)Auto32k.mc.player)) {
                        int swordIndex = -1;
                        for (int i = 0; i < 5; ++i) {
                            if (!EntityUtil.is32k(((Slot)Auto32k.mc.player.openContainer.inventorySlots.get((int)0)).inventory.getStackInSlot(i))) continue;
                            swordIndex = i;
                            break;
                        }
                        if (swordIndex == -1) {
                            return;
                        }
                        if (this.slot.getValue() != 0) {
                            InventoryUtil.switchToHotbarSlot(this.slot.getValue() - 1, false);
                        } else if (!this.placeMode.getValue().equals("Hopper") && this.shulkerSlot > 35 && this.shulkerSlot != 45) {
                            InventoryUtil.switchToHotbarSlot(44 - this.shulkerSlot, false);
                        }
                        Auto32k.mc.playerController.windowClick(Auto32k.mc.player.openContainer.windowId, swordIndex, this.slot.getValue() == 0 ? Auto32k.mc.player.inventory.currentItem : this.slot.getValue() - 1, ClickType.SWAP, (EntityPlayer)Auto32k.mc.player);
                    } else if (this.closeGui.getValue().booleanValue() && this.secretClose.getValue().booleanValue()) {
                        Auto32k.mc.player.closeScreen();
                    }
                    this.failed = false;
                }
            }
        }
        if (this.didHopper && this.ticksPast > this.delay.getValue() * 2 && this.encase.getValue().booleanValue()) {
            ArrayList place_targets = new ArrayList();
            Collections.addAll(place_targets, this.offsetsHopper);
            if (this.offsetStep >= place_targets.size()) {
                this.offsetStep = 0;
            }
            boolean foundblock = false;
            while (!foundblock && this.offsetStep < place_targets.size()) {
                BlockPos offset_pos = new BlockPos((Vec3d)place_targets.get(this.offsetStep));
                BlockPos target_pos = new BlockPos((Vec3i)this.hopperPos.add(offset_pos.getX(), offset_pos.getY(), offset_pos.getZ()));
                if (Auto32k.mc.world.getBlockState(target_pos).getMaterial().isReplaceable()) {
                    foundblock = true;
                }
                for (Entity entity : Auto32k.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(target_pos))) {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                    foundblock = false;
                }
                if (foundblock) {
                    BlockUtil.placeBlock(target_pos, InventoryUtil.findHotbarBlock(BlockObsidian.class), (boolean)this.rotate.getValue(), (boolean)this.rotate.getValue(), this.swing);
                }
                ++this.offsetStep;
            }
        }
        ++this.ticksPast;
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public void onPacketSend(PacketEvent.Send event) {
        if (Auto32k.mc.currentScreen instanceof GuiHopper && event.getPacket() instanceof CPacketCloseWindow && this.secretClose.getValue().booleanValue()) {
            event.setCancelled(true);
            this.packet = (CPacketCloseWindow)event.getPacket();
        }
    }
}

