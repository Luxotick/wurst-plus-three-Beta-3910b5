/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  org.lwjgl.input.Keyboard
 */
package me.travis.wurstplusthree.hack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.gui.WurstplusGuiNew;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.ReflectionUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

public final class Hacks
implements Globals {
    private final List<Hack> hacks = new ArrayList<Hack>();
    private final List<Hack> drawnHacks = new ArrayList<Hack>();

    public Hacks() {
        try {
            ArrayList<Class<?>> modClasses = ReflectionUtil.getClassesForPackage("me.travis.wurstplusthree.hack.hacks");
            modClasses.spliterator().forEachRemaining(aClass -> {
                if (Hack.class.isAssignableFrom((Class<?>)aClass)) {
                    try {
                        Hack module = (Hack)aClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                        this.hacks.add(module);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.hacks.sort(Comparator.comparing(Hack::getPriority));
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public final List<Hack> getHacks() {
        return this.hacks;
    }

    public final List<Hack> getDrawnHacks() {
        return this.drawnHacks;
    }

    public final boolean isDrawHack(Hack hack) {
        return this.drawnHacks.contains(hack);
    }

    public final void addDrawHack(Hack hack) {
        this.drawnHacks.add(hack);
    }

    public final void removeDrawnHack(Hack hack) {
        if (!this.isDrawHack(hack)) {
            return;
        }
        this.drawnHacks.remove(hack);
    }

    public final Hack getHackByName(String name) {
        for (Hack hack : this.hacks) {
            if (!hack.getName().equalsIgnoreCase(name)) continue;
            return hack;
        }
        return null;
    }

    public final void enablehack(String name) {
        this.getHackByName(name).enable();
    }

    public final void disablehack(String name) {
        this.getHackByName(name).disable();
    }

    public final boolean ishackEnabled(String name) {
        try {
            return this.getHackByName(name).isEnabled();
        }
        catch (NullPointerException error) {
            return false;
        }
    }

    public final void onUpdate() {
        this.hacks.stream().filter(Hack::isEnabled).spliterator().forEachRemaining(Hack::onUpdate);
        if (Hacks.mc.currentScreen == null) {
            for (Hack hack : this.hacks) {
                if (!hack.isHold() || hack.getBind() < 0) continue;
                if (Keyboard.isKeyDown((int)hack.getBind())) {
                    if (hack.isEnabled()) continue;
                    hack.enable();
                    continue;
                }
                if (!hack.isEnabled()) continue;
                hack.disable();
            }
        }
    }

    public final void onTick() {
        this.hacks.stream().filter(Hack::isEnabled).spliterator().forEachRemaining(Hack::onTick);
    }

    public final void onRender2D(Render2DEvent event) {
        this.hacks.stream().filter(Hack::isEnabled).spliterator().forEachRemaining(hack -> hack.onRender2D(event));
    }

    public final void onRender3D(Render3DEvent event) {
        this.hacks.stream().filter(Hack::isEnabled).spliterator().forEachRemaining(hack -> hack.onRender3D(event));
    }

    public final void onLogout() {
        this.hacks.spliterator().forEachRemaining(Hack::onLogout);
    }

    public final void onLogin() {
        this.hacks.spliterator().forEachRemaining(Hack::onLogin);
    }

    public final void onUnload() {
        this.hacks.forEach(((EventBus)MinecraftForge.EVENT_BUS)::unregister);
        this.hacks.forEach(WurstplusThree.EVENT_PROCESSOR::removeEventListener);
        this.hacks.forEach(Hack::onUnload);
    }

    public final void unloadAll() {
        for (Hack hack : this.hacks) {
            hack.disable();
        }
    }

    public final void onKeyDown(int key) {
        if (key <= 0 || Hacks.mc.currentScreen instanceof WurstplusGuiNew) {
            return;
        }
        for (Hack hack : this.hacks) {
            if (hack.isHold() || hack.getBind() != key) continue;
            hack.toggle();
        }
    }

    public final List<Hack.Category> getCategories() {
        ArrayList<Hack.Category> cats = new ArrayList<Hack.Category>();
        for (Hack.Category category : Hack.Category.values()) {
            if (category.getName().equalsIgnoreCase("hidden") || category.getName().equalsIgnoreCase("hud")) continue;
            cats.add(category);
        }
        return cats;
    }

    public final List<Hack> getHacksByCategory(Hack.Category cat) {
        ArrayList<Hack> hacks = new ArrayList<Hack>();
        for (Hack hack : this.hacks) {
            if (hack.getCategory() != cat) continue;
            hacks.add(hack);
        }
        hacks.sort(Comparator.comparing(Hack::getName));
        return hacks;
    }

    public final List<Hack> getEnabledHacks() {
        ArrayList<Hack> hacks = new ArrayList<Hack>();
        for (Hack hack : this.hacks) {
            if (!hack.isEnabled()) continue;
            hacks.add(hack);
        }
        return hacks;
    }

    public final List<Hack> getHacksAlp() {
        ArrayList<Hack> sortedHacks = new ArrayList<Hack>(this.hacks);
        sortedHacks.sort(Comparator.comparing(Hack::getName));
        return sortedHacks;
    }

    public final List<Hack> getSortedHacks(boolean reverse, boolean customFont) {
        if (customFont) {
            if (reverse) {
                List<Hack> list = this.getEnabledHacks().stream().sorted(Comparator.comparing(hack -> WurstplusThree.GUI_FONT_MANAGER.getTextWidth(hack.getFullArrayString()))).collect(Collectors.toList());
                Collections.reverse(list);
                return list;
            }
            return this.getEnabledHacks().stream().sorted(Comparator.comparing(hack -> WurstplusThree.GUI_FONT_MANAGER.getTextWidth(hack.getFullArrayString()))).collect(Collectors.toList());
        }
        if (reverse) {
            List<Hack> list = this.getEnabledHacks().stream().sorted(Comparator.comparing(hack -> Hacks.mc.fontRenderer.getStringWidth(hack.getFullArrayString()))).collect(Collectors.toList());
            Collections.reverse(list);
            return list;
        }
        return this.getEnabledHacks().stream().sorted(Comparator.comparing(hack -> Hacks.mc.fontRenderer.getStringWidth(hack.getFullArrayString()))).collect(Collectors.toList());
    }
}

