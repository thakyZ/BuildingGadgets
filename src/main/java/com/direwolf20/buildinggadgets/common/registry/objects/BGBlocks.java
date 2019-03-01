package com.direwolf20.buildinggadgets.common.registry.objects;

import com.direwolf20.buildinggadgets.common.blocks.ConstructionBlock;
import com.direwolf20.buildinggadgets.common.blocks.ConstructionBlockPowder;
import com.direwolf20.buildinggadgets.common.blocks.EffectBlock;
import com.direwolf20.buildinggadgets.common.blocks.templatemanager.TemplateManager;
import com.direwolf20.buildinggadgets.common.registry.BlockBuilder;
import com.direwolf20.buildinggadgets.common.registry.BlockRegistryContainer;
import com.direwolf20.buildinggadgets.common.utils.ref.Reference;
import com.direwolf20.buildinggadgets.common.utils.ref.Reference.BlockReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

import static com.direwolf20.buildinggadgets.common.registry.objects.BGItems.itemProperties;
import static com.direwolf20.buildinggadgets.common.registry.objects.BGItems.itemPropertiesWithoutGroup;
import static com.direwolf20.buildinggadgets.common.registry.objects.BuildingObjects.EFFECT_BLOCK_MATERIAL;

@ObjectHolder(Reference.MODID)
@EventBusSubscriber(modid = Reference.MODID, bus = Bus.MOD)
public final class BGBlocks {

    private BGBlocks() {}

    private static final BlockRegistryContainer container = new BlockRegistryContainer();
    // Blocks
    @ObjectHolder(BlockReference.EFFECT_BLOCK)
    public static Block effectBlock;
    @ObjectHolder(BlockReference.CONSTRUCTION_BLOCK)
    public static Block constructionBlock;
    @ObjectHolder(BlockReference.CONSTRUCTION_BLOCK_POWDER)
    public static Block constructionBlockPowder;
    @ObjectHolder(BlockReference.TEMPLATE_MANAGER)
    public static Block templateManger;

    static void init() {
        container.add(new BlockBuilder(BlockReference.EFFECT_BLOCK_RL)
                .builder(Block.Properties.create(EFFECT_BLOCK_MATERIAL).hardnessAndResistance(20f))
                .item(itemPropertiesWithoutGroup())
                .factory(EffectBlock::new));
        container.add(new BlockBuilder(BlockReference.CONSTRUCTION_BLOCK_RL)
                .builder(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5f,0f))
                .item(itemPropertiesWithoutGroup())
                .factory(ConstructionBlock::new));
        container.add(new BlockBuilder(BlockReference.CONSTRUCTION_BLOCK_POWDER_RL)
                .builder(Block.Properties.create(Material.SAND).hardnessAndResistance(20f))
                .item(itemProperties())
                .factory(ConstructionBlockPowder::new));
        container.add(new BlockBuilder(BlockReference.TEMPLATE_MANAGER_RL)
                .builder(Block.Properties.create(Material.ROCK).hardnessAndResistance(2f))
                .item(itemProperties())
                .factory(TemplateManager::new));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        container.register(event);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        container.registerItemBlocks(event);
    }

    static void cleanup() {
        container.clear();
    }
}
