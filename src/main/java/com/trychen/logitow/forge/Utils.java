package com.trychen.logitow.forge;

import com.trychen.logitow.stack.Color;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public interface Utils {
    static String getMinecraftColorCodeFromBlockColor(Color color) {
        switch (color) {
            case RED:
                return "§c";
            case GREEN:
                return "§2";
            case BLUE:
                return "§1";
            case YELLOW:
                return "§e";
            case PINK:
                return "§d";
            case PURPLE:
                return "§5";
            case BLACK:
                return "§0";
            case ORANGE:
                return "§6";
            case CYAN:
                return "§b";
            default:
                return "§f";
        }
    }

    static String getI18NFromBlockColor(Color color) {
        String key = "item.fireworksCharge." + color.name().toLowerCase();
        return I18n.hasKey(key)?I18n.format(key):I18n.format("item.fireworksCharge.white");
    }

    static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }
}
