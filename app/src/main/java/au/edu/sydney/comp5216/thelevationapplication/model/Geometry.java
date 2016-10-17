package au.edu.sydney.comp5216.thelevationapplication.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class Geometry implements Parcelable {

    public Location location;

    private Geometry(Parcel in) {
        this.location = in.readParcelable(Geometry.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location, flags);
    }

    public static final Parcelable.Creator<Geometry> CREATOR
            = new Parcelable.Creator<Geometry>() {

        @Override
        public Geometry createFromParcel(Parcel source) {
            return new Geometry(source);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };
}
