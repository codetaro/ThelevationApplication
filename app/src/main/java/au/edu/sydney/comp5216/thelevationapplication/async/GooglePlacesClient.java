package au.edu.sydney.comp5216.thelevationapplication.async;

import com.squareup.otto.Produce;

import java.util.HashMap;
import java.util.Map;

import au.edu.sydney.comp5216.thelevationapplication.BusProvider;
import au.edu.sydney.comp5216.thelevationapplication.event.PlaceEvent;

import au.edu.sydney.comp5216.thelevationapplication.model.PlaceResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by gyua0818 on 2016/10/17.
 */
public class GooglePlacesClient {

    private static final String GOOGLE_PLACES_ENDPOINT = "https://maps.googleapis.com";
    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyCcam2Nf_iXjpHhBRIgblQ3o4k9Nr0Dydc";

    interface GooglePlacesApiClient {
        @GET("/maps/api/place/textsearch/json")
        void search(@QueryMap Map<String, String> options, Callback<PlaceResults> cb);
    }

    public void search(String query) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(GOOGLE_PLACES_ENDPOINT)
                .build();

        GooglePlacesApiClient client = restAdapter.create(GooglePlacesApiClient.class);

        Callback<PlaceResults> callback = new Callback<PlaceResults>() {
            @Override
            public void success(PlaceResults placeResults, Response response) {
                BusProvider.getInstance().post(producePlacesEvent(placeResults));
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO
            }
        };

        final Map<String, String> options = new HashMap<String, String>();
        options.put("query", query);
        options.put("key", GOOGLE_PLACES_API_KEY);
        client.search(options, callback);
    }

    @Produce
    public PlaceEvent producePlacesEvent(PlaceResults results) {
        return new PlaceEvent(results);
    }
}
