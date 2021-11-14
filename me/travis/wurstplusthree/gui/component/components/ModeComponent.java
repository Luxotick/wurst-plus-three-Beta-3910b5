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
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.util.RenderUtil2D;

public class ModeComponent
extends Component {
    private boolean hovered;
    private final HackButton parent;
    private final EnumSetting set;
    private int offset;
    private int modeIndex;

    public ModeComponent(EnumSetting set, HackButton button, Hack mod, int offset) {
        this.set = set;
        this.parent = button;
        this.offset = offset;
        this.modeIndex = 0;
        this.setShown(true);
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        if (!this.isShown()) {
            return;
        }
        RenderUtil2D.drawRectMC(this.parent.parent.getX() + 5, this.parent.parent.getY() + this.offset + 0, this.parent.parent.getX() + this.parent.parent.getWidth() - 5, this.parent.parent.getY() + this.offset + 16 + 0, this.hovered ? WurstplusGuiNew.GUI_HOVERED_COLOR() : (this.set.isChild() ? WurstplusGuiNew.GUI_CHILDBUTTON() : WurstplusGuiNew.GUI_COLOR()));
        if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.set.getName() + ": " + this.set.getValue(), this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
        } else {
            ModeComponent.mc.fontRenderer.drawStringWithShadow(this.set.getName() + ": " + this.set.getValue(), (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
        }
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        boolean old = this.isShown();
        this.setShown(this.set.isShown());
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
            this.increment();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1 && this.parent.isOpen) {
            this.deincrement();
        }
    }

    public void increment() {
        this.set.setValue(this.set.getModes().get((this.set.getModes().indexOf(this.set.value) + 1) % this.set.getModes().size()));
    }

    public void deincrement() {
        this.set.setValue(this.set.getModes().get((this.set.getModes().indexOf(this.set.value) - 1) % this.set.getModes().size()));
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

