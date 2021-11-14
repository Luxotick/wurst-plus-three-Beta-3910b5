/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.component.components;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.component.HackButton;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.RenderUtil2D;

public class ParentComponent
extends Component {
    private boolean hovered;
    private ParentSetting option;
    private final HackButton parent;
    private int offset;
    private int x;
    private int y;

    public ParentComponent(ParentSetting option, HackButton button, int offset) {
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
        RenderUtil2D.drawGradientRect(this.parent.parent.getX() + 5, this.parent.parent.getY() + this.offset + 0, this.parent.parent.getX() + this.parent.parent.getWidth() - 5, this.parent.parent.getY() + this.offset + 16 + 0 - 2, this.hovered ? Gui.INSTANCE.groupHoverColor.getValue().hashCode() : Gui.INSTANCE.groupColor.getValue().hashCode(), this.hovered ? Gui.INSTANCE.groupHoverColor.getValue().hashCode() : Gui.INSTANCE.groupColor.getValue().hashCode(), this.isMouseOnButton(mouseX, mouseY));
        WurstplusGuiNew.drawRect((int)(this.parent.parent.getX() + 5), (int)(this.parent.parent.getY() + this.offset + 0 + 16 - 2), (int)(this.parent.parent.getX() + this.parent.parent.getWidth() - 5), (int)(this.parent.parent.getY() + this.offset + 16 + 0), (int)(this.hovered ? WurstplusGuiNew.GUI_HOVERED_COLOR() : WurstplusGuiNew.GUI_CHILDBUTTON()));
        if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.option.getName(), this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.option.getValue() != false ? "-" : "+", this.parent.parent.getX() + 90 + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
        } else {
            ParentComponent.mc.fontRenderer.drawStringWithShadow(this.option.getName(), (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
            ParentComponent.mc.fontRenderer.drawStringWithShadow(this.option.getValue() != false ? "-" : "+", (float)(this.parent.parent.getX() + 90 + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
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
        if (this.isMouseOnButton(mouseX, mouseY) && (button == 0 || button == 1) && this.parent.isOpen) {
            this.option.toggle();
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

