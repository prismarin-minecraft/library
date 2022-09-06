package in.prismar.library.spigot.hologram;

import in.prismar.library.spigot.hologram.placeholder.HologramPlaceholder;
import lombok.Getter;
import lombok.Setter;
import in.prismar.library.common.tuple.Tuple;
import in.prismar.library.spigot.hologram.line.HologramLine;
import in.prismar.library.spigot.hologram.line.HologramLineType;
import in.prismar.library.spigot.hologram.placeholder.HologramPlaceholder;
import in.prismar.library.spigot.hologram.placeholder.HologramPlaceholderValue;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class Hologram {

    private Location location;
    private List<HologramPlaceholder> placeholders;
    private Map<UUID, HologramViewer> viewers;
    private List<Tuple<HologramLineType, Object>> lines;

    @Setter
    private boolean global;

    public Hologram(Location location) {
        this.location = location;
        this.placeholders = new ArrayList<>();
        this.viewers = new HashMap<>();
        this.lines = new ArrayList<>();
        this.global = true;
    }

    public Hologram(Location location, String... lines) {
        this(location);
        for(String line : lines) {
            this.lines.add(new Tuple<>(HologramLineType.TEXT, line));
        }
    }

    public Hologram insertLine(int index, HologramLineType type, Object content) {
        this.lines.set(index, new Tuple<>(type, content));
        return this;
    }

    public Hologram addLine(HologramLineType type, Object content) {
        this.lines.add(new Tuple<>(type, content));
        return this;
    }

    public Hologram updateLines(String... lines) {
        this.lines.clear();
        for(String line : lines) {
            this.lines.add(new Tuple<>(HologramLineType.TEXT, line));
        }
        refreshViewers();
        return this;
    }

    public Hologram refreshViewers() {
        for(HologramViewer viewer : this.viewers.values()) {
            refreshViewer(viewer);
        }
        return this;
    }

    public Hologram refreshViewer(HologramViewer viewer) {
        viewer.getLines().clear();
        viewer.setSpawned(false);
        return this;
    }

    public Hologram enable() {
        HologramBootstrap.getInstance().addHologram(this);
        return this;
    }

    public Hologram disable() {
        HologramBootstrap.getInstance().removeHologram(this);
        return this;
    }

    public Hologram addPlaceholder(String key, HologramPlaceholderValue replacer) {
        this.placeholders.add(new HologramPlaceholder(key, replacer));
        return this;
    }

    public HologramViewer addViewer(Player player) {
        HologramViewer viewer = new HologramViewer(player);
        viewers.put(player.getUniqueId(), viewer);
        return viewer;
    }

    @Nullable
    public HologramViewer removeViewer(Player player) {
        return this.viewers.remove(player.getUniqueId());
    }

    public HologramViewer getViewer(Player player) {
        return viewers.get(player.getUniqueId());
    }

    public boolean existsViewer(Player player) {
        return viewers.containsKey(player.getUniqueId());
    }

    public void updateViewer(HologramViewer viewer) {
        Player player = viewer.getPlayer();
        if(location.getWorld().equals(player.getWorld())) {
            if(location.distanceSquared(player.getLocation()) <= HologramBootstrap.getInstance().getMinSpawnDistance()) {
                if(!viewer.isSpawned()) {
                    viewer.setSpawned(true);
                    spawnLines(viewer);
                }
            } else {
                if(viewer.isSpawned()) {
                    viewer.setSpawned(false);
                    despawnLines(viewer);
                }
            }
        } else {
            if(viewer.isSpawned()) {
                viewer.setSpawned(false);
            }

        }
    }

    public void updateViewers() {
        for(HologramViewer viewer : this.viewers.values()) {
            updateViewer(viewer);
        }
    }

    protected void spawnLines(HologramViewer viewer) {
        if(!viewer.getLines().isEmpty()) {
            for(HologramLine line : viewer.getLines()) {
                line.spawn(viewer.getPlayer());
            }
            return;
        }
        Location location = this.location.clone();

        for(Tuple<HologramLineType, Object> line : lines) {
            Object value = line.getSecond();
            if(line.getFirst() == HologramLineType.TEXT) {
                for(HologramPlaceholder placeholder : this.placeholders) {
                    value = value.toString().replace(placeholder.getKey(), placeholder.getReplacer().value(viewer.getPlayer()));
                }
            }
            HologramLine holoLine = new HologramLine(location, line.getFirst(), value);
            holoLine.spawn(viewer.getPlayer());
            viewer.getLines().add(holoLine);
            double space = 0;
            switch (line.getFirst()) {
                case ITEM_HEAD:
                    space = HologramBootstrap.getInstance().getSpaceBetweenLineHeads();
                    break;
                default:
                    space = HologramBootstrap.getInstance().getSpaceBetweenLineTexts();
                    break;
            }
            location = location.subtract(0, space, 0);
        }
    }

    protected void despawnLines(HologramViewer viewer) {
        for(HologramLine line : viewer.getLines()) {
            line.despawn(viewer.getPlayer());
        }
    }

    public void despawnAll() {
        for(HologramViewer viewer : viewers.values()) {
            despawnLines(viewer);
        }
    }

    public void clear() {
        despawnAll();
        viewers.clear();
    }
}
