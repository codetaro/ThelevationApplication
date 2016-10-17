package au.edu.sydney.comp5216.thelevationapplication.fragments;

import android.location.Location;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.Connection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import au.edu.sydney.comp5216.thelevationapplication.BusProvider;
import au.edu.sydney.comp5216.thelevationapplication.R;
import au.edu.sydney.comp5216.thelevationapplication.UuidUtil;
import au.edu.sydney.comp5216.thelevationapplication.async.GoogleElevationClient;
import au.edu.sydney.comp5216.thelevationapplication.event.ElevateEvent;
import au.edu.sydney.comp5216.thelevationapplication.model.ElevationResult;
import au.edu.sydney.comp5216.thelevationapplication.model.ElevationResults;

/**
 * Created by gyua0818 on 2016/10/16.
 */
public class YouOnMapFragment extends Fragment implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleMap mMap;  // Might be null if Google Play services APK is not available.
    private boolean mGooglePlayEnabled;
    private GoogleElevationClient mElevateClient;
    private LocationClient mLocationClient;
    private TextView mInfoLabel;
    private LocationRequest mLocationRequest;
    private boolean mUpdatesRequested;

    private Location mCurrentLocation;
    private String mElevationRequestId;


    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int UPDATE_INTERVAL_IN_SECONDS = 60 * 2;
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mGooglePlayEnabled = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity().getApplicationContext())
                == ConnectionResult.SUCCESS;
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_you_on_map, container, false);
        mElevateClient = new GoogleElevationClient();
        mLocationClient = new LocationClient(this.getActivity(), this, this);
        mInfoLabel = (TextView) rootView.findViewById(R.id.info_label);
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL);
        // Start with updates turned on
        mUpdatesRequested = true;

        return rootView;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateUiWithLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void updateUiWithLocation() {
        if (mGooglePlayEnabled) {
            mElevationRequestId = UuidUtil.generate();
            mElevateClient.fetchElevation(mElevationRequestId,
                    mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude());
        }
    }

    @Subscribe
    public void onElevateEvent(ElevateEvent event) {
        if (mElevationRequestId == null) {
            return;
        }
        if (mElevationRequestId.equals(event.requestId)) {
            final ElevationResults results = event.results;
            if (results.results.size() == 1) {
                final ElevationResult entry = results.results.get(0);
                final LatLng latLng = new LatLng(entry.location.lat, entry.location.lng);
                if (mMap != null) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title("You"));
                    mInfoLabel.setText(String.format("%.2f m.", entry.elevation));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        BusProvider.getInstance().register(this);
        mLocationClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
        mLocationClient.disconnect();
        BusProvider.getInstance().unregister(this);
    }

    private void setUpMapIfNeeded() {
        if (mGooglePlayEnabled) {
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
//                mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
//                        .getMap();
                mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }
}
