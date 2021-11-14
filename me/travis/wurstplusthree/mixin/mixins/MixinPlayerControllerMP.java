/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.travis.wurstplusthree.mixin.mixins;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.BlockDestroyEvent;
import me.travis.wurstplusthree.event.events.BlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={PlayerControllerMP.class})
public class MixinPlayerControllerMP {
    @Inject(method={"clickBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void clickBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
        BlockEvent event = new BlockEvent(3, pos, face);
        WurstplusThree.EVENT_PROCESSOR.postEvent(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method={"onPlayerDamageBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
        BlockEvent event = new BlockEvent(4, pos, face);
        WurstplusThree.EVENT_PROCESSOR.postEvent(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method={"onPlayerDestroyBlock"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V")}, cancellable=true)
    private void onPlayerDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        BlockDestroyEvent event = new BlockDestroyEvent(pos);
        WurstplusThree.EVENT_PROCESSOR.postEvent(event);
    }
}

