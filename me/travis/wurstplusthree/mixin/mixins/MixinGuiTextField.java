/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiTextField
 */
package me.travis.wurstplusthree.mixin.mixins;

import me.travis.wurstplusthree.WurstplusThree;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiTextField.class})
public abstract class MixinGuiTextField
extends Gui {
    @Shadow
    public abstract String getText();

    @Inject(method={"textboxKeyTyped"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiTextField;writeText(Ljava/lang/String;)V")})
    private void typed(char typedChar, int keyCode, CallbackInfoReturnable<Boolean> cir) {
        String text = this.getText();
        if (text.startsWith(WurstplusThree.COMMANDS.getPrefix())) {
            text = text.replace(WurstplusThree.COMMANDS.getPrefix(), "");
        }
    }

    @Inject(method={"drawTextBox"}, at={@At(value="RETURN")})
    private void renderBox(CallbackInfo ci) {
    }
}

