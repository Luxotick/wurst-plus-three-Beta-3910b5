/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientConnectedToServerEvent
 */
package me.travis.wurstplusthree.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.command.Commands;
import me.travis.wurstplusthree.gui.hud.element.HudElement;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.hack.hacks.client.HudEditor;
import me.travis.wurstplusthree.hack.hacks.combat.Burrow;
import me.travis.wurstplusthree.setting.Setting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.KeySetting;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.WhitelistUtil;
import me.travis.wurstplusthree.util.elements.WurstplusPlayer;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ConfigManager
implements Globals {
    private final String mainFolder = "Wurstplus3/";
    private final String configsFolder = "Wurstplus3/configs/";
    private String activeConfigFolder = "Wurstplus3/configs/default/";
    public final String pluginFolder = "Wurstplus3/plugins/";
    public String configName = "default";
    private final String drawnFile = "drawn.txt";
    private final String enemiesFile = "enemies.json";
    private final String friendsFile = "friends.json";
    private final String fontFile = "font.txt";
    private final String burrowFile = "burrowBlocks.txt";
    private final String IRCtoken = "IRCtoken.dat";
    private final String hudFile = "hud.txt";
    private final String coordsFile = "playersCoords.txt";
    private final String drawnDir = "Wurstplus3/drawn.txt";
    private final String fontDir = "Wurstplus3/font.txt";
    private final String burrowDir = "Wurstplus3/burrowBlocks.txt";
    private final String IRCdir = "Wurstplus3/IRCtoken.dat";
    private final String enemiesDir = "Wurstplus3/enemies.json";
    private final String friendsDir = "Wurstplus3/friends.json";
    private final String hudDir = "Wurstplus3/hud.txt";
    private final String coordsDir = "Wurstplus3/playersCoords.txt";
    private final Path mainFolderPath = Paths.get("Wurstplus3/", new String[0]);
    private final Path configsFolderPath = Paths.get("Wurstplus3/configs/", new String[0]);
    private Path activeConfigFolderPath = Paths.get(this.activeConfigFolder, new String[0]);
    private final Path drawnPath = Paths.get("Wurstplus3/drawn.txt", new String[0]);
    private final Path fontPath = Paths.get("Wurstplus3/font.txt", new String[0]);
    private final Path burrowPath = Paths.get("Wurstplus3/burrowBlocks.txt", new String[0]);
    private final Path IRCpath = Paths.get("Wurstplus3/IRCtoken.dat", new String[0]);
    private final Path hudPath = Paths.get("Wurstplus3/hud.txt", new String[0]);
    private final Path coordsPath = Paths.get("Wurstplus3/playersCoords.txt", new String[0]);

    public void loadConfig() {
        try {
            this.loadEnemies();
            this.loadFriends();
            this.loadSettings();
            this.loadBinds();
            this.loadDrawn();
            this.loadFont();
            this.loadBurrowBlock();
            this.loadHud();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            this.loadSettings();
            this.loadBinds();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        String serverIP = "@" + ConfigManager.mc.getCurrentServerData().serverIP;
        Path path = Paths.get("Wurstplus3/configs/" + serverIP + "/", new String[0]);
        if (Files.exists(path, new LinkOption[0])) {
            this.setActiveConfigFolder(serverIP + "/");
        }
    }

    public void saveConfig() {
        try {
            this.verifyDir(this.mainFolderPath);
            this.verifyDir(this.configsFolderPath);
            this.verifyDir(this.activeConfigFolderPath);
            this.saveHud();
            this.saveEnemies();
            this.saveFriends();
            this.saveSettings();
            this.saveBinds();
            this.saveDrawn();
            this.saveFont();
            this.saveBurrowBlock();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getActiveConfigFolder() {
        return this.activeConfigFolder;
    }

    public boolean setActiveConfigFolder(String folder) {
        if (folder.equals(this.activeConfigFolder)) {
            return false;
        }
        this.saveConfig();
        this.configName = folder.replace("/", "");
        this.activeConfigFolder = "Wurstplus3/configs/" + folder;
        this.activeConfigFolderPath = Paths.get(this.activeConfigFolder, new String[0]);
        String currentConfigDir = "Wurstplus3/Wurstplus3/configs/" + this.activeConfigFolder;
        Paths.get(currentConfigDir, new String[0]);
        String bindsFile = "binds.txt";
        String bindsDir = currentConfigDir + bindsFile;
        Paths.get(bindsDir, new String[0]);
        if (!Files.exists(Paths.get("Wurstplus3/configs/" + folder, new String[0]), new LinkOption[0])) {
            this.clearSettings();
        }
        this.reloadConfig();
        return true;
    }

    private void saveFriends() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(WurstplusThree.FRIEND_MANAGER.getFriends());
        OutputStreamWriter file = new OutputStreamWriter((OutputStream)new FileOutputStream("Wurstplus3/friends.json"), StandardCharsets.UTF_8);
        file.write(json);
        file.close();
    }

    private void loadFriends() throws IOException {
        Gson gson = new Gson();
        BufferedReader reader = Files.newBufferedReader(Paths.get("Wurstplus3/friends.json", new String[0]));
        WurstplusThree.FRIEND_MANAGER.setFriends((List)gson.fromJson((Reader)reader, new TypeToken<ArrayList<WurstplusPlayer>>(){}.getType()));
        ((Reader)reader).close();
    }

    private void saveEnemies() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(WurstplusThree.ENEMY_MANAGER.getEnemies());
        OutputStreamWriter file = new OutputStreamWriter((OutputStream)new FileOutputStream("Wurstplus3/enemies.json"), StandardCharsets.UTF_8);
        file.write(json);
        file.close();
    }

    private void loadEnemies() throws IOException {
        Gson gson = new Gson();
        BufferedReader reader = Files.newBufferedReader(Paths.get("Wurstplus3/enemies.json", new String[0]));
        WurstplusThree.ENEMY_MANAGER.setEnemies((List)gson.fromJson((Reader)reader, new TypeToken<ArrayList<WurstplusPlayer>>(){}.getType()));
        ((Reader)reader).close();
    }

    private void saveSettings() throws IOException {
        for (Hack hack : WurstplusThree.HACKS.getHacks()) {
            String fileName = this.activeConfigFolder + hack.getName() + ".txt";
            Path filePath = Paths.get(fileName, new String[0]);
            this.deleteFile(fileName);
            this.verifyFile(filePath);
            File file = new File(fileName);
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            for (Setting setting : hack.getSettings()) {
                if (setting instanceof ColourSetting) {
                    ColourSetting color = (ColourSetting)setting;
                    br.write(setting.getName() + ":" + color.getValue().getRed() + ":" + color.getValue().getGreen() + ":" + color.getValue().getBlue() + ":" + color.getValue().getAlpha() + ":" + color.getRainbow() + "\r\n");
                    continue;
                }
                if (setting instanceof KeySetting) {
                    KeySetting key = (KeySetting)setting;
                    br.write(setting.getName() + ":" + key.getKey() + "\r\n");
                    continue;
                }
                br.write(setting.getName() + ":" + setting.getValue() + "\r\n");
            }
            br.close();
        }
    }

    private void loadSettings() throws IOException {
        for (Hack hack : WurstplusThree.HACKS.getHacks()) {
            String line;
            String file_name = this.activeConfigFolder + hack.getName() + ".txt";
            File file = new File(file_name);
            if (!file.exists()) continue;
            FileInputStream fi_stream = new FileInputStream(file.getAbsolutePath());
            DataInputStream di_stream = new DataInputStream(fi_stream);
            BufferedReader br = new BufferedReader(new InputStreamReader(di_stream));
            while ((line = br.readLine()) != null) {
                String colune = line.trim();
                String name = colune.split(":")[0];
                String value = colune.split(":")[1];
                Setting setting = hack.getSettingByName(name);
                if (setting == null) continue;
                switch (setting.getType()) {
                    case "boolean": {
                        setting.setValue(Boolean.parseBoolean(value));
                        break;
                    }
                    case "colour": {
                        try {
                            ColourSetting colourSetting = (ColourSetting)setting;
                            int red = Integer.parseInt(value);
                            int green = Integer.parseInt(colune.split(":")[2]);
                            int blue = Integer.parseInt(colune.split(":")[3]);
                            int alpha = Integer.parseInt(colune.split(":")[4]);
                            boolean rainbow = Boolean.parseBoolean(colune.split(":")[5]);
                            colourSetting.setRainbow(rainbow);
                            colourSetting.setValue(red, green, blue, alpha);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "double": {
                        setting.setValue(Double.parseDouble(value));
                        break;
                    }
                    case "enum": {
                        setting.setValue(value);
                        break;
                    }
                    case "int": {
                        setting.setValue(Integer.parseInt(value));
                        break;
                    }
                    case "key": {
                        KeySetting key = (KeySetting)setting;
                        key.setKey(Integer.parseInt(value));
                    }
                }
            }
            br.close();
        }
    }

    private void clearSettings() {
        for (Hack hack : WurstplusThree.HACKS.getHacks()) {
            if (hack instanceof Gui || hack instanceof HudEditor) continue;
            hack.setHold(false);
            hack.setEnabled(false);
            hack.setBind(0);
            for (Setting setting : hack.getSettings()) {
                setting.setValue(setting.defaultValue);
            }
        }
    }

    private void saveBinds() throws IOException {
        String file_name = this.activeConfigFolder + "BINDS.txt";
        Path file_path = Paths.get(file_name, new String[0]);
        this.deleteFile(file_name);
        this.verifyFile(file_path);
        File file = new File(file_name);
        BufferedWriter br = new BufferedWriter(new FileWriter(file));
        br.write(Commands.prefix + "\r\n");
        for (Hack module : WurstplusThree.HACKS.getHacks()) {
            br.write(module.getName() + ":" + module.getBind() + ":" + module.isEnabled() + ":" + module.isHold() + "\r\n");
        }
        br.close();
    }

    private void loadBinds() throws IOException {
        String line;
        String file_name = this.activeConfigFolder + "BINDS.txt";
        File file = new File(file_name);
        FileInputStream fi_stream = new FileInputStream(file.getAbsolutePath());
        DataInputStream di_stream = new DataInputStream(fi_stream);
        BufferedReader br = new BufferedReader(new InputStreamReader(di_stream));
        boolean flag = true;
        while ((line = br.readLine()) != null) {
            try {
                if (flag) {
                    Commands.prefix = line;
                    flag = false;
                    continue;
                }
                String colune = line.trim();
                String tag = colune.split(":")[0];
                String bind = colune.split(":")[1];
                String active = colune.split(":")[2];
                String hold = colune.split(":")[3];
                Hack hack = WurstplusThree.HACKS.getHackByName(tag);
                hack.setBind(Integer.parseInt(bind));
                hack.setHold(Boolean.parseBoolean(hold));
                hack.setEnabled(Boolean.parseBoolean(active));
            }
            catch (Exception exception) {}
        }
        br.close();
    }

    private void saveHud() throws IOException {
        FileWriter writer = new FileWriter("Wurstplus3/hud.txt");
        for (HudElement hudElement : WurstplusThree.HUD_MANAGER.getHudElements()) {
            writer.write(hudElement.getName() + ":" + hudElement.getX() + ":" + hudElement.getY() + ":" + hudElement.isEnabled() + System.lineSeparator());
        }
        writer.close();
    }

    private void loadHud() throws IOException {
        for (String hudElement : Files.readAllLines(this.hudPath).stream().distinct().collect(Collectors.toList())) {
            try {
                String trim = hudElement.trim();
                String name = trim.split(":")[0];
                int x = Integer.parseInt(trim.split(":")[1]);
                int y = Integer.parseInt(trim.split(":")[2]);
                boolean enabled = Boolean.parseBoolean(trim.split(":")[3]);
                HudElement element = WurstplusThree.HUD_MANAGER.getElementByName(name);
                if (element == null) continue;
                element.setX(x);
                element.setY(y);
                element.setEnabled(enabled);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDrawn() throws IOException {
        FileWriter writer = new FileWriter("Wurstplus3/drawn.txt");
        for (Hack hack : WurstplusThree.HACKS.getDrawnHacks()) {
            writer.write(hack.getName() + System.lineSeparator());
        }
        writer.close();
    }

    private void loadDrawn() throws IOException {
        for (String hackName : Files.readAllLines(this.drawnPath).stream().distinct().collect(Collectors.toList())) {
            Hack hack = WurstplusThree.HACKS.getHackByName(hackName);
            if (hack == null) continue;
            WurstplusThree.HACKS.addDrawHack(hack);
        }
    }

    private void saveFont() throws IOException {
        FileWriter writer = new FileWriter("Wurstplus3/font.txt");
        writer.write(WurstplusThree.GUI_FONT_MANAGER.fontName + System.lineSeparator());
        writer.write(WurstplusThree.GUI_FONT_MANAGER.fontSize + System.lineSeparator());
        writer.close();
    }

    private void loadFont() throws IOException {
        boolean flag = true;
        for (String line : Files.readAllLines(this.fontPath)) {
            if (flag) {
                WurstplusThree.GUI_FONT_MANAGER.setFont(line);
                flag = false;
                continue;
            }
            WurstplusThree.GUI_FONT_MANAGER.setFontSize(Integer.parseInt(line));
            return;
        }
        WurstplusThree.GUI_FONT_MANAGER.setFont();
    }

    public void saveCoords(String coords) throws IOException {
        FileWriter fw = new FileWriter("Wurstplus3/playersCoords.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy -- HH:mm:ss");
        Date date = new Date();
        bw.write("[" + formatter.format(date) + "] " + coords);
        bw.newLine();
        bw.close();
    }

    public void saveBurrowBlock() throws IOException {
        FileWriter writer = new FileWriter("Wurstplus3/burrowBlocks.txt");
        String s = WurstplusThree.COMMANDS.getBurrowCommand().getBBlock();
        writer.write(s);
        writer.close();
    }

    private void loadBurrowBlock() throws IOException {
        for (String l : Files.readAllLines(this.burrowPath)) {
            Burrow a = (Burrow)WurstplusThree.HACKS.getHackByName("Burrow");
            new WhitelistUtil();
            a.setBlock(WhitelistUtil.findBlock(l));
            WurstplusThree.COMMANDS.getBurrowCommand().setBBlock(l);
        }
    }

    public void saveIRCtoken(String token) throws IOException {
        FileWriter writer = new FileWriter("Wurstplus3/IRCtoken.dat");
        writer.append(ConfigManager.mc.player.getName() + ":" + ConfigManager.mc.player.getUniqueID() + ":" + token);
        writer.close();
    }

    public String loadIRCtoken() throws IOException {
        String line = "";
        Iterator<String> iterator = Files.readAllLines(this.IRCpath).iterator();
        if (iterator.hasNext()) {
            String l;
            line = l = iterator.next();
        }
        String[] split = line.split(":");
        return split[2];
    }

    public boolean deleteFile(String path) throws IOException {
        File f = new File(path);
        return f.delete();
    }

    public void verifyFile(Path path) throws IOException {
        if (!Files.exists(path, new LinkOption[0])) {
            Files.createFile(path, new FileAttribute[0]);
        }
    }

    public void verifyDir(Path path) throws IOException {
        if (!Files.exists(path, new LinkOption[0])) {
            Files.createDirectory(path, new FileAttribute[0]);
        }
    }
}

