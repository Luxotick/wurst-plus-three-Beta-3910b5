/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.component;

import java.util.ArrayList;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.component.HackButton;
import me.travis.wurstplusthree.gui.hud.HudButton;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.util.ColorUtil;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.RenderUtil2D;

public class CategoryComponent
implements Globals {
    public ArrayList<Component> components;
    public Hack.Category category;
    private final int width;
    private final int height;
    public int x;
    public int y;
    public boolean isOpen;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    public float animationValue = 0.0f;

    public CategoryComponent(Hack.Category cat) {
        this.category = cat;
        this.components = new ArrayList();
        this.width = 120;
        this.height = 16;
        this.x = 5;
        this.y = 5;
        this.dragX = 0;
        this.isOpen = true;
        this.isDragging = false;
        int tY = this.height;
        if (this.category.equals((Object)Hack.Category.HUD)) {
            for (HudElement element : WurstplusThree.HUD_MANAGER.getHudElements()) {
                HudButton hudButton = new HudButton(element, this, tY);
                this.components.add(hudButton);
                tY += 16;
            }
        } else {
            for (Hack mod : WurstplusThree.HACKS.getHacksAlp()) {
                if (!mod.getCategory().equals((Object)this.category)) continue;
                HackButton moduleButton = new HackButton();
                moduleButton.init(mod, this, tY, false);
                this.components.add(moduleButton);
                tY += 16;
            }
        }
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public void renderFrame(int mouseX, int mouseY) {
        RenderUtil2D.drawGradientRect(this.x + 4, this.y, this.x + this.width - 5, this.y + this.height, Gui.INSTANCE.headButtonColor.getValue().hashCode(), Gui.INSTANCE.headButtonColor.getValue().hashCode(), false);
        if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.category.getName(), this.x + 6, (float)this.y + (float)this.height / 2.0f - 4.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
        } else {
            CategoryComponent.mc.fontRenderer.drawStringWithShadow(this.category.getName(), (float)(this.x + 6), (float)this.y + (float)this.height / 2.0f - 4.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
        }
        if (this.isOpen && !this.components.isEmpty()) {
            int x = 0;
            for (Component component : this.components) {
                component.renderComponent(mouseX, mouseY);
                ++x;
                if (!(component instanceof HackButton) || !((HackButton)component).isOpen) continue;
                x += ((HackButton)component).subCompLength;
            }
            x *= 16;
            switch (Gui.INSTANCE.type.getValue()) {
                case "Sin": {
                    ColorUtil.type type2 = ColorUtil.type.SPECIAL;
                    switch (Gui.INSTANCE.SinMode.getValue()) {
                        case "Special": {
                            type2 = ColorUtil.type.SPECIAL;
                            break;
                        }
                        case "Hue": {
                            type2 = ColorUtil.type.HUE;
                            break;
                        }
                        case "Saturation": {
                            type2 = ColorUtil.type.SATURATION;
                            break;
                        }
                        case "Brightness": {
                            type2 = ColorUtil.type.BRIGHTNESS;
                        }
                    }
                    RenderUtil2D.drawVLineG(this.x + 4, this.y + 1 + 16 - 1, x, ColorUtil.getSinState(Gui.INSTANCE.buttonColor.getColor(), 1000.0, 255, type2).hashCode(), ColorUtil.getSinState(Gui.INSTANCE.buttonColor.getColor(), Gui.INSTANCE.rainbowDelay.getValue().intValue(), 255, type2).hashCode());
                    break;
                }
                case "Rainbow": {
                    RenderUtil2D.drawVLineG(this.x + 4, this.y + 1 + 16 - 1, x, ColorUtil.releasedDynamicRainbow(0, 255).hashCode(), ColorUtil.releasedDynamicRainbow(Gui.INSTANCE.rainbowDelay.getValue(), 255).hashCode());
                    break;
                }
                case "None": {
                    RenderUtil2D.drawVLine(this.x + 4, this.y + 1 + 16 - 1, x, Gui.INSTANCE.buttonColor.getValue().hashCode());
                }
            }
        }
    }

    public void refresh() {
        int off = this.height;
        for (Component comp : this.components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }

    public int getX() {
        return this.x;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public int getWidth() {
        return this.width;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }
}

