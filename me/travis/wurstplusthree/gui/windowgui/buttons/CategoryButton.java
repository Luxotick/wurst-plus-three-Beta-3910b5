/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.windowgui.buttons;

import java.util.ArrayList;
import me.travis.wurstplusthree.gui.component.Component;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.util.Globals;

public class CategoryButton
implements Globals {
    public ArrayList<Component> components;
    public Hack.Category category;

    public CategoryButton(Hack.Category category) {
        this.category = category;
        this.components = new ArrayList();
    }
}

