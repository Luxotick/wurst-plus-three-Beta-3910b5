/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.gui.component;

import java.awt.Color;
import java.util.ArrayList;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.gui.component.CategoryComponent;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.component.components.BooleanComponent;
import me.travis.wurstplusthree.gui.component.components.ColorComponent;
import me.travis.wurstplusthree.gui.component.components.KeyBindComponent;
import me.travis.wurstplusthree.gui.component.components.ModeComponent;
import me.travis.wurstplusthree.gui.component.components.ModuleBindComponent;
import me.travis.wurstplusthree.gui.component.components.ParentComponent;
import me.travis.wurstplusthree.gui.component.components.ShownComponent;
import me.travis.wurstplusthree.gui.component.components.SliderComponent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.setting.Setting;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.setting.type.KeySetting;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.RenderUtil2D;
import org.lwjgl.opengl.GL11;

public class HackButton
extends Component {
    public Hack mod;
    public CategoryComponent parent;
    private ArrayList<Component> subcomponents;
    private ArrayList<Setting> notInitSettings;
    private ArrayList<Boolean> oldVals;
    public boolean isOpen;
    private boolean isHovered;
    public int offset;
    public int subCompLength = 0;
    public int opY;

    public ArrayList<Component> getChildren() {
        ArrayList<Component> children = new ArrayList<Component>();
        for (Component component : this.subcomponents) {
            if (component.getParent() != this) continue;
            children.add(component);
        }
        return children;
    }

    public void init(Hack mod, CategoryComponent parent, int offset, boolean update) {
        if (!update) {
            this.mod = mod;
            this.parent = parent;
            this.offset = offset;
        }
        this.subcomponents = new ArrayList();
        this.notInitSettings = new ArrayList();
        this.oldVals = new ArrayList();
        if (!update) {
            this.isOpen = false;
        }
        this.opY = offset + 16 + 0;
        if (WurstplusThree.SETTINGS.getSettingFromHack(mod) != null) {
            for (Setting s : WurstplusThree.SETTINGS.getSettingFromHack(mod)) {
                if (s instanceof BooleanSetting) {
                    if (!s.isShown()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new BooleanComponent((BooleanSetting)s, this, this.opY));
                    this.opY += 16;
                    continue;
                }
                if (s instanceof EnumSetting) {
                    if (!s.isShown()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new ModeComponent((EnumSetting)s, this, mod, this.opY));
                    this.opY += 16;
                    continue;
                }
                if (s instanceof IntSetting) {
                    if (!s.isShown()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new SliderComponent((IntSetting)s, this, this.opY));
                    this.opY += 16;
                    continue;
                }
                if (s instanceof DoubleSetting) {
                    if (!s.isShown()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new SliderComponent((DoubleSetting)s, this, this.opY));
                    this.opY += 16;
                    continue;
                }
                if (s instanceof ColourSetting) {
                    if (!s.isShown()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new ColorComponent((ColourSetting)s, this, this.opY));
                    this.opY += 16;
                    continue;
                }
                if (s instanceof KeySetting) {
                    if (!s.isShown()) {
                        this.notInitSettings.add(s);
                        continue;
                    }
                    this.subcomponents.add(new KeyBindComponent((KeySetting)s, this, this.opY));
                    this.opY += 16;
                    continue;
                }
                if (!(s instanceof ParentSetting)) continue;
                if (!s.isShown()) {
                    this.notInitSettings.add(s);
                    continue;
                }
                this.subcomponents.add(new ParentComponent((ParentSetting)s, this, this.opY));
                this.opY += 16;
            }
            this.subcomponents.add(new ModuleBindComponent(this, this.opY));
            this.opY += 16;
            this.subcomponents.add(new ShownComponent(this, this.opY));
            if (update) {
                parent.refresh();
            }
        }
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
        int opY = this.offset + 16 + 0;
        for (Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 16;
        }
    }

    @Override
    public void renderComponent(int MouseX, int MouseY) {
        this.subCompLength = 0;
        if (this.mod.isEnabled()) {
            RenderUtil2D.drawGradientRect(this.parent.getX() + 5, this.parent.getY() + this.offset + 0, this.parent.getX() + this.parent.getWidth() - 5, this.parent.getY() + 16 + this.offset + 0, Gui.INSTANCE.buttonColor.getValue().hashCode(), Gui.INSTANCE.buttonColor.getValue().hashCode(), this.isHovered);
        } else {
            RenderUtil2D.drawRectMC(this.parent.getX() + 5, this.parent.getY() + this.offset + 0, this.parent.getX() + this.parent.getWidth() - 5, this.parent.getY() + 16 + this.offset + 0, this.isHovered ? WurstplusGuiNew.GUI_HOVERED_COLOR() : WurstplusGuiNew.GUI_MODULECOLOR());
        }
        if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.mod.getName(), this.parent.getX() + 6, (float)(this.parent.getY() + this.offset + 0) + 8.0f - 4.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
        } else {
            HackButton.mc.fontRenderer.drawStringWithShadow(this.mod.getName(), (float)(this.parent.getX() + 6), (float)(this.parent.getY() + this.offset + 0) + 8.0f - 4.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
        }
        if (this.isOpen && !this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                if (!comp.isShown()) continue;
                comp.renderComponent(MouseX, MouseY);
                if (comp instanceof ColorComponent) {
                    if (((ColorComponent)comp).isOpen()) {
                        this.subCompLength += 6;
                        continue;
                    }
                    ++this.subCompLength;
                    continue;
                }
                ++this.subCompLength;
            }
        }
        this.renderArrow();
    }

    @Override
    public void renderToolTip(int mouseX, int mouseY) {
        if (this.isHovered && Gui.INSTANCE.toolTips.getValue().booleanValue()) {
            int length = WurstplusThree.GUI_FONT_MANAGER.getTextWidth(this.mod.getDescription());
            int height = WurstplusThree.GUI_FONT_MANAGER.getTextHeight();
            RenderUtil2D.drawRectMC(mouseX + 6, mouseY + 9, mouseX + length + 10, mouseY + height + 13, Gui.INSTANCE.toolTipColor.getValue().hashCode());
            WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.mod.getDescription(), mouseX + 8, mouseY + 11, new Color(255, 255, 255).hashCode());
        }
        for (Component component : this.subcomponents) {
            component.renderToolTip(mouseX, mouseY);
        }
    }

    @Override
    public int getHeight() {
        if (this.isOpen) {
            int val = 0;
            for (Component c : this.subcomponents) {
                if (!c.isShown()) {
                    --val;
                }
                if (!(c instanceof ColorComponent) || !((ColorComponent)c).isOpen()) continue;
                val += 5;
            }
            return 16 * (this.subcomponents.size() + 1 + val);
        }
        return 16;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        block7: {
            this.isHovered = this.isMouseOnButton(mouseX, mouseY);
            if (!this.subcomponents.isEmpty()) {
                for (Component comp : this.subcomponents) {
                    comp.updateComponent(mouseX, mouseY);
                }
            }
            if (this.notInitSettings.isEmpty()) break block7;
            if (this.oldVals.isEmpty()) {
                for (Setting s : this.notInitSettings) {
                    this.oldVals.add(s.isShown());
                }
            } else {
                int index = 0;
                for (Setting s : this.notInitSettings) {
                    boolean old = this.oldVals.get(index);
                    boolean init = false;
                    if (s.isShown() != old) {
                        init = true;
                    }
                    ++index;
                    if (!init) continue;
                    this.oldVals.clear();
                    this.init(this.mod, this.parent, this.offset, true);
                    break;
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.isOpen = !this.isOpen;
            this.parent.refresh();
            for (Component comp : this.parent.getComponents()) {
                if (!(comp instanceof HackButton) || !((HackButton)comp).isOpen || !((HackButton)comp).isOpen) continue;
                for (Component comp2 : ((HackButton)comp).getChildren()) {
                    if (!(comp2 instanceof ColorComponent)) continue;
                    ((ColorComponent)comp2).setOpen(false);
                    this.parent.refresh();
                }
            }
        }
        for (Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        for (Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.getX() + 5 && x < this.parent.getX() + this.parent.getWidth() - 5 && y > this.parent.getY() + this.offset && y < this.parent.getY() + 16 + 0 + this.offset;
    }

    private void renderArrow() {
        switch (Gui.INSTANCE.arrowType.getValue()) {
            case "Type1": {
                if (this.isOpen) {
                    RenderUtil.drawTriangleOutline((float)this.parent.getX() + 105.0f, (float)(this.parent.getY() + this.offset) + 12.0f, 5.0f, 2.0f, 1.0f, 1.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
                    break;
                }
                RenderUtil.drawTriangleOutline((float)this.parent.getX() + 105.0f, (float)(this.parent.getY() + this.offset) + 12.0f, 5.0f, 1.0f, 2.0f, 1.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
                break;
            }
            case "Type2": {
                if (this.isOpen) {
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)((float)this.parent.getX() + 102.0f), (double)((float)(this.parent.getY() + this.offset) + 12.0f), (double)0.0);
                    GL11.glRotatef((float)-90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslated((double)(-((float)this.parent.getX() + 102.0f)), (double)(-((float)(this.parent.getY() + this.offset) + 12.0f)), (double)0.0);
                    RenderUtil.drawTriangleOutline((float)this.parent.getX() + 105.0f, (float)(this.parent.getY() + this.offset) + 12.0f, 5.0f, 2.0f, 1.0f, 1.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
                    GL11.glPopMatrix();
                    break;
                }
                RenderUtil.drawTriangleOutline((float)this.parent.getX() + 105.0f, (float)(this.parent.getY() + this.offset) + 12.0f, 5.0f, 2.0f, 1.0f, 1.0f, Gui.INSTANCE.fontColor.getValue().hashCode());
            }
        }
    }
}

