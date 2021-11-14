/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundEvent
 *  org.lwjgl.input.Mouse
 */
package me.travis.wurstplusthree.gui.hud;

import java.io.IOException;
import java.util.ArrayList;
import me.travis.wurstplusthree.gui.component.CategoryComponent;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.util.RenderUtil2D;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import org.lwjgl.input.Mouse;

public class WurstplusHudGui
extends GuiScreen {
    public ArrayList<CategoryComponent> categoryComponents = new ArrayList();
    public ArrayList<Integer> linesH;
    public ArrayList<Integer> linesV;

    public WurstplusHudGui() {
        this.categoryComponents.add(new CategoryComponent(Hack.Category.HUD));
        this.linesH = new ArrayList();
        this.linesV = new ArrayList();
    }

    public void initGui() {
        int i;
        this.linesH.clear();
        this.linesV.clear();
        ScaledResolution sr = new ScaledResolution(this.mc);
        int vStart = sr.getScaledWidth() / HudEditor.INSTANCE.vLines.getValue();
        int hStart = sr.getScaledHeight() / HudEditor.INSTANCE.hLines.getValue();
        for (i = 0; i < HudEditor.INSTANCE.vLines.getValue(); ++i) {
            this.linesV.add(vStart * (i + 1));
        }
        for (i = 0; i < HudEditor.INSTANCE.hLines.getValue(); ++i) {
            this.linesH.add(hStart * (i + 1));
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.scrollWheelCheck();
        if (HudEditor.INSTANCE.grid.getValue().booleanValue()) {
            ScaledResolution sr = new ScaledResolution(this.mc);
            for (int i : this.linesV) {
                RenderUtil2D.drawVLine(i, 0, sr.getScaledHeight(), HudEditor.INSTANCE.gridColor.getValue().hashCode());
            }
            for (int i : this.linesH) {
                RenderUtil2D.drawHLine(0, i, sr.getScaledWidth(), HudEditor.INSTANCE.gridColor.getValue().hashCode());
            }
        }
        for (CategoryComponent categoryComponent : this.categoryComponents) {
            categoryComponent.renderFrame(mouseX, mouseY);
            categoryComponent.updatePosition(mouseX, mouseY);
            for (Component comp : categoryComponent.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (CategoryComponent categoryComponent : this.categoryComponents) {
            if (categoryComponent.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                categoryComponent.setDrag(true);
                categoryComponent.dragX = mouseX - categoryComponent.getX();
                categoryComponent.dragY = mouseY - categoryComponent.getY();
            }
            if (categoryComponent.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                categoryComponent.setOpen(!categoryComponent.isOpen());
                this.mc.soundHandler.playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
            }
            if (!categoryComponent.isOpen() || categoryComponent.getComponents().isEmpty()) continue;
            for (Component component : categoryComponent.getComponents()) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) {
        for (CategoryComponent categoryComponent : this.categoryComponents) {
            if (!categoryComponent.isOpen() || keyCode == 1 || categoryComponent.getComponents().isEmpty()) continue;
            for (Component component : categoryComponent.getComponents()) {
                component.keyTyped(typedChar, keyCode);
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (CategoryComponent categoryComponent : this.categoryComponents) {
            categoryComponent.setDrag(false);
        }
        for (CategoryComponent categoryComponent : this.categoryComponents) {
            if (!categoryComponent.isOpen() || categoryComponent.getComponents().isEmpty()) continue;
            for (Component component : categoryComponent.getComponents()) {
                component.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    private void scrollWheelCheck() {
        block3: {
            int dWheel;
            block2: {
                dWheel = Mouse.getDWheel();
                if (dWheel >= 0) break block2;
                for (CategoryComponent categoryComponent : this.categoryComponents) {
                    categoryComponent.setY(categoryComponent.getY() - Gui.INSTANCE.scrollSpeed.getValue());
                }
                break block3;
            }
            if (dWheel <= 0) break block3;
            for (CategoryComponent categoryComponent : this.categoryComponents) {
                categoryComponent.setY(categoryComponent.getY() + Gui.INSTANCE.scrollSpeed.getValue());
            }
        }
    }
}

