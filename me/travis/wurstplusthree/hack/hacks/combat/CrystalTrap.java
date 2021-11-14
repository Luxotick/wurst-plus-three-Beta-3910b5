/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.hack.hacks.combat.CrystalAura;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.RotationUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

@Hack.Registration(name="Crystal Trap", description="Traps your enemy and proceeds to RAPE him WTF?!?!?!", category=Hack.Category.COMBAT, priority=HackPriority.Highest)
public class CrystalTrap
extends Hack {
    DoubleSetting range = new DoubleSetting("Range", (Double)4.5, (Double)0.0, (Double)7.0, this);
    EnumSetting structure = new EnumSetting("Structure", "Minimum", Arrays.asList("Minimum", "Trap", "NoStep"), this);
    EnumSetting swing = new EnumSetting("Swing", "Mainhand", Arrays.asList("Mainhand", "Offhand", "None"), this);
    EnumSetting breakMode = new EnumSetting("BreakMode", "Packet", Arrays.asList("Sequential", "Normal", "Packet"), this);
    BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)false, (Hack)this);
    ColourSetting color = new ColourSetting("Render", new Colour(0, 0, 0, 255), (Hack)this);
    IntSetting breakdelay = new IntSetting("BreakDelay", 0, 0, 10, this);
    BooleanSetting stopCA = new BooleanSetting("StopCa", (Boolean)false, (Hack)this);
    private int offsetStep = 0;
    private int obsidianslot;
    private int pickslot;
    private boolean sendPacket;
    private int delayCounter = 0;
    private boolean stoppedCa;
    private int tickCounter = 0;
    private step currentStep;
    private BlockPos breakBlock;
    private boolean firstPacket = true;
    Entity player;
    EntityEnderCrystal crystal;
    private final Vec3d[] offsetsMinimum = new Vec3d[]{new Vec3d(1.0, 2.0, 0.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0)};
    private final Vec3d[] offsetsTrap = new Vec3d[]{new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0)};
    private final Vec3d[] offsetsNoStep = new Vec3d[]{new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0)};

    @Override
    public void onEnable() {
        if (this.nullCheck()) {
            this.disable();
            return;
        }
        if (this.stopCA.getValue().booleanValue() && CrystalAura.INSTANCE.isEnabled()) {
            this.stoppedCa = true;
            CrystalAura.INSTANCE.disable();
        }
        this.sendPacket = false;
        this.crystal = null;
        this.breakBlock = null;
        this.firstPacket = true;
        this.player = null;
        this.currentStep = step.Trapping;
        this.player = this.findClosestTarget();
    }

    @Override
    public void onDisable() {
        if (this.stoppedCa) {
            CrystalAura.INSTANCE.enable();
            this.stoppedCa = false;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void onUpdate() {
        if (this.player == null) {
            return;
        }
        this.check();
        switch (1.$SwitchMap$me$travis$wurstplusthree$hack$hacks$combat$CrystalTrap$step[this.currentStep.ordinal()]) {
            case 1: {
                place_targets = new ArrayList<E>();
                if (this.structure.is("Minimum")) {
                    Collections.addAll(place_targets, this.offsetsMinimum);
                }
                if (this.structure.is("Trap")) {
                    Collections.addAll(place_targets, this.offsetsTrap);
                }
                if (this.structure.is("NoStep")) {
                    Collections.addAll(place_targets, this.offsetsNoStep);
                }
                if (this.offsetStep >= place_targets.size()) {
                    this.offsetStep = 0;
                    return;
                }
                foundblock = false;
                while (!foundblock && this.offsetStep <= place_targets.size() - 1) {
                    offset_pos = new BlockPos((Vec3d)place_targets.get(this.offsetStep));
                    target_pos = new BlockPos(this.player.getPositionVector()).down().add(offset_pos.getX(), offset_pos.getY(), offset_pos.getZ());
                    if (CrystalTrap.mc.world.getBlockState(target_pos).getMaterial().isReplaceable()) {
                        foundblock = true;
                    }
                    for (Entity entity : CrystalTrap.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(target_pos))) {
                        if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                        foundblock = false;
                    }
                    if (foundblock) {
                        BlockUtil.placeBlock(target_pos, this.obsidianslot, (boolean)this.rotate.getValue(), (boolean)this.rotate.getValue(), this.swing);
                    }
                    ++this.offsetStep;
                }
                if (this.breakBlock == null) return;
                if (CrystalTrap.mc.world.getBlockState(new BlockPos((Vec3i)this.breakBlock)).getBlock() instanceof BlockObsidian == false) return;
                this.currentStep = step.Breaking;
                this.sendPacket = false;
                this.tickCounter = 0;
                return;
            }
            case 2: {
                rotated = false;
                if (this.crystal == null && this.tickCounter > 42 || this.breakMode.is("Sequential")) {
                    if (this.rotate.getValue().booleanValue()) {
                        RotationUtil.faceVector(new Vec3d((Vec3i)this.breakBlock), true);
                        rotated = true;
                    }
                    BlockUtil.placeCrystalOnBlock(this.breakBlock, EnumHand.OFF_HAND, true);
                }
                if (!(CrystalTrap.mc.world.getBlockState(this.breakBlock).getBlock() instanceof BlockAir)) ** GOTO lbl50
                this.currentStep = step.Explode;
                ** GOTO lbl87
lbl50:
                // 1 sources

                InventoryUtil.switchToHotbarSlot(this.pickslot, false);
                if (this.rotate.getValue().booleanValue() && !rotated) {
                    RotationUtil.faceVector(new Vec3d((Vec3i)this.breakBlock), true);
                }
                direction = BlockUtil.getPlaceableSide(this.breakBlock);
                var5_8 = this.breakMode.getValue();
                var6_10 = -1;
                switch (var5_8.hashCode()) {
                    case -1911998296: {
                        if (!var5_8.equals("Packet")) break;
                        var6_10 = 0;
                        break;
                    }
                    case -1955878649: {
                        if (!var5_8.equals("Normal")) break;
                        var6_10 = 1;
                        break;
                    }
                    case 1829453087: {
                        if (!var5_8.equals("Sequential")) break;
                        var6_10 = 2;
                        break;
                    }
                }
                switch (var6_10) {
                    case 0: {
                        if (!this.sendPacket) {
                            CrystalTrap.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.breakBlock, direction));
                            CrystalTrap.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakBlock, direction));
                            this.sendPacket = true;
                            return;
                        }
                    }
                    case 1: {
                        CrystalTrap.mc.player.swingArm(EnumHand.MAIN_HAND);
                        CrystalTrap.mc.playerController.onPlayerDamageBlock(this.breakBlock, direction);
                        return;
                    }
                    case 2: {
                        if (this.firstPacket) {
                            CrystalTrap.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.breakBlock, direction));
                            this.firstPacket = false;
                        }
                        CrystalTrap.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakBlock, direction));
                        break;
                    }
                }
            }
lbl87:
            // 4 sources

            case 3: {
                if (!(CrystalTrap.mc.world.getBlockState(this.breakBlock).getBlock() instanceof BlockAir)) {
                    this.currentStep = step.Breaking;
                    return;
                }
                if (this.crystal != null) {
                    if (this.crystal.isDead) {
                        this.crystal = null;
                        this.currentStep = step.Trapping;
                        return;
                    }
                    if (this.delayCounter < this.breakdelay.getValue() - 1) return;
                    if (this.rotate.getValue().booleanValue()) {
                        RotationUtil.faceVector(this.crystal.getPositionVector(), true);
                    }
                    EntityUtil.attackEntity((Entity)this.crystal, true, true);
                    this.delayCounter = 0;
                    return;
                }
                this.currentStep = step.Trapping;
                return;
            }
        }
    }

    private EntityEnderCrystal getCrystal() {
        if (this.breakBlock != null) {
            for (Entity crystal : CrystalTrap.mc.world.loadedEntityList) {
                if (!(crystal instanceof EntityEnderCrystal) || crystal.isDead || !(crystal.getDistance((double)this.breakBlock.getX(), (double)this.breakBlock.getY(), (double)this.breakBlock.getZ()) <= 2.0)) continue;
                return (EntityEnderCrystal)crystal;
            }
        }
        return null;
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public final void onPacketSend(@NotNull PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayerDigging && this.breakMode.is("Sequential")) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                event.setCancelled(true);
            }
            if (packet.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK && !this.firstPacket) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.breakBlock != null) {
            RenderUtil.drawBoxESP(this.breakBlock, this.color.getColor(), this.color.getColor(), 2.0f, false, true, false);
        }
    }

    private void check() {
        ++this.delayCounter;
        ++this.tickCounter;
        if (this.player == null) {
            ClientMessage.sendMessage("No target lol");
            this.disable();
            return;
        }
        if (this.player.isDead) {
            ClientMessage.sendMessage("We fucking Killed him EZZZZZZZZZZZ");
            this.disable();
            return;
        }
        if (!EntityUtil.isInHole(this.player)) {
            ClientMessage.sendMessage("lmfaoo your target ran out of the hole");
            this.disable();
            return;
        }
        if ((double)CrystalTrap.mc.player.getDistance(this.player) >= this.range.getValue()) {
            ClientMessage.sendMessage("Enemy out of range!?");
            this.disable();
            return;
        }
        if (!(CrystalTrap.mc.world.getBlockState(this.player.getPosition().add(0, 3, 0)).getBlock() instanceof BlockAir && CrystalTrap.mc.world.getBlockState(this.player.getPosition().add(0, 4, 0)).getBlock() instanceof BlockAir && CrystalTrap.mc.world.getBlockState(this.player.getPosition().add(0, 5, 0)).getBlock() instanceof BlockAir)) {
            ClientMessage.sendMessage("Oh shit no space here");
            this.disable();
            return;
        }
        this.crystal = this.getCrystal();
        if (!(CrystalTrap.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal)) {
            ClientMessage.sendMessage("Make sure to have crystals in your offhand!!");
            this.disable();
            return;
        }
        this.obsidianslot = CrystalTrap.findObiInHotbar();
        if (this.obsidianslot == -1) {
            ClientMessage.sendMessage("No obsidian found in your hotbar (Kinda bruh moment ngl)");
            this.disable();
            return;
        }
        this.pickslot = CrystalTrap.findPickInHotbar();
        if (this.pickslot == -1) {
            ClientMessage.sendMessage("No pickaxe found what was your plan in the first place lol");
            this.disable();
            return;
        }
        this.breakBlock = new BlockPos(this.player.posX, this.player.posY + 2.0, this.player.posZ);
        if (!(CrystalTrap.mc.world.getBlockState(this.breakBlock).getBlock() instanceof BlockAir) && !(CrystalTrap.mc.world.getBlockState(this.breakBlock).getBlock() instanceof BlockObsidian)) {
            ClientMessage.sendMessage("Something is wrong with the break position try reanabling the module");
            this.disable();
            return;
        }
    }

    public EntityPlayer findClosestTarget() {
        if (CrystalTrap.mc.world.playerEntities.isEmpty()) {
            return null;
        }
        EntityPlayer closestTarget = null;
        for (EntityPlayer target : CrystalTrap.mc.world.playerEntities) {
            if (target == CrystalTrap.mc.player || !target.isEntityAlive() || target.getPositionVector() == CrystalTrap.mc.player.getPositionVector() || !EntityUtil.isInHole((Entity)target) || WurstplusThree.FRIEND_MANAGER.isFriend(target.getName()) || !(CrystalTrap.mc.world.getBlockState(target.getPosition().add(0, 3, 0)).getBlock() instanceof BlockAir) || !(CrystalTrap.mc.world.getBlockState(target.getPosition().add(0, 4, 0)).getBlock() instanceof BlockAir) || !(CrystalTrap.mc.world.getBlockState(target.getPosition().add(0, 5, 0)).getBlock() instanceof BlockAir) || (double)CrystalTrap.mc.player.getDistance((Entity)target) >= this.range.getValue() || target.getHealth() <= 0.0f || closestTarget != null && CrystalTrap.mc.player.getDistance((Entity)target) > CrystalTrap.mc.player.getDistance((Entity)closestTarget)) continue;
            closestTarget = target;
        }
        if (closestTarget == null) {
            this.disable();
            ClientMessage.sendMessage("No target disabling...");
        }
        return closestTarget;
    }

    public static int findObiInHotbar() {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = CrystalTrap.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock) || !((block = ((ItemBlock)stack.getItem()).getBlock()) instanceof BlockObsidian)) continue;
            return i;
        }
        return -1;
    }

    public static int findPickInHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (!(CrystalTrap.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemPickaxe)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public String getDisplayInfo() {
        return this.currentStep.toString();
    }

    public static enum step {
        Trapping,
        Breaking,
        Explode;

    }
}

