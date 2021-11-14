/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParser
 *  com.mojang.util.UUIDTypeAdapter
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockConcretePowder
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.travis.wurstplusthree.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.util.UUIDTypeAdapter;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.util.CrystalUtil;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.elements.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class PlayerUtil
implements Globals {
    private static final BlockPos[] surroundOffset = new BlockPos[]{new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};

    public static UUID getUUIDFromName(String name) {
        try {
            lookUpUUID process = new lookUpUUID(name);
            Thread thread = new Thread(process);
            thread.start();
            thread.join();
            return process.getUUID();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plusY) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy - h;
                while (true) {
                    float f = y;
                    float f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plusY, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(PlayerUtil.mc.player.posX), Math.floor(PlayerUtil.mc.player.posY), Math.floor(PlayerUtil.mc.player.posZ));
    }

    public static BlockPos getPlayerPos(double pY) {
        return new BlockPos(Math.floor(PlayerUtil.mc.player.posX), Math.floor(PlayerUtil.mc.player.posY + pY), Math.floor(PlayerUtil.mc.player.posZ));
    }

    public static double[] forward(double speed) {
        float forward = PlayerUtil.mc.player.movementInput.moveForward;
        float side = PlayerUtil.mc.player.movementInput.moveStrafe;
        float yaw = PlayerUtil.mc.player.prevRotationYaw + (PlayerUtil.mc.player.rotationYaw - PlayerUtil.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static int findObiInHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = PlayerUtil.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block instanceof BlockEnderChest) {
                return i;
            }
            if (!(block instanceof BlockObsidian)) continue;
            return i;
        }
        return -1;
    }

    public static int findSandInHotbar() {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = PlayerUtil.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock) || !((block = ((ItemBlock)stack.getItem()).getBlock()) instanceof BlockConcretePowder)) continue;
            return i;
        }
        return -1;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }

    public static boolean isInHole() {
        BlockPos player_block = PlayerUtil.getPlayerPos();
        return PlayerUtil.mc.world.getBlockState(player_block.east()).getBlock() != Blocks.AIR && PlayerUtil.mc.world.getBlockState(player_block.west()).getBlock() != Blocks.AIR && PlayerUtil.mc.world.getBlockState(player_block.north()).getBlock() != Blocks.AIR && PlayerUtil.mc.world.getBlockState(player_block.south()).getBlock() != Blocks.AIR;
    }

    public static String requestIDs(String data) {
        try {
            String query = "https://api.mojang.com/profiles/minecraft";
            URL url = new URL("https://api.mojang.com/profiles/minecraft");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes(StandardCharsets.UTF_8));
            os.close();
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            String res = PlayerUtil.convertStreamToString(in);
            ((InputStream)in).close();
            conn.disconnect();
            return res;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.moveForward != 0.0f || entity.moveStrafing != 0.0f;
    }

    public static void setSpeed(EntityLivingBase entity, double speed) {
        double[] dir = PlayerUtil.forward(speed);
        entity.motionX = dir[0];
        entity.motionZ = dir[1];
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (PlayerUtil.mc.player != null && PlayerUtil.mc.player.isPotionActive(Potion.getPotionById((int)1))) {
            int amplifier = PlayerUtil.mc.player.getActivePotionEffect(Potion.getPotionById((int)1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static Item getBestItem(Block block) {
        String tool = block.getHarvestTool(block.getDefaultState());
        if (tool != null) {
            switch (tool) {
                case "axe": {
                    return Items.DIAMOND_AXE;
                }
                case "shovel": {
                    return Items.DIAMOND_SHOVEL;
                }
            }
            return Items.DIAMOND_PICKAXE;
        }
        return Items.DIAMOND_PICKAXE;
    }

    public static ItemStack getItemStackFromItem(Item item) {
        if (PlayerUtil.mc.player == null) {
            return null;
        }
        for (int slot = 0; slot <= 9; ++slot) {
            if (PlayerUtil.mc.player.inventory.getStackInSlot(slot).getItem() != item) continue;
            return PlayerUtil.mc.player.inventory.getStackInSlot(slot);
        }
        return null;
    }

    public static FacingDirection getFacing() {
        int yaw = (int)Math.floor(PlayerUtil.mc.player.getRotationYawHead());
        if (yaw <= 0) {
            yaw += 360;
        }
        yaw = (yaw % 360 + 360) % 360;
        int facing = (yaw += 45) / 45;
        switch (facing) {
            case 0: 
            case 1: {
                return FacingDirection.SOUTH;
            }
            case 2: 
            case 3: {
                return FacingDirection.WEST;
            }
            case 4: 
            case 5: {
                return FacingDirection.NORTH;
            }
            case 6: 
            case 7: {
                return FacingDirection.EAST;
            }
        }
        return FacingDirection.SOUTH;
    }

    public static BlockPos GetPositionVectorBlockPos(Entity entity, @Nullable BlockPos toAdd) {
        Vec3d v = entity.getPositionVector();
        if (toAdd == null) {
            return new BlockPos(v.x, v.y, v.z);
        }
        return new BlockPos(v.x, v.y, v.z).add((Vec3i)toAdd);
    }

    public static ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>> GetPlayersReadyToBeCitied() {
        ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>> players = new ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>>();
        for (Entity entity : PlayerUtil.mc.world.playerEntities.stream().filter(entityPlayer -> !WurstplusThree.FRIEND_MANAGER.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
            ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
            for (int i = 0; i < 4; ++i) {
                BlockPos o = PlayerUtil.GetPositionVectorBlockPos(entity, surroundOffset[i]);
                if (PlayerUtil.mc.world.getBlockState(o).getBlock() != Blocks.OBSIDIAN) continue;
                boolean passCheck = false;
                switch (i) {
                    case 0: {
                        passCheck = CrystalUtil.canPlaceCrystal(o.north(1).down(), false, false);
                        break;
                    }
                    case 1: {
                        passCheck = CrystalUtil.canPlaceCrystal(o.east(1).down(), false, false);
                        break;
                    }
                    case 2: {
                        passCheck = CrystalUtil.canPlaceCrystal(o.south(1).down(), false, false);
                        break;
                    }
                    case 3: {
                        passCheck = CrystalUtil.canPlaceCrystal(o.west(1).down(), false, false);
                    }
                }
                if (!passCheck) continue;
                positions.add(o);
            }
            if (positions.isEmpty()) continue;
            players.add(new Pair((EntityPlayer)entity, positions));
        }
        return players;
    }

    public static enum FacingDirection {
        NORTH,
        SOUTH,
        EAST,
        WEST;

    }

    public static class lookUpUUID
    implements Runnable {
        private final String name;
        private volatile UUID uuid;

        public lookUpUUID(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            JsonElement element;
            String s;
            NetworkPlayerInfo profile;
            try {
                ArrayList infoMap = new ArrayList(Objects.requireNonNull(Globals.mc.getConnection()).getPlayerInfoMap());
                profile = infoMap.stream().filter(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(this.name)).findFirst().orElse(null);
                assert (profile != null);
                this.uuid = profile.getGameProfile().getId();
            }
            catch (Exception e2) {
                profile = null;
            }
            if (profile == null && (s = PlayerUtil.requestIDs("[\"" + this.name + "\"]")) != null && !s.isEmpty() && (element = new JsonParser().parse(s)).getAsJsonArray().size() != 0) {
                try {
                    String id = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                    this.uuid = UUIDTypeAdapter.fromString((String)id);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public UUID getUUID() {
            return this.uuid;
        }

        public String getName() {
            return this.name;
        }
    }
}

