/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.setting.type;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.Setting;

public class ParentSetting
extends Setting<Boolean> {
    public ParentSetting(String name, Hack parent) {
        super(name, Boolean.valueOf(false), parent);
    }

    public void toggle() {
        this.value = (Boolean)this.value == false;
    }

    @Override
    public boolean isShown() {
        return true;
    }

    @Override
    public Boolean getValue() {
        return (Boolean)this.value;
    }

    @Override
    public String getType() {
        return "boolean";
    }
}

