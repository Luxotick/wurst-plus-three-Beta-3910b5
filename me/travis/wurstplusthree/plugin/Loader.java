/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.plugin.Plugin;

public class Loader {
    File pluginDir;
    File[] pluginsFiles;
    ArrayList<String> classes;
    ArrayList<URL> urls;
    ArrayList<String> names;
    ArrayList<Plugin> plugins;

    public Loader() {
        WurstplusThree.LOGGER.info("Loading Plugins");
        WurstplusThree.CONFIG_MANAGER.getClass();
        this.pluginDir = new File("Wurstplus3/plugins/");
        if (!this.pluginDir.exists()) {
            this.pluginDir.mkdir();
        }
        this.pluginsFiles = this.pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
        this.plugins = new ArrayList();
        this.loadClasses();
    }

    public void loadClasses() {
        if (this.pluginsFiles != null && this.pluginsFiles.length > 0) {
            this.classes = new ArrayList();
            this.urls = new ArrayList(this.pluginsFiles.length);
            this.names = new ArrayList();
            for (File file : this.pluginsFiles) {
                try {
                    JarFile pluginJar = new JarFile(file);
                    pluginJar.stream().forEach(jarEntry -> {
                        if (jarEntry.getName().endsWith(".class")) {
                            this.classes.add(jarEntry.getName());
                        }
                        if (jarEntry.getName().equals("config.json")) {
                            try {
                                String line;
                                URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
                                InputStream s = classLoader.getResourceAsStream("config.json");
                                InputStreamReader streamReader = new InputStreamReader(s);
                                BufferedReader stream = new BufferedReader(streamReader);
                                while ((line = stream.readLine()) != null) {
                                    String[] split = line.split(":");
                                    if (!split[0].equals("name")) continue;
                                    this.names.add(split[1]);
                                }
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    URL url = file.toURI().toURL();
                    this.urls.add(url);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            URLClassLoader classLoader = new URLClassLoader(this.urls.toArray(new URL[this.urls.size()]));
            this.classes.spliterator().forEachRemaining(name -> {
                try {
                    Class<?> cls = classLoader.loadClass(name.replaceAll("/", ".").replace(".class", ""));
                    Class<?>[] ife = cls.getInterfaces();
                    WurstplusThree.LOGGER.info(ife[0].getName());
                    if (ife[0].equals(Plugin.class)) {
                        WurstplusThree.LOGGER.info("asd");
                        Plugin plugin = (Plugin)cls.newInstance();
                        this.plugins.add(plugin);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        if (!this.plugins.isEmpty()) {
            WurstplusThree.LOGGER.info(this.plugins.get(0).name());
            this.plugins.spliterator().forEachRemaining(Plugin::init);
        }
        if (!this.names.isEmpty()) {
            for (String name2 : this.names) {
                WurstplusThree.LOGGER.info("Loaded -> " + name2);
            }
        }
        WurstplusThree.LOGGER.info("Loaded Plugins");
    }
}

