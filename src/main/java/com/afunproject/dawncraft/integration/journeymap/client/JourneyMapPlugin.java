package com.afunproject.dawncraft.integration.journeymap.client;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.integration.journeymap.network.AddWaypointMessage;
import com.google.common.collect.Lists;
import journeymap.client.api.ClientPlugin;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.display.Waypoint;
import journeymap.client.api.display.WaypointGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

@ClientPlugin
public class JourneyMapPlugin implements IClientPlugin {

    private static final WaypointGroup GROUP = new WaypointGroup(Constants.MODID, "structures");
    private static JourneyMapPlugin instance;
    private IClientAPI api;
    private List<AddWaypointMessage> waypoints = Lists.newArrayList();

    @Override
    public void initialize(IClientAPI api) {
        instance = this;
        this.api = api;
    }

    @Override
    public String getModId() {
        return Constants.MODID;
    }

    @Override
    public void onEvent(ClientEvent clientEvent) {
        if (clientEvent.type == ClientEvent.Type.MAPPING_STARTED) {
            for (AddWaypointMessage message : waypoints) {
                try {
                    String name = message.getStructure();
                    BlockPos pos = message.getPos();
                    ResourceKey<Level> dim = Minecraft.getInstance().player.level.dimension();
                    Waypoint waypoint = new Waypoint(Constants.MODID, name, dim, pos);
                    waypoint.setColor(0xA20CD6);
                    waypoint.setGroup(GROUP);
                    api.show(waypoint);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            waypoints.clear();
        }
    }

    public void addWaypoint(AddWaypointMessage message) {
        waypoints.add(message);
    }

    public static JourneyMapPlugin getInstance() {
        return instance;
    }
}
