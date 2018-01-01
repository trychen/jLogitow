package com.trychen.logitow.forge;

import com.trychen.logitow.forge.ui.GuiLogitow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public enum KeyHelper {
    INSTANCE;
    private KeyBinding keyLogitowDevicesManager = new KeyBinding("key.logitow.manager.desc", Keyboard.KEY_M, "key.logitow.category");

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        ClientRegistry.registerKeyBinding(keyLogitowDevicesManager);
    }

    @SubscribeEvent
    public void handleKey(GuiScreenEvent.KeyboardInputEvent e) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && Keyboard.isKeyDown(keyLogitowDevicesManager.getKeyCode()))  handleManager();
    }

    @SubscribeEvent
    public void handleKey(InputEvent.KeyInputEvent e) {
        if (keyLogitowDevicesManager.isPressed()) handleManager();
    }

    public void handleManager(){
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiLogitow)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiLogitow());
        }
    }
}
