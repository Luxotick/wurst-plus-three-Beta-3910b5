/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerListItem$Action
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.client.event.GuiScreenEvent$InitGuiEvent$Post
 *  net.minecraftforge.client.event.MouseEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$MouseInputEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientConnectedToServerEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientDisconnectionFromServerEvent
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.event;

import com.google.common.base.Strings;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.UUID;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.ConnectionEvent;
import me.travis.wurstplusthree.event.events.DeathEvent;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.event.events.TotemPopEvent;
import me.travis.wurstplusthree.event.events.UpdateWalkingPlayerEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.gui.alt.defult.GuiAltButton;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.hack.hacks.render.Chams;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.elements.GLUProjection;
import me.travis.wurstplusthree.util.elements.Timer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class Events
implements Globals {
    private final Timer logoutTimer = new Timer();
    private long time = -1L;

    public Events() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        WurstplusThree.EVENT_PROCESSOR.addEventListener(this);
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        WurstplusThree.EVENT_PROCESSOR.removeEventListener(this);
    }

    @SubscribeEvent
    public void onMousePress(InputEvent.MouseInputEvent event) {
        int button = new MouseEvent().getButton();
        if (System.currentTimeMillis() - this.time < (long)Gui.INSTANCE.mouseDelay.getValue().intValue()) {
            this.time = System.currentTimeMillis();
            return;
        }
        this.time = System.currentTimeMillis();
        for (Hack hack : WurstplusThree.HACKS.getHacks()) {
            if (hack.getBind() >= -1 || hack.getBind() == 0) continue;
            if (button == 0 && hack.getBind() == -2) {
                hack.toggle();
                continue;
            }
            if (button == 1 && hack.getBind() == -3) {
                hack.toggle();
                continue;
            }
            if (button == 2 && hack.getBind() == -4) {
                hack.toggle();
                continue;
            }
            if (button == 3 && hack.getBind() == -5) {
                hack.toggle();
                continue;
            }
            if (button != 4 || hack.getBind() != -6) continue;
            hack.toggle();
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (!this.nullCheck()) {
            WurstplusThree.KD_MANAGER.onAttackEntity(event);
        }
    }

    @CommitEvent
    public void onSendAttackPacket(PacketEvent.Send event) {
        if (!this.nullCheck()) {
            WurstplusThree.KD_MANAGER.onSendAttackPacket(event);
        }
    }

    @CommitEvent
    public void onEntityDeath(DeathEvent event) {
        if (!this.nullCheck()) {
            WurstplusThree.KD_MANAGER.onEntityDeath(event);
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!this.nullCheck() && event.getEntity().getEntityWorld().isRemote && event.getEntityLiving().equals((Object)Events.mc.player)) {
            WurstplusThree.HACKS.onUpdate();
            WurstplusThree.HELP_MANAGER.onUpdate();
            WurstplusThree.KD_MANAGER.onUpdate();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!this.nullCheck()) {
            WurstplusThree.HACKS.onTick();
        }
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            WurstplusThree.ROTATION_MANAGER.updateRotations();
            WurstplusThree.POS_MANAGER.updatePosition();
        }
        if (event.getStage() == 1) {
            WurstplusThree.ROTATION_MANAGER.restoreRotations();
            WurstplusThree.POS_MANAGER.restorePosition();
        }
    }

    @SubscribeEvent(priority=net.minecraftforge.fml.common.eventhandler.EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            ScaledResolution resolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            WurstplusThree.HACKS.onRender2D(render2DEvent);
            WurstplusThree.HUD_MANAGER.onRender2D(render2DEvent);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        Events.mc.profiler.startSection("wurstplus");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.shadeModel((int)7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth((float)1.0f);
        Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        GLUProjection projection = GLUProjection.getInstance();
        IntBuffer viewPort = GLAllocation.createDirectIntBuffer((int)16);
        FloatBuffer modelView = GLAllocation.createDirectFloatBuffer((int)16);
        FloatBuffer projectionPort = GLAllocation.createDirectFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projectionPort);
        GL11.glGetInteger((int)2978, (IntBuffer)viewPort);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        projection.updateMatrices(viewPort, modelView, projectionPort, (double)scaledResolution.getScaledWidth() / (double)Events.mc.displayWidth, (double)scaledResolution.getScaledHeight() / (double)Events.mc.displayHeight);
        WurstplusThree.HACKS.onRender3D(render3dEvent);
        GlStateManager.glLineWidth((float)1.0f);
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        Events.mc.profiler.endSection();
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        WurstplusThree.HACKS.onLogout();
        WurstplusThree.POP_MANAGER.onLogout();
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        WurstplusThree.HACKS.onLogin();
        if (Events.mc.world == null || !Events.mc.world.isRemote) {
            return;
        }
        WurstplusThree.CONFIG_MANAGER.onLogin(event);
    }

    @SubscribeEvent(priority=net.minecraftforge.fml.common.eventhandler.EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if (event.getMessage().startsWith(WurstplusThree.COMMANDS.getPrefix())) {
            event.setCanceled(true);
            try {
                Events.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    WurstplusThree.COMMANDS.executeCommand(event.getMessage().substring(1));
                } else {
                    ClientMessage.sendErrorMessage("enter a command");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                ClientMessage.sendErrorMessage("error handling command");
            }
            event.setMessage("");
        }
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public final void onPacketSend(@NotNull PacketEvent.Send event) {
        WurstplusThree.ROTATION_MANAGER.onPacketSend(event);
    }

    @CommitEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() != 0) {
            return;
        }
        WurstplusThree.SERVER_MANAGER.onPacketReceived();
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            WurstplusThree.ROTATION_MANAGER.onPacketReceive((SPacketPlayerPosLook)event.getPacket());
        }
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            try {
                if (packet.getOpCode() == 35 && packet.getEntity((World)Events.mc.world) instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer)packet.getEntity((World)Events.mc.world);
                    WurstplusThree.EVENT_PROCESSOR.addEventListener(new TotemPopEvent(player));
                    Chams.INSTANCE.onPopLol(new TotemPopEvent(player));
                    WurstplusThree.POP_MANAGER.onTotemPop(player);
                }
            }
            catch (Exception exception) {}
        } else if (event.getPacket() instanceof SPacketPlayerListItem && !this.nullCheck() && this.logoutTimer.passedS(1.0)) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals((Object)packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals((Object)packet.getAction())) {
                return;
            }
            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty((String)data.getProfile().getName()) || data.getProfile().getId() != null).forEach(data -> {
                UUID id = data.getProfile().getId();
                switch (packet.getAction()) {
                    case ADD_PLAYER: {
                        String name = data.getProfile().getName();
                        WurstplusThree.EVENT_PROCESSOR.addEventListener(new ConnectionEvent(0, id, name));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        EntityPlayer entity = Events.mc.world.getPlayerEntityByUUID(id);
                        if (entity != null) {
                            String logoutName = entity.getName();
                            WurstplusThree.EVENT_PROCESSOR.addEventListener(new ConnectionEvent(1, entity, id, logoutName));
                            break;
                        }
                        WurstplusThree.EVENT_PROCESSOR.addEventListener(new ConnectionEvent(2, id, null));
                    }
                }
            });
        } else if (event.getPacket() instanceof SPacketTimeUpdate) {
            WurstplusThree.SERVER_MANAGER.update();
        }
    }

    @SubscribeEvent
    public void GuiEvent(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiMainMenu) {
            event.getButtonList().add(new GuiAltButton(69, gui.width / 2 + 104, gui.height / 4 + 48 + 72 + 12, 20, 20, "Alt"));
        }
    }
}

