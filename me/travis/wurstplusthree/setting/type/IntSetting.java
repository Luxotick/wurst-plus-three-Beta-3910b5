/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.setting.type;

import java.util.function.Predicate;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.Setting;
import me.travis.wurstplusthree.setting.type.ParentSetting;

public class IntSetting
extends Setting<Integer> {
    private final int min;
    private final int max;

    public IntSetting(String name, int value, int min, int max, Hack parent) {
        super(name, Integer.valueOf(value), parent);
        this.min = min;
        this.max = max;
    }

    public IntSetting(String name, int value, int min, int max, ParentSetting parent) {
        super(name, Integer.valueOf(value), parent);
        this.min = min;
        this.max = max;
    }

    public IntSetting(String name, int value, int min, int max, Hack parent, Predicate shown) {
        super(name, Integer.valueOf(value), parent, shown);
        this.min = min;
        this.max = max;
    }

    public IntSetting(String name, int value, int min, int max, ParentSetting parent, Predicate shown) {
        super(name, Integer.valueOf(value), parent, shown);
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer getValue() {
        return (Integer)this.value;
    }

    public int getMax() {
        return this.max;
    }

    public int getMin() {
        return this.min;
    }

    public double getNumber() {
        return ((Integer)this.value).intValue();
    }

    public void setNumber(double value) {
        this.value = Math.toIntExact(Math.round(value));
        if (this.getParent().isEnabled()) {
            this.getParent().onSettingChange();
        }
    }

    public double getMaximumValue() {
        return this.max;
    }

    public double getMinimumValue() {
        return this.min;
    }

    public int getPrecision() {
        return 0;
    }

    @Override
    public String getType() {
        return "int";
    }
}

