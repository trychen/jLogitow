package com.trychen.logitow.forge.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class LogitowToast implements IToast {
    public static final ResourceLocation TEXTURE_TOASTS = new ResourceLocation("logitow:textures/gui/toasts.png");

    private final LogitowToast.Type type;
    private String title;
    private String subtitle;
    private long firstDrawTime;
    private boolean newDisplay;

    public static final ITextComponent TEXT_CONNECTED_TITLE = new TextComponentTranslation("logitow.toast.connected.title");
    public static final String TEXT_CONNECTED_SUBTITLE_KEY = "logitow.toast.connected.subtitle";

    public static final ITextComponent TEXT_DISCONNECTED_TITLE = new TextComponentTranslation("logitow.toast.disconnected.title");
    public static final String TEXT_DISCONNECTED_SUBTITLE_KEY = "logitow.toast.disconnected.subtitle";

    public LogitowToast(LogitowToast.Type typeIn, ITextComponent titleComponent, @Nullable ITextComponent subtitleComponent) {
        this.type = typeIn;
        this.title = titleComponent.getUnformattedText();
        this.subtitle = subtitleComponent == null ? null : subtitleComponent.getUnformattedText();
    }

    public IToast.Visibility draw(GuiToast toastGui, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }

        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        toastGui.drawTexturedModalRect(0, 0, 0, type.textureOffset, 160, 32);

        if (this.subtitle == null) {
            toastGui.getMinecraft().fontRenderer.drawStringWithShadow(this.title, 28, 12, type.titleColor);
        } else {
            toastGui.getMinecraft().fontRenderer.drawStringWithShadow(this.title, type.textXOffset, 7, type.titleColor);
            toastGui.getMinecraft().fontRenderer.drawStringWithShadow(this.subtitle, type.textXOffset + 1, 18, type.subtitleColor);
        }

        return delta - this.firstDrawTime < 3000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
    }

    public void setDisplayedText(ITextComponent titleComponent, @Nullable ITextComponent subtitleComponent) {
        this.title = titleComponent.getUnformattedText();
        this.subtitle = subtitleComponent == null ? null : subtitleComponent.getUnformattedText();
        this.newDisplay = true;
    }

    public LogitowToast.Type getType() {
        return this.type;
    }

    public static LogitowToast showConnect(UUID deviceUUID) {
        LogitowToast toast = new LogitowToast(Type.DEVICE_CONNECTED, TEXT_CONNECTED_TITLE, new TextComponentTranslation(TEXT_CONNECTED_SUBTITLE_KEY, deviceUUID.toString().substring(0, 8)));
        Minecraft.getMinecraft().getToastGui().add(toast);
        return toast;
    }

    public static LogitowToast showDisconnect(UUID deviceUUID) {
        LogitowToast toast = new LogitowToast(Type.DEVICE_DISCONNECTED, TEXT_DISCONNECTED_TITLE, new TextComponentTranslation(TEXT_DISCONNECTED_SUBTITLE_KEY, deviceUUID.toString().substring(0, 8)));
        Minecraft.getMinecraft().getToastGui().add(toast);
        return toast;
    }

    @SideOnly(Side.CLIENT)
    public static enum Type {
        DEVICE_CONNECTED(32, 0x00d965, 0x00873C, 28),
        DEVICE_DISCONNECTED(0, -256, -1, 28);
        int textureOffset, titleColor, subtitleColor, textXOffset;

        Type(int textureOffset, int titleColor, int subtitleColor, int textXOffset) {
            this.textureOffset = textureOffset;
            this.titleColor = titleColor;
            this.subtitleColor = subtitleColor;
            this.textXOffset = textXOffset;
        }
    }
}