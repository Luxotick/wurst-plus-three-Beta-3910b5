/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.component.components;

import java.awt.Color;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.component.HackButton;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.util.RenderUtil2D;

public class BooleanComponent
extends Component {
    private boolean hovered;
    private BooleanSetting option;
    private final HackButton parent;
    private int offset;
    private int x;
    private int y;

    public BooleanComponent(BooleanSetting option, HackButton button, int offset) {
        this.option = option;
        this.parent = button;
        this.offset = offset;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.setShown(true);
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        if (!this.isShown()) {
            return;
        }
        WurstplusGuiNew.drawRect((int)(this.parent.parent.getX() + 5), (int)(this.parent.parent.getY() + this.offset + 0), (int)(this.parent.parent.getX() + this.parent.parent.getWidth() - 5), (int)(this.parent.parent.getY() + this.offset + 16 + 0), (int)(this.hovered ? WurstplusGuiNew.GUI_HOVERED_COLOR() : (this.option.isChild() ? WurstplusGuiNew.GUI_CHILDBUTTON() : WurstplusGuiNew.GUI_COLOR())));
        RenderUtil2D.drawBorderedRect(this.parent.parent.getX() + 5 + 85, this.parent.parent.getY() + this.offset + 3 + 0, this.parent.parent.getX() + 115 - 5, this.parent.parent.getY() + this.offset + 16 + 0 - 3, 1, this.option.getValue() == false ? WurstplusGuiNew.GUI_COLOR() : Gui.INSTANCE.buttonColor.getValue().hashCode(), new Color(0, 0, 0, 200).hashCode(), this.hovered);
        RenderUtil2D.drawRectMC(this.parent.parent.getX() + 5 + (this.option.getValue() != false ? 95 : 88), this.parent.parent.getY() + this.offset + 5 + 0, this.parent.parent.getX() + (this.option.getValue() != false ? 112 : 105) - 5, this.parent.parent.getY() + this.offset + 16 + 0 - 5, new Color(50, 50, 50, 255).hashCode());
        if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.option.getName(), this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
        } else {
            BooleanComponent.mc.fontRenderer.drawStringWithShadow(this.option.getName(), (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
        }
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        boolean old = this.isShown();
        this.setShown(this.option.isShown());
        if (old != this.isShown()) {
            this.parent.init(this.parent.mod, this.parent.parent, this.parent.offset, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (!this.isShown()) {
            return;
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            this.option.setValue(this.option.getValue() == false);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + 5 && x < this.parent.parent.getX() + 120 - 5 && y > this.parent.parent.getY() + this.offset + 0 && y < this.parent.parent.getY() + this.offset + 16 + 0;
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

