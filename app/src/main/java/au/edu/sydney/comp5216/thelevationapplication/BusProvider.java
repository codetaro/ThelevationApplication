package au.edu.sydney.comp5216.thelevationapplication;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class BusProvider {

    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return BUS;
    }
}
