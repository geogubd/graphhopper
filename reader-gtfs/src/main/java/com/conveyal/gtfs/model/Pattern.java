/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.conveyal.gtfs.model;

import com.conveyal.gtfs.GTFSFeed;
import com.google.common.base.Joiner;
import com.vividsolutions.jts.geom.LineString;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by landon on 2/5/16.
 */
public class Pattern implements Serializable {
    public static final long serialVersionUID = 1L;

    public String pattern_id;
    public int[] segmentIndex;
    public double[] segmentFraction;
    public List<String> orderedStops;
    // TODO: change list of trips to set
    public List<String> associatedTrips;
    // TODO: add set of shapes
//    public Set<String> associatedShapes;
    public LineString geometry;
    public String name;
    public String route_id;
    public static Joiner joiner = Joiner.on("-").skipNulls();
    public String feed_id;

    // TODO: Should a Pattern be generated for a single trip or a set of trips that share the same ordered stop list?
    public Pattern (GTFSFeed feed, List<String> orderedStops, List<String> trips){
        this.feed_id = feed.feedId;

        this.pattern_id = UUID.randomUUID().toString();

        // Assign ordered stops to key of tripsForStopPattern
        this.orderedStops = orderedStops;

        // Assign associated trips to value of tripsForStopPattern
        this.associatedTrips = trips;

        // Get geometry for first trip in list of associated trips
        String trip_id = associatedTrips.get(0);
        Trip trip;

        trip = feed.trips.get(trip_id);
        this.geometry = feed.getTripGeometry(trip.trip_id);

        // patterns are now on one and only one route
        this.route_id = trip.route_id;

        if (trip.trip_headsign != null){
            name = trip.trip_headsign;
        }
        else if (trip.trip_short_name != null) {
            name = trip.trip_short_name;
        }
        else if (trip.direction_id >= 0){
            name = String.valueOf(trip.direction_id);
        }
        else{
            name = joiner.join(orderedStops);
        }

        // TODO: Implement segmentIndex using JTS to segment out LineString by stops.

        // TODO: Implement segmentFraction using JTS to segment out LineString by stops.

    }
}
