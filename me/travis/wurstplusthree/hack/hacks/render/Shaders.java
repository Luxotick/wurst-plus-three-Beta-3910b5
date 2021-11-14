/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 */
package me.travis.wurstplusthree.hack.hacks.render;

import java.util.Arrays;
import java.util.Locale;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import net.minecraft.util.ResourceLocation;

@Hack.Registration(name="Shaders", description="huge module", category=Hack.Category.RENDER)
public class Shaders
extends Hack {
    public EnumSetting type = new EnumSetting("Mode", "Flip", Arrays.asList("AntiAlias", "Art", "Bits", "Blobs", "Blobs2", "Blur", "Bumpy", "Color_Convolve", "Creeper", "Deconverge", "Desaturate", "flip", "fxaa", "Green", "Invert", "Notch", "ntsc", "Outline", "Pencil", "Phosphor", "Scan_Pincushion", "Sobel", "Spider", "Wobble"), this);
    public BooleanSetting onlyInGui = new BooleanSetting("OnlyInGui", (Boolean)false, (Hack)this);
    private boolean loaded = false;

    @Override
    public void onUpdate() {
        if (this.onlyInGui.getValue().booleanValue()) {
            if (Shaders.mc.currentScreen != null && !this.loaded) {
                Shaders.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + this.type.getValue().toLowerCase(Locale.ROOT) + ".json"));
                this.loaded = true;
            } else if (Shaders.mc.entityRenderer.isShaderActive() && Shaders.mc.currentScreen == null) {
                this.loaded = false;
                Shaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        } else if (!Shaders.mc.entityRenderer.isShaderActive()) {
            Shaders.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + this.type.getValue().toLowerCase(Locale.ROOT) + ".json"));
        }
    }

    @Override
    public void onSettingChange() {
        if (Shaders.mc.entityRenderer.isShaderActive()) {
            Shaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        Shaders.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + this.type.getValue().toLowerCase(Locale.ROOT) + ".json"));
    }

    @Override
    public void onDisable() {
        if (Shaders.mc.entityRenderer.isShaderActive()) {
            Shaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
}

