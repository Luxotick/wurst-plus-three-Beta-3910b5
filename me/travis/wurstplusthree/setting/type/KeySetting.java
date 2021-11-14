/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.travis.wurstplusthree.setting.type;

import java.util.function.Predicate;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.Setting;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import org.lwjgl.input.Keyboard;

public class KeySetting
extends Setting<Integer> {
    public KeySetting(String name, int value, Hack parent) {
        super(name, Integer.valueOf(value), parent);
    }

    public KeySetting(String name, int value, ParentSetting parent) {
        super(name, Integer.valueOf(value), parent);
    }

    public KeySetting(String name, int value, Hack parent, Predicate shown) {
        super(name, Integer.valueOf(value), parent, shown);
    }

    public KeySetting(String name, int value, ParentSetting parent, Predicate shown) {
        super(name, Integer.valueOf(value), parent, shown);
    }

    public int getKey() {
        return (Integer)this.value;
    }

    public void setKey(int key) {
        this.value = key;
        if (this.getParent().isEnabled()) {
            this.getParent().onSettingChange();
        }
    }

    public boolean isDown() {
        if ((Integer)this.value <= 0) {
            return false;
        }
        return Keyboard.isKeyDown((int)((Integer)this.value));
    }

    public String getKeyName() {
        return Keyboard.getKeyName((int)this.getKey());
    }

    @Override
    public String getType() {
        return "key";
    }
}

