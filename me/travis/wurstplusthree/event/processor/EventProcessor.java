/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.event.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.Event;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.event.processor.Listener;
import org.jetbrains.annotations.NotNull;

public final class EventProcessor {
    private final List<Listener> events = new ArrayList<Listener>();

    public final void addEventListener(@NotNull Object object) throws IllegalArgumentException {
        this.getEvents(object);
    }

    public final void removeEventListener(@NotNull Object object) {
        this.events.removeIf(listener -> listener.object == object);
    }

    private void getEvents(@NotNull Object object) {
        Class<?> clazz = object.getClass();
        Arrays.stream(clazz.getDeclaredMethods()).spliterator().forEachRemaining(method -> {
            if (method.isAnnotationPresent(CommitEvent.class)) {
                Class<?>[] prams = method.getParameterTypes();
                if (prams.length != 1) {
                    throw new IllegalArgumentException("Method " + method + " doesnt have any event parameters");
                }
                if (!Event.class.isAssignableFrom(prams[0])) {
                    throw new IllegalArgumentException("Method " + method + " doesnt have any event parameters only non event parameters");
                }
                this.events.add(new Listener((Method)method, object, prams[0], this.getPriority((Method)method)));
                this.events.sort(Comparator.comparing(o -> o.priority));
            }
        });
    }

    public final boolean postEvent(@NotNull Event event) {
        this.events.spliterator().forEachRemaining(listener -> {
            if (listener.event == event.getClass()) {
                listener.method.setAccessible(true);
                try {
                    listener.method.invoke(listener.object, event);
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    private EventPriority getPriority(@NotNull Method method) {
        return method.getAnnotation(CommitEvent.class).priority();
    }
}

