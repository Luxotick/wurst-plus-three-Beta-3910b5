/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 */
package me.travis.wurstplusthree.gui.hud.element.elements;

import java.awt.Color;
import java.util.ListIterator;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.util.HudUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

@HudElement.Element(name="Armor", posX=100, posY=100)
public class HudArmor
extends HudElement {
    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public int getWidth() {
        if (this.nullCheck()) {
            return 0;
        }
        int length = 0;
        for (ItemStack is : HudArmor.mc.player.inventory.armorInventory) {
            length += 20;
        }
        return length;
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        GlStateManager.enableTexture2D();
        int iteration = -20;
        NonNullList list = HudArmor.mc.player.inventory.armorInventory;
        ListIterator listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            ItemStack is = (ItemStack)listIterator.previous();
            iteration += 20;
            if (is.isEmpty()) continue;
            int x = this.getX() + iteration;
            int y = this.getY() + 10;
            GlStateManager.enableDepth();
            HudArmor.mc.getRenderItem().zLevel = 200.0f;
            mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
            mc.getRenderItem().renderItemOverlayIntoGUI(HudArmor.mc.fontRenderer, is, x, y, "");
            HudArmor.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.getCount() > 1 ? is.getCount() + "" : "";
            HudUtil.drawHudString(s, x + 19 - 2 - WurstplusThree.GUI_FONT_MANAGER.getTextWidth(s), y + 7, 1677215);
            float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (red > 1.0f) {
                red = 1.0f;
            }
            if (green > 1.0f) {
                green = 1.0f;
            }
            if (red < 0.0f) {
                red = 0.0f;
            }
            if (green < 0.0f) {
                green = 0.0f;
            }
            HudUtil.drawHudString(dmg + "", (int)((float)(x + 8) - (float)WurstplusThree.GUI_FONT_MANAGER.getTextWidth(dmg + "") / 2.0f), y - 9, new Color(red, green, 0.0f).getRGB());
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}

