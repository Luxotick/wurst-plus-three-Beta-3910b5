/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.hud.element;

import java.util.ArrayList;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.util.ReflectionUtil;

public final class HudManager {
    ArrayList<HudElement> hudElements = new ArrayList();

    public HudManager() {
        try {
            ArrayList<Class<?>> modClasses = ReflectionUtil.getClassesForPackage("me.travis.wurstplusthree.gui.hud.element.elements");
            modClasses.spliterator().forEachRemaining(aClass -> {
                if (HudElement.class.isAssignableFrom((Class<?>)aClass)) {
                    try {
                        HudElement module = (HudElement)aClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                        this.hudElements.add(module);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<HudElement> getHudElements() {
        return this.hudElements;
    }

    public HudElement getElementByName(String name) {
        for (HudElement element : this.hudElements) {
            if (!element.getName().equalsIgnoreCase(name)) continue;
            return element;
        }
        return null;
    }

    public final void onRender2D(Render2DEvent event) {
        this.hudElements.stream().filter(HudElement::isEnabled).spliterator().forEachRemaining(hack -> hack.onRender2D(event));
    }
}

