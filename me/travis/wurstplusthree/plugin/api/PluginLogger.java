/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.plugin.api;

import me.travis.wurstplusthree.WurstplusThree;

public class PluginLogger {
    public static void print(String msg) {
        WurstplusThree.LOGGER.info("Plugin -> " + msg);
    }
}

