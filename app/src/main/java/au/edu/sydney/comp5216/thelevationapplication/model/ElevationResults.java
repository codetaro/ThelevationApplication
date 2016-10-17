package au.edu.sydney.comp5216.thelevationapplication.model;

import java.util.ArrayList;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class ElevationResults {

    public String status;
    public ArrayList<ElevationResult> results;

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("status=")
           .append(status);
        for (ElevationResult e : results) {
            buf.append("\n")
               .append("elevation=")
               .append(e.elevation);
        }

        return buf.toString();
    }
}
