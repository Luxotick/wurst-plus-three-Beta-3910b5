/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.travis.wurstplusthree.util.Globals;

public abstract class Command
implements Globals {
    protected List<String> names;
    protected List<List<String>> arguments;

    public Command(String name) {
        this.names = Collections.singletonList(name);
    }

    public Command(String ... names) {
        this.names = Arrays.asList((Object[])names.clone());
    }

    public Command(List<List<String>> arguments) {
        this.arguments = arguments;
    }

    public abstract void execute(String[] var1);

    public List<String> getNames() {
        return this.names;
    }

    public List<List<String>> getArguments() {
        return this.arguments;
    }

    public boolean isName(String name) {
        for (String name_ : this.names) {
            if (!name_.equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }
}

