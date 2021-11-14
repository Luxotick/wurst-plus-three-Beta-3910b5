/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package me.travis.wurstplusthree.hack.hacks.client;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.client.gui.GuiScreen;

@Hack.Registration(name="Hud Editor", description="swag custom Hud editor", category=Hack.Category.CLIENT, isListening=true)
public class HudEditor
extends Hack {
    public static HudEditor INSTANCE;
    public BooleanSetting customFont = new BooleanSetting("Custom Font", (Boolean)true, (Hack)this);
    public ColourSetting fontColor = new ColourSetting("Font Color", new Colour(255, 255, 255, 255), (Hack)this);
    public BooleanSetting grid = new BooleanSetting("Grid", (Boolean)false, (Hack)this);
    public IntSetting vLines = new IntSetting("Vertical Lines", 10, 0, 50, this, s -> this.grid.getValue());
    public IntSetting hLines = new IntSetting("Horizontal Lines", 10, 0, 50, this, s -> this.grid.getValue());
    public ColourSetting gridColor = new ColourSetting("Grid Color", new Colour(255, 255, 255, 255), (Hack)this, s -> this.grid.getValue());
    public BooleanSetting alignment = new BooleanSetting("Alignment Lines", (Boolean)true, (Hack)this);
    public ColourSetting alignmentColor = new ColourSetting("Alignment Color", new Colour(255, 200, 0, 140), (Hack)this, s -> this.alignment.getValue());
    public IntSetting welcomerOffset = new IntSetting("Rainbow Offset", 500, 0, 20000, this);
    public BooleanSetting showOff = new BooleanSetting("Show Coords", (Boolean)false, (Hack)this);
    public BooleanSetting arrayOutline = new BooleanSetting("Array Outline", (Boolean)false, (Hack)this);
    public ParentSetting welcomer = new ParentSetting("Welcomer", this);
    public BooleanSetting welcomerName = new BooleanSetting("Watermark Name", (Boolean)true, this.welcomer);
    public BooleanSetting welcomerFps = new BooleanSetting("Watermark Fps", (Boolean)false, this.welcomer);
    public BooleanSetting welcomerTps = new BooleanSetting("Watermark Tps", (Boolean)false, this.welcomer);
    public BooleanSetting welcomerPing = new BooleanSetting("Watermark Ping", (Boolean)false, this.welcomer);
    public BooleanSetting welcomerTime = new BooleanSetting("Watermark Time", (Boolean)false, this.welcomer);
    public ParentSetting kdcomponent = new ParentSetting("Clout Manager", this);
    public BooleanSetting kd = new BooleanSetting("KD", (Boolean)true, (Hack)this);
    public BooleanSetting streak = new BooleanSetting("Streak", (Boolean)false, (Hack)this);
    public BooleanSetting kills = new BooleanSetting("Kills", (Boolean)false, (Hack)this);
    public BooleanSetting deaths = new BooleanSetting("Deaths", (Boolean)false, (Hack)this);

    public HudEditor() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen((GuiScreen)WurstplusThree.HUDGUI);
        this.disable();
    }
}

