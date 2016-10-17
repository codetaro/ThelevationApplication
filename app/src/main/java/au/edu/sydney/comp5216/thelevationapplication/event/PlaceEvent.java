package au.edu.sydney.comp5216.thelevationapplication.event;

import au.edu.sydney.comp5216.thelevationapplication.model.PlaceResults;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class PlaceEvent {

    public PlaceResults results;

    public PlaceEvent(PlaceResults results) {
        this.results = results;
    }
}
