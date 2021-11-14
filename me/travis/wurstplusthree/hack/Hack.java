/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraftforge.common.MinecraftForge
 *  org.lwjgl.input.Keyboard
 */
package me.travis.wurstplusthree.hack;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.setting.Setting;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.Globals;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class Hack
implements Globals {
    private final String name = this.getMod().name();
    private final String description = this.getMod().description();
    private final Category category = this.getMod().category();
    private final HackPriority priority = this.getMod().priority();
    private int bind = this.getMod().bind();
    private boolean hold = this.getMod().hold();
    private boolean notification = this.getMod().shown();
    private boolean isEnabled = this.getMod().enabled();
    private int isListening = this.getMod().isListening() ? 0 : 1;

    private Registration getMod() {
        return this.getClass().getAnnotation(Registration.class);
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onUnload() {
    }

    public void onSettingChange() {
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public boolean isHold() {
        return this.hold;
    }

    public void toggleHold() {
        this.hold = !this.hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public void stopListening() {
        this.isListening = -1;
    }

    public void enable() {
        this.isEnabled = true;
        this.onEnable();
        if (this.isEnabled() && this.isListening()) {
            MinecraftForge.EVENT_BUS.register((Object)this);
            WurstplusThree.EVENT_PROCESSOR.addEventListener(this);
        }
        if (this.notification) {
            ClientMessage.sendToggleMessage(this, true);
        }
    }

    public void disable() {
        if (this.isListening != 0) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            WurstplusThree.EVENT_PROCESSOR.removeEventListener(this);
        }
        this.isEnabled = false;
        this.onDisable();
        if (this.notification) {
            ClientMessage.sendToggleMessage(this, false);
        }
    }

    public void toggle() {
        if (this.isEnabled()) {
            this.disable();
        } else {
            this.enable();
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled && !this.isEnabled()) {
            this.enable();
        }
        if (!enabled && this.isEnabled()) {
            this.disable();
        }
    }

    public boolean isListening() {
        return this.isListening >= -1;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDisplayInfo() {
        return null;
    }

    public int getBind() {
        return this.bind;
    }

    public boolean isNotification() {
        return this.notification;
    }

    public String getBindName() {
        return Keyboard.getKeyName((int)this.bind);
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public Category getCategory() {
        return this.category;
    }

    public List<Setting> getSettings() {
        ArrayList<Setting> settings = new ArrayList<Setting>();
        for (Setting setting : WurstplusThree.SETTINGS.getSettings()) {
            if (setting.getParent() != this) continue;
            settings.add(setting);
        }
        return settings;
    }

    public String getFullArrayString() {
        return this.name + (this.getDisplayInfo() != null ? (Object)ChatFormatting.GOLD + "[" + this.getDisplayInfo().toUpperCase() + "]" : "");
    }

    public Setting getSettingByName(String name) {
        for (Setting setting : this.getSettings()) {
            if (!setting.getName().equalsIgnoreCase(name)) continue;
            return setting;
        }
        return null;
    }

    public HackPriority getPriority() {
        return this.priority;
    }

    public static enum Category {
        CHAT("Chat"),
        COMBAT("Combat"),
        MISC("Misc"),
        RENDER("Render"),
        PLAYER("Player"),
        CLIENT("Client"),
        HIDDEN("Hidden"),
        HUD("Hud");

        private final String name;

        private Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface Registration {
        public String name();

        public String description();

        public Category category();

        public HackPriority priority() default HackPriority.Normal;

        public boolean isListening() default false;

        public int bind() default 0;

        public boolean enabled() default false;

        public boolean shown() default true;

        public boolean hold() default false;
    }
}

