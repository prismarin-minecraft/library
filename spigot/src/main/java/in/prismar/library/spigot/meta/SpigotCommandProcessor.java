package in.prismar.library.spigot.meta;

import in.prismar.library.meta.MetaEntity;
import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.processor.AbstractMetaProcessor;
import in.prismar.library.meta.processor.MetaProcessorType;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import in.prismar.library.spigot.setup.SpigotSetup;
import org.bukkit.command.Command;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SpigotCommandProcessor extends AbstractMetaProcessor {

    private final SpigotSetup setup;

    public SpigotCommandProcessor(SpigotSetup setup, MetaRegistry registry) {
        super(registry, MetaProcessorType.SCAN);
        this.setup = setup;
    }

    @Override
    public void process(Class<?> target) throws Exception {
        if(!target.isAnnotationPresent(AutoCommand.class)) {
            return;
        }
        MetaEntity entity = getRegistry().registerEntity(target);
        if(entity.getInstance() instanceof Command command) {
            setup.addCommand(command);
        } else if(entity.getInstance() instanceof SpigotCommand<?> command) {
            setup.addCommand(command);
        }
    }
}
