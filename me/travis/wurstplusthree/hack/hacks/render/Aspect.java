/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.event.events.PerspectiveEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.DoubleSetting;

@Hack.Registration(name="Aspect", description="Does aspect shit", category=Hack.Category.RENDER, isListening=false)
public class Aspect
extends Hack {
    DoubleSetting aspect;

    public Aspect() {
        this.aspect = new DoubleSetting("Aspect", (Double)((double)(Aspect.mc.displayWidth / Aspect.mc.displayHeight) + 0.0), (Double)0.0, (Double)3.0, this);
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void onPerspectiveEvent(PerspectiveEvent event) {
        event.setAspect(this.aspect.getValue().floatValue());
    }
}

