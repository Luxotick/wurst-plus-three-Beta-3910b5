/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.event.processor;

import java.lang.reflect.Method;
import me.travis.wurstplusthree.event.processor.EventPriority;
import org.jetbrains.annotations.NotNull;

public final class Listener {
    public final Method method;
    public final Object object;
    public final Class<?> event;
    public final EventPriority priority;

    public Listener(@NotNull Method method, @NotNull Object object, @NotNull Class<?> event, @NotNull EventPriority priority) {
        this.method = method;
        this.object = object;
        this.event = event;
        this.priority = priority;
    }
}

