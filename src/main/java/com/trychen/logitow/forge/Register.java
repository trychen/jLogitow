package com.trychen.logitow.forge;
import com.trychen.logitow.forge.build.BlockLogitowCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Register {
    public static final Block logitowCore = new BlockLogitowCore();
    public static final Item itemLogitowCore = new ItemBlock(logitowCore);

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event){
        event.getRegistry().register(logitowCore);
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event){
        event.getRegistry().register(itemLogitowCore.setRegistryName(logitowCore.getRegistryName()));
        ModelLoader.registerItemVariants(itemLogitowCore, new ModelResourceLocation("logitow:" + "core_block", "inventory"));
    }
}