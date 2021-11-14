/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.command.commands;

import java.util.ArrayList;
import java.util.List;
import me.travis.wurstplusthree.command.Command;
import me.travis.wurstplusthree.util.ClientMessage;

public class ClipBindCommand
extends Command {
    static List<Integer> keys = new ArrayList<Integer>();

    public ClipBindCommand() {
        super("clipbind", "bindclip");
    }

    @Override
    public void execute(String[] message) {
        keys.clear();
        int key = 0;
        String keylist = "";
        for (String s : message) {
            if (s.toLowerCase().equals("shift")) {
                key = 16;
            } else if (s.toLowerCase().equals("alt")) {
                key = 18;
            } else if (s.toLowerCase().equals("ctrl")) {
                key = 17;
            } else if (s.toLowerCase().equals("f1")) {
                key = 112;
            } else if (s.toLowerCase().equals("f2")) {
                key = 113;
            } else if (s.toLowerCase().equals("f3")) {
                key = 114;
            } else if (s.toLowerCase().equals("f4")) {
                key = 115;
            } else if (s.toLowerCase().equals("f5")) {
                key = 116;
            } else if (s.toLowerCase().equals("f6")) {
                key = 117;
            } else if (s.toLowerCase().equals("f7")) {
                key = 118;
            } else if (s.toLowerCase().equals("f8")) {
                key = 119;
            } else if (s.toLowerCase().equals("f9")) {
                key = 120;
            } else if (s.toLowerCase().equals("f10")) {
                key = 121;
            } else if (s.toLowerCase().equals("f11")) {
                key = 122;
            } else if (s.toLowerCase().equals("f12")) {
                key = 123;
            } else if (s.toLowerCase().equals("q")) {
                key = 81;
            } else if (s.toLowerCase().equals("w")) {
                key = 87;
            } else if (s.toLowerCase().equals("e")) {
                key = 69;
            } else if (s.toLowerCase().equals("r")) {
                key = 82;
            } else if (s.toLowerCase().equals("t")) {
                key = 84;
            } else if (s.toLowerCase().equals("y")) {
                key = 89;
            } else if (s.toLowerCase().equals("u")) {
                key = 85;
            } else if (s.toLowerCase().equals("i")) {
                key = 73;
            } else if (s.toLowerCase().equals("o")) {
                key = 79;
            } else if (s.toLowerCase().equals("p")) {
                key = 80;
            } else if (s.toLowerCase().equals("a")) {
                key = 65;
            } else if (s.toLowerCase().equals("s")) {
                key = 83;
            } else if (s.toLowerCase().equals("d")) {
                key = 68;
            } else if (s.toLowerCase().equals("f")) {
                key = 70;
            } else if (s.toLowerCase().equals("g")) {
                key = 71;
            } else if (s.toLowerCase().equals("h")) {
                key = 72;
            } else if (s.toLowerCase().equals("j")) {
                key = 74;
            } else if (s.toLowerCase().equals("k")) {
                key = 75;
            } else if (s.toLowerCase().equals("l")) {
                key = 76;
            } else if (s.toLowerCase().equals("z")) {
                key = 90;
            } else if (s.toLowerCase().equals("x")) {
                key = 88;
            } else if (s.toLowerCase().equals("c")) {
                key = 67;
            } else if (s.toLowerCase().equals("v")) {
                key = 86;
            } else if (s.toLowerCase().equals("b")) {
                key = 66;
            } else if (s.toLowerCase().equals("n")) {
                key = 78;
            } else if (s.toLowerCase().equals("m")) {
                key = 77;
            } else if (s.toLowerCase().equals(",")) {
                key = 44;
            } else if (s.toLowerCase().equals(".")) {
                key = 46;
            } else if (s.toLowerCase().equals(";")) {
                key = 59;
            } else if (s.toLowerCase().equals("1")) {
                key = 49;
            } else if (s.toLowerCase().equals("2")) {
                key = 50;
            } else if (s.toLowerCase().equals("3")) {
                key = 51;
            } else if (s.toLowerCase().equals("4")) {
                key = 52;
            } else if (s.toLowerCase().equals("5")) {
                key = 53;
            } else if (s.toLowerCase().equals("6")) {
                key = 54;
            } else if (s.toLowerCase().equals("7")) {
                key = 55;
            } else if (s.toLowerCase().equals("8")) {
                key = 56;
            } else if (s.toLowerCase().equals("9")) {
                key = 57;
            } else if (s.toLowerCase().equals("0")) {
                key = 48;
            } else if (s.toLowerCase().equals("=")) {
                key = 61;
            } else if (s.toLowerCase().equals("-")) {
                key = 45;
            } else {
                ClientMessage.sendErrorMessage("Error key not supported");
                return;
            }
            keys.add(key);
            keylist = keylist + s + " ";
        }
        ClientMessage.sendMessage("Bind set to " + keylist);
    }

    public static List<Integer> getKeys() {
        return keys;
    }

    public static void setKeys(List<Integer> keyArray) {
        keys.clear();
        keys = keyArray;
    }
}

