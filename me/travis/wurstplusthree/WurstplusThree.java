/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.Display
 */
package me.travis.wurstplusthree;

import me.travis.wurstplusthree.command.Commands;
import me.travis.wurstplusthree.event.Events;
import me.travis.wurstplusthree.event.processor.EventProcessor;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.gui.hud.WurstplusHudGui;
import me.travis.wurstplusthree.gui.hud.element.HudManager;
import me.travis.wurstplusthree.hack.Hacks;
import me.travis.wurstplusthree.manager.AltManager;
import me.travis.wurstplusthree.manager.CapeManager;
import me.travis.wurstplusthree.manager.ConfigManager;
import me.travis.wurstplusthree.manager.CosmeticManager;
import me.travis.wurstplusthree.manager.EnemyManager;
import me.travis.wurstplusthree.manager.FriendManager;
import me.travis.wurstplusthree.manager.HelpManager;
import me.travis.wurstplusthree.manager.KDManager;
import me.travis.wurstplusthree.manager.PopManager;
import me.travis.wurstplusthree.manager.PositionManager;
import me.travis.wurstplusthree.manager.RotationManager;
import me.travis.wurstplusthree.manager.ServerManager;
import me.travis.wurstplusthree.manager.SongManager;
import me.travis.wurstplusthree.manager.fonts.DonatorFont;
import me.travis.wurstplusthree.manager.fonts.GuiFont;
import me.travis.wurstplusthree.manager.fonts.MenuFont;
import me.travis.wurstplusthree.networking.chat.handler.ChatHandling;
import me.travis.wurstplusthree.networking.chat.handler.ClientHandling;
import me.travis.wurstplusthree.networking.proxy.WurstPlusProxy;
import me.travis.wurstplusthree.setting.Settings;
import me.travis.wurstplusthree.util.RenderUtil2D;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid="wurstplusthree", name="Wurst+3", version="0.7.0+3910b5a53abaa7a012c8dd616a29ffd4d5397611")
public class WurstplusThree {
    public static final String MODID = "wurstplusthree";
    public static final String MODNAME = "Wurst+3";
    public static final String MODVER = "0.7.0+3910b5a53abaa7a012c8dd616a29ffd4d5397611";
    public static final Logger LOGGER = LogManager.getLogger((String)"wurstplusthree");
    public static Events EVENTS;
    public static EventProcessor EVENT_PROCESSOR;
    public static Commands COMMANDS;
    public static Hacks HACKS;
    public static Settings SETTINGS;
    public static WurstplusGuiNew GUI2;
    public static WurstplusHudGui HUDGUI;
    public static MenuFont MENU_FONT_MANAGER;
    public static HelpManager HELP_MANAGER;
    public static GuiFont GUI_FONT_MANAGER;
    public static DonatorFont DONATOR_FONT_MANAGER;
    public static FriendManager FRIEND_MANAGER;
    public static EnemyManager ENEMY_MANAGER;
    public static PopManager POP_MANAGER;
    public static ServerManager SERVER_MANAGER;
    public static PositionManager POS_MANAGER;
    public static RotationManager ROTATION_MANAGER;
    public static ConfigManager CONFIG_MANAGER;
    public static SongManager SONG_MANAGER;
    public static CapeManager CAPE_MANAGER;
    public static CosmeticManager COSMETIC_MANAGER;
    public static AltManager ALT_MANAGER;
    public static ClientHandling CLIENT_HANDLING;
    public static ChatHandling CHAT_HANDLING;
    public static HudManager HUD_MANAGER;
    public static KDManager KD_MANAGER;
    public static WurstPlusProxy PROXY;
    public static RenderUtil2D RENDER_UTIL_2D;
    @Mod.Instance
    public static WurstplusThree INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("loading Wurst+3");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        this.load();
        LOGGER.info("Wurst+3 : 0.7.0+3910b5a53abaa7a012c8dd616a29ffd4d5397611 has been loaded");
        Display.setTitle((String)"Wurst+3 | v0.7.0+3910b5a53abaa7a012c8dd616a29ffd4d5397611");
    }

    public void load() {
        EVENT_PROCESSOR = new EventProcessor();
        EVENTS = new Events();
        SETTINGS = new Settings();
        RENDER_UTIL_2D = new RenderUtil2D();
        COMMANDS = new Commands();
        HACKS = new Hacks();
        HUD_MANAGER = new HudManager();
        this.loadManagers();
        CLIENT_HANDLING = new ClientHandling();
        CHAT_HANDLING = new ChatHandling();
        CONFIG_MANAGER.loadConfig();
        GUI2 = new WurstplusGuiNew();
        HUDGUI = new WurstplusHudGui();
    }

    public static void unLoad() {
        CONFIG_MANAGER.saveConfig();
    }

    public void loadManagers() {
        MENU_FONT_MANAGER = new MenuFont();
        GUI_FONT_MANAGER = new GuiFont();
        FRIEND_MANAGER = new FriendManager();
        ENEMY_MANAGER = new EnemyManager();
        POP_MANAGER = new PopManager();
        SERVER_MANAGER = new ServerManager();
        POS_MANAGER = new PositionManager();
        HELP_MANAGER = new HelpManager();
        ROTATION_MANAGER = new RotationManager();
        CONFIG_MANAGER = new ConfigManager();
        SONG_MANAGER = new SongManager();
        CAPE_MANAGER = new CapeManager();
        DONATOR_FONT_MANAGER = new DonatorFont();
        COSMETIC_MANAGER = new CosmeticManager();
        ALT_MANAGER = new AltManager();
        KD_MANAGER = new KDManager();
    }
}

