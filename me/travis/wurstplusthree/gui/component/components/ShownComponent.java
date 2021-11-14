/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.component.components;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.component.HackButton;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.util.RenderUtil2D;

public class ShownComponent
extends Component {
    private String name;
    private boolean isHovered;
    private HackButton parent;
    private int offset;
    private int x;
    private int y;
    private Hack module;

    public ShownComponent(HackButton button, int offset) {
        this.parent = button;
        this.name = "Shown";
        this.offset = offset;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.setShown(true);
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        this.module = this.parent.mod;
        RenderUtil2D.drawRectMC(this.parent.parent.getX() + 5, this.parent.parent.getY() + this.offset + 0, this.parent.parent.getX() + this.parent.parent.getWidth() - 5, this.parent.parent.getY() + this.offset + 16 + 0, this.isHovered ? WurstplusGuiNew.GUI_HOVERED_COLOR() : WurstplusGuiNew.GUI_COLOR());
        if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow("Shown: " + (this.module.isNotification() ? "True" : "False"), this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
        } else {
            ShownComponent.mc.fontRenderer.drawStringWithShadow("Shown: " + (this.module.isNotification() ? "True" : "False"), (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
        }
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + 5 && x < this.parent.parent.getX() + 120 - 5 && y > this.parent.parent.getY() + this.offset + 0 && y < this.parent.parent.getY() + this.offset + 16 + 0;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            this.module.setNotification(!this.module.isNotification());
        }
    }

    @Override
    public HackButton getParent() {
        return this.parent;
    }

    @Override
    public int getOffset() {
        return this.offset;
    }
}

