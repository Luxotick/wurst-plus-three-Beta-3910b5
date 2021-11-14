/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.travis.wurstplusthree.gui.component.components;

import java.awt.Color;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.ColorCopyEvent;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.component.HackButton;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.util.RenderUtil2D;
import org.lwjgl.input.Mouse;

public class ColorComponent
extends Component {
    private final ColourSetting set;
    private final HackButton parent;
    private Color finalColor;
    private int offset;
    private boolean isOpen;
    private boolean firstTimeOpen;
    private final int booleanButtonOffset = 80;
    boolean pickingColor = false;
    boolean pickingHue = false;
    boolean pickingAlpha = false;

    public ColorComponent(ColourSetting value, HackButton button, int offset) {
        this.set = value;
        this.parent = button;
        this.offset = offset;
        this.isOpen = false;
        this.firstTimeOpen = true;
        this.setShown(true);
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        RenderUtil2D.drawRectMC(this.parent.parent.getX() + 5, this.parent.parent.getY() + this.offset + 0, this.parent.parent.getX() + 5 + 95, this.parent.parent.getY() + this.offset + 16 + 0, Gui.INSTANCE.buttonColor.getValue().hashCode());
        RenderUtil2D.drawRectMC(this.parent.parent.getX() + this.parent.parent.getWidth() - 5 - 5, this.parent.parent.getY() + this.offset + 0, this.parent.parent.getX() + this.parent.parent.getWidth() - 5, this.parent.parent.getY() + this.offset + 16 + 0, Gui.INSTANCE.buttonColor.getValue().hashCode());
        RenderUtil2D.drawRectMC(this.parent.parent.getX() + 5 + 95, this.parent.parent.getY() + this.offset + 0, this.parent.parent.getX() + this.parent.parent.getWidth() - 5 - 5, this.parent.parent.getY() + this.offset + 0 + 3, Gui.INSTANCE.buttonColor.getValue().hashCode());
        RenderUtil2D.drawRectMC(this.parent.parent.getX() + 5 + 95, this.parent.parent.getY() + this.offset + 16 + 0, this.parent.parent.getX() + this.parent.parent.getWidth() - 5 - 5, this.parent.parent.getY() + this.offset + 16 + 0 - 3, Gui.INSTANCE.buttonColor.getValue().hashCode());
        RenderUtil2D.drawBorderedRect(this.parent.parent.getX() + 5 + 95, this.parent.parent.getY() + this.offset + 0 + 3, this.parent.parent.getX() + this.parent.parent.getWidth() - 5 - 5, this.parent.parent.getY() + this.offset + 16 + 0 - 3, 1, this.set.getValue().hashCode(), this.set.isChild() ? WurstplusGuiNew.GUI_CHILDBUTTON() : WurstplusGuiNew.GUI_COLOR(), false);
        if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.set.getName(), this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
        } else {
            ColorComponent.mc.fontRenderer.drawStringWithShadow(this.set.getName(), (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
        }
        if (this.isOpen) {
            WurstplusGuiNew.drawRect((int)(this.parent.parent.getX() + 5), (int)(this.parent.parent.getY() + this.offset + 0 + 16), (int)(this.parent.parent.getX() + this.parent.parent.getWidth() - 5), (int)(this.parent.parent.getY() + this.offset + 16 + 0 + 80), (int)(this.set.isChild() ? WurstplusGuiNew.GUI_CHILDBUTTON() : WurstplusGuiNew.GUI_COLOR()));
            this.drawPicker(this.set, this.parent.parent.getX() + 7, this.parent.parent.getY() + this.offset + 19, this.parent.parent.getX() + 100, this.parent.parent.getY() + this.offset + 19, this.parent.parent.getX() + 7, this.parent.parent.getY() + this.offset + 72, mouseX, mouseY);
            this.set.setValue(this.finalColor);
            RenderUtil2D.drawBorderedRect(this.parent.parent.getX() + 5 + 85, this.parent.parent.getY() + this.offset + 4 + 0 + 80, this.parent.parent.getX() + 115 - 5, this.parent.parent.getY() + this.offset + 16 + 0 - 2 + 80, 1, this.set.getRainbow() ? new Color(this.set.getValue().getRed(), this.set.getValue().getGreen(), this.set.getValue().getBlue(), 255).hashCode() : (this.set.isChild() ? WurstplusGuiNew.GUI_CHILDBUTTON() : WurstplusGuiNew.GUI_COLOR()), new Color(0, 0, 0, 200).hashCode(), false);
            RenderUtil2D.drawRectMC(this.parent.parent.getX() + 5 + (this.set.getRainbow() ? 95 : 88), this.parent.parent.getY() + this.offset + 6 + 0 + 80, this.parent.parent.getX() + (this.set.getRainbow() ? 112 : 105) - 5, this.parent.parent.getY() + this.offset + 16 + 0 - 4 + 80, new Color(50, 50, 50, 255).hashCode());
            if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
                WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow("Rainbow", this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 5 + 0 + 80, Gui.INSTANCE.fontColor.getValue().hashCode());
            } else {
                ColorComponent.mc.fontRenderer.drawStringWithShadow("Rainbow", (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 5 + 0 + 80), Gui.INSTANCE.fontColor.getValue().hashCode());
            }
        }
    }

    @Override
    public void renderToolTip(int mouseX, int mouseY) {
        if (this.isMouseOnButton(mouseX, mouseY) && this.parent.isOpen && Gui.INSTANCE.toolTips.getValue().booleanValue()) {
            String hex = String.format("#%02x%02x%02x", this.set.getValue().getRed(), this.set.getValue().getGreen(), this.set.getValue().getBlue());
            String text = "R: " + this.set.getValue().getRed() + " G: " + this.set.getValue().getGreen() + " B: " + this.set.getValue().getBlue() + " A: " + this.set.getValue().getAlpha() + "  " + hex;
            int length = WurstplusThree.GUI_FONT_MANAGER.getTextWidth(text);
            int height = WurstplusThree.GUI_FONT_MANAGER.getTextHeight();
            RenderUtil2D.drawRectMC(mouseX + 1, mouseY + 4, mouseX + length + 5, mouseY + height + 8, Gui.INSTANCE.toolTipColor.getValue().hashCode());
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(text, mouseX + 3, mouseY + 8, new Color(255, 255, 255).hashCode());
        }
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        ColorCopyEvent event;
        if (!this.isShown()) {
            return;
        }
        if (this.isMouseOnButton(mouseX, mouseY) && this.parent.isOpen && button == 1) {
            for (Component comp : this.parent.parent.getComponents()) {
                if (!(comp instanceof HackButton) || !((HackButton)comp).isOpen) continue;
                for (Component comp2 : ((HackButton)comp).getChildren()) {
                    if (!(comp2 instanceof ColorComponent) || !((ColorComponent)comp2).isOpen || comp2 == this) continue;
                    ((ColorComponent)comp2).setOpen(false);
                    this.parent.parent.refresh();
                }
            }
            this.setOpen(!this.isOpen);
            this.parent.parent.refresh();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && this.parent.isOpen && button == 0) {
            WurstplusThree.GUI2.colorClipBoard = this.set.getValue();
            event = new ColorCopyEvent(this.set.getValue(), this, ColorCopyEvent.EventType.COPY);
            WurstplusThree.EVENT_PROCESSOR.postEvent(event);
        }
        if (this.isMouseOnButton(mouseX, mouseY) && this.parent.isOpen && button == 2) {
            this.set.setValue(WurstplusThree.GUI2.colorClipBoard);
            event = new ColorCopyEvent(this.set.getValue(), this, ColorCopyEvent.EventType.PAST);
            WurstplusThree.EVENT_PROCESSOR.postEvent(event);
        }
        if (!this.isOpen && !this.firstTimeOpen) {
            this.firstTimeOpen = true;
        }
        if (this.isOpen && this.firstTimeOpen) {
            boolean flag = false;
            for (Component component : this.parent.getChildren()) {
                if (!flag && component == this) {
                    flag = true;
                    continue;
                }
                if (!flag) continue;
                component.setOff(component.getOffset() + 80);
            }
            this.firstTimeOpen = false;
        }
        if (this.isOpen && ColorComponent.mouseOver(this.parent.parent.getX() + 5 + 85, this.parent.parent.getY() + this.offset + 4 + 0 + 80, this.parent.parent.getX() + 115 - 5, this.parent.parent.getY() + this.offset + 16 + 0 - 2 + 80, mouseX, mouseY)) {
            this.set.setRainbow(!this.set.getRainbow());
        }
    }

    public void drawPicker(ColourSetting subColor, int pickerX, int pickerY, int hueSliderX, int hueSliderY, int alphaSliderX, int alphaSliderY, int mouseX, int mouseY) {
        float restrictedX;
        float[] color = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        try {
            color = new float[]{Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[0], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[1], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[2], (float)subColor.getColor().getAlpha() / 255.0f};
        }
        catch (Exception exception) {
            // empty catch block
        }
        int pickerWidth = 90;
        int pickerHeight = 51;
        int hueSliderWidth = 10;
        int hueSliderHeight = 59;
        int alphaSliderHeight = 10;
        int alphaSliderWidth = 90;
        if (!(this.pickingColor || this.pickingHue || this.pickingAlpha)) {
            if (Mouse.isButtonDown((int)0) && ColorComponent.mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY)) {
                this.pickingColor = true;
            } else if (Mouse.isButtonDown((int)0) && ColorComponent.mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight, mouseX, mouseY)) {
                this.pickingHue = true;
            } else if (Mouse.isButtonDown((int)0) && ColorComponent.mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + alphaSliderWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY)) {
                this.pickingAlpha = true;
            }
        }
        if (this.pickingHue) {
            float restrictedY = Math.min(Math.max(hueSliderY, mouseY), hueSliderY + hueSliderHeight);
            color[0] = (restrictedY - (float)hueSliderY) / (float)hueSliderHeight;
            color[0] = (float)Math.min(0.97, (double)color[0]);
        }
        if (this.pickingAlpha) {
            restrictedX = Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + pickerWidth);
            color[3] = 1.0f - (restrictedX - (float)alphaSliderX) / (float)pickerWidth;
        }
        if (this.pickingColor) {
            restrictedX = Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
            color[1] = (restrictedX - (float)pickerX) / (float)pickerWidth;
            color[2] = 1.0f - (restrictedY - (float)pickerY) / (float)pickerHeight;
            color[2] = (float)Math.max(0.04000002, (double)color[2]);
            color[1] = (float)Math.max(0.022222223, (double)color[1]);
        }
        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);
        float selectedRed = (float)(selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (float)(selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (float)(selectedColor & 0xFF) / 255.0f;
        RenderUtil2D.drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, 255.0f);
        this.drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);
        int cursorX = (int)((float)pickerX + color[1] * (float)pickerWidth);
        int cursorY = (int)((float)(pickerY + pickerHeight) - color[2] * (float)pickerHeight);
        RenderUtil2D.drawRectMC(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, -1);
        this.finalColor = ColorComponent.alphaIntegrate(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);
        this.drawAlphaSlider(alphaSliderX, alphaSliderY, pickerWidth, alphaSliderHeight, (float)this.finalColor.getRed() / 255.0f, (float)this.finalColor.getGreen() / 255.0f, (float)this.finalColor.getBlue() / 255.0f, color[3]);
    }

    public static Color alphaIntegrate(Color color, float alpha) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        return new Color(red, green, blue, alpha);
    }

    public void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step2 = 0;
        if (height > width) {
            RenderUtil2D.drawRectMC(x, y, x + width, y + 4, -65536);
            y += 4;
            for (int colorIndex = 0; colorIndex < 6; ++colorIndex) {
                int previousStep = Color.HSBtoRGB((float)step2 / 6.0f, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float)(step2 + 1) / 6.0f, 1.0f, 1.0f);
                RenderUtil2D.drawGradientRect(x, (float)y + (float)step2 * ((float)height / 6.0f), x + width, (float)y + (float)(step2 + 1) * ((float)height / 6.0f), previousStep, nextStep, false);
                ++step2;
            }
            int sliderMinY = (int)((float)y + (float)height * hue) - 4;
            RenderUtil2D.drawRectMC(x, sliderMinY - 1, x + width, sliderMinY + 1, -1);
        } else {
            for (int colorIndex = 0; colorIndex < 6; ++colorIndex) {
                int previousStep = Color.HSBtoRGB((float)step2 / 6.0f, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float)(step2 + 1) / 6.0f, 1.0f, 1.0f);
                RenderUtil2D.gradient(x + step2 * (width / 6), y, x + (step2 + 1) * (width / 6), y + height, previousStep, nextStep, true);
                ++step2;
            }
            int sliderMinX = (int)((float)x + (float)width * hue);
            RenderUtil2D.drawRectMC(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        }
    }

    public void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
        boolean left = true;
        int checkerBoardSquareSize = height / 2;
        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                RenderUtil2D.drawRectMC(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, -1);
                RenderUtil2D.drawRectMC(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, -7303024);
                if (squareIndex < width - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    RenderUtil2D.drawRectMC(minX, y, maxX, y + height, -7303024);
                    RenderUtil2D.drawRectMC(minX, y + checkerBoardSquareSize, maxX, y + height, -1);
                }
            }
            left = !left;
        }
        RenderUtil2D.drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1.0f).getRGB(), 0);
        int sliderMinX = (int)((float)(x + width) - (float)width * alpha);
        RenderUtil2D.drawRectMC(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.pickingColor = false;
        this.pickingHue = false;
        this.pickingAlpha = false;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + 5 && x < this.parent.parent.getX() + 120 - 5 && y > this.parent.parent.getY() + this.offset + 0 && y < this.parent.parent.getY() + this.offset + 16 + 0;
    }

    public void setOpen(boolean v) {
        this.isOpen = v;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public HackButton getParent() {
        return this.parent;
    }

    @Override
    public int getOffset() {
        return this.offset;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        boolean old = this.isShown();
        this.setShown(this.set.isShown());
        if (old != this.isShown()) {
            this.parent.init(this.parent.mod, this.parent.parent, this.parent.offset, true);
        }
    }

    public static boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }
}

