/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketEntityMetadata
 */
package me.travis.wurstplusthree.mixin.mixins;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.DeathEvent;
import me.travis.wurstplusthree.util.Globals;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient {
    @Inject(method={"handleEntityMetadata"}, at={@At(value="RETURN")}, cancellable=true)
    private void handleEntityMetadataHook(SPacketEntityMetadata packetIn, CallbackInfo info) {
        Entity entity;
        if (Globals.mc.world != null && (entity = Globals.mc.world.getEntityByID(packetIn.getEntityId())) instanceof EntityPlayer) {
            EntityPlayer entityPlayer;
            EntityPlayer player = (EntityPlayer)entity;
            if (entityPlayer.getHealth() <= 0.0f) {
                WurstplusThree.EVENT_PROCESSOR.postEvent(new DeathEvent(player));
                WurstplusThree.POP_MANAGER.onDeath(player);
                if (player.getName().equalsIgnoreCase(Globals.mc.player.getName())) {
                    WurstplusThree.KD_MANAGER.addDeath();
                }
            }
        }
    }
}

