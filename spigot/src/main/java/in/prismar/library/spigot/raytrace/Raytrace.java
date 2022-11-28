package in.prismar.library.spigot.raytrace;


import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitbox;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxFace;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxHelper;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import in.prismar.library.spigot.raytrace.result.RaytraceResult;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class Raytrace {

    private Location origin;
    private Vector direction;

    private List<RaytraceHitbox> hitboxes;

    public Raytrace(Location origin, Vector direction) {
        this.origin = origin.clone();
        this.direction = direction.clone().normalize();
    }

    public Raytrace(Location origin, Vector direction, List<RaytraceHitbox> hitboxes) {
        this.origin = origin.clone();
        this.direction = direction.clone().normalize();
        this.hitboxes = hitboxes;
    }

    public RaytraceResult ray(double range) {
        if(hitboxes == null){
            this.hitboxes = RaytraceHitboxHelper.collectPossibleEntityHitboxes(origin, direction, range, 180);
            this.hitboxes.addAll(RaytraceHitboxHelper.collectPossibleBlockHitboxes(origin, direction, range));
        }
        RaytraceResult result = new RaytraceResult();
        List<RaytraceHit> hits = new ArrayList<>();

        for(RaytraceHitbox hitbox : hitboxes) {
            RaytraceHitboxFace closestFace = null;
            double closestFaceDistance = range * range;
            Location closestFaceIntersection = null;
            for(RaytraceHitboxFace face : hitbox.getFaces()) {
                Location intersection = intersectFace(face);
                if(face.isBetween(intersection)) {
                    double distance = intersection.distanceSquared(origin);
                    
                    // check if the intersection point lies on the positive direction
                    var delta = intersection.toVector().subtract(origin.toVector());
                    if(delta.dot(direction) < 0) {
                    	continue;
                    }
                    
                    if(distance <= closestFaceDistance) {
                        closestFaceDistance = distance;
                        closestFace = face;
                        closestFaceIntersection = intersection;
                    }
                }
            }
            if(closestFace != null) {
                RaytraceHit hit = hitbox.asHit(closestFaceIntersection);
                hits.add(hit);
            }
        }
        result.setHits(hits);
        return result;
    }

    private Location intersectFace(RaytraceHitboxFace face) {
        Vector difference = origin.toVector().subtract(face.a());
        double differenceDot = difference.dot(face.n());
        double directionDot = direction.dot(face.n());
        double length = differenceDot / directionDot;
        Location point = origin.clone().subtract(direction.clone().multiply(length));
        return point;
    }
}
