/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.manager;

import java.util.ArrayList;
import java.util.List;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.elements.WurstplusPlayer;

public class FriendManager
implements Globals {
    private List<WurstplusPlayer> friends = new ArrayList<WurstplusPlayer>();

    public void addFriend(String name) {
        if (!this.isFriend(name)) {
            this.friends.add(new WurstplusPlayer(name));
        }
    }

    public void removeFriend(String name) {
        this.friends.removeIf(player -> player.getName().equalsIgnoreCase(name));
    }

    public boolean isFriend(String name) {
        for (WurstplusPlayer player : this.friends) {
            if (!player.getName().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public boolean hasFriends() {
        return !this.friends.isEmpty();
    }

    public List<WurstplusPlayer> getFriends() {
        return this.friends;
    }

    public void toggleFriend(String name) {
        if (this.isFriend(name)) {
            this.removeFriend(name);
        } else {
            this.addFriend(name);
        }
    }

    public void clear() {
        this.friends.clear();
    }

    public void setFriends(List<WurstplusPlayer> list) {
        this.friends = list;
    }
}

