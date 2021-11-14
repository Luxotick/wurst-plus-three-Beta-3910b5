/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.event.processor;

public class Event {
    private boolean isCancelled = false;

    public final boolean isCancelled() {
        return this.isCancelled;
    }

    public final void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }
}

