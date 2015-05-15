package com.pqqqqq.directessentials.data.region;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.util.command.source.LocatedSource;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Created by Kevin on 2015-05-14.
 */
public class Cuboid {
    private World world;
    private Vector3d vec1;
    private Vector3d vec2;

    public Cuboid() {
        this(null);
    }

    public Cuboid(World world) {
        this(world, null, null);
    }

    public Cuboid(World world, Vector3d vec1, Vector3d vec2) {
        this.world = world;
        this.vec1 = vec1;
        this.vec2 = vec2;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Vector3d getVec1() {
        return vec1;
    }

    public void setVec1(Vector3d vec1) {
        this.vec1 = vec1;
    }

    public Vector3d getVec2() {
        return vec2;
    }

    public void setVec2(Vector3d vec2) {
        this.vec2 = vec2;
    }

    public void setAsVec1(Location loc) {
        if (world == null) {
            this.world = (World) loc.getExtent();
        } else if (!loc.getExtent().equals(this.world)) {
            throw new IllegalArgumentException("Can't set a cuboid between two worlds");
        }

        setVec1(loc.getPosition());
    }

    public void setAsVec2(Location loc) {
        if (world == null) {
            this.world = (World) loc.getExtent();
        } else if (!loc.getExtent().equals(this.world)) {
            throw new IllegalArgumentException("Can't set a cuboid between two worlds");
        }
        setVec2(loc.getPosition());
    }

    public boolean isStable() {
        return world != null && vec1 != null && vec2 != null;
    }

    public double getMinX() {
        return Math.min(vec1.getX(), vec2.getX());
    }

    public double getMaxX() {
        return Math.max(vec1.getX(), vec2.getX());
    }

    public double getMinY() {
        return Math.min(vec1.getY(), vec2.getY());
    }

    public double getMaxY() {
        return Math.max(vec1.getY(), vec2.getY());
    }

    public double getMinZ() {
        return Math.min(vec1.getZ(), vec2.getZ());
    }

    public double getMaxZ() {
        return Math.max(vec1.getZ(), vec2.getZ());
    }

    public boolean containsNoWorld(double x, double y, double z) {
        return x >= getMinX() && x <= getMaxX() && y >= getMinY() && y <= getMaxY() && z >= getMinZ() && z <= getMaxZ();
    }

    public boolean containsNoWorld(Vector3d vec) {
        return containsNoWorld(vec.getX(), vec.getY(), vec.getZ());
    }

    public boolean containsNoWorld(Location location) {
        return containsNoWorld(location.getPosition());
    }

    public boolean containsNoWorld(LocatedSource lsource) {
        return containsNoWorld(lsource.getLocation());
    }

    public boolean contains(World world, double x, double y, double z) {
        return this.world.equals(world) && containsNoWorld(x, y, z);
    }

    public boolean contains(World world, Vector3d vec) {
        return this.world.equals(world) && containsNoWorld(vec);
    }

    public boolean contains(Location location) {
        return this.world.equals(location.getExtent()) && containsNoWorld(location);
    }

    public boolean contains(LocatedSource locatedSource) {
        return this.world.equals(locatedSource.getWorld()) && containsNoWorld(locatedSource);
    }

    public int getSize() {
        if (!isStable()) {
            return -1;
        }
        
        Vector3d sub = vec2.sub(vec1).abs().add(1D, 1D, 1D);
        return sub.getFloorX() * sub.getFloorY() * sub.getFloorZ();
    }
}
