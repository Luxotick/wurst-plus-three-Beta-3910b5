/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.travis.wurstplusthree.gui.component.components;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.gui.component.HackButton;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.setting.type.KeySetting;
import me.travis.wurstplusthree.util.RenderUtil2D;
import org.lwjgl.input.Keyboard;

public class ModuleBindComponent
extends Component {
    private boolean isHovered;
    private boolean isBinding;
    private final HackButton parent;
    private KeySetting setting;
    private int offset;
    private int x;
    private int y;
    private final boolean normal;
    private Hack module;

    public ModuleBindComponent(HackButton button, int offset) {
        this.parent = button;
        this.offset = offset;
        this.normal = true;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.setShown(true);
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        if (this.normal) {
            this.module = this.parent.mod;
            RenderUtil2D.drawRectMC(this.parent.parent.getX() + 5, this.parent.parent.getY() + this.offset + 0, this.parent.parent.getX() + this.parent.parent.getWidth() - 5, this.parent.parent.getY() + this.offset + 16 + 0, this.isHovered ? WurstplusGuiNew.GUI_HOVERED_COLOR() : WurstplusGuiNew.GUI_COLOR());
            if (this.module.getBind() == -1) {
                if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
                    WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.isBinding ? "Listening..." : (this.module.isHold() ? "Hold" : "Toggle") + " - NONE", this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
                } else {
                    ModuleBindComponent.mc.fontRenderer.drawStringWithShadow(this.isBinding ? "Listening..." : (this.module.isHold() ? "Hold" : "Toggle") + " - NONE", (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
                }
            } else if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
                WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.isBinding ? "Listening..." : (this.module.isHold() ? "Hold" : "Toggle") + " - " + this.getRenderKey(), this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
            } else {
                ModuleBindComponent.mc.fontRenderer.drawStringWithShadow(this.isBinding ? "Listening..." : (this.module.isHold() ? "Hold" : "Toggle") + " - " + this.getRenderKey(), (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
            }
        } else {
            RenderUtil2D.drawRectMC(this.parent.parent.getX() + 5, this.parent.parent.getY() + this.offset + 0, this.parent.parent.getX() + this.parent.parent.getWidth() - 5, this.parent.parent.getY() + this.offset + 16 + 0, this.isHovered ? WurstplusGuiNew.GUI_HOVERED_COLOR() : WurstplusGuiNew.GUI_COLOR());
            if (this.setting.getKey() == -1) {
                if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
                    WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.isBinding ? "Listening..." : (this.module.isHold() ? "Hold" : "Toggle") + " - NONE", this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
                } else {
                    ModuleBindComponent.mc.fontRenderer.drawStringWithShadow(this.isBinding ? "Listening..." : (this.module.isHold() ? "Hold" : "Toggle") + " - NONE", (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
                }
            } else if (Gui.INSTANCE.customFont.getValue().booleanValue()) {
                WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.isBinding ? "Listening..." : (this.module.isHold() ? "Hold" : "Toggle") + " - " + this.getRenderKey(), this.parent.parent.getX() + 12, this.parent.parent.getY() + this.offset + 3 + 0, Gui.INSTANCE.fontColor.getValue().hashCode());
            } else {
                ModuleBindComponent.mc.fontRenderer.drawStringWithShadow(this.isBinding ? "Listening..." : (this.module.isHold() ? "Hold" : "Toggle") + " - " + this.getRenderKey(), (float)(this.parent.parent.getX() + 12), (float)(this.parent.parent.getY() + this.offset + 3 + 0), Gui.INSTANCE.fontColor.getValue().hashCode());
            }
        }
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.parent.getX() + 5 && x < this.parent.parent.getX() + 120 - 5 && y > this.parent.parent.getY() + this.offset + 0 && y < this.parent.parent.getY() + this.offset + 16 + 0;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isBinding) {
            this.isBinding = false;
            if (this.normal) {
                switch (button) {
                    case 0: {
                        this.module.setBind(-2);
                        break;
                    }
                    case 1: {
                        this.module.setBind(-3);
                        break;
                    }
                    case 2: {
                        this.module.setBind(-4);
                        break;
                    }
                    case 3: {
                        this.module.setBind(-5);
                        break;
                    }
                    case 4: {
                        this.module.setBind(-6);
                    }
                }
            } else {
                WurstplusThree.LOGGER.info((Object)button);
                switch (button) {
                    case 0: {
                        this.setting.setKey(-2);
                        break;
                    }
                    case 1: {
                        this.setting.setKey(-3);
                        break;
                    }
                    case 2: {
                        this.setting.setKey(-4);
                        break;
                    }
                    case 3: {
                        this.setting.setKey(-5);
                        break;
                    }
                    case 4: {
                        this.setting.setKey(-6);
                    }
                }
            }
            return;
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen) {
            boolean bl = this.isBinding = !this.isBinding;
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1 && this.parent.isOpen) {
            this.module.toggleHold();
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (this.isBinding) {
            if (this.normal) {
                this.module.setBind(key);
                if (key == 211) {
                    this.module.setBind(-1);
                }
            } else {
                this.setting.setKey(key);
                if (key == 211) {
                    this.setting.setKey(-1);
                }
            }
            this.isBinding = false;
        }
    }

    @Override
    public HackButton getParent() {
        return this.parent;
    }

    @Override
    public int getOffset() {
        return this.offset;
    }

    private String getRenderKey() {
        if (this.normal) {
            if (this.module == null) {
                return "NONE";
            }
            switch (this.module.getBind()) {
                case -2: {
                    return "M0";
                }
                case -3: {
                    return "M1";
                }
                case -4: {
                    return "M2";
                }
                case -5: {
                    return "M3";
                }
                case -6: {
                    return "M4";
                }
            }
            return Keyboard.getKeyName((int)this.module.getBind());
        }
        switch (this.setting.getKey()) {
            case -2: {
                return "M0";
            }
            case -3: {
                return "M1";
            }
            case -4: {
                return "M2";
            }
            case -5: {
                return "M3";
            }
            case -6: {
                return "M4";
            }
        }
        return Keyboard.getKeyName((int)this.setting.getKey());
    }
}

