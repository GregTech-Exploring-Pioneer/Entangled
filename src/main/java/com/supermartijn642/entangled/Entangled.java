package com.supermartijn642.entangled;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.block.BaseBlockEntityType;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import com.supermartijn642.entangled.generators.*;
import com.supermartijn642.entangled.integration.TheOneProbePlugin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;

@Mod("entangled")
public class Entangled {

    public static final Logger LOGGER = CommonUtils.getLogger("entangled");

    @RegistryEntryAcceptor(namespace = "entangled", identifier = "block", registry = RegistryEntryAcceptor.Registry.BLOCKS)
    public static EntangledBlock block;
    @RegistryEntryAcceptor(namespace = "entangled", identifier = "tile", registry = RegistryEntryAcceptor.Registry.BLOCK_ENTITY_TYPES)
    public static BaseBlockEntityType<EntangledBlockEntity> tile;
    @RegistryEntryAcceptor(namespace = "entangled", identifier = "item", registry = RegistryEntryAcceptor.Registry.ITEMS)
    public static EntangledBinderItem item;

    public Entangled(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TheOneProbePlugin::interModEnqueue);

        register();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EntangledClient::register);
        registerGenerators();
    }

    private static void register(){
        RegistrationHandler handler = RegistrationHandler.get("entangled");

        // Entangled block
        handler.registerBlock("block", EntangledBlock::new);
        handler.registerItem("block", () -> new BaseBlockItem(block, ItemProperties.create().group(CreativeItemGroup.getDecoration())));
        // Entangled block entity type
        handler.registerBlockEntityType("tile", () -> BaseBlockEntityType.create(EntangledBlockEntity::new, block));
        // Entangled binder
        handler.registerItem("item", EntangledBinderItem::new);
    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get("entangled");

        // Add all the generators
        handler.addGenerator(EntangledBlockStateGenerator::new);
        handler.addGenerator(EntangledModelGenerator::new);
        handler.addGenerator(EntangledLanguageGenerator::new);
        handler.addGenerator(EntangledLootTableGenerator::new);
        handler.addGenerator(EntangledRecipeGenerator::new);
        handler.addGenerator(EntangledTagGenerator::new);
    }
}
