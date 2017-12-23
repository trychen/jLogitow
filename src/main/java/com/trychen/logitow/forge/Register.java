package com.trychen.logitow.forge;

import com.trychen.logitow.forge.block.LogitowBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Register {
    public static final Block logitowCore = new LogitowBlock();

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event){
        event.getRegistry().register(logitowCore);
    }
    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event){
        event.getRegistry().register(new ItemBlock(logitowCore).setRegistryName(logitowCore.getRegistryName()));
    }
}
