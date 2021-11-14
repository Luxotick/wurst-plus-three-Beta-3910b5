/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.util.elements;

public class Alt {
    String username;
    String password;
    boolean isCracked;

    public Alt(String username, String password) {
        this.username = username;
        this.password = password;
        this.isCracked = password.isEmpty();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean getCracked() {
        return this.isCracked;
    }
}

