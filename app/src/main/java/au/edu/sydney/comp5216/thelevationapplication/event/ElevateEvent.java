package au.edu.sydney.comp5216.thelevationapplication.event;

import au.edu.sydney.comp5216.thelevationapplication.model.ElevationResult;
import au.edu.sydney.comp5216.thelevationapplication.model.ElevationResults;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class ElevateEvent {

    public ElevationResults results;
    public String requestId;

    public ElevateEvent(String requestId, ElevationResults results) {
        this.requestId = requestId;
        this.results = results;
    }

    public ElevationResult getPrimaryEntry() {
        if (results != null && results.results.size() == 1) {
            return results.results.get(0);
        } else {
            return null;
        }
    }
}
