/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.gui.hud.element;

import java.awt.Color;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.util.RenderUtil2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HudDragComponent
extends Component {
    public HudElement element;
    public int width;
    public int height;
    public boolean dragging;
    public boolean hovered;
    public int dragX;
    public int dragY;
    private int oldMouseX;
    private int oldMouseY;
    private int ticks;
    private ResourceLocation arrow;

    public HudDragComponent(HudElement element, int width, int height) {
        this.element = element;
        this.width = width;
        this.height = height;
        this.dragging = false;
        this.hovered = false;
        this.oldMouseX = 0;
        this.oldMouseY = 0;
        this.ticks = 120;
        this.dragX = 0;
        this.dragY = 0;
        this.arrow = new ResourceLocation("textures/arrow.png");
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        RenderUtil2D.drawRectMC(this.element.getX(), this.element.getY(), this.element.getX() + this.width, this.element.getY() + this.height, !this.hovered ? new Color(255, 255, 255, 40).hashCode() : new Color(255, 255, 255, 75).hashCode());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (!this.element.isEnabled()) {
            return;
        }
        if (this.isMouseOn(mouseX, mouseY) && button == 0) {
            this.dragging = true;
            this.dragX = mouseX - this.element.getX();
            this.dragY = mouseY - this.element.getY();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.dragging = false;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        if (!this.element.isEnabled()) {
            return;
        }
        this.width = this.element.getWidth();
        this.height = this.element.getHeight();
        this.hovered = this.isMouseOn(mouseX, mouseY);
        boolean isSnappedH = false;
        boolean isSnappedV = false;
        int snapOffset = 2;
        if (this.dragging) {
            block0: for (HudElement hudElement : WurstplusThree.HUD_MANAGER.getHudElements()) {
                if (!HudEditor.INSTANCE.alignment.getValue().booleanValue()) break;
                if (hudElement == this.element || !hudElement.isEnabled()) continue;
                ScaledResolution sr = new ScaledResolution(mc);
                if (Math.round((float)this.element.getX() + (float)this.width / 2.0f) == sr.getScaledWidth() / 2) {
                    RenderUtil2D.drawVLine(sr.getScaledWidth() / 2, 0, sr.getScaledHeight(), HudEditor.INSTANCE.alignmentColor.getValue().hashCode());
                }
                if (Math.round((float)this.element.getY() + (float)this.height / 2.0f) == sr.getScaledHeight() / 2) {
                    RenderUtil2D.drawHLine(0, sr.getScaledHeight() / 2, sr.getScaledWidth(), HudEditor.INSTANCE.alignmentColor.getValue().hashCode());
                }
                if (this.element.getX() == hudElement.getX()) {
                    RenderUtil2D.drawVLine(this.element.getX(), 0, sr.getScaledHeight(), HudEditor.INSTANCE.alignmentColor.getValue().hashCode());
                }
                if (this.element.getX() + this.width == hudElement.getX() + hudElement.getWidth()) {
                    RenderUtil2D.drawVLine(this.element.getX() + this.width, 0, sr.getScaledHeight(), HudEditor.INSTANCE.alignmentColor.getValue().hashCode());
                }
                if (this.element.getY() == hudElement.getY()) {
                    RenderUtil2D.drawHLine(0, this.element.getY(), sr.getScaledWidth(), HudEditor.INSTANCE.alignmentColor.getValue().hashCode());
                }
                if (this.element.getY() + this.height == hudElement.getY() + hudElement.getHeight()) {
                    RenderUtil2D.drawHLine(0, this.element.getY() + this.height, sr.getScaledWidth(), HudEditor.INSTANCE.alignmentColor.getValue().hashCode());
                }
                for (int i = -snapOffset; i <= snapOffset; ++i) {
                    if (this.element.getX() + this.width / 2 == sr.getScaledWidth() / 2) {
                        isSnappedH = true;
                        continue block0;
                    }
                    if (this.element.getY() + this.height / 2 == sr.getScaledHeight() / 2) {
                        isSnappedV = true;
                        continue block0;
                    }
                    if (this.element.getX() == hudElement.getX() + i) {
                        this.element.setX(hudElement.getX());
                        isSnappedH = true;
                        continue block0;
                    }
                    if (this.element.getX() + this.width == hudElement.getX() + hudElement.getWidth() + i) {
                        this.element.setX(hudElement.getX() + hudElement.getWidth() - this.width);
                        isSnappedH = true;
                        continue block0;
                    }
                    if (this.element.getY() == hudElement.getY() + i) {
                        this.element.setY(hudElement.getY());
                        isSnappedV = true;
                        continue block0;
                    }
                    if (this.element.getY() + this.height != hudElement.getY() + hudElement.getHeight() + i) continue;
                    this.element.setY(hudElement.getY() + hudElement.getHeight() - this.height);
                    isSnappedV = true;
                    continue block0;
                }
            }
            if (isSnappedH || isSnappedV) {
                --this.ticks;
                if (this.ticks <= 0 && (this.oldMouseX != mouseX || this.oldMouseY != mouseY)) {
                    if (this.oldMouseX != mouseX && isSnappedV) {
                        this.ticks = 60;
                        this.oldMouseX = mouseX;
                        isSnappedV = false;
                    }
                    if (this.oldMouseY != mouseY && isSnappedH) {
                        this.ticks = 60;
                        this.oldMouseY = mouseY;
                        isSnappedH = false;
                    }
                }
            }
            double size = WurstplusThree.GUI_FONT_MANAGER.getTextWidth("X: " + this.element.getX() + ", Y: " + this.element.getY());
            RenderUtil2D.drawRectMC(this.element.getX(), this.element.getY() - 5, this.element.getX() + (int)Math.round(size - 14.285714285714286), this.element.getY() - 10, new Color(0, 0, 0, 100).hashCode());
            if (!isSnappedH) {
                this.element.setX(mouseX - this.dragX);
            }
            if (!isSnappedV) {
                this.element.setY(mouseY - this.dragY);
            }
            GL11.glPushMatrix();
            GL11.glScaled((double)0.7, (double)0.7, (double)0.7);
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow("X: " + this.element.getX() + ", Y: " + this.element.getY(), Math.round((double)this.element.getX() / 0.7), Math.round((double)this.element.getY() / 0.7) - 12L, new Color(255, 255, 255).hashCode());
            GL11.glPopMatrix();
        }
    }

    private boolean isMouseOn(int mouseX, int mouseY) {
        return mouseX > this.element.getX() && mouseX < this.element.getX() + this.width && mouseY > this.element.getY() && mouseY < this.element.getY() + this.height;
    }
}

