/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.util.logview;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.util.logview.RefreshLog;

public class Gui
extends JFrame {
    private static final Path logPath = Paths.get("logs/latest.log", new String[0]);
    public JTextArea display;
    public ArrayList<String> log;
    public RefreshLog thread;

    public Gui() {
        this.draw();
    }

    public void draw() {
        JFrame frame = new JFrame("Wurst + 3 Log viewer");
        frame.setDefaultCloseOperation(2);
        this.log = this.getLog();
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Log"));
        this.display = new JTextArea(45, 65);
        this.display.setEditable(false);
        for (String l : this.log) {
            this.display.append(l + "\n");
        }
        JScrollPane scroll = new JScrollPane(this.display);
        scroll.setVerticalScrollBarPolicy(22);
        panel.add(scroll);
        frame.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                Gui.this.thread.stop(false);
            }
        });
        frame.add(panel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setThead(RefreshLog thread) {
        this.thread = thread;
    }

    public ArrayList<String> getLog() {
        ArrayList<String> log = new ArrayList<String>();
        try {
            log.addAll(Files.readAllLines(logPath));
        }
        catch (IOException e) {
            WurstplusThree.LOGGER.error((Object)e);
        }
        return log;
    }
}

