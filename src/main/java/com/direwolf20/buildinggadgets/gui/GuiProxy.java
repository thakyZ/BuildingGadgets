package com.direwolf20.buildinggadgets.gui;

import com.direwolf20.buildinggadgets.blocks.templatemanager.TemplateManagerContainer;
import com.direwolf20.buildinggadgets.blocks.templatemanager.TemplateManagerGUI;
import com.direwolf20.buildinggadgets.blocks.templatemanager.TemplateManagerTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TemplateManagerTileEntity) {
            return new TemplateManagerContainer(player.inventory, (TemplateManagerTileEntity) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TemplateManagerTileEntity) {
            TemplateManagerTileEntity containerTileEntity = (TemplateManagerTileEntity) te;
            return new TemplateManagerGUI(containerTileEntity, new TemplateManagerContainer(player.inventory, containerTileEntity));
        }
        return null;
    }
}
