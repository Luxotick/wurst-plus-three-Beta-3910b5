/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.server.SPacketMoveVehicle
 *  net.minecraft.util.math.AxisAlignedBB
 */
package me.travis.wurstplusthree.hack.hacks.player;

import java.util.Arrays;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.JesusEvent;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.events.UpdateWalkingPlayerEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.util.math.AxisAlignedBB;

@Hack.Registration(name="Jesus", description="its jebus, say hello jebus SHGDFYGSDKJHFGSDHJ", category=Hack.Category.PLAYER, isListening=false)
public class Jesus
extends Hack {
    public static Jesus INSTANCE;
    public static AxisAlignedBB offset;
    public EnumSetting mode = new EnumSetting("Mode", "Vanilla", Arrays.asList("Vanilla", "Trampoline", "Bounce", "Normal"), this);
    public BooleanSetting cancelVehicle = new BooleanSetting("No Vehcile", (Boolean)false, (Hack)this);
    public EnumSetting eventMode = new EnumSetting("Event", "Pre", Arrays.asList("Pre", "Post", "All"), this);
    public BooleanSetting fall = new BooleanSetting("NoFall", (Boolean)false, (Hack)this);
    private boolean grounded = false;

    public Jesus() {
        INSTANCE = this;
    }

    @CommitEvent
    public void updateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.nullCheck() || WurstplusThree.HACKS.ishackEnabled("Freecam")) {
            return;
        }
        if (!(event.getStage() != 0 || this.mode.is("Trampoline") || Jesus.mc.player.isSneaking() || Jesus.mc.player.noClip || Jesus.mc.gameSettings.keyBindJump.isKeyDown() || !EntityUtil.isInLiquid())) {
            Jesus.mc.player.motionY = 0.1f;
        }
        if (event.getStage() == 0 && this.mode.is("Trampoline") && !this.eventMode.is("Post")) {
            this.doTrampoline();
        } else if (event.getStage() == 1 && this.mode.is("Trampoline") && !this.eventMode.is("Pre")) {
            this.doTrampoline();
        }
    }

    @CommitEvent
    public void sendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && WurstplusThree.HACKS.ishackEnabled("Freecam") && (this.mode.is("Bounce") || this.mode.is("Normal")) && Jesus.mc.player.getRidingEntity() == null && !Jesus.mc.gameSettings.keyBindJump.isKeyDown()) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            if (!EntityUtil.isInLiquid() && EntityUtil.isOnLiquid(0.05f) && EntityUtil.checkCollide() && Jesus.mc.player.ticksExisted % 3 == 0) {
                packet.y -= (double)0.05f;
            }
        }
    }

    @CommitEvent
    public void onLiquidCollision(JesusEvent event) {
        if (this.nullCheck() || WurstplusThree.HACKS.ishackEnabled("Freecam")) {
            return;
        }
        if (event.getStage() == 0 && !this.mode.is("Trampoline") && Jesus.mc.world != null && Jesus.mc.player != null && EntityUtil.checkCollide() && !(Jesus.mc.player.motionY >= (double)0.1f) && (double)event.getPos().getY() < Jesus.mc.player.posY - (double)0.05f) {
            if (Jesus.mc.player.getRidingEntity() != null) {
                event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, (double)0.95f, 1.0));
            } else {
                event.setBoundingBox(Block.FULL_BLOCK_AABB);
            }
            event.setCancelled(true);
        }
    }

    @CommitEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (this.cancelVehicle.getValue().booleanValue() && event.getPacket() instanceof SPacketMoveVehicle) {
            event.setCancelled(true);
        }
    }

    @Override
    public String getDisplayInfo() {
        return (String)this.mode.value;
    }

    private void doTrampoline() {
        if (Jesus.mc.player.isSneaking()) {
            return;
        }
        if (EntityUtil.isAboveLiquid((Entity)Jesus.mc.player) && !Jesus.mc.player.isSneaking() && !Jesus.mc.gameSettings.keyBindJump.pressed) {
            Jesus.mc.player.motionY = 0.1;
            return;
        }
        if (Jesus.mc.player.onGround || Jesus.mc.player.isOnLadder()) {
            this.grounded = false;
        }
        if (Jesus.mc.player.motionY > 0.0) {
            if (Jesus.mc.player.motionY < 0.03 && this.grounded) {
                Jesus.mc.player.motionY += 0.06713;
            } else if (Jesus.mc.player.motionY <= 0.05 && this.grounded) {
                Jesus.mc.player.motionY *= 1.20000000999;
                Jesus.mc.player.motionY += 0.06;
            } else if (Jesus.mc.player.motionY <= 0.08 && this.grounded) {
                Jesus.mc.player.motionY *= 1.20000003;
                Jesus.mc.player.motionY += 0.055;
            } else if (Jesus.mc.player.motionY <= 0.112 && this.grounded) {
                Jesus.mc.player.motionY += 0.0535;
            } else if (this.grounded) {
                Jesus.mc.player.motionY *= 1.000000000002;
                Jesus.mc.player.motionY += 0.0517;
            }
        }
        if (this.grounded && Jesus.mc.player.motionY < 0.0 && Jesus.mc.player.motionY > -0.3) {
            Jesus.mc.player.motionY += 0.045835;
        }
        if (!this.fall.getValue().booleanValue()) {
            Jesus.mc.player.fallDistance = 0.0f;
        }
        if (!EntityUtil.checkForLiquid((Entity)Jesus.mc.player, true)) {
            return;
        }
        if (EntityUtil.checkForLiquid((Entity)Jesus.mc.player, true)) {
            Jesus.mc.player.motionY = 0.5;
        }
        this.grounded = true;
    }

    static {
        offset = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.9999, 1.0);
    }
}

