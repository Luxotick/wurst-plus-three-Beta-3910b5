/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.setting.type;

import java.util.function.Predicate;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.Setting;
import me.travis.wurstplusthree.setting.type.ParentSetting;

public class BooleanSetting
extends Setting<Boolean> {
    public BooleanSetting(String name, Boolean value, Hack parent) {
        super(name, value, parent);
    }

    public BooleanSetting(String name, Boolean value, ParentSetting parent) {
        super(name, value, parent);
    }

    public BooleanSetting(String name, Boolean value, Hack parent, Predicate shown) {
        super(name, value, parent, shown);
    }

    public BooleanSetting(String name, Boolean value, ParentSetting parent, Predicate shown) {
        super(name, value, parent, shown);
    }

    public void toggle() {
        this.value = (Boolean)this.value == false;
        if (this.getParent().isEnabled()) {
            this.getParent().onSettingChange();
        }
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

