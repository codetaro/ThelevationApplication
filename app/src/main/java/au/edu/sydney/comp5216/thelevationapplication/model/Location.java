package au.edu.sydney.comp5216.thelevationapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class Location implements Parcelable {

    public double lat;
    public double lng;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    private Location(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }
}
