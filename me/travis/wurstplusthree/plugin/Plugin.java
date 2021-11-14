/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.plugin;

public interface Plugin {
    default public void init() {
    }

    default public String name() {
        return this.getClass().getSimpleName();
    }
}

