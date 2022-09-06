package in.prismar.library.spigot.location;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class LocationUtil {

    public static Location getCenterOfBlock(Location location) {
        return location.getBlock().getLocation().clone().add(0.5, 0.0, 0.5);
    }

    public static Location getCenterOfChunk(Chunk chunk) {
        Location center = new Location(chunk.getWorld(), chunk.getX() << 4, 64, chunk.getZ() << 4).add(7, 0, 7);
        center.setY(center.getWorld().getHighestBlockYAt(center));
        return center;
    }

    public static Location stringToLocation(String stringLocation) {
        String[] args = stringLocation.split(";");
        World world = Bukkit.getWorld(args[0]);
        double x = Double.valueOf(args[1]).doubleValue();
        double y = Double.valueOf(args[2]).doubleValue();
        double z = Double.valueOf(args[3]).doubleValue();
        double yaw = Double.valueOf(args[4]).doubleValue();
        double pitch = Double.valueOf(args[5]).doubleValue();
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public static String locationToString(Location location) {
        StringBuilder builder = (new StringBuilder()).append(location.getWorld().getName()).append(";").append(location.getX()).append(";").append(location.getY()).append(";").append(location.getZ()).append(";").append(location.getYaw()).append(";").append(location.getPitch());
        return builder.toString();
    }
}
