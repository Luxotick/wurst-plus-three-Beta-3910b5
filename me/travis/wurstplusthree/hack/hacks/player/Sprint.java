/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.player;

import java.util.Arrays;
import me.travis.wurstplusthree.event.events.MoveEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.EnumSetting;

@Hack.Registration(name="Sprint", description="sprints automatically", category=Hack.Category.PLAYER)
public class Sprint
extends Hack {
    public EnumSetting mode = new EnumSetting("Mode", "Legit", Arrays.asList("legit", "Rage"), this);

    @CommitEvent
    public void onMove(MoveEvent event) {
        if (event.getStage() == 1 && this.mode.is("Rage") && (Sprint.mc.player.movementInput.moveForward != 0.0f || Sprint.mc.player.moveStrafing != 0.0f)) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (this.mode.is("Legit")) {
            if (Sprint.mc.gameSettings.keyBindForward.isKeyDown()) {
                Sprint.mc.player.setSprinting(true);
            }
        } else {
            Sprint.mc.player.setSprinting(true);
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.getValue();
    }
}

