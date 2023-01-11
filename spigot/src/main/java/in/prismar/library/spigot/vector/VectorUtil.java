package in.prismar.library.spigot.vector;

import in.prismar.library.common.math.MathUtil;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class VectorUtil {

    public static Vector getRandomizedDirection(Player player, double spread) {
        Vector dir = player.getLocation().getDirection().normalize();
        dir.setX(dir.getX() + getRandFactor(spread));
        dir.setY(dir.getY() + getRandFactor(spread));
        dir.setZ(dir.getZ() + getRandFactor(spread));
        return dir;
    }

    public static double getRandFactor(double spread) {
        return MathUtil.randomDouble(-0.01, 0.01) * spread;
    }

    public static Vector rotateVector(Vector v, float yawDegrees, float pitchDegrees) {
        double yaw = Math.toRadians(-1 * (yawDegrees + 90));
        double pitch = Math.toRadians(-pitchDegrees);

        double cosYaw = Math.cos(yaw);
        double cosPitch = Math.cos(pitch);
        double sinYaw = Math.sin(yaw);
        double sinPitch = Math.sin(pitch);

        double initialX, initialY, initialZ;
        double x, y, z;

        initialX = v.getX();
        initialY = v.getY();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        initialZ = v.getZ();
        initialX = x;
        z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;
        return new Vector(x, y, z);
    }
}
