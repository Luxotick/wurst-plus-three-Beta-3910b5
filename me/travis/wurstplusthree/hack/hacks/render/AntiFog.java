/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FogColors
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FogDensity
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Hack.Registration(name="Anti Fog", description="removes fog", category=Hack.Category.RENDER, isListening=false)
public class AntiFog
extends Hack {
    public BooleanSetting clear = new BooleanSetting("RemoveFog", (Boolean)true, (Hack)this);
    public BooleanSetting colour = new BooleanSetting("ColourFog", (Boolean)true, (Hack)this);
    public ColourSetting overworldColour = new ColourSetting("Overworld", new Colour(255, 255, 255, 255), (Hack)this, s -> this.colour.getValue());
    public ColourSetting netherColour = new ColourSetting("Nether", new Colour(255, 255, 255, 255), (Hack)this, s -> this.colour.getValue());
    public ColourSetting endColour = new ColourSetting("End", new Colour(255, 255, 255, 255), (Hack)this, s -> this.colour.getValue());

    @SubscribeEvent
    public void fogDensity(EntityViewRenderEvent.FogDensity event) {
        if (this.clear.getValue().booleanValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void fogColour(EntityViewRenderEvent.FogColors event) {
        if (this.colour.getValue().booleanValue()) {
            switch (AntiFog.mc.player.dimension) {
                case 0: {
                    event.setRed((float)this.overworldColour.getValue().getRed() / 255.0f);
                    event.setGreen((float)this.overworldColour.getValue().getGreen() / 255.0f);
                    event.setBlue((float)this.overworldColour.getValue().getBlue() / 255.0f);
                }
                case 1: {
                    event.setRed((float)this.endColour.getValue().getRed() / 255.0f);
                    event.setGreen((float)this.endColour.getValue().getGreen() / 255.0f);
                    event.setBlue((float)this.endColour.getValue().getBlue() / 255.0f);
                }
                case -1: {
                    event.setRed((float)this.netherColour.getValue().getRed() / 255.0f);
                    event.setGreen((float)this.netherColour.getValue().getGreen() / 255.0f);
                    event.setBlue((float)this.netherColour.getValue().getBlue() / 255.0f);
                }
            }
        }
    }
}

