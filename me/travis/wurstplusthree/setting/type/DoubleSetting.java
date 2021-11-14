/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.setting.type;

import java.util.function.Predicate;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.Setting;
import me.travis.wurstplusthree.setting.type.ParentSetting;

public class DoubleSetting
extends Setting<Double> {
    private final double min;
    private final double max;

    public DoubleSetting(String name, Double value, Double min, Double max, Hack parent) {
        super(name, value, parent);
        this.min = min;
        this.max = max;
    }

    public DoubleSetting(String name, Double value, Double min, Double max, ParentSetting parent) {
        super(name, value, parent);
        this.min = min;
        this.max = max;
    }

    public DoubleSetting(String name, Double value, Double min, Double max, Hack parent, Predicate shown) {
        super(name, value, parent, shown);
        this.min = min;
        this.max = max;
    }

    public DoubleSetting(String name, Double value, Double min, Double max, ParentSetting parent, Predicate shown) {
        super(name, value, parent, shown);
        this.min = min;
        this.max = max;
    }

    @Override
    public Double getValue() {
        return (Double)this.value;
    }

    public Double getMax() {
        return this.max;
    }

    public Double getMin() {
        return this.min;
    }

    public double getNumber() {
        return (Double)this.value;
    }

    public void setNumber(double value) {
        this.value = value;
        if (this.getParent().isEnabled()) {
            this.getParent().onSettingChange();
        }
    }

    public Float getAsFloat() {
        return Float.valueOf((float)((Double)this.value).doubleValue());
    }

    public double getMaximumValue() {
        return this.max;
    }

    public double getMinimumValue() {
        return this.min;
    }

    public int getPrecision() {
        return 2;
    }

    @Override
    public String getType() {
        return "double";
    }
}

