/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.hud.element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.Render2DEvent;
import me.travis.wurstplusthree.util.Globals;

public class HudElement
implements Globals {
    private final String name = this.getElement().name();
    private int x = this.getElement().posX();
    private int y = this.getElement().posY();
    private float rotation = this.getElement().rotation();
    private float scale = this.getElement().scale();
    private boolean enabled = this.getElement().enabled();

    public Element getElement() {
        return this.getClass().getAnnotation(Element.class);
    }

    public void onRender2D(Render2DEvent event) {
    }

    public int getWidth() {
        return 30;
    }

    public int getHeight() {
        return WurstplusThree.GUI_FONT_MANAGER.getTextHeight();
    }

    public String getName() {
        return this.name;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface Element {
        public String name();

        public int posX() default 0;

        public int posY() default 0;

        public float rotation() default 0.0f;

        public float scale() default 0.0f;

        public boolean enabled() default false;
    }
}

