/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 */
package me.travis.wurstplusthree.mixin.mixins;

import me.travis.wurstplusthree.gui.alt.defult.GuiAccountSelector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiMainMenu.class})
public abstract class MixinGuiMainMenu
extends GuiScreen {
    @Inject(method={"actionPerformed"}, at={@At(value="HEAD")})
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 69) {
            this.mc.displayGuiScreen((GuiScreen)new GuiAccountSelector());
        }
    }
}

