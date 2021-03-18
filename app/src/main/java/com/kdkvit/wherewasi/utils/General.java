package com.kdkvit.wherewasi.utils;

import java.util.ArrayList;
import java.util.List;

import models.Interaction;
import models.LocationsGroup;
import models.MyLocation;

public class General {

    private static final int TIME_BETWEEN_NEW_LOCATIONS = 1000 * 60 * 15;
    private static final int TIME_BETWEEN_LOCATION_AND_INTERACTION = 1000 * 60 * 10;

    public static List<LocationsGroup> getLocationsGroup(List<MyLocation> locations, List<Interaction> interactions) {
        List<LocationsGroup> groups = new ArrayList<>();
        if (locations.size() == 0) return groups;
        MyLocation lastLocation = null;
        for (MyLocation location : locations) {
            if (lastLocation == null || (location.getStartTime() - lastLocation.getStartTime()) > TIME_BETWEEN_NEW_LOCATIONS) {
                groups.add(0, new LocationsGroup()); //Insert on top of the list
            }
            groups.get(0).addLocation(location); //Add location to the top of the group
            lastLocation = location;
        }

        for (Interaction interaction : interactions) {
            for (int j = 0; j < groups.size(); j++) {
                if (((interaction.getLastSeen() - groups.get(j).getStartTime().getTime()) >= TIME_BETWEEN_LOCATION_AND_INTERACTION)
                        && (interaction.getLastSeen() - groups.get(j).getEndTime().getTime() < 0)) {
                    groups.get(j).addInteraction(interaction);
                } else {
                    break; //Stops if not in group any more since the groups are ordered
                }
            }
        }
        return groups;
    }

    public static boolean checkIfLocationInGroup(LocationsGroup group,MyLocation location){
        if(group.locationsSize()==0) return true;
        return (location.getStartTime() - group.getLastLocation().getStartTime() <= TIME_BETWEEN_NEW_LOCATIONS);
    }
}

