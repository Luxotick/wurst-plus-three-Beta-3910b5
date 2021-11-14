/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.event.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.travis.wurstplusthree.event.processor.EventPriority;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface CommitEvent {
    public EventPriority priority() default EventPriority.NONE;
}

