/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.util;

import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.util.Globals;

public class HackUtil
implements Globals {
    public static boolean shouldPause(Hack hack) {
        return WurstplusThree.HACKS.ishackEnabled("Surround") && !hack.getName().equalsIgnoreCase("Surround");
    }
}

