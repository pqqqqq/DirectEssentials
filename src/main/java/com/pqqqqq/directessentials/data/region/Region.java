package com.pqqqqq.directessentials.data.region;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.pqqqqq.directessentials.util.SaveUtils;
import com.pqqqqq.directessentials.wrappers.interfaces.ISaveable;
import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 2015-05-14.
 */
public class Region implements IWeakValue, ISaveable {
    private String name;
    private String owner;
    private List<String> members = new ArrayList<String>();

    private Cuboid cuboid;

    public static Map<String, Region> loadRegions(ConfigurationNode node) {
        Map<String, Region> regions = new HashMap<String, Region>();

        ConfigurationNode regionsNode = node.getNode("regions");
        for (ConfigurationNode regionNode : regionsNode.getChildrenMap().values()) {
            String owner = regionNode.getNode("owner").getString(null);
            Optional<World> world = SaveUtils.loadWorld(regionNode);
            Vector3d vec1 = SaveUtils.loadVector(regionNode.getNode("vec1"));
            Vector3d vec2 = SaveUtils.loadVector(regionNode.getNode("vec2"));

            if (world.isPresent() && vec1 != null && vec2 != null) {
                Region region = new Region(regionNode.getKey().toString(), owner, world.get(), vec1, vec2);
                region.getMembers().addAll(SaveUtils.loadStringList(regionNode.getNode("members")));

                regions.put(regionNode.getKey().toString(), region);
            }
        }

        return regions;
    }

    public Region() {
        cuboid = new Cuboid();
    }

    public Region(String name, String owner, World world, Vector3d vec1, Vector3d vec2) {
        cuboid = new Cuboid(world, vec1, vec2);
        init(name, owner, world, vec1, vec2);
    }

    public Region(String name, String owner, Cuboid cuboid) {
        this.cuboid = cuboid;
        init(name, owner, cuboid.getWorld(), cuboid.getVec1(), cuboid.getVec2());
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public boolean canBuild(Player player) {
        if (player.hasPermission("directessentials.region.override." + name)) { // Excludes encompassing override permission
            return true;
        }

        String uuid = player.getUniqueId().toString();
        return owner != null && owner.equals(uuid) || members.contains(uuid);
    }

    public void init(Object... args) {
        this.name = (String) args[0];
        this.owner = (String) args[1];
        this.cuboid.setWorld((World) args[2]);
        this.cuboid.setVec1((Vector3d) args[3]);
        this.cuboid.setVec2((Vector3d) args[4]);
    }

    public void save(ConfigurationNode node) {
        ConfigurationNode regions = node.getNode("regions");
        ConfigurationNode thisRegion = regions.getNode(name);

        thisRegion.getNode("owner").setValue(this.owner);
        SaveUtils.saveWorld(cuboid.getWorld(), thisRegion);
        SaveUtils.saveVector(cuboid.getVec1(), thisRegion.getNode("vec1"));
        SaveUtils.saveVector(cuboid.getVec2(), thisRegion.getNode("vec2"));
        SaveUtils.saveStringList(members, thisRegion.getNode("members"));
    }
}
