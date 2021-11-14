/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 */
package me.travis.wurstplusthree.mixin.mixins;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.BlockCollisionBoundingBoxEvent;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BlockWeb.class})
public class MixinBlockWeb {
    @Inject(method={"getCollisionBoundingBox"}, at={@At(value="HEAD")}, cancellable=true)
    public void getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> callbackInfoReturnable) {
        BlockCollisionBoundingBoxEvent bb = new BlockCollisionBoundingBoxEvent(pos);
        WurstplusThree.EVENT_PROCESSOR.postEvent(bb);
        if (bb.isCancelled()) {
            callbackInfoReturnable.setReturnValue(bb.getBoundingBox());
            callbackInfoReturnable.cancel();
        }
    }
}

