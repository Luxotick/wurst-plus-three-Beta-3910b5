/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.wurstplusthree.util;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.elements.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil
implements Globals {
    public static Timer rotationTimer = new Timer();
    private static float yaw;
    private static float pitch;
    public static boolean isSpoofingAngles;

    public static int getDirection4D() {
        return MathHelper.floor((double)((double)(RotationUtil.mc.player.rotationYaw * 4.0f / 360.0f) + 0.5)) & 3;
    }

    public static String getDirection4D(boolean northRed) {
        int dirnumber = RotationUtil.getDirection4D();
        if (dirnumber == 0) {
            return "South (+Z)";
        }
        if (dirnumber == 1) {
            return "West (-X)";
        }
        if (dirnumber == 2) {
            return (northRed ? "\u00a7c" : "") + "North (-Z)";
        }
        if (dirnumber == 3) {
            return "East (+X)";
        }
        return "Loading...";
    }

    public static void resetRotations() {
        try {
            yaw = RotationUtil.mc.player.rotationYaw;
            pitch = RotationUtil.mc.player.rotationPitch;
            RotationUtil.mc.player.rotationYawHead = RotationUtil.mc.player.rotationYaw;
            rotationTimer.reset();
        }
        catch (Exception ignored) {
            WurstplusThree.LOGGER.info("Failed to reset rotations...");
        }
    }

    public static void rotateHead(double x, double y, double z, EntityPlayer player) {
        double[] r = RotationUtil.calculateLookAt(x, y, z, player);
        RotationUtil.mc.player.setRotationYawHead((float)r[0]);
        RotationUtil.setYawAndPitch((float)r[0], (float)r[1]);
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = RotationUtil.getLegitRotations(vec);
        RotationUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float)MathHelper.normalizeAngle((int)((int)rotations[1]), (int)360) : rotations[1], RotationUtil.mc.player.onGround));
        RotationUtil.setYawAndPitch(rotations[0], rotations[1]);
    }

    public static void vecLookAt(Vec3d vec) {
        float[] rotations = RotationUtil.getLegitRotations(vec);
        RotationUtil.setYawAndPitch(rotations[0], rotations[1]);
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        double yaw = Math.atan2(dirz /= len, dirx /= len);
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;
        return new double[]{yaw += 90.0, pitch};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(RotationUtil.mc.player.posX, RotationUtil.mc.player.posY + (double)RotationUtil.mc.player.getEyeHeight(), RotationUtil.mc.player.posZ);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{RotationUtil.mc.player.rotationYaw + MathHelper.wrapDegrees((float)(yaw - RotationUtil.mc.player.rotationYaw)), RotationUtil.mc.player.rotationPitch + MathHelper.wrapDegrees((float)(pitch - RotationUtil.mc.player.rotationPitch))};
    }

    public static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    public static void setMinRotations(float yaw, float pitch) {
        RotationUtil.mc.player.rotationYaw = yaw;
        RotationUtil.mc.player.rotationYawHead = yaw;
        RotationUtil.mc.player.rotationPitch = pitch;
    }

    public static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = RotationUtil.mc.player.rotationYaw;
            pitch = RotationUtil.mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }
}

