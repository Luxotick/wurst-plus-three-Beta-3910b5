/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.travis.wurstplusthree.event.events.UpdateWalkingPlayerEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.InventoryUtil;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Hack.Registration(name="Auto Web", description="webs ppl coz its crazy annoying", category=Hack.Category.COMBAT, priority=HackPriority.High)
public class AutoWeb
extends Hack {
    DoubleSetting range = new DoubleSetting("Range", (Double)5.0, (Double)1.0, (Double)8.0, this);
    BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)true, (Hack)this);
    IntSetting type = new IntSetting("Type", 3, 1, 3, this);
    IntSetting delayTick = new IntSetting("Delay", 1, 0, 10, this);
    BooleanSetting packet = new BooleanSetting("Packet", (Boolean)true, (Hack)this);
    BooleanSetting lowFeet = new BooleanSetting("LowFeet", (Boolean)false, (Hack)this);
    BooleanSetting legs = new BooleanSetting("Legs", (Boolean)true, (Hack)this);
    BooleanSetting chest = new BooleanSetting("Chest", (Boolean)true, (Hack)this, s -> this.legs.getValue());
    BooleanSetting head = new BooleanSetting("Head", (Boolean)false, (Hack)this, s -> this.legs.getValue() != false && this.chest.getValue() != false);
    EntityPlayer player;
    boolean r = false;
    int delay = 0;

    @Override
    public void onEnable() {
        if (this.nullCheck()) {
            return;
        }
        this.r = false;
        this.delay = 0;
    }

    @Override
    public void onTick() {
        if (this.type.getValue() == 3) {
            this.r = false;
            this.trap();
        }
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public void onWalingEvent(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.type.getValue() == 2) {
            this.r = this.rotate.getValue();
            this.trap();
        }
    }

    @Override
    public void onUpdate() {
        if (this.type.getValue() == 1) {
            this.r = false;
            this.trap();
        }
    }

    private void trap() {
        if (this.delay < this.delayTick.getValue()) {
            ++this.delay;
            return;
        }
        this.delay = 0;
        this.player = this.getTarget(this.range.getValue().floatValue(), false);
        List<Vec3d> placeTargets = this.getPos();
        if (placeTargets == null) {
            return;
        }
        this.placeList(placeTargets);
    }

    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoWeb.mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, range) || trapped && player.isInWeb) continue;
            if (target == null) {
                target = player;
                distance = AutoWeb.mc.player.getDistanceSq((Entity)player);
                continue;
            }
            if (!(AutoWeb.mc.player.getDistanceSq((Entity)player) < distance)) continue;
            target = player;
            distance = AutoWeb.mc.player.getDistanceSq((Entity)player);
        }
        return target;
    }

    private List<Vec3d> getPos() {
        ArrayList<Vec3d> list = new ArrayList<Vec3d>();
        if (this.player == null) {
            return null;
        }
        Vec3d baseVec = this.player.getPositionVector();
        if (this.lowFeet.getValue().booleanValue()) {
            list.add(baseVec.add(0.0, -1.0, 0.0));
        }
        if (this.legs.getValue().booleanValue()) {
            list.add(baseVec);
        }
        if (this.chest.getValue().booleanValue()) {
            list.add(baseVec.add(0.0, 1.0, 0.0));
        }
        if (this.head.getValue().booleanValue()) {
            list.add(baseVec.add(0.0, 2.0, 0.0));
        }
        return list;
    }

    private void placeList(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(AutoWeb.mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), AutoWeb.mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (Vec3d vec3d3 : list) {
            BlockPos position = new BlockPos(vec3d3);
            int placeability = BlockUtil.isPositionPlaceable(position, false);
            if (placeability != 3 && placeability != 1) continue;
            this.placeBlock(position);
        }
    }

    private void placeBlock(BlockPos pos) {
        int oldSlot = AutoWeb.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockWeb.class) == -1) {
            return;
        }
        AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(InventoryUtil.findHotbarBlock(BlockWeb.class)));
        AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoWeb.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        AutoWeb.mc.playerController.updateController();
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.r, (boolean)this.packet.getValue(), true);
        AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoWeb.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        AutoWeb.mc.playerController.updateController();
        AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(oldSlot));
    }

    private Vec3d predict(Vec3d startPos) {
        double defSpeed = 0.4913640415;
        double x = this.player.motionX;
        double z = this.player.motionZ;
        if (x < 0.5 && x > -0.5) {
            return startPos;
        }
        if (z < 0.5 && z > -0.5) {
            return startPos;
        }
        double predictX = EntityUtil.predictPos(x, 0.152);
        double predictZ = EntityUtil.predictPos(z, 0.152);
        return startPos;
    }
}

