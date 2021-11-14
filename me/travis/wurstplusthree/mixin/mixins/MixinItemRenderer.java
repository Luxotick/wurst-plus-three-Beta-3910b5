/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.EnumHandSide
 */
package me.travis.wurstplusthree.mixin.mixins;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.RenderItemEvent;
import me.travis.wurstplusthree.hack.hacks.render.NoRender;
import me.travis.wurstplusthree.hack.hacks.render.ViewModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemRenderer.class}, priority=0x7FFFFFFF)
public abstract class MixinItemRenderer {
    Minecraft mc = Minecraft.getMinecraft();

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.fire.getValue().booleanValue()) {
            info.cancel();
        }
    }

    @Redirect(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/ItemRenderer;transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V"))
    public void transformRedirect(ItemRenderer renderer, EnumHandSide hand, float y) {
        RenderItemEvent event = new RenderItemEvent(0.56f, -0.52f + y * -0.6f, -0.72f, -0.56f, -0.52f + y * -0.6f, -0.72f, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        WurstplusThree.EVENT_PROCESSOR.postEvent(event);
        if (hand == EnumHandSide.RIGHT) {
            GlStateManager.translate((double)(!this.mc.player.isHandActive() || this.mc.player.getItemInUseCount() <= 0 || this.mc.player.getActiveHand() != EnumHand.MAIN_HAND || ViewModel.INSTANCE.fixEating.getValue() == false ? event.getMainX() : (double)0.56f), (double)event.getMainY(), (double)(!this.mc.player.isHandActive() || this.mc.player.getItemInUseCount() <= 0 || this.mc.player.getActiveHand() != EnumHand.MAIN_HAND || ViewModel.INSTANCE.fixEating.getValue() == false ? event.getMainZ() : (double)-0.72f));
            GlStateManager.scale((double)event.getMainHandScaleX(), (double)event.getMainHandScaleY(), (double)event.getMainHandScaleZ());
            GlStateManager.rotate((float)((float)event.getMainRAngel()), (float)((float)event.getMainRx()), (float)((float)event.getMainRy()), (float)((float)event.getMainRz()));
        } else {
            GlStateManager.translate((double)event.getOffX(), (double)event.getOffY(), (double)event.getOffZ());
            GlStateManager.scale((double)event.getOffHandScaleX(), (double)event.getOffHandScaleY(), (double)event.getOffHandScaleZ());
            GlStateManager.rotate((float)((float)event.getOffRAngel()), (float)((float)event.getOffRx()), (float)((float)event.getOffRy()), (float)((float)event.getOffRz()));
        }
    }
}

