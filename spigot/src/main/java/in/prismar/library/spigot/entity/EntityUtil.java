package in.prismar.library.spigot.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class EntityUtil {

    @Nullable
    public static Entity findNearestEntity(Location location, double range, Predicate<Entity> predicate) {
        Entity nearest = null;
        double currentRange = range * range;
        for(Entity entity : location.getWorld().getEntities()) {
            if(!predicate.test(entity)) {
                continue;
            }
            double distance = entity.getLocation().distanceSquared(location);
            if(distance < currentRange) {
                nearest = entity;
                currentRange = distance;
            }
        }
        return nearest;
    }

    @Nullable
    public static Entity findNearestEntity(Location location, double range) {
        return findNearestEntity(location, range, entity -> true);
    }
}
