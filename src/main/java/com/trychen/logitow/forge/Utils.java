package com.trychen.logitow.forge;

import com.trychen.logitow.stack.Color;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.EnumDyeColor;

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

    static EnumDyeColor transformToEnumDyeColor(Color color) {
        switch (color) {
            case RED:
                return EnumDyeColor.RED;
            case BLUE:
                return EnumDyeColor.BLUE;
            case YELLOW:
                return EnumDyeColor.YELLOW;
            case CYAN:
                return EnumDyeColor.CYAN;
            case WHITE:
                return EnumDyeColor.WHITE;
            case ORANGE:
                return EnumDyeColor.ORANGE;
            case PINK:
                return EnumDyeColor.PINK;
            case PURPLE:
                return EnumDyeColor.PURPLE;
            case BLACK:
                return EnumDyeColor.BLACK;
            case GREEN:
                return EnumDyeColor.GREEN;
            default:
                return EnumDyeColor.WHITE;
        }
    }

    static Color transformToColor(EnumDyeColor color) {
        switch (color) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
            case CYAN:
                return Color.CYAN;
            case WHITE:
                return Color.WHITE;
            case ORANGE:
                return Color.ORANGE;
            case PINK:
                return Color.PINK;
            case PURPLE:
                return Color.PURPLE;
            case BLACK:
                return Color.BLACK;
            case GREEN:
                return Color.GREEN;
            default:
                return Color.WHITE;
        }
    }

}
