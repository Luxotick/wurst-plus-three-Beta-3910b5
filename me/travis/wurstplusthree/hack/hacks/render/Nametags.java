/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import me.travis.wurstplusthree.util.elements.NametagRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

@Hack.Registration(name="Nametags", description="makes name above player", category=Hack.Category.RENDER, isListening=false)
public class Nametags
extends Hack {
    public static Nametags INSTANCE;
    public BooleanSetting customFont = new BooleanSetting("CustomFont", (Boolean)true, (Hack)this);
    public BooleanSetting simple = new BooleanSetting("Simplfy", (Boolean)false, (Hack)this);
    public BooleanSetting gameMode = new BooleanSetting("Gamemode", (Boolean)false, (Hack)this);
    public BooleanSetting armour = new BooleanSetting("Armour", (Boolean)true, (Hack)this);
    public BooleanSetting durability = new BooleanSetting("Durability", (Boolean)true, (Hack)this);
    public BooleanSetting popCounter = new BooleanSetting("TotemPops", (Boolean)true, (Hack)this);
    public BooleanSetting invisibles = new BooleanSetting("Invisibles", (Boolean)false, (Hack)this);
    public IntSetting distance = new IntSetting("Distance", 250, 0, 500, this);
    public IntSetting arrowPos = new IntSetting("ArrowPos", 28, 0, 50, this);
    public DoubleSetting scale = new DoubleSetting("Scale", (Double)0.05, (Double)0.01, (Double)0.1, this);
    public DoubleSetting height = new DoubleSetting("Height", (Double)2.5, (Double)0.5, (Double)5.0, this);
    public IntSetting textOffset = new IntSetting("TextOffset", 0, -5, 5, this, s -> this.customFont.getValue());
    public BooleanSetting outline = new BooleanSetting("Outline", (Boolean)true, (Hack)this);
    public DoubleSetting outlineWidth = new DoubleSetting("Width", (Double)1.5, (Double)0.1, (Double)3.0, this);
    public ColourSetting fontColour = new ColourSetting("FontColour", new Colour(255, 120, 0, 200), (Hack)this);
    public ColourSetting outlineColour = new ColourSetting("OutlineColour", new Colour(255, 80, 0, 150), (Hack)this);
    public ColourSetting outlineColourFriend = new ColourSetting("FriendColour", new Colour(20, 20, 255, 150), (Hack)this);
    public ColourSetting outlineColourEnemy = new ColourSetting("EnemyColour", new Colour(255, 20, 20, 150), (Hack)this);
    private final NametagRenderer nametagRenderer = new NametagRenderer();
    private final ICamera camera = new Frustum();
    private boolean shownItem;

    public Nametags() {
        INSTANCE = this;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.nullCheck()) {
            return;
        }
        double cx = Nametags.mc.player.lastTickPosX + (Nametags.mc.player.posX - Nametags.mc.player.lastTickPosX) * (double)event.getPartialTicks();
        double cy = Nametags.mc.player.lastTickPosY + (Nametags.mc.player.posY - Nametags.mc.player.lastTickPosY) * (double)event.getPartialTicks();
        double cz = Nametags.mc.player.lastTickPosZ + (Nametags.mc.player.posZ - Nametags.mc.player.lastTickPosZ) * (double)event.getPartialTicks();
        this.camera.setPosition(cx, cy, cz);
        try {
            for (EntityPlayer player : Nametags.mc.world.playerEntities) {
                if (!player.isEntityAlive() || player == mc.getRenderViewEntity() || player.getDistance((Entity)Nametags.mc.player) > (float)this.distance.getValue().intValue() || !this.camera.isBoundingBoxInFrustum(player.getEntityBoundingBox())) continue;
                NetworkPlayerInfo npi = Nametags.mc.player.connection.getPlayerInfo(player.getGameProfile().getId());
                double pX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)Nametags.mc.timer.renderPartialTicks - Nametags.mc.renderManager.renderPosX;
                double pY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)Nametags.mc.timer.renderPartialTicks - Nametags.mc.renderManager.renderPosY;
                double pZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)Nametags.mc.timer.renderPartialTicks - Nametags.mc.renderManager.renderPosZ;
                if (this.getShortGamemode(npi.getGameType().getName()).equalsIgnoreCase("SP") && !this.invisibles.getValue().booleanValue() || player.getName().startsWith("Body #")) continue;
                this.renderNametag(player, pX, pY, pZ);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
    }

    public void renderNametag(EntityPlayer player, double x, double y, double z) {
        this.shownItem = false;
        GlStateManager.pushMatrix();
        FontRenderer var13 = Nametags.mc.fontRenderer;
        NetworkPlayerInfo npi = Nametags.mc.player.connection.getPlayerInfo(player.getGameProfile().getId());
        boolean isFriend = WurstplusThree.FRIEND_MANAGER.isFriend(player.getName());
        boolean isEnemy = WurstplusThree.ENEMY_MANAGER.isEnemy(player.getName());
        String name = ((isFriend || isEnemy) && player.isSneaking() ? "\u00a79" : "\u00a7r") + (isFriend ? ChatFormatting.AQUA : (isEnemy ? ChatFormatting.RED : "")) + player.getName() + (Object)ChatFormatting.RESET + (this.gameMode.getValue() != false ? " [" + this.getShortGamemode(npi.getGameType().getName()) + "]" : "") + " " + '\u00a7' + this.getPing(npi.getResponseTime()) + npi.getResponseTime() + "ms " + '\u00a7' + this.getHealth(player.getHealth() + player.getAbsorptionAmount()) + MathHelper.ceil((float)(player.getHealth() + player.getAbsorptionAmount())) + (this.popCounter.getValue() != false ? " \u00a7" + this.getPop(WurstplusThree.POP_MANAGER.getTotemPops(player)) + '\u00a7' + "r" : "");
        name = name.replace(".0", "");
        float distance = Nametags.mc.player.getDistance((Entity)player);
        float var15 = (float)((distance / 5.0f <= 2.0f ? 2.0 : (double)(distance / 5.0f) * (this.scale.getValue() * 10.0 + 1.0)) * 2.5 * (this.scale.getValue() / 10.0));
        boolean far = distance / 5.0f > 2.0f;
        GL11.glTranslated((double)((float)x), (double)((double)((float)y) + this.height.getValue() - (player.isSneaking() ? 0.4 : 0.0) + (far ? (double)(distance / 12.0f) - 0.7 : 0.25)), (double)((float)z));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-Nametags.mc.renderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)Nametags.mc.renderManager.playerViewX, (float)(Nametags.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GL11.glScalef((float)(-var15), (float)(-var15), (float)var15);
        this.nametagRenderer.disableGlCap(2896, 2929);
        this.nametagRenderer.enableGlCap(3042);
        GL11.glBlendFunc((int)770, (int)771);
        int width = this.customFont.getValue() != false ? WurstplusThree.GUI_FONT_MANAGER.getTextWidth(name) / 2 + 1 : var13.getStringWidth(name) / 2;
        int color = Color.BLACK.getRGB();
        int outlineColor = WurstplusThree.FRIEND_MANAGER.isFriend(player.getName()) ? this.outlineColourFriend.getValue().getRGB() : (WurstplusThree.ENEMY_MANAGER.isEnemy(player.getName()) ? this.outlineColourEnemy.getValue().getRGB() : this.outlineColour.getValue().getRGB());
        Gui.drawRect((int)(-width - 2), (int)10, (int)(width + 1), (int)20, (int)Nametags.changeAlpha(color, 120));
        if (this.outline.getValue().booleanValue()) {
            RenderUtil.drawOutlineLine(-width - 2, 10.0, width + 1, 20.0, this.outlineWidth.getValue(), outlineColor);
        }
        if (!far) {
            RenderUtil.drawTriangleOutline((float)width - (float)WurstplusThree.GUI_FONT_MANAGER.getTextWidth(name) / 2.0f, this.arrowPos.getValue().intValue(), 5.0f, 2.0f, 1.0f, this.outlineWidth.getValue().floatValue(), outlineColor);
        }
        if (this.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(name, -width, 13 + this.textOffset.getValue(), this.fontColour.getValue().getRGB());
        } else {
            Nametags.mc.fontRenderer.drawStringWithShadow(name, (float)(-width), 11.0f, this.fontColour.getValue().getRGB());
        }
        if (this.armour.getValue().booleanValue()) {
            int xOffset = -6;
            int count = 0;
            for (ItemStack armourStack : player.inventory.armorInventory) {
                if (armourStack == null) continue;
                xOffset -= 8;
                if (armourStack.getItem() == Items.AIR) continue;
                ++count;
            }
            if (player.getHeldItemOffhand().getItem() != Items.AIR) {
                ++count;
            }
            int cacheX = xOffset - 8;
            xOffset += 8 * (5 - count) - (count == 0 ? 4 : 0);
            if (player.getHeldItemMainhand().getItem() != Items.AIR) {
                ItemStack renderStack = player.getHeldItemMainhand().copy();
                this.renderItem(player, renderStack, xOffset -= 8, -7, cacheX, true);
                xOffset += 16;
            } else {
                this.shownItem = true;
            }
            for (int index = 3; index >= 0; --index) {
                ItemStack armourStack = (ItemStack)player.inventory.armorInventory.get(index);
                if (armourStack.getItem() == Items.AIR) continue;
                ItemStack renderStack1 = armourStack.copy();
                this.renderItem(player, renderStack1, xOffset, -7, cacheX, false);
                xOffset += 16;
            }
            if (player.getHeldItemOffhand().getItem() != Items.AIR) {
                ItemStack renderOffhand = player.getHeldItemOffhand().copy();
                this.renderItem(player, renderOffhand, xOffset, -7, cacheX, false);
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        } else if (this.durability.getValue().booleanValue()) {
            int xOffset = -6;
            int count = 0;
            for (ItemStack armourStack : player.inventory.armorInventory) {
                if (armourStack == null) continue;
                xOffset -= 8;
                if (armourStack.getItem() == Items.AIR) continue;
                ++count;
            }
            if (player.getHeldItemOffhand().getItem() != Items.AIR) {
                ++count;
            }
            xOffset += 8 * (5 - count) - (count == 0 ? 4 : 0);
            for (int index = 3; index >= 0; --index) {
                ItemStack armourStack;
                armourStack = (ItemStack)player.inventory.armorInventory.get(index);
                if (armourStack.getItem() == Items.AIR) continue;
                ItemStack renderStack1 = armourStack.copy();
                this.renderDurabilityText(renderStack1, xOffset);
                xOffset += 16;
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        }
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        this.nametagRenderer.resetCaps();
        GlStateManager.resetColor();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    private void renderDurabilityText(ItemStack stack, int x) {
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
            float green = ((float)stack.getMaxDamage() - (float)stack.getItemDamage()) / (float)stack.getMaxDamage();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (this.customFont.getValue().booleanValue()) {
                WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(dmg + "%", x * 2 + 4, -17.0f, new Color(red, green, 0.0f).getRGB());
            } else {
                Nametags.mc.fontRenderer.drawStringWithShadow(dmg + "%", (float)(x * 2 + 4), -17.0f, new Color(red, green, 0.0f).getRGB());
            }
        }
    }

    public void renderItem(EntityPlayer player, ItemStack stack, int x, int y, int nameX, boolean showHeldItemText) {
        GL11.glPushMatrix();
        GL11.glDepthMask((boolean)true);
        GlStateManager.clear((int)256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = -100.0f;
        GlStateManager.scale((float)1.0f, (float)1.0f, (float)0.01f);
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y / 2 - 12);
        if (this.durability.getValue().booleanValue()) {
            mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRenderer, stack, x, y / 2 - 12);
        }
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.scale((float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.disableDepth();
        this.renderEnchantText(player, stack, x, y - 18);
        if (!this.shownItem && showHeldItemText) {
            if (this.customFont.getValue().booleanValue()) {
                WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(stack.getDisplayName().equalsIgnoreCase("Air") ? "" : stack.getDisplayName(), (float)(nameX * 2 + 95) - (float)WurstplusThree.GUI_FONT_MANAGER.getTextWidth(stack.getDisplayName()) / 2.0f, y - 37, -1);
            } else {
                Nametags.mc.fontRenderer.drawStringWithShadow(stack.getDisplayName().equalsIgnoreCase("Air") ? "" : stack.getDisplayName(), (float)(nameX * 2 + 95) - (float)Nametags.mc.fontRenderer.getStringWidth(stack.getDisplayName()) / 2.0f, (float)(y - 37), -1);
            }
            this.shownItem = true;
        }
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glPopMatrix();
    }

    public void renderEnchantText(EntityPlayer player, ItemStack stack, int x, int y) {
        int yCount = y;
        if ((stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) && this.durability.getValue().booleanValue()) {
            float green = ((float)stack.getMaxDamage() - (float)stack.getItemDamage()) / (float)stack.getMaxDamage();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (this.customFont.getValue().booleanValue()) {
                WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(dmg + "%", x * 2 + 4, y - 10, new Color(red, green, 0.0f).getRGB());
            } else {
                Nametags.mc.fontRenderer.drawStringWithShadow(dmg + "%", (float)(x * 2 + 4), (float)(y - 10), new Color(red, green, 0.0f).getRGB());
            }
        }
        if (this.simple.getValue().booleanValue() && this.isMaxEnchants(stack)) {
            GL11.glPushMatrix();
            GL11.glScalef((float)1.0f, (float)1.0f, (float)0.0f);
            if (this.simple.getValue().booleanValue()) {
                if (this.customFont.getValue().booleanValue()) {
                    WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow("Max", x * 2 + 7, yCount + 24, Colour.RED.getRGB());
                } else {
                    Nametags.mc.fontRenderer.drawStringWithShadow("Max", (float)(x * 2 + 7), (float)(yCount + 24), Colour.RED.getRGB());
                }
            }
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
            return;
        }
        NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            short id = enchants.getCompoundTagAt(index).getShort("id");
            short level = enchants.getCompoundTagAt(index).getShort("lvl");
            Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc == null || enc.isCurse()) continue;
            String encName = level == 1 ? enc.getTranslatedName((int)level).substring(0, 3).toLowerCase() : enc.getTranslatedName((int)level).substring(0, 2).toLowerCase() + level;
            encName = encName.substring(0, 1).toUpperCase() + encName.substring(1);
            GL11.glPushMatrix();
            GL11.glScalef((float)1.0f, (float)1.0f, (float)0.0f);
            if (this.customFont.getValue().booleanValue()) {
                WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(encName, x * 2 + 3, yCount, -1);
            } else {
                Nametags.mc.fontRenderer.drawStringWithShadow(encName, (float)(x * 2 + 3), (float)yCount, -1);
            }
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
            yCount += 8;
        }
    }

    public boolean isMaxEnchants(ItemStack stack) {
        return stack.getEnchantmentTagList().tagCount() > 2;
    }

    public String getHealth(float health) {
        if (health > 18.0f) {
            return "a";
        }
        if (health > 16.0f) {
            return "2";
        }
        if (health > 12.0f) {
            return "e";
        }
        if (health > 8.0f) {
            return "6";
        }
        if (health > 5.0f) {
            return "c";
        }
        return "4";
    }

    public String getPing(float ping) {
        if (ping > 200.0f) {
            return "c";
        }
        if (ping > 100.0f) {
            return "e";
        }
        return "a";
    }

    public String getPop(int pops) {
        if (pops > 10) {
            return "c" + pops;
        }
        if (pops > 4) {
            return "e" + pops;
        }
        return "a" + pops;
    }

    public static int changeAlpha(int origColor, int userInputedAlpha) {
        return userInputedAlpha << 24 | (origColor &= 0xFFFFFF);
    }

    public String getShortGamemode(String gameType) {
        if (gameType.equalsIgnoreCase("survival")) {
            return "S";
        }
        if (gameType.equalsIgnoreCase("creative")) {
            return "C";
        }
        if (gameType.equalsIgnoreCase("adventure")) {
            return "A";
        }
        if (gameType.equalsIgnoreCase("spectator")) {
            return "SP";
        }
        return "NONE";
    }
}

