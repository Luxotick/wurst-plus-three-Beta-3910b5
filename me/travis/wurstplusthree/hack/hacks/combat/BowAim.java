/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemBow
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.MathsUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.Vec3d;

@Hack.Registration(name="Bow Aim", description="Aimbot for minecraft", category=Hack.Category.COMBAT, priority=HackPriority.Lowest)
public class BowAim
extends Hack {
    @Override
    public void onUpdate() {
        if (BowAim.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && BowAim.mc.player.isHandActive() && BowAim.mc.player.getItemInUseMaxCount() >= 3) {
            EntityPlayer player = null;
            float tickDis = 100.0f;
            for (EntityPlayer p : BowAim.mc.world.playerEntities) {
                float dis;
                if (p instanceof EntityPlayerSP || WurstplusThree.FRIEND_MANAGER.isFriend(p.getName()) || !((dis = p.getDistance((Entity)BowAim.mc.player)) < tickDis)) continue;
                tickDis = dis;
                player = p;
            }
            if (player != null) {
                Vec3d pos = EntityUtil.getInterpolatedPos(player, mc.getRenderPartialTicks());
                float[] angels = MathsUtil.calcAngle(EntityUtil.getInterpolatedPos((Entity)BowAim.mc.player, mc.getRenderPartialTicks()), pos);
                BowAim.mc.player.rotationYaw = angels[0];
                BowAim.mc.player.rotationPitch = angels[1];
            }
        }
    }
}

