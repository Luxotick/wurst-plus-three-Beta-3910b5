/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package me.travis.wurstplusthree.gui.hud;

import java.util.ArrayList;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.gui.component.CategoryComponent;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.hud.element.HudDragComponent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.util.RenderUtil2D;
import net.minecraft.client.gui.GuiScreen;

public class HudButton
extends Component {
    public final HudElement element;
    public final CategoryComponent parent;
    public int offset;
    private boolean isHovered;
    private final ArrayList<HudDragComponent> dragComponents = new ArrayList();

    public HudButton(HudElement element, CategoryComponent parent, int offset) {
        this.element = element;
        this.parent = parent;
        this.offset = offset;
        this.dragComponents.add(new HudDragComponent(element, element.getWidth(), element.getHeight()));
    }

    @Override
    public void renderComponent(int MouseX, int MouseY) {
        if (this.element.isEnabled()) {
            RenderUtil2D.drawGradientRect(this.parent.getX() + 5, this.parent.getY() + this.offset + 0, this.parent.getX() + this.parent.getWidth() - 5, this.parent.getY() + 16 + this.offset + 0, Gui.INSTANCE.buttonColor.getValue().hashCode(), Gui.INSTANCE.buttonColor.getValue().hashCode(), this.isHovered);
            for (HudDragComponent dragComponent : this.dragComponents) {
                dragComponent.renderComponent(MouseX, MouseY);
            }
        } else {
            RenderUtil2D.drawRectMC(this.parent.getX() + 5, this.parent.getY() + this.offset + 0, this.parent.getX() + this.parent.getWidth() - 5, this.parent.getY() + 16 + this.offset + 0, this.isHovered ? WurstplusGuiNew.GUI_HOVERED_COLOR() : WurstplusGuiNew.GUI_MODULECOLOR());
        }
        if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.element.getName(), this.parent.getX() + 6, (float)(this.parent.getY() + this.offset + 0) + 8.0f - 4.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
        } else {
            HudButton.mc.fontRenderer.drawStringWithShadow(this.element.getName(), (float)(this.parent.getX() + 6), (float)(this.parent.getY() + this.offset + 0) + 8.0f - 4.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
        }
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        for (HudDragComponent dragComponent : this.dragComponents) {
            dragComponent.updateComponent(mouseX, mouseY);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.getX() + 5 && x < this.parent.getX() + this.parent.getWidth() - 5 && y > this.parent.getY() + this.offset && y < this.parent.getY() + 16 + 0 + this.offset;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            if (this.element.getName().equalsIgnoreCase("return")) {
                mc.displayGuiScreen((GuiScreen)WurstplusThree.GUI2);
                return;
            }
            this.element.toggle();
        }
        for (HudDragComponent dragComponent : this.dragComponents) {
            dragComponent.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (HudDragComponent dragComponent : this.dragComponents) {
            dragComponent.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    public ArrayList<HudDragComponent> getDragComponents() {
        return this.dragComponents;
    }
}

