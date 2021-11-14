/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.MobEffects
 */
package me.travis.wurstplusthree.hack.hacks.player;

import java.util.Arrays;
import me.travis.wurstplusthree.event.events.MoveEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import me.travis.wurstplusthree.util.elements.Timer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;

@Hack.Registration(name="Speed", description="makes u go faster", category=Hack.Category.PLAYER)
public class Speed
extends Hack {
    EnumSetting mode = new EnumSetting("Mode", "Strafe", Arrays.asList("Strafe", "Fake", "YPort"), this);
    DoubleSetting yPortSpeed = new DoubleSetting("YPort Speed", (Double)0.06, (Double)0.01, (Double)0.15, this, s -> this.mode.is("YPort"));
    DoubleSetting jumpHeight = new DoubleSetting("Jump Height", (Double)0.41, (Double)0.0, (Double)1.0, this, s -> this.mode.is("Strafe"));
    DoubleSetting timerVal = new DoubleSetting("Timer Speed", (Double)1.15, (Double)1.0, (Double)1.5, this, s -> this.mode.is("Strafe"));
    private boolean slowdown;
    private double playerSpeed;
    private final Timer timer = new Timer();

    @Override
    public void onEnable() {
        this.playerSpeed = PlayerUtil.getBaseMoveSpeed();
    }

    @Override
    public void onDisable() {
        EntityUtil.resetTimer();
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            this.disable();
            return;
        }
        if (this.mode.is("YPort")) {
            this.handleYPortSpeed();
        }
    }

    private void handleYPortSpeed() {
        if (!PlayerUtil.isMoving((EntityLivingBase)Speed.mc.player) || Speed.mc.player.isInWater() && Speed.mc.player.isInLava() || Speed.mc.player.collidedHorizontally) {
            return;
        }
        if (Speed.mc.player.onGround) {
            EntityUtil.setTimer(1.15f);
            Speed.mc.player.jump();
            PlayerUtil.setSpeed((EntityLivingBase)Speed.mc.player, PlayerUtil.getBaseMoveSpeed() + this.yPortSpeed.getValue());
        } else {
            Speed.mc.player.motionY = -1.0;
            EntityUtil.resetTimer();
        }
    }

    @CommitEvent
    public void onMove(MoveEvent event) {
        if (Speed.mc.player.isInLava() || Speed.mc.player.isInWater() || Speed.mc.player.isOnLadder() || Speed.mc.player.isInWeb) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("Strafe")) {
            double speedY = this.jumpHeight.getValue();
            if (Speed.mc.player.onGround && PlayerUtil.isMoving((EntityLivingBase)Speed.mc.player) && this.timer.passedMs(300L)) {
                EntityUtil.setTimer(this.timerVal.getValue().floatValue());
                if (Speed.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    speedY += (double)((float)(Speed.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f);
                }
                Speed.mc.player.motionY = speedY;
                event.setY(Speed.mc.player.motionY);
                this.playerSpeed = PlayerUtil.getBaseMoveSpeed() * (EntityUtil.isColliding(0.0, -0.5, 0.0) instanceof BlockLiquid && !EntityUtil.isInLiquid() ? 0.9 : 1.901);
                this.slowdown = true;
                this.timer.reset();
            } else {
                EntityUtil.resetTimer();
                if (this.slowdown || Speed.mc.player.collidedHorizontally) {
                    double d;
                    if (EntityUtil.isColliding(0.0, -0.8, 0.0) instanceof BlockLiquid && !EntityUtil.isInLiquid()) {
                        d = 0.4;
                    } else {
                        this.playerSpeed = PlayerUtil.getBaseMoveSpeed();
                        d = 0.7 * this.playerSpeed;
                    }
                    this.playerSpeed -= d;
                    this.slowdown = false;
                } else {
                    this.playerSpeed -= this.playerSpeed / 159.0;
                }
            }
            this.playerSpeed = Math.max(this.playerSpeed, PlayerUtil.getBaseMoveSpeed());
            double[] dir = PlayerUtil.forward(this.playerSpeed);
            event.setX(dir[0]);
            event.setZ(dir[1]);
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.getValue();
    }
}

