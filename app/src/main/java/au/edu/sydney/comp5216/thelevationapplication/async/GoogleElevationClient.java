package au.edu.sydney.comp5216.thelevationapplication.async;

import com.squareup.otto.Produce;

import java.util.HashMap;
import java.util.Map;

import au.edu.sydney.comp5216.thelevationapplication.BusProvider;
import au.edu.sydney.comp5216.thelevationapplication.event.ElevateEvent;
import au.edu.sydney.comp5216.thelevationapplication.model.ElevationResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class GoogleElevationClient {

    private static final String GOOGLE_ELEVATION_ENDPOINT = "https://maps.googleapis.com";
    private static final String GOOGLE_ELEVATION_API_KEY = "AIzaSyCcam2Nf_iXjpHhBRIgblQ3o4k9Nr0Dydc";

    interface GoogleElevationApiClient {
        @GET("/maps/api/elevation/json")
        void getElevationForLatLng(@QueryMap Map<String, String> options, Callback<ElevationResults> cb);
    }

    public void fetchElevation(final String requestId, double lat, double lng) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(GOOGLE_ELEVATION_ENDPOINT)
                .build();

        GoogleElevationApiClient client = restAdapter.create(GoogleElevationApiClient.class);

        Callback<ElevationResults> callback = new Callback<ElevationResults>() {
            @Override
            public void success(ElevationResults elevationResults, Response response) {
                BusProvider.getInstance().post(produceElevateEvent(requestId, elevationResults));
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO
            }
        };

        final Map<String, String> options = new HashMap<String, String>();
        final String latLng = String.format("%f,%f", lat, lng);
        options.put("locations", latLng);
        options.put("key", GOOGLE_ELEVATION_API_KEY);
        client.getElevationForLatLng(options, callback);
    }

    @Produce
    public ElevateEvent produceElevateEvent(String requestId, ElevationResults results) {
        return new ElevateEvent(requestId, results);
    }
}
