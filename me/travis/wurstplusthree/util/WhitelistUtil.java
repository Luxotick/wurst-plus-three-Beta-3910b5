/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 */
package me.travis.wurstplusthree.util;

import net.minecraft.block.Block;

public class WhitelistUtil {
    public static Block findBlock(String name) {
        return Block.getBlockFromName((String)name);
    }
}

