/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParser
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.util.Session
 *  org.apache.commons.io.IOUtils
 */
package me.travis.wurstplusthree.gui.alt.defult;

import com.google.gson.JsonParser;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.apache.commons.io.IOUtils;

public class GuiAccountSelector
extends GuiScreen {
    private GuiTextField username;
    private GuiTextField token;

    public void initGui() {
        this.username = new GuiTextField(0, this.mc.fontRenderer, this.width / 2 - 100, this.height / 2 - 50, 200, 20);
        this.token = new GuiTextField(1, this.mc.fontRenderer, this.width / 2 - 100, this.height / 2 - 25, 200, 20);
        this.addButton(new GuiButton(2, this.width / 2 - 102, this.height / 2, 204, 20, "Login"));
        this.addButton(new GuiButton(3, this.width / 2 - 102, this.height / 2 + 23, 204, 20, "Exit"));
        this.username.setFocused(true);
        this.username.setText("Username");
        this.token.setText("Token");
        this.token.setMaxStringLength(1000);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.token.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 2) {
            this.login(this.username.getText(), this.token.getText());
        }
        if (button.id == 3) {
            this.mc.displayGuiScreen(null);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.username.mouseClicked(mouseX, mouseY, mouseButton);
        this.token.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void keyTyped(char typedChar, int keyCode) {
        this.username.textboxKeyTyped(typedChar, keyCode);
        this.token.textboxKeyTyped(typedChar, keyCode);
    }

    private void login(String username, String token) {
        try {
            String content = IOUtils.toString((URL)new URL("https://api.mojang.com/users/profiles/minecraft/" + username), (Charset)StandardCharsets.UTF_8);
            String uuid = new JsonParser().parse(content).getAsJsonObject().get("id").getAsString();
            Session session = new Session(username, UUID.fromString(uuid.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5")).toString(), token, "mojang");
            Field field = Minecraft.class.getDeclaredField("field_71449_j");
            field.setAccessible(true);
            field.set((Object)Minecraft.getMinecraft(), (Object)session);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

