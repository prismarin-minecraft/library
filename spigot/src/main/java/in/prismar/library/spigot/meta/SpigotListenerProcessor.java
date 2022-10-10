package in.prismar.library.spigot.meta;

import in.prismar.library.meta.MetaEntity;
import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.processor.AbstractMetaProcessor;
import in.prismar.library.meta.processor.MetaProcessorPhase;
import in.prismar.library.spigot.meta.anno.AutoListener;
import in.prismar.library.spigot.setup.SpigotSetup;
import org.bukkit.event.Listener;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SpigotListenerProcessor extends AbstractMetaProcessor {

    private final SpigotSetup setup;

    public SpigotListenerProcessor(SpigotSetup setup, MetaRegistry registry) {
        super(registry, MetaProcessorPhase.DISCOVERY);
        this.setup = setup;
    }

    @Override
    public void process(Class<?> target) throws Exception {
        if(!target.isAnnotationPresent(AutoListener.class)) {
            return;
        }
        MetaEntity entity = getRegistry().registerEntity(target);
        if(entity.getInstance() instanceof Listener listener) {
            setup.addListener(listener);
        }
    }
}
