/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.manager;

import java.util.ArrayList;
import java.util.List;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.elements.WurstplusPlayer;

public class EnemyManager
implements Globals {
    private List<WurstplusPlayer> enemies = new ArrayList<WurstplusPlayer>();

    public void addEnemy(String name) {
        if (!this.isEnemy(name)) {
            this.enemies.add(new WurstplusPlayer(name));
        }
    }

    public void removeEnemy(String name) {
        this.enemies.removeIf(player -> player.getName().equalsIgnoreCase(name));
    }

    public boolean isEnemy(String name) {
        for (WurstplusPlayer player : this.enemies) {
            if (!player.getName().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public boolean hasEnemies() {
        return !this.enemies.isEmpty();
    }

    public List<WurstplusPlayer> getEnemies() {
        return this.enemies;
    }

    public void clear() {
        this.enemies.clear();
    }

    public void setEnemies(List<WurstplusPlayer> list) {
        this.enemies = list;
    }
}

