/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package me.travis.wurstplusthree.hack.hacks.client;

import java.util.Arrays;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.client.gui.GuiScreen;

@Hack.Registration(name="Gui", description="swag custom gui", category=Hack.Category.CLIENT, isListening=true, bind=54)
public class Gui
extends Hack {
    public static Gui INSTANCE;
    public ParentSetting colors = new ParentSetting("Colours", this);
    public ColourSetting headButtonColor = new ColourSetting("Head Button", new Colour(255, 150, 90, 255), this.colors);
    public ColourSetting modColor = new ColourSetting("Mod Color", new Colour(45, 45, 45, 255), this.colors);
    public ColourSetting settingColor = new ColourSetting("Setting Color", new Colour(25, 25, 25, 255), this.colors);
    public ColourSetting settingColorHover = new ColourSetting("Setting Color Hover", new Colour(20, 20, 20, 255), this.colors);
    public ColourSetting buttonColor = new ColourSetting("Button", new Colour(224, 156, 96, 255), this.colors);
    public ColourSetting fontColor = new ColourSetting("Font", new Colour(255, 255, 255, 255), this.colors);
    public ColourSetting groupColor = new ColourSetting("Group Setting", new Colour(45, 45, 45, 255), this.colors);
    public ColourSetting groupHoverColor = new ColourSetting("Group Hover Color", new Colour(32, 32, 32, 255), this.colors);
    public IntSetting rainbowDelay = new IntSetting("RainbowDelay", 100, 0, 5000, this);
    public EnumSetting type = new EnumSetting("Type", "None", Arrays.asList("None", "Rainbow", "Sin"), this);
    public EnumSetting SinMode = new EnumSetting("Sine Mode", "Special", Arrays.asList("Special", "Hue", "Saturation", "Brightness"), this);
    public IntSetting scrollSpeed = new IntSetting("ScrollSpeed", 15, 1, 100, this);
    public BooleanSetting blur = new BooleanSetting("Blur", (Boolean)true, (Hack)this);
    public ParentSetting gradientParent = new ParentSetting("Gradient", this);
    public BooleanSetting gradient = new BooleanSetting("Enabled", (Boolean)false, this.gradientParent);
    public ColourSetting gradientStartColor = new ColourSetting("GradientStartColor", new Colour(255, 122, 5, 100), this.gradientParent);
    public ColourSetting gradientEndColor = new ColourSetting("GradientEndColor", new Colour(255, 122, 5, 100), this.gradientParent);
    public BooleanSetting animation = new BooleanSetting("Animation", (Boolean)true, (Hack)this);
    public IntSetting animationStages = new IntSetting("AnimationStages", 250, 1, 1000, this);
    public EnumSetting arrowType = new EnumSetting("ArrowType", "Off", Arrays.asList("Off", "Type1", "Type2"), this);
    public IntSetting mouseDelay = new IntSetting("MouseDelay", 250, 100, 300, this);
    public BooleanSetting customScreen = new BooleanSetting("CustomMenu", (Boolean)true, (Hack)this);
    public BooleanSetting customFont = new BooleanSetting("CustomFont", (Boolean)true, (Hack)this);
    public ParentSetting toolTipsParent = new ParentSetting("ToolTips", this);
    public BooleanSetting toolTips = new BooleanSetting("Show ToolTips", (Boolean)true, this.toolTipsParent);
    public ColourSetting toolTipColor = new ColourSetting("ToolTipColour", new Colour(0, 0, 0, 100), this.toolTipsParent);

    public Gui() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen((GuiScreen)WurstplusThree.GUI2);
        this.disable();
    }
}

