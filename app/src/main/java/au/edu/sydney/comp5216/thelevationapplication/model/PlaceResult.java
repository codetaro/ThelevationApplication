package au.edu.sydney.comp5216.thelevationapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class PlaceResult implements Parcelable {

    public String formatted_address;
    public Geometry geometry;
    public String icon;
    public String id;
    public String name;
    public double elevation;

    private PlaceResult(Parcel in) {
        this.formatted_address = in.readString();
        this.geometry = in.readParcelable(PlaceResult.class.getClassLoader());
        this.icon = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.elevation = in.readDouble();
    }

    public double getLat() {
        return geometry.location.lat;
    }

    public double getLng() {
        return geometry.location.lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<PlaceResult> CREATOR = new Parcelable.Creator<PlaceResult>() {

        @Override
        public PlaceResult createFromParcel(Parcel source) {
            return new PlaceResult(source);
        }

        @Override
        public PlaceResult[] newArray(int size) {
            return new PlaceResult[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(formatted_address);
        dest.writeParcelable(geometry, flags);
        dest.writeString(icon);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(elevation);
    }
}
