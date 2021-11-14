/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiNewChat
 */
package me.travis.wurstplusthree.mixin.mixins;

import java.util.List;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.hacks.chat.CustomChat;
import me.travis.wurstplusthree.util.elements.Rainbow;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={GuiNewChat.class})
public class MixinGuiNewChat
extends Gui {
    @Shadow
    @Final
    public List<ChatLine> drawnChatLines;

    @Redirect(method={"drawChat"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadowMaybe(FontRenderer fontRenderer, String message, float x, float y, int color) {
        if (!CustomChat.INSTANCE.isEnabled()) {
            return fontRenderer.drawStringWithShadow(message, x, y, color);
        }
        if (CustomChat.INSTANCE.customFont.getValue().booleanValue()) {
            if (CustomChat.INSTANCE.rainbow.getValue().booleanValue()) {
                WurstplusThree.GUI_FONT_MANAGER.drawStringRainbow(message, x, y, true);
            } else {
                WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(message, x, y, color);
            }
        } else if (CustomChat.INSTANCE.rainbow.getValue().booleanValue()) {
            fontRenderer.drawStringWithShadow(message, x, y, Rainbow.getColour().getRGB());
        } else {
            fontRenderer.drawStringWithShadow(message, x, y, color);
        }
        return 0;
    }

    @Redirect(method={"setChatLine"}, at=@At(value="INVOKE", target="Ljava/util/List;size()I", ordinal=0, remap=false))
    public int drawnChatLinesSize(List<ChatLine> list) {
        return CustomChat.INSTANCE.isEnabled() && CustomChat.INSTANCE.infinite.getValue() != false ? -2147483647 : list.size();
    }

    @Redirect(method={"setChatLine"}, at=@At(value="INVOKE", target="Ljava/util/List;size()I", ordinal=2, remap=false))
    public int chatLinesSize(List<ChatLine> list) {
        return CustomChat.INSTANCE.isEnabled() && CustomChat.INSTANCE.infinite.getValue() != false ? -2147483647 : list.size();
    }
}

