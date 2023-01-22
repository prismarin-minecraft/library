package in.prismar.library.spigot.raytrace.hitbox;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copyright (c) Maga, All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Maga
 **/
public class RaytraceHitboxHelper {

    public static List<RaytraceHitbox> collectPossibleEntityHitboxes(Location origin, Vector direction, double range, double fov) {
        final World world = origin.getWorld();
        final double rangeSqrt = range * range;
        final var diff = fov - 90;
        final var factor = (double) 4 / 10;
        final var result = diff * factor + 61;
        final var minDotProduct = Math.cos(Math.toRadians(result));
        List<RaytraceHitbox> hitboxes = new ArrayList<>();

        for (Player player : world.getPlayers()) {
            Location location = player.getEyeLocation();
            if (location.distanceSquared(origin) <= rangeSqrt) {
                double deltaX = location.getX() - origin.getX();
                double deltaY = location.getY() - origin.getY();
                double deltaZ = location.getZ() - origin.getZ();
                Vector targetDirection = new Vector(deltaX, deltaY, deltaZ).normalize();
                double dot = targetDirection.dot(direction);

                if (dot > minDotProduct) {
                    hitboxes.add(new RaytraceEntityHitbox(player));
                }
            }
        }
        return hitboxes;
    }

    public static List<RaytraceHitbox> collectPossibleBlockHitboxesProfessionalWay(Location origin, Vector direction, double range) {
        List<RaytraceHitbox> hitboxes = new ArrayList<>();
        Vector start = origin.toVector();
        Vector end = start.clone().add(direction.clone().multiply(range));
        if (start.equals(end)) {
            return hitboxes;
        } else {
            double d0 = Mth.lerp(-1.0E-7D, end.getX(), start.getX());
            double d1 = Mth.lerp(-1.0E-7D, end.getY(), start.getY());
            double d2 = Mth.lerp(-1.0E-7D, end.getZ(), start.getZ());
            double d3 = Mth.lerp(-1.0E-7D, start.getX(), end.getX());
            double d4 = Mth.lerp(-1.0E-7D, start.getY(), end.getY());
            double d5 = Mth.lerp(-1.0E-7D, start.getZ(), end.getZ());
            int i = Mth.floor(d3);
            int j = Mth.floor(d4);
            int k = Mth.floor(d5);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(i, j, k);
            double d6 = d0 - d3;
            double d7 = d1 - d4;
            double d8 = d2 - d5;
            int l = Mth.sign(d6);
            int i1 = Mth.sign(d7);
            int j1 = Mth.sign(d8);
            double d9 = l == 0 ? Double.MAX_VALUE : (double) l / d6;
            double d10 = i1 == 0 ? Double.MAX_VALUE : (double) i1 / d7;
            double d11 = j1 == 0 ? Double.MAX_VALUE : (double) j1 / d8;
            double d12 = d9 * (l > 0 ? 1.0D - Mth.frac(d3) : Mth.frac(d3));
            double d13 = d10 * (i1 > 0 ? 1.0D - Mth.frac(d4) : Mth.frac(d4));
            double d14 = d11 * (j1 > 0 ? 1.0D - Mth.frac(d5) : Mth.frac(d5));

            while (d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D) {
                if (d12 < d13) {
                    if (d12 < d14) {
                        i += l;
                        d12 += d9;
                    } else {
                        k += j1;
                        d14 += d11;
                    }
                } else if (d13 < d14) {
                    j += i1;
                    d13 += d10;
                } else {
                    k += j1;
                    d14 += d11;
                }
				Block block = origin.getWorld().getBlockAt(i, j, k);
                if(block.getType().isOccluding()) {
					hitboxes.add(new RaytraceBlockHitbox(block));
				}
            }
            return hitboxes;

        }
    }

    public static List<RaytraceHitbox> collectPossibleBlockHitboxes(Location origin, Vector direction, double range) {
        /* Shared constants */
        final var directionN = direction.clone()
                .normalize();
        final var dirX = directionN.getX();
        final var dirY = directionN.getY();
        final var dirZ = directionN.getZ();
        final var world = origin.getWorld();
        final var x = origin.getX();
        final var y = origin.getY();
        final var z = origin.getZ();


        Set<Block> blocks = new HashSet<>();
        int n = (int) range; // safe to use lower bound as we outline the block
        for (int i = 0; i < n; i++) {
            for (int outlineX = -1; outlineX < 2; outlineX++) {
                for (int outlineY = -1; outlineY < 2; outlineY++) {
                    for (int outlineZ = -1; outlineZ < 2; outlineZ++) {
                        var position = new Location(world, x + dirX * i + outlineX, y + dirY * i + outlineY, z + dirZ * i + outlineZ);
                        var block = position.getBlock();
                        if (block.getType()
                                .isOccluding()) {
                            blocks.add(block);
                        }
                    }
                }
            }
        }

        List<RaytraceHitbox> hitboxes = new ArrayList<>();
        for (var block : blocks) {
            hitboxes.add(new RaytraceBlockHitbox(block));
        }
        return hitboxes;
    }
}
