/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.jetbrains.annotations.NotNull;
import sun.net.www.protocol.file.FileURLConnection;

public final class ReflectionUtil {
    private static void checkDirectory(@NotNull File directory, @NotNull String pckgname, @NotNull ArrayList<Class<?>> classes) throws ClassNotFoundException {
        if (directory.exists() && directory.isDirectory()) {
            String[] files;
            for (String file : files = directory.list()) {
                if (file.endsWith(".class")) {
                    try {
                        classes.add(Class.forName(pckgname + '.' + file.substring(0, file.length() - 6)));
                    }
                    catch (NoClassDefFoundError noClassDefFoundError) {}
                    continue;
                }
                File tmpDirectory = new File(directory, file);
                if (!tmpDirectory.isDirectory()) continue;
                ReflectionUtil.checkDirectory(tmpDirectory, pckgname + "." + file, classes);
            }
        }
    }

    private static void checkJarFile(@NotNull JarURLConnection connection, @NotNull String pckgname, @NotNull ArrayList<Class<?>> classes) throws ClassNotFoundException, IOException {
        JarFile jarFile = connection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        JarEntry jarEntry = null;
        while (entries.hasMoreElements() && (jarEntry = entries.nextElement()) != null) {
            String name = jarEntry.getName();
            if (!name.contains(".class") || !(name = name.substring(0, name.length() - 6).replace('/', '.')).contains(pckgname)) continue;
            try {
                classes.add(Class.forName(name));
            }
            catch (NoClassDefFoundError noClassDefFoundError) {}
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ArrayList<Class<?>> getClassesForPackage(@NotNull String pckgname) throws ClassNotFoundException {
        ArrayList classes = new ArrayList();
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            Enumeration<URL> resources = cld.getResources(pckgname.replace('.', '/'));
            URL url = null;
            while (resources.hasMoreElements() && (url = resources.nextElement()) != null) {
                try {
                    URLConnection connection = url.openConnection();
                    if (connection instanceof JarURLConnection) {
                        ReflectionUtil.checkJarFile((JarURLConnection)connection, pckgname, classes);
                        continue;
                    }
                    if (!(connection instanceof FileURLConnection)) throw new ClassNotFoundException(pckgname + " (" + url.getPath() + ") does not appear to be a valid package");
                    try {
                        ReflectionUtil.checkDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), pckgname, classes);
                    }
                    catch (UnsupportedEncodingException ex) {
                        throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Unsupported encoding)", ex);
                    }
                }
                catch (IOException ioex) {
                    throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname, ioex);
                    return classes;
                }
            }
        }
        catch (NullPointerException ex) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)", ex);
        }
        catch (IOException ioex) {
            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname, ioex);
        }
    }
}

