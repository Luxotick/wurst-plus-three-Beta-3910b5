/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.Gui
 */
package me.travis.wurstplusthree.gui.component.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.BigDecimal;
import java.math.RoundingMode;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.component.HackButton;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.util.MathsUtil;
import me.travis.wurstplusthree.util.RenderUtil2D;

public class SliderComponent
extends Component {
    private boolean hovered;
    private final HackButton parent;
    private DoubleSetting setD = null;
    private IntSetting setI = null;
    private int offset;
    private int x;
    private int y;
    private boolean isChild;
    private boolean dragging = false;
    private double renderWidth;

    public SliderComponent(DoubleSetting value, HackButton button, int offset) {
        this.setD = value;
        this.parent = button;
        this.offset = offset;
        this.isChild = value.isChild();
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.setShown(true);
    }

    public SliderComponent(IntSetting value, HackButton button, int offset) {
        this.setI = value;
        this.parent = button;
        this.offset = offset;
        this.isChild = value.isChild();
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.setShown(true);
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        if (!this.isShown()) {
            return;
        }
        net.minecraft.client.gui.Gui.drawRect((int)(this.parent.parent.getX() + 5), (int)(this.parent.parent.getY() + this.offset + 0), (int)(this.parent.parent.getX() + this.parent.parent.getWidth() - 5), (int)(this.parent.parent.getY() + this.offset + 16 + 0), (int)(this.hovered ? WurstplusGuiNew.GUI_HOVERED_COLOR() : (this.isChild ? WurstplusGuiNew.GUI_CHILDBUTTON() : WurstplusGuiNew.GUI_COLOR())));
        RenderUtil2D.drawGradientRect(this.parent.parent.getX() + 5, this.parent.parent.getY() + this.offset + 0, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 16 + 0, Gui.INSTANCE.buttonColor.getValue().hashCode(), Gui.INSTANCE.buttonColor.getValue().hashCode(), this.hovered);
        if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.isInt() ? this.setI.getName() + " " + (Object)ChatFormatting.GRAY + MathsUtil.round(this.setI.getValue().intValue(), 2) : this.setD.getName() + " " + (Object)ChatFormatting.GRAY + this.setD.getValue(), this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
        } else {
            SliderComponent.mc.fontRenderer.drawStringWithShadow(this.isInt() ? this.setI.getName() + " " + (Object)ChatFormatting.GRAY + MathsUtil.round(this.setI.getValue().intValue(), 2) : this.setD.getName() + " " + (Object)ChatFormatting.GRAY + this.setD.getValue(), (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
        }
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        boolean old = this.isShown();
        if (this.setD == null) {
            this.setShown(this.setI.isShown());
        } else {
            this.setShown(this.setD.isShown());
        }
        if (old != this.isShown()) {
            this.parent.init(this.parent.mod, this.parent.parent, this.parent.offset, true);
        }
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        int widthTest = 110;
        double diff = Math.min(widthTest, Math.max(0, mouseX - this.x));
        if (this.isInt()) {
            int min = this.setI.getMin();
            int max = this.setI.getMax();
            this.renderWidth = (float)widthTest * (float)(this.setI.getValue() - min) / (float)(max - min) + 5.0f;
            if (this.dragging) {
                if (diff == 0.0) {
                    this.setI.setValue(this.setI.getMin());
                } else {
                    int newValue = (int)SliderComponent.roundToPlace(diff / (double)widthTest * (double)(max - min) + (double)min, 2);
                    this.setI.setValue(newValue);
                }
            }
        } else {
            double min = this.setD.getMin();
            double max = this.setD.getMax();
            this.renderWidth = (double)widthTest * (this.setD.getValue() - min) / (max - min) + 5.0;
            if (this.dragging) {
                if (diff == 0.0) {
                    this.setD.setValue(this.setD.getMin());
                } else {
                    double newValue = SliderComponent.roundToPlace(diff / (double)widthTest * (max - min) + min, 2);
                    this.setD.setValue(newValue);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (!this.isShown()) {
            return;
        }
        if (this.isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            this.dragging = true;
        }
        if (this.isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.dragging = false;
    }

    private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public boolean isMouseOnButtonD(int x, int y) {
        return x > this.x + 5 && x < this.x + (this.parent.parent.getWidth() / 2 + 1) - 5 && y > this.y && y < this.y + 16;
    }

    public boolean isMouseOnButtonI(int x, int y) {
        return x > this.x + this.parent.parent.getWidth() / 2 + 5 && x < this.x + this.parent.parent.getWidth() - 5 && y > this.y && y < this.y + 16;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + 5 && x < this.parent.parent.getX() + 120 - 5 && y > this.parent.parent.getY() + this.offset + 0 && y < this.parent.parent.getY() + this.offset + 16 + 0;
    }

    public boolean isInt() {
        return this.setI != null;
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

