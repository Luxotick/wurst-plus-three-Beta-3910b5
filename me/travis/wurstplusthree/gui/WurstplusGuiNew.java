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
package me.travis.wurstplusthree.gui;

import java.io.IOException;
import java.util.ArrayList;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.ColorCopyEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.gui.component.CategoryComponent;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import org.lwjgl.input.Mouse;

public class WurstplusGuiNew
extends GuiScreen {
    public static final int WIDTH = 120;
    public static final int HEIGHT = 16;
    public static final int MODULE_WIDTH = 5;
    public static final int MODULE_OFFSET = 0;
    public static final int SETTING_OFFSET = 5;
    public static final int FONT_HEIGHT = 4;
    public static final int MODULE_FONT_SIZE = 6;
    public static final int SUB_FONT_SIZE = 12;
    public static final int COLOR_FONT_SIZE = 24;
    public Colour colorClipBoard;
    public ColorCopyEvent colorEvent;
    public boolean shouldShow;
    private boolean flag = false;
    public static ArrayList<CategoryComponent> categoryComponents;

    public static int GUI_MODULECOLOR() {
        return Gui.INSTANCE.modColor.getValue().hashCode();
    }

    public static int GUI_COLOR() {
        return Gui.INSTANCE.settingColor.getValue().hashCode();
    }

    public static int GUI_CHILDBUTTON() {
        return Gui.INSTANCE.settingColor.getValue().hashCode();
    }

    public static int GUI_HOVERED_COLOR() {
        return Gui.INSTANCE.settingColorHover.getValue().hashCode();
    }

    public WurstplusGuiNew() {
        categoryComponents = new ArrayList();
        this.colorClipBoard = new Colour(0, 0, 0);
        int startX = 10;
        for (Hack.Category category : WurstplusThree.HACKS.getCategories()) {
            CategoryComponent categoryComponent = new CategoryComponent(category);
            categoryComponent.setX(startX);
            categoryComponents.add(categoryComponent);
            startX += categoryComponent.getWidth() + 10;
            this.flag = false;
        }
    }

    public void initGui() {
        WurstplusThree.EVENT_PROCESSOR.addEventListener((Object)this);
        this.shouldShow = false;
        this.flag = false;
        for (CategoryComponent c : categoryComponents) {
            c.animationValue = 0.0f;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.scrollWheelCheck();
        ScaledResolution sr = new ScaledResolution(this.mc);
        boolean gradientShadow = Gui.INSTANCE.gradient.getValue();
        if (gradientShadow) {
            this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Gui.INSTANCE.gradientStartColor.getValue().getRGB(), Gui.INSTANCE.gradientEndColor.getValue().getRGB());
        }
        if (!this.flag && Gui.INSTANCE.animation.getValue().booleanValue()) {
            this.animate(sr);
        }
        for (CategoryComponent categoryComponent : categoryComponents) {
            categoryComponent.renderFrame(mouseX, mouseY);
            categoryComponent.updatePosition(mouseX, mouseY);
            for (Component comp : categoryComponent.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
        for (CategoryComponent categoryComponent : categoryComponents) {
            for (Component component : categoryComponent.getComponents()) {
                component.renderToolTip(mouseX, mouseY);
            }
        }
        if (!this.shouldShow || this.colorEvent != null) {
            // empty if block
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (CategoryComponent categoryComponent : categoryComponents) {
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
        for (CategoryComponent categoryComponent : categoryComponents) {
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
        for (CategoryComponent categoryComponent : categoryComponents) {
            categoryComponent.setDrag(false);
        }
        for (CategoryComponent categoryComponent : categoryComponents) {
            if (!categoryComponent.isOpen() || categoryComponent.getComponents().isEmpty()) continue;
            for (Component component : categoryComponent.getComponents()) {
                component.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    public void onGuiClosed() {
        WurstplusThree.CONFIG_MANAGER.saveConfig();
        WurstplusThree.EVENT_PROCESSOR.removeEventListener((Object)this);
    }

    private void scrollWheelCheck() {
        block3: {
            int dWheel;
            block2: {
                dWheel = Mouse.getDWheel();
                if (dWheel >= 0) break block2;
                for (CategoryComponent categoryComponent : categoryComponents) {
                    categoryComponent.setY(categoryComponent.getY() - Gui.INSTANCE.scrollSpeed.getValue());
                }
                break block3;
            }
            if (dWheel <= 0) break block3;
            for (CategoryComponent categoryComponent : categoryComponents) {
                categoryComponent.setY(categoryComponent.getY() + Gui.INSTANCE.scrollSpeed.getValue());
            }
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public static ArrayList<CategoryComponent> getCategories() {
        return categoryComponents;
    }

    private void animate(ScaledResolution sr) {
        int deltaTime = WurstplusThree.RENDER_UTIL_2D.getDeltaTime();
        for (CategoryComponent c : categoryComponents) {
            float SEQUENCES = Gui.INSTANCE.animationStages.getValue().intValue();
            int y = 500;
            if (c.animationValue < 500.0f) {
                c.animationValue += 500.0f * ((float)deltaTime / SEQUENCES);
            }
            c.animationValue = WurstplusGuiNew.constrainToRange(c.animationValue, 0.0f, 500.0f);
            float newY = (float)sr.getScaledHeight() - c.animationValue - 2.0f;
            c.setY((int)newY);
        }
        int i = 0;
        for (CategoryComponent c : categoryComponents) {
            if ((double)c.getY() <= 7.0) {
                ++i;
                continue;
            }
            if (c.getY() <= 38 && this.mc.gameSettings.fullScreen) {
                ++i;
                continue;
            }
            if (c.getY() > 578 || this.mc.gameSettings.guiScale != 1) continue;
            ++i;
        }
        if (i == categoryComponents.size()) {
            this.flag = true;
        }
    }

    private static float constrainToRange(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    @CommitEvent
    public void ColorCopyEvent(ColorCopyEvent event) {
        this.colorEvent = event;
        this.shouldShow = true;
    }
}

