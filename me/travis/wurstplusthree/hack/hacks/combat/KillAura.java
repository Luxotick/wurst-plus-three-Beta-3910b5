/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemSword
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.Arrays;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.UpdateWalkingPlayerEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.DamageUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;

@Hack.Registration(name="Kill Aura", description="hits people", category=Hack.Category.COMBAT, priority=HackPriority.Low)
public class KillAura
extends Hack {
    public EnumSetting mode = new EnumSetting("Mode", "Normal", Arrays.asList("Normal", "32k"), this);
    public EnumSetting switch0 = new EnumSetting("Switch", "None", Arrays.asList("None", "Auto", "SwordOnly"), this);
    public EnumSetting targetMode = new EnumSetting("Target", "Focus", Arrays.asList("Focus", "Closest", "Health"), this);
    public DoubleSetting range = new DoubleSetting("Range", (Double)4.5, (Double)0.0, (Double)7.0, this);
    public BooleanSetting rotate = new BooleanSetting("Rotate", (Boolean)false, (Hack)this);
    public BooleanSetting raytrace = new BooleanSetting("Walls", (Boolean)true, (Hack)this);
    public BooleanSetting swingArm = new BooleanSetting("Swing", (Boolean)true, (Hack)this);
    public IntSetting ttkDelay = new IntSetting("32kDelay", 3, 0, 10, this, s -> this.mode.is("32k"));
    private Entity target;
    private int ticksPassed;

    @Override
    public void onEnable() {
        this.target = null;
        this.ticksPassed = 0;
    }

    @Override
    public void onTick() {
        if (!this.rotate.getValue().booleanValue()) {
            this.doKA();
        }
        ++this.ticksPassed;
    }

    @CommitEvent
    public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue() && (this.target = this.getTarget()) != null) {
            WurstplusThree.ROTATION_MANAGER.lookAtEntity(this.target);
            this.doKA();
        }
    }

    private void doKA() {
        this.target = this.getTarget();
        if (this.target == null) {
            return;
        }
        switch (this.switch0.getValue()) {
            case "Auto": {
                for (int I = 0; I < 9; ++I) {
                    if (!(KillAura.mc.player.inventory.getStackInSlot(I).getItem() instanceof ItemSword) || !EntityUtil.is32k(KillAura.mc.player.inventory.getStackInSlot(I)) && !this.mode.is("Normal")) continue;
                    KillAura.mc.player.inventory.currentItem = I;
                    KillAura.mc.playerController.updateController();
                    break;
                }
            }
            case "SwordOnly": {
                if (KillAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) break;
                return;
            }
        }
        if (this.mode.is("32k")) {
            if (EntityUtil.holding32k((EntityPlayer)KillAura.mc.player) && this.ticksPassed >= this.ttkDelay.getValue()) {
                this.ticksPassed = 0;
                for (EntityPlayer player : KillAura.mc.world.playerEntities) {
                    if (EntityUtil.isntValid((Entity)player, this.range.getValue())) continue;
                    EntityUtil.attackEntity((Entity)player, true, this.swingArm.getValue());
                }
            }
        } else if (!this.shouldWait()) {
            EntityUtil.attackEntity(this.target, false, this.swingArm.getValue());
        }
    }

    private boolean shouldWait() {
        if (KillAura.mc.player.getCooledAttackStrength(this.getLagComp()) < 1.0f) {
            return true;
        }
        return KillAura.mc.player.ticksExisted % 2 == 1;
    }

    private float getLagComp() {
        return -(20.0f - WurstplusThree.SERVER_MANAGER.getTPS());
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = this.range.getValue().floatValue();
        double maxHealth = 36.0;
        for (Entity entity : KillAura.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityPlayer) || EntityUtil.isntValid(entity, distance) || !this.raytrace.getValue().booleanValue() && !KillAura.mc.player.canEntityBeSeen(entity)) continue;
            if (target == null) {
                target = entity;
                distance = KillAura.mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                target = entity;
                break;
            }
            if (this.targetMode.is("Health") && KillAura.mc.player.getDistanceSq(entity) < distance) {
                target = entity;
                distance = KillAura.mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
            if (this.targetMode.is("Health") || !((double)EntityUtil.getHealth(entity) < maxHealth)) continue;
            target = entity;
            distance = KillAura.mc.player.getDistanceSq(entity);
            maxHealth = EntityUtil.getHealth(entity);
        }
        return target;
    }

    @Override
    public String getDisplayInfo() {
        return this.target instanceof EntityPlayer ? this.target.getName() : null;
    }
}

