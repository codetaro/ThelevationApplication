package au.edu.sydney.comp5216.thelevationapplication;

import java.util.UUID;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class UuidUtil {
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
