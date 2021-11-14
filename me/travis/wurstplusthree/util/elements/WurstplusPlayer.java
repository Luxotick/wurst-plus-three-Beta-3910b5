/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.util.elements;

import java.util.UUID;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.PlayerUtil;

public class WurstplusPlayer
implements Globals {
    private final String name;
    private String nickName;

    public WurstplusPlayer(String name) {
        this.name = name;
        PlayerUtil.getUUIDFromName(name);
    }

    public WurstplusPlayer(String name, UUID uuid) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String name) {
        this.nickName = name;
    }
}

