/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.setting;

import java.util.ArrayList;
import java.util.List;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.Setting;

public class Settings {
    private final List<Setting> settings = new ArrayList<Setting>();

    public void addSetting(Setting setting) {
        this.settings.add(setting);
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public List<Setting> getSettingFromHack(Hack hack) {
        ArrayList<Setting> settings = new ArrayList<Setting>();
        for (Setting setting : this.settings) {
            if (setting.getParent() != hack) continue;
            settings.add(setting);
        }
        return settings;
    }

    public List<Setting> getSettingFromHack(String hack) {
        ArrayList<Setting> settings = new ArrayList<Setting>();
        for (Setting setting : this.settings) {
            if (!setting.getParent().getName().equalsIgnoreCase(hack)) continue;
            settings.add(setting);
        }
        return settings;
    }
}

