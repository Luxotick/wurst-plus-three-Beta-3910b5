/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.login.server.SPacketEnableCompression
 *  net.minecraft.network.login.server.SPacketEncryptionRequest
 *  net.minecraft.network.login.server.SPacketLoginSuccess
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketClientStatus
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketCreativeInventoryAction
 *  net.minecraft.network.play.client.CPacketCustomPayload
 *  net.minecraft.network.play.server.SPacketAdvancementInfo
 *  net.minecraft.network.play.server.SPacketAnimation
 *  net.minecraft.network.play.server.SPacketBlockAction
 *  net.minecraft.network.play.server.SPacketBlockBreakAnim
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.network.play.server.SPacketCamera
 *  net.minecraft.network.play.server.SPacketChangeGameState
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.network.play.server.SPacketChunkData
 *  net.minecraft.network.play.server.SPacketCloseWindow
 *  net.minecraft.network.play.server.SPacketCollectItem
 *  net.minecraft.network.play.server.SPacketCombatEvent
 *  net.minecraft.network.play.server.SPacketConfirmTransaction
 *  net.minecraft.network.play.server.SPacketCooldown
 *  net.minecraft.network.play.server.SPacketCustomPayload
 *  net.minecraft.network.play.server.SPacketCustomSound
 *  net.minecraft.network.play.server.SPacketDestroyEntities
 *  net.minecraft.network.play.server.SPacketDisconnect
 *  net.minecraft.network.play.server.SPacketDisplayObjective
 *  net.minecraft.network.play.server.SPacketEffect
 *  net.minecraft.network.play.server.SPacketEntity
 *  net.minecraft.network.play.server.SPacketEntityAttach
 *  net.minecraft.network.play.server.SPacketEntityEffect
 *  net.minecraft.network.play.server.SPacketEntityEquipment
 *  net.minecraft.network.play.server.SPacketEntityHeadLook
 *  net.minecraft.network.play.server.SPacketEntityMetadata
 *  net.minecraft.network.play.server.SPacketEntityProperties
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketEntityTeleport
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.network.play.server.SPacketHeldItemChange
 *  net.minecraft.network.play.server.SPacketJoinGame
 *  net.minecraft.network.play.server.SPacketKeepAlive
 *  net.minecraft.network.play.server.SPacketMaps
 *  net.minecraft.network.play.server.SPacketMoveVehicle
 *  net.minecraft.network.play.server.SPacketMultiBlockChange
 *  net.minecraft.network.play.server.SPacketOpenWindow
 *  net.minecraft.network.play.server.SPacketParticles
 *  net.minecraft.network.play.server.SPacketPlayerAbilities
 *  net.minecraft.network.play.server.SPacketPlayerListHeaderFooter
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketRecipeBook
 *  net.minecraft.network.play.server.SPacketRemoveEntityEffect
 *  net.minecraft.network.play.server.SPacketRespawn
 *  net.minecraft.network.play.server.SPacketScoreboardObjective
 *  net.minecraft.network.play.server.SPacketSelectAdvancementsTab
 *  net.minecraft.network.play.server.SPacketServerDifficulty
 *  net.minecraft.network.play.server.SPacketSetExperience
 *  net.minecraft.network.play.server.SPacketSetPassengers
 *  net.minecraft.network.play.server.SPacketSetSlot
 *  net.minecraft.network.play.server.SPacketSignEditorOpen
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnExperienceOrb
 *  net.minecraft.network.play.server.SPacketSpawnGlobalEntity
 *  net.minecraft.network.play.server.SPacketSpawnMob
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.network.play.server.SPacketSpawnPainting
 *  net.minecraft.network.play.server.SPacketSpawnPlayer
 *  net.minecraft.network.play.server.SPacketSpawnPosition
 *  net.minecraft.network.play.server.SPacketTabComplete
 *  net.minecraft.network.play.server.SPacketUnloadChunk
 *  net.minecraft.network.play.server.SPacketUpdateHealth
 *  net.minecraft.network.play.server.SPacketUpdateTileEntity
 *  net.minecraft.network.play.server.SPacketUseBed
 *  net.minecraft.network.status.server.SPacketPong
 *  net.minecraft.network.status.server.SPacketServerInfo
 *  net.minecraft.world.World
 */
package me.travis.wurstplusthree.hack.hacks.misc;

import java.util.Arrays;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.util.ClientMessage;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.world.World;

@Hack.Registration(name="Packet Logger", description="Logs incoming and outgoing packets", category=Hack.Category.MISC, priority=HackPriority.Lowest)
public final class PacketLogger
extends Hack {
    BooleanSetting incoming = new BooleanSetting("Receive", (Boolean)true, (Hack)this);
    BooleanSetting AdvancementInfo = new BooleanSetting("SPacketAdvancementInfo", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SAnimation = new BooleanSetting("SPacketAnimation", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SBlockAction = new BooleanSetting("SPacketBlockAction", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SBlockBreakAnim = new BooleanSetting("SPacketBlockBreakAnim", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SBlockChange = new BooleanSetting("SPacketBlockChange", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SCamera = new BooleanSetting("SPacketCamera", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SChat = new BooleanSetting("SPacketChat", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SCooldown = new BooleanSetting("SPacketCooldown", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SChunkData = new BooleanSetting("SPacketChunkData", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SChangeGameState = new BooleanSetting("SPacketChangeGameState", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SCloseWindow = new BooleanSetting("SPacketCloseWindow", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SCollectItem = new BooleanSetting("SPacketCollectItem", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SCombatEvent = new BooleanSetting("SPacketCombatEvent", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SConfirmTransaction = new BooleanSetting("SPacketConfirmTransaction", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SCustomPayload = new BooleanSetting("SPacketCustomPayload", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SCustomSound = new BooleanSetting("SPacketCustomSound", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SDestroyEntities = new BooleanSetting("SPacketDestroyEntities", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SDisconnect = new BooleanSetting("SPacketDisconnect", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SDisplayObjective = new BooleanSetting("SPacketDisplayObjective", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEffect = new BooleanSetting("SPacketEffect", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntity = new BooleanSetting("SPacketEntity", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntityAttach = new BooleanSetting("SPacketEntityAttach", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntityEffect = new BooleanSetting("SPacketEntityEffect", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntityEquipment = new BooleanSetting("SPacketEntityEquipment", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntityHeadLook = new BooleanSetting("SPacketEntityHeadLook", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntityMetadata = new BooleanSetting("SPacketEntityMetadata", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntityProperties = new BooleanSetting("SPacketEntityProperties", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntityStatus = new BooleanSetting("SPacketEntityStatus", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntityTeleport = new BooleanSetting("SPacketEntityTeleport", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEntityVelocity = new BooleanSetting("SPacketEntityVelocity", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SExplosion = new BooleanSetting("SPacketExplosion", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEnableCompression = new BooleanSetting("SPacketEnableCompression", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SEncryptionRequest = new BooleanSetting("SPacketEncryptionRequest", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SHeldItemChange = new BooleanSetting("SPacketHeldItemChange", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SJoinGame = new BooleanSetting("SPacketJoinGame", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SKeepAlive = new BooleanSetting("SPacketKeepAlive", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SLoginSuccess = new BooleanSetting("SPacketLoginSuccess", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SMaps = new BooleanSetting("SPacketMaps", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SMoveVehicle = new BooleanSetting("SPacketMoveVehicle", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SMultiBlockChange = new BooleanSetting("SPacketMultiBlockChange", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SOpenWindow = new BooleanSetting("SPacketOpenWindow", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SParticles = new BooleanSetting("SPacketParticles", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SPlayerAbilities = new BooleanSetting("SPacketPlayerAbilities", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SPlayerListHeaderFooter = new BooleanSetting("SPacketPlayerListHeaderFooter", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SPlayerListItem = new BooleanSetting("SPacketPlayerListItem", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SPlayerPosLook = new BooleanSetting("SPacketPlayerPosLook", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SPong = new BooleanSetting("SPacketPong", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SRecipeBook = new BooleanSetting("SPacketRecipeBook", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SRespawn = new BooleanSetting("SPacketRespawn", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SRemoveEntityEffect = new BooleanSetting("SPacketRemoveEntityEffect", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SScoreboardObjective = new BooleanSetting("SPacketScoreboardObjective", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SServerDifficulty = new BooleanSetting("SPacketServerDifficulty", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSelectAdvancementsTab = new BooleanSetting("SPacketSelectAdvancementsTab", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SServerInfo = new BooleanSetting("SPacketServerInfo", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSetExperience = new BooleanSetting("SPacketSetExperience", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSetPassengers = new BooleanSetting("SPacketSetPassengers", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSetSlot = new BooleanSetting("SPacketSetSlot", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSignEditorOpen = new BooleanSetting("SPacketSignEditorOpen", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSoundEffect = new BooleanSetting("SPacketSoundEffect", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSpawnGlobalEntity = new BooleanSetting("SPacketSpawnGlobalEntity", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSpawnMob = new BooleanSetting("SPacketSpawnMob", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSpawnPlayer = new BooleanSetting("SPacketSpawnPlayer", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSpawnExperienceOrb = new BooleanSetting("SPacketSpawnExperienceOrb", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSpawnPainting = new BooleanSetting("SPacketSpawnPainting", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSpawnObject = new BooleanSetting("SPacketSpawnObject", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SSpawnPosition = new BooleanSetting("SPacketSpawnPosition", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting STabComplete = new BooleanSetting("SPacketTabComplete", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SUnloadChunk = new BooleanSetting("SPacketUnloadChunk", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SUseBed = new BooleanSetting("SPacketUseBed", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting SUpdateHealth = new BooleanSetting("SPacketUpdateHealth", (Boolean)false, (Hack)this, s -> this.incoming.getValue());
    BooleanSetting outgoing = new BooleanSetting("Outgoing", (Boolean)true, (Hack)this);
    BooleanSetting CAnimation = new BooleanSetting("CPacketAnimation", (Boolean)false, (Hack)this, s -> this.outgoing.getValue());
    BooleanSetting CChatMessage = new BooleanSetting("CPacketChatMessage", (Boolean)false, (Hack)this, s -> this.outgoing.getValue());
    BooleanSetting CClickWindow = new BooleanSetting("CPacketClickWindow", (Boolean)false, (Hack)this, s -> this.outgoing.getValue());
    BooleanSetting CConfirmTeleport = new BooleanSetting("CPacketConfirmTeleport", (Boolean)false, (Hack)this, s -> this.outgoing.getValue());
    BooleanSetting CClientStatus = new BooleanSetting("CPacketClientStatus", (Boolean)false, (Hack)this, s -> this.outgoing.getValue());
    BooleanSetting CCustomPayload = new BooleanSetting("CPacketCustomPayload", (Boolean)false, (Hack)this, s -> this.outgoing.getValue());
    BooleanSetting CCreativeInventoryAction = new BooleanSetting("CPacketCreativeInventoryAction", (Boolean)false, (Hack)this, s -> this.outgoing.getValue());

    @CommitEvent(priority=EventPriority.LOW)
    public final void outgoingEvent(PacketEvent.Send event) {
        if (!this.outgoing.getValue().booleanValue()) {
            return;
        }
        if (event.getPacket() instanceof CPacketAnimation && this.CAnimation.getValue().booleanValue()) {
            CPacketAnimation s = (CPacketAnimation)event.getPacket();
            ClientMessage.sendMessage("CPacketAnimation\n - Hand name: " + s.getHand().name());
        } else if (event.getPacket() instanceof CPacketChatMessage && this.CChatMessage.getValue().booleanValue()) {
            CPacketChatMessage s = (CPacketChatMessage)event.getPacket();
            ClientMessage.sendMessage("CPacketChatMessage\n - Message: " + s.message);
        } else if (event.getPacket() instanceof CPacketClickWindow && this.CClickWindow.getValue().booleanValue()) {
            CPacketClickWindow s = (CPacketClickWindow)event.getPacket();
            ClientMessage.sendMessage("CPacketClickWindow\n - Acton Number: " + s.getActionNumber() + "\n - Window ID: " + s.getWindowId() + "\n - Item Name: " + s.getClickedItem().getDisplayName() + "\n - Click Type Name: " + s.getClickType().name());
        } else if (event.getPacket() instanceof CPacketConfirmTeleport && this.CConfirmTeleport.getValue().booleanValue()) {
            CPacketConfirmTeleport s = (CPacketConfirmTeleport)event.getPacket();
            ClientMessage.sendMessage("CPacketConfirmTeleport\n - Tp id: " + s.getTeleportId());
        } else if (event.getPacket() instanceof CPacketClientStatus && this.CClientStatus.getValue().booleanValue()) {
            CPacketClientStatus s = (CPacketClientStatus)event.getPacket();
            ClientMessage.sendMessage("CPacketClientStatus\n - Status Name: " + s.getStatus().name());
        } else if (event.getPacket() instanceof CPacketCustomPayload && this.CCustomPayload.getValue().booleanValue()) {
            CPacketCustomPayload s = (CPacketCustomPayload)event.getPacket();
            ClientMessage.sendMessage("CPacketCustomPayload\n - Channel: " + s.channel + "\n - Data: " + s.data.readString(10000));
        } else if (event.getPacket() instanceof CPacketCreativeInventoryAction && this.CCreativeInventoryAction.getValue().booleanValue()) {
            CPacketCreativeInventoryAction s = (CPacketCreativeInventoryAction)event.getPacket();
            ClientMessage.sendMessage("CPacketCreativeInventoryAction\n - Item name: " + s.getStack().getDisplayName() + "\n - Slot Id: " + s.getSlotId());
        }
    }

    @CommitEvent(priority=EventPriority.LOW)
    public final void incomingEvent(PacketEvent.Receive event) {
        if (!this.incoming.getValue().booleanValue()) {
            return;
        }
        if (event.getPacket() instanceof SPacketAdvancementInfo && this.AdvancementInfo.getValue().booleanValue()) {
            SPacketAdvancementInfo s = (SPacketAdvancementInfo)event.getPacket();
            ClientMessage.sendMessage("SPacketAdvancementInfo:\n -Is First Sync: " + s.isFirstSync());
        } else if (event.getPacket() instanceof SPacketAnimation && this.SAnimation.getValue().booleanValue()) {
            SPacketAnimation s = (SPacketAnimation)event.getPacket();
            ClientMessage.sendMessage("SPacketAnimation:\n - Animation Type: " + s.getAnimationType() + "\n - Entity Id: " + s.getEntityID());
        } else if (event.getPacket() instanceof SPacketCamera && this.SCamera.getValue().booleanValue()) {
            SPacketCamera s = (SPacketCamera)event.getPacket();
            ClientMessage.sendMessage("SPacketCamera:\n - Entity name: " + s.getEntity((World)PacketLogger.mc.world).getName() + "\n - Entity Id: " + s.entityId);
        } else if (event.getPacket() instanceof SPacketChat && this.SChat.getValue().booleanValue()) {
            SPacketChat s = (SPacketChat)event.getPacket();
            ClientMessage.sendMessage("SPacketChat:\n - Chat Type: " + s.type.name() + "\n - Formatted Text: " + s.chatComponent.getFormattedText());
        } else if (event.getPacket() instanceof SPacketBlockAction && this.SBlockAction.getValue().booleanValue()) {
            SPacketBlockAction s = (SPacketBlockAction)event.getPacket();
            ClientMessage.sendMessage("SPacketBlockAction:\n - Block Type Name: " + s.getBlockType().getLocalizedName() + "\n - Block Type: " + (Object)s.getBlockType() + "\n - Block Pos: " + (Object)s.getBlockPosition() + "\n - Data1: " + s.getData1() + "\n - Data2: " + s.getData2());
        } else if (event.getPacket() instanceof SPacketBlockBreakAnim && this.SBlockBreakAnim.getValue().booleanValue()) {
            SPacketBlockBreakAnim s = (SPacketBlockBreakAnim)event.getPacket();
            ClientMessage.sendMessage("SPacketBlockBreakAnim:\n - Break Id: " + s.getBreakerId() + "\n - Block Pos: " + (Object)s.getPosition() + "\n - Progress: " + s.getProgress());
        } else if (event.getPacket() instanceof SPacketBlockChange && this.SBlockChange.getValue().booleanValue()) {
            SPacketBlockChange s = (SPacketBlockChange)event.getPacket();
            ClientMessage.sendMessage("SPacketBlockChange:\n - Block Pos: " + (Object)s.getBlockPosition() + "\n - Block Name: " + s.blockState.getBlock().getLocalizedName() + "\n - Block State: " + (Object)s.getBlockState());
        } else if (event.getPacket() instanceof SPacketCooldown && this.SCooldown.getValue().booleanValue()) {
            SPacketCooldown s = (SPacketCooldown)event.getPacket();
            ClientMessage.sendMessage("SPacketCooldown:\n - Item: " + (Object)s.getItem() + "\n - Ticks: " + s.getTicks());
        } else if (event.getPacket() instanceof SPacketChunkData && this.SChunkData.getValue().booleanValue()) {
            SPacketChunkData s = (SPacketChunkData)event.getPacket();
            ClientMessage.sendMessage("SPacketChunkData:\n - Chunk Pos: " + s.getChunkX() + " " + s.getChunkZ());
        } else if (event.getPacket() instanceof SPacketChangeGameState && this.SChangeGameState.getValue().booleanValue()) {
            SPacketChangeGameState s = (SPacketChangeGameState)event.getPacket();
            ClientMessage.sendMessage("SPacketChangeGameState:\n - Game State Value: " + s.getValue() + "\n - Game State: " + s.getGameState());
        } else if (event.getPacket() instanceof SPacketCloseWindow && this.SCloseWindow.getValue().booleanValue()) {
            ClientMessage.sendMessage("SPacketCloseWindow");
        } else if (event.getPacket() instanceof SPacketCollectItem && this.SCollectItem.getValue().booleanValue()) {
            SPacketCollectItem s = (SPacketCollectItem)event.getPacket();
            ClientMessage.sendMessage("SPacketCollectItem:\n - Entity ID: " + s.getEntityID() + "\n - Amount: " + s.getAmount() + "\n - Collected Item Id: " + s.getCollectedItemEntityID());
        } else if (event.getPacket() instanceof SPacketCombatEvent && this.SCombatEvent.getValue().booleanValue()) {
            SPacketCombatEvent s = (SPacketCombatEvent)event.getPacket();
            ClientMessage.sendMessage("SPacketCombatEvent:\n - Entity ID: " + s.entityId + "\n - Player Id: " + s.playerId + "\n - Event Name: " + s.eventType.name() + "\n - Duration: " + s.duration + "\n - Death Message: " + s.deathMessage.getFormattedText());
        } else if (event.getPacket() instanceof SPacketConfirmTransaction && this.SConfirmTransaction.getValue().booleanValue()) {
            SPacketConfirmTransaction s = (SPacketConfirmTransaction)event.getPacket();
            ClientMessage.sendMessage("SPacketConfirmTransaction:\n - Action Number: " + s.getActionNumber() + "\n - Window Id: " + s.getWindowId() + "\n - Was Accepted: " + s.wasAccepted());
        } else if (event.getPacket() instanceof SPacketCustomPayload && this.SCustomPayload.getValue().booleanValue()) {
            SPacketCustomPayload s = (SPacketCustomPayload)event.getPacket();
            ClientMessage.sendMessage("SPacketCustomPayload:\n - Channel Name: " + s.getChannelName() + "\n - Buffer Data: " + s.getBufferData().readString(1000));
        } else if (event.getPacket() instanceof SPacketCustomSound && this.SCustomSound.getValue().booleanValue()) {
            SPacketCustomSound s = (SPacketCustomSound)event.getPacket();
            ClientMessage.sendMessage("SPacketCustomSound:\n - Sound Name: " + s.getSoundName() + "\n - Sound Category: " + s.getCategory().getName() + "\n - Sound Pos: " + s.getX() + " " + s.getY() + " " + s.getZ() + "\n - Sound Pitch: " + s.getPitch() + "\n - Sound Volume: " + s.getVolume());
        } else if (event.getPacket() instanceof SPacketDestroyEntities && this.SDestroyEntities.getValue().booleanValue()) {
            SPacketDestroyEntities s = (SPacketDestroyEntities)event.getPacket();
            ClientMessage.sendMessage("SPacketDestroyEntities:\n");
            Arrays.stream(s.getEntityIDs()).forEach(id -> ClientMessage.sendMessage("Removed Id: " + id));
        } else if (event.getPacket() instanceof SPacketDisconnect && this.SDisconnect.getValue().booleanValue()) {
            SPacketDisconnect s = (SPacketDisconnect)event.getPacket();
            ClientMessage.sendMessage("SPacketDisconnect:\n - Disconnect Reason: " + s.getReason().getFormattedText());
        } else if (event.getPacket() instanceof SPacketDisplayObjective && this.SDisplayObjective.getValue().booleanValue()) {
            SPacketDisplayObjective s = (SPacketDisplayObjective)event.getPacket();
            ClientMessage.sendMessage("SPacketDisplayObjective:\n - Objective Name: " + s.getName() + "\n - Objective Pos: " + s.getPosition());
        } else if (event.getPacket() instanceof SPacketEffect && this.SEffect.getValue().booleanValue()) {
            SPacketEffect s = (SPacketEffect)event.getPacket();
            ClientMessage.sendMessage("SPacketEffect:\n - Sound Data: " + s.getSoundData() + "\n - Sound Pos: " + (Object)s.getSoundPos() + "\n - Sound Type: " + s.getSoundType() + "\n - Is Sound Server Wide: " + s.isSoundServerwide());
        } else if (event.getPacket() instanceof SPacketEntity && this.SEntity.getValue().booleanValue()) {
            SPacketEntity s = (SPacketEntity)event.getPacket();
            ClientMessage.sendMessage("SPacketEntity:\n - Entity Name: " + s.getEntity((World)PacketLogger.mc.world).getName() + "\n - Entity Id: " + s.getEntity((World)PacketLogger.mc.world).entityId + "\n - Entity Pitch: " + s.getPitch() + "\n - Is Entity OnGround: " + s.getOnGround() + "\n - Entity Yaw: " + s.getYaw() + "\n - Entity Pos: " + s.getX() + " " + s.getY() + " " + s.getZ());
        } else if (event.getPacket() instanceof SPacketEntityAttach && this.SEntityAttach.getValue().booleanValue()) {
            SPacketEntityAttach s = (SPacketEntityAttach)event.getPacket();
            ClientMessage.sendMessage("SPacketEntityAttach:\n - Entity Id: " + s.getEntityId() + "\n - Entity Vehicle Id: " + s.getVehicleEntityId());
        } else if (event.getPacket() instanceof SPacketEntityEffect && this.SEntityEffect.getValue().booleanValue()) {
            SPacketEntityEffect s = (SPacketEntityEffect)event.getPacket();
            ClientMessage.sendMessage("SPacketEntityEffect:\n - Entity Id: " + s.getEntityId() + "\n - Effect Amplifier: " + s.getAmplifier() + "\n - Effect ID: " + s.getEffectId() + "\n - Effect Duration: " + s.getDuration() + "\n - Is Effect Ambient: " + s.getIsAmbient());
        } else if (event.getPacket() instanceof SPacketEntityEquipment && this.SEntityEquipment.getValue().booleanValue()) {
            SPacketEntityEquipment s = (SPacketEntityEquipment)event.getPacket();
            ClientMessage.sendMessage("SPacketEntityEquipment:\n - Entity Id: " + s.getEntityID() + "\n - Equipment Slot Name: " + s.getEquipmentSlot().getName() + "\n - Item Name: " + s.getItemStack().getDisplayName());
        } else if (event.getPacket() instanceof SPacketEntityHeadLook && this.SEntityHeadLook.getValue().booleanValue()) {
            SPacketEntityHeadLook s = (SPacketEntityHeadLook)event.getPacket();
            ClientMessage.sendMessage("SPacketEntityHeadLook:\n - Entity Id: " + s.getEntity((World)PacketLogger.mc.world).entityId + "\n - Entity Name: " + s.getEntity((World)PacketLogger.mc.world).getName() + "\n - Yaw: " + s.getYaw());
        } else if (event.getPacket() instanceof SPacketEntityMetadata && this.SEntityMetadata.getValue().booleanValue()) {
            SPacketEntityMetadata s = (SPacketEntityMetadata)event.getPacket();
            ClientMessage.sendMessage("SPacketEntityMetadata:\n - Entity Id: " + s.getEntityId());
        } else if (event.getPacket() instanceof SPacketEntityProperties && this.SEntityProperties.getValue().booleanValue()) {
            SPacketEntityProperties s = (SPacketEntityProperties)event.getPacket();
            ClientMessage.sendMessage("SPacketEntityProperties:\n - Entity Id: " + s.getEntityId());
        } else if (event.getPacket() instanceof SPacketEntityStatus && this.SEntityStatus.getValue().booleanValue()) {
            SPacketEntityStatus s = (SPacketEntityStatus)event.getPacket();
            ClientMessage.sendMessage("SPacketEntityStatus:\n - Entity Id: " + s.getEntity((World)PacketLogger.mc.world).getEntityId() + "\n - Entity Name: " + s.getEntity((World)PacketLogger.mc.world).getName() + "\n - Entity OP code: " + s.getOpCode());
        } else if (event.getPacket() instanceof SPacketEntityTeleport && this.SEntityTeleport.getValue().booleanValue()) {
            SPacketEntityTeleport s = (SPacketEntityTeleport)event.getPacket();
            ClientMessage.sendMessage("SPacketEntityTeleport:\n - Entity Id: " + s.getEntityId() + "\n - Entity Pos: " + s.getX() + " " + s.getY() + " " + s.getZ() + "\n - Entity Yaw: " + s.getYaw() + "\n - Entity Pitch: " + s.getPitch() + "\n - Is Entity On Ground: " + s.getOnGround());
        } else if (event.getPacket() instanceof SPacketEntityVelocity && this.SEntityVelocity.getValue().booleanValue()) {
            SPacketEntityVelocity s = (SPacketEntityVelocity)event.getPacket();
            ClientMessage.sendMessage("SPacketEntityVelocity:\n - Entity Id: " + s.getEntityID() + "\n - MotionX: " + s.motionX + "\n - MotionY: " + s.motionY + "\n - MotionZ: " + s.motionZ);
        } else if (event.getPacket() instanceof SPacketExplosion && this.SExplosion.getValue().booleanValue()) {
            SPacketExplosion s = (SPacketExplosion)event.getPacket();
            ClientMessage.sendMessage("SPacketExplosion:\n - Explosion Pos: " + s.posX + " " + s.getY() + " " + s.getZ() + "\n - MotionX: " + s.motionX + "\n - MotionY: " + s.motionY + "\n - MotionZ: " + s.motionZ + "\n - Strength: " + s.getStrength());
        } else if (event.getPacket() instanceof SPacketEnableCompression && this.SEnableCompression.getValue().booleanValue()) {
            SPacketEnableCompression s = (SPacketEnableCompression)event.getPacket();
            ClientMessage.sendMessage("SPacketEnableCompression:\n - Compression Threshold: " + s.getCompressionThreshold());
        } else if (event.getPacket() instanceof SPacketEncryptionRequest && this.SEncryptionRequest.getValue().booleanValue()) {
            SPacketEncryptionRequest s = (SPacketEncryptionRequest)event.getPacket();
            ClientMessage.sendMessage("SPacketEncryptionRequest:\n - Server Id: " + s.getServerId() + "\n - Public key: " + s.getPublicKey());
        } else if (event.getPacket() instanceof SPacketHeldItemChange && this.SHeldItemChange.getValue().booleanValue()) {
            SPacketHeldItemChange s = (SPacketHeldItemChange)event.getPacket();
            ClientMessage.sendMessage("SPacketEncryptionRequest:\n - Held Item Hotbar Index: " + s.getHeldItemHotbarIndex());
        } else if (event.getPacket() instanceof SPacketJoinGame && this.SJoinGame.getValue().booleanValue()) {
            SPacketJoinGame s = (SPacketJoinGame)event.getPacket();
            ClientMessage.sendMessage("SPacketJoinGame:\n - Player ID: " + s.getPlayerId() + "\n - Difficulty: " + s.getDifficulty().name() + "\n - Dimension: " + s.getDimension() + "\n - Game Type: " + s.getGameType().getName() + "\n - World Type: " + s.getWorldType().getName() + "\n - Max Players: " + s.getMaxPlayers() + "\n - Is Hardcore Mode: " + s.isHardcoreMode());
        } else if (event.getPacket() instanceof SPacketKeepAlive && this.SKeepAlive.getValue().booleanValue()) {
            SPacketKeepAlive s = (SPacketKeepAlive)event.getPacket();
            ClientMessage.sendMessage("SPacketKeepAlive:\n - ID: " + s.getId());
        } else if (event.getPacket() instanceof SPacketLoginSuccess && this.SLoginSuccess.getValue().booleanValue()) {
            SPacketLoginSuccess s = (SPacketLoginSuccess)event.getPacket();
            ClientMessage.sendMessage("SPacketLoginSuccess:\n - Name: " + s.getProfile().getName());
        } else if (event.getPacket() instanceof SPacketMaps && this.SMaps.getValue().booleanValue()) {
            SPacketMaps s = (SPacketMaps)event.getPacket();
            ClientMessage.sendMessage("SPacketMaps:\n - Map ID: " + s.getMapId());
        } else if (event.getPacket() instanceof SPacketMoveVehicle && this.SMoveVehicle.getValue().booleanValue()) {
            SPacketMoveVehicle s = (SPacketMoveVehicle)event.getPacket();
            ClientMessage.sendMessage("SPacketMoveVehicle:\n - Pitch: " + s.getPitch() + "\n - Yaw: " + s.getYaw() + "\n - Pos: " + s.getX() + " " + s.getY() + " " + s.getZ());
        } else if (event.getPacket() instanceof SPacketMultiBlockChange && this.SMultiBlockChange.getValue().booleanValue()) {
            SPacketMultiBlockChange s = (SPacketMultiBlockChange)event.getPacket();
            ClientMessage.sendMessage("SPacketMultiBlockChange");
        } else if (event.getPacket() instanceof SPacketOpenWindow && this.SOpenWindow.getValue().booleanValue()) {
            SPacketOpenWindow s = (SPacketOpenWindow)event.getPacket();
            ClientMessage.sendMessage("SPacketOpenWindow:\n - Gui ID: " + s.getGuiId() + "\n - Entity ID: " + s.getEntityId() + "\n - Window ID: " + s.getWindowId() + "\n - Window Title: " + (Object)s.getWindowTitle() + "\n - Slot Count: " + s.getSlotCount());
        } else if (event.getPacket() instanceof SPacketParticles && this.SParticles.getValue().booleanValue()) {
            SPacketParticles s = (SPacketParticles)event.getPacket();
            ClientMessage.sendMessage("SPacketParticles:\n - Particle Count: " + s.getParticleCount() + "\n - Particle Speed: " + s.getParticleSpeed() + "\n - Particle Name: " + s.getParticleType().getParticleName() + "\n - Pos: " + s.getXCoordinate() + " " + s.getYCoordinate() + " " + s.getZCoordinate());
        } else if (event.getPacket() instanceof SPacketPlayerAbilities && this.SPlayerAbilities.getValue().booleanValue()) {
            SPacketPlayerAbilities s = (SPacketPlayerAbilities)event.getPacket();
            ClientMessage.sendMessage("SPacketPlayerAbilities:\n - Walk Speed: " + s.getWalkSpeed() + "\n - Fly Speed: " + s.getFlySpeed() + "\n - Is Allow Flying: " + s.isAllowFlying() + "\n - Is Creative Mode: " + s.isCreativeMode() + "\n - Is Flying: " + s.isFlying() + "\n - Is Flying: " + s.isInvulnerable());
        } else if (event.getPacket() instanceof SPacketPlayerListHeaderFooter && this.SPlayerListHeaderFooter.getValue().booleanValue()) {
            SPacketPlayerListHeaderFooter s = (SPacketPlayerListHeaderFooter)event.getPacket();
            ClientMessage.sendMessage("SPacketPlayerListHeaderFooter:\n - Footer: " + s.getFooter().getFormattedText() + "\n - Header: " + (Object)s.getHeader());
        } else if (event.getPacket() instanceof SPacketPlayerListItem && this.SPlayerListItem.getValue().booleanValue()) {
            SPacketPlayerListItem s = (SPacketPlayerListItem)event.getPacket();
            ClientMessage.sendMessage("SPacketPlayerListItem:\n - Action Name: " + s.getAction().name());
        } else if (event.getPacket() instanceof SPacketPlayerPosLook && this.SPlayerPosLook.getValue().booleanValue()) {
            SPacketPlayerPosLook s = (SPacketPlayerPosLook)event.getPacket();
            ClientMessage.sendMessage("SPacketPlayerPosLook:\n - Pitch: " + s.getPitch() + "\n - Yaw: " + s.getYaw() + "\n - Pos: " + s.getX() + " " + s.getY() + " " + s.getZ() + "\n - Teleport ID: " + s.getTeleportId());
        } else if (event.getPacket() instanceof SPacketPong && this.SPong.getValue().booleanValue()) {
            SPacketPong s = (SPacketPong)event.getPacket();
            ClientMessage.sendMessage("SPacketPong");
        } else if (event.getPacket() instanceof SPacketRecipeBook && this.SRecipeBook.getValue().booleanValue()) {
            SPacketRecipeBook s = (SPacketRecipeBook)event.getPacket();
            ClientMessage.sendMessage("SPacketRecipeBook");
        } else if (event.getPacket() instanceof SPacketRespawn && this.SRespawn.getValue().booleanValue()) {
            SPacketRespawn s = (SPacketRespawn)event.getPacket();
            ClientMessage.sendMessage("SPacketRecipeBook: \n - Dimension ID " + s.getDimensionID() + "\n - WorldType Name " + s.getWorldType().getName() + "\n - Difficulty " + s.getDifficulty().name() + "\n - GameType name " + s.getGameType().name());
        } else if (event.getPacket() instanceof SPacketRemoveEntityEffect && this.SRemoveEntityEffect.getValue().booleanValue()) {
            SPacketRemoveEntityEffect s = (SPacketRemoveEntityEffect)event.getPacket();
            ClientMessage.sendMessage("SPacketRemoveEntityEffect: \n - Entity Name " + s.getEntity((World)PacketLogger.mc.world).getName() + "\n - Potion Name " + s.getPotion().getName() + "\n - Entity ID " + s.getEntity((World)PacketLogger.mc.world).getEntityId());
        } else if (event.getPacket() instanceof SPacketScoreboardObjective && this.SScoreboardObjective.getValue().booleanValue()) {
            SPacketScoreboardObjective s = (SPacketScoreboardObjective)event.getPacket();
            ClientMessage.sendMessage("SPacketScoreboardObjective: \n - Objective Name " + s.getObjectiveName() + "\n - Acton " + s.getAction() + "\n - Render Type Name" + s.getRenderType().name());
        } else if (event.getPacket() instanceof SPacketServerDifficulty && this.SServerDifficulty.getValue().booleanValue()) {
            SPacketServerDifficulty s = (SPacketServerDifficulty)event.getPacket();
            ClientMessage.sendMessage("SPacketServerDifficulty: \n - Difficulty Name " + s.getDifficulty().name());
        } else if (event.getPacket() instanceof SPacketSelectAdvancementsTab && this.SSelectAdvancementsTab.getValue().booleanValue()) {
            SPacketSelectAdvancementsTab s = (SPacketSelectAdvancementsTab)event.getPacket();
            ClientMessage.sendMessage("SPacketSelectAdvancementsTab");
        } else if (event.getPacket() instanceof SPacketServerInfo && this.SServerInfo.getValue().booleanValue()) {
            SPacketServerInfo s = (SPacketServerInfo)event.getPacket();
            ClientMessage.sendMessage("SPacketServerInfo: \n - Server Info " + s.getResponse().getJson());
        } else if (event.getPacket() instanceof SPacketSetExperience && this.SSetExperience.getValue().booleanValue()) {
            SPacketSetExperience s = (SPacketSetExperience)event.getPacket();
            ClientMessage.sendMessage("SPacketSetExperience: \n - Experience Bar " + s.getExperienceBar() + "\n - Total Experience " + s.getTotalExperience() + "\n - Level " + s.getLevel());
        } else if (event.getPacket() instanceof SPacketSetPassengers && this.SSetPassengers.getValue().booleanValue()) {
            SPacketSetPassengers s = (SPacketSetPassengers)event.getPacket();
            ClientMessage.sendMessage("SPacketSetPassengers: \n - Entity ID " + s.getEntityId() + "\n - Passengers ID " + s.getPassengerIds());
        } else if (event.getPacket() instanceof SPacketSetSlot && this.SSetSlot.getValue().booleanValue()) {
            SPacketSetSlot s = (SPacketSetSlot)event.getPacket();
            ClientMessage.sendMessage("SPacketSetSlot: \n - Window ID " + s.getWindowId() + "\n - Slot " + s.getSlot() + "\n - Item Name " + s.getStack().getDisplayName());
        } else if (event.getPacket() instanceof SPacketSignEditorOpen && this.SSignEditorOpen.getValue().booleanValue()) {
            SPacketSignEditorOpen s = (SPacketSignEditorOpen)event.getPacket();
            ClientMessage.sendMessage("SPacketSignEditorOpen: \n - Sign Pos " + (Object)s.getSignPosition());
        } else if (event.getPacket() instanceof SPacketSoundEffect && this.SSoundEffect.getValue().booleanValue()) {
            SPacketSoundEffect s = (SPacketSoundEffect)event.getPacket();
            ClientMessage.sendMessage("SPacketSoundEffect: \n - Sound Name: " + (Object)s.getSound().getSoundName() + "\n - Sound Category: " + s.getCategory().getName() + "\n - Sound Pos: " + s.getX() + " " + s.getY() + " " + s.getZ() + "\n - Sound Pitch: " + s.getPitch() + "\n - Sound Volume: " + s.getVolume());
        } else if (event.getPacket() instanceof SPacketSpawnGlobalEntity && this.SSpawnGlobalEntity.getValue().booleanValue()) {
            SPacketSpawnGlobalEntity s = (SPacketSpawnGlobalEntity)event.getPacket();
            ClientMessage.sendMessage("SPacketSpawnGlobalEntity: \n - Entity ID: " + s.getEntityId() + "\n - Pos: " + s.getX() + " " + s.getY() + " " + s.getZ() + "\n - Type: " + s.getType());
        } else if (event.getPacket() instanceof SPacketSpawnMob && this.SSpawnMob.getValue().booleanValue()) {
            SPacketSpawnMob s = (SPacketSpawnMob)event.getPacket();
            ClientMessage.sendMessage("SPacketSpawnMob: \n - Entity ID: " + s.getEntityID() + "\n - Pos: " + s.getX() + " " + s.getY() + " " + s.getZ() + "\n - UUID: " + s.getUniqueId() + "\n - Yaw " + s.getYaw() + "\n - Pitch " + s.getPitch() + "\n - Type: " + s.getEntityType());
        } else if (event.getPacket() instanceof SPacketSpawnPlayer && this.SSpawnPlayer.getValue().booleanValue()) {
            SPacketSpawnPlayer s = (SPacketSpawnPlayer)event.getPacket();
            ClientMessage.sendMessage("SPacketSpawnPlayer: \n - Entity ID: " + s.getEntityID() + "\n - Pos: " + s.getX() + " " + s.getY() + " " + s.getZ() + "\n - UUID: " + s.getUniqueId() + "\n - Yaw " + s.getYaw() + "\n - Pitch " + s.getPitch());
        } else if (event.getPacket() instanceof SPacketSpawnExperienceOrb && this.SSpawnExperienceOrb.getValue().booleanValue()) {
            SPacketSpawnExperienceOrb s = (SPacketSpawnExperienceOrb)event.getPacket();
            ClientMessage.sendMessage("SPacketSpawnExperienceOrb: \n - Entity ID: " + s.getEntityID() + "\n - Pos: " + s.getX() + " " + s.getY() + " " + s.getZ() + "\n - XP value: " + s.getXPValue());
        } else if (event.getPacket() instanceof SPacketSpawnPainting && this.SSpawnPainting.getValue().booleanValue()) {
            SPacketSpawnPainting s = (SPacketSpawnPainting)event.getPacket();
            ClientMessage.sendMessage("SPacketSpawnPainting: \n - Entity ID: " + s.getEntityID() + "\n - Title: " + s.getTitle() + "\n - Pos: " + (Object)s.getPosition() + "\n - UUID: " + s.getUniqueId() + "\n - Facing: " + s.getFacing().getName());
        } else if (event.getPacket() instanceof SPacketSpawnObject && this.SSpawnObject.getValue().booleanValue()) {
            SPacketSpawnObject s = (SPacketSpawnObject)event.getPacket();
            ClientMessage.sendMessage("SPacketSpawnObject: \n - Entity ID: " + s.getEntityID() + "\n - Pos: " + s.getX() + " " + s.getY() + " " + s.getZ() + "\n - Speed Pos: " + s.getSpeedX() + " " + s.getSpeedY() + " " + s.getSpeedZ() + "\n - UUID: " + s.getUniqueId() + "\n - Data: " + s.getData() + "\n - Type: " + s.getType() + "\n - Pitch: " + s.getPitch() + "\n - Yaw: " + s.getYaw());
        } else if (event.getPacket() instanceof SPacketSpawnPosition && this.SSpawnPosition.getValue().booleanValue()) {
            SPacketSpawnPosition s = (SPacketSpawnPosition)event.getPacket();
            ClientMessage.sendMessage("SPacketSpawnPosition: \n - Pos: " + (Object)s.getSpawnPos());
        } else if (event.getPacket() instanceof SPacketTabComplete && this.STabComplete.getValue().booleanValue()) {
            SPacketTabComplete s = (SPacketTabComplete)event.getPacket();
            ClientMessage.sendMessage("SPacketTabComplete");
        } else if (event.getPacket() instanceof SPacketUnloadChunk && this.SUnloadChunk.getValue().booleanValue()) {
            SPacketUnloadChunk s = (SPacketUnloadChunk)event.getPacket();
            ClientMessage.sendMessage("SPacketUnloadChunk\n - Chunk Pos: " + s.getX() + " " + s.getZ());
        } else if (event.getPacket() instanceof SPacketUseBed && this.SUseBed.getValue().booleanValue()) {
            SPacketUseBed s = (SPacketUseBed)event.getPacket();
            ClientMessage.sendMessage("SPacketUseBed\n - Pos: " + (Object)s.getBedPosition() + "\n - Player name: " + s.getPlayer((World)PacketLogger.mc.world).getName());
        } else if (event.getPacket() instanceof SPacketUpdateHealth && this.SUpdateHealth.getValue().booleanValue()) {
            SPacketUpdateHealth s = (SPacketUpdateHealth)event.getPacket();
            ClientMessage.sendMessage("SPacketUpdateHealth\n - Health: " + s.getHealth() + "\n - Food: " + s.getFoodLevel() + "\n - Saturation: " + s.getSaturationLevel());
        } else if (event.getPacket() instanceof SPacketUpdateTileEntity && this.SUpdateHealth.getValue().booleanValue()) {
            SPacketUpdateTileEntity s = (SPacketUpdateTileEntity)event.getPacket();
            ClientMessage.sendMessage("SPacketUpdateTileEntity\n - Pos: " + (Object)s.getPos() + "\n - Type: " + s.getTileEntityType() + "\n - NBT tag: " + (Object)s.getNbtCompound());
        }
    }
}

