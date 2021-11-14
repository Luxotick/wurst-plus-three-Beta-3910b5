/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Session
 */
package me.travis.wurstplusthree.manager;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.util.elements.Alt;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltManager {
    ArrayList<Alt> alts;
    String altApi = "https://auth.mcleaks.net/v1/";
    Minecraft mc = Minecraft.getMinecraft();

    public AltManager() {
        this.alts = new ArrayList();
    }

    public void addAlt(String u, String p) {
        this.alts.add(new Alt(u, p));
    }

    public void removeAlt(Alt alt) {
        this.alts.remove(alt);
    }

    public boolean login(Alt alt) {
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(alt.getUsername());
        auth.setPassword(alt.getPassword());
        try {
            auth.logIn();
            Session session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
            this.setSession(session);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean loginCracked(String alt) {
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        auth.logOut();
        Session crackedSession = new Session(alt, alt, "0", "legacy");
        try {
            this.setSession(crackedSession);
            WurstplusThree.LOGGER.info("Logged in as " + alt);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean loginMcLeaks(String token) {
        if (token.length() != 16) {
            return false;
        }
        JsonObject requestObject = new JsonObject();
        requestObject.add("token", new GsonBuilder().create().toJsonTree((Object)token));
        try {
            String response = this.postJson(this.altApi + "redeem", requestObject.toString());
            JsonObject responseObject = new JsonParser().parse(response).getAsJsonObject();
            if (!responseObject.get("success").getAsBoolean()) {
                return false;
            }
            JsonObject resultObject = responseObject.get("result").getAsJsonObject();
            Session session = new Session(resultObject.get("mcname").getAsString(), resultObject.get("session").getAsString(), token, "mojang");
            this.setSession(session);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private void setSession(Session newSession) {
        Class<?> mc = Minecraft.getMinecraft().getClass();
        try {
            Field session = null;
            for (Field field : mc.getDeclaredFields()) {
                if (!field.getType().isInstance((Object)newSession)) continue;
                session = field;
                WurstplusThree.LOGGER.info("Attempting Injection into Session.");
            }
            if (session == null) {
                throw new IllegalStateException("No field of type " + Session.class.getCanonicalName() + " declared.");
            }
            session.setAccessible(true);
            session.set((Object)Minecraft.getMinecraft(), (Object)newSession);
            session.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String postJson(String urlString, String content) {
        try {
            String line;
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(content);
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            connection.disconnect();
            return builder.toString();
        }
        catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Alt> getAlts() {
        return this.alts;
    }
}

