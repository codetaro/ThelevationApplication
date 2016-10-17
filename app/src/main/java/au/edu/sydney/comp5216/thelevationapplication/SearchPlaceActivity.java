package au.edu.sydney.comp5216.thelevationapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import au.edu.sydney.comp5216.thelevationapplication.async.GoogleElevationClient;
import au.edu.sydney.comp5216.thelevationapplication.async.GooglePlacesClient;
import au.edu.sydney.comp5216.thelevationapplication.event.ElevateEvent;
import au.edu.sydney.comp5216.thelevationapplication.event.PlaceEvent;
import au.edu.sydney.comp5216.thelevationapplication.model.PlaceResult;

public class SearchPlaceActivity extends Activity {

    private EditText mSearchInput;
    private ListView mListView;
    private GooglePlacesClient mSearchClient;
    private GoogleElevationClient mElevationClient;
    private ArrayAdapter mArrayAdapter;
    private ArrayList<PlaceResult> mPlacesList;
    private PlaceResult mChosenPlace;
    private ProgressDialog mProgressDialog;
    private int mSlot;
    private String mElevationRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);

        mSearchInput = (EditText) findViewById(R.id.search);
        mSearchInput.setOnEditorActionListener(textListener);
        mListView = (ListView) findViewById(R.id.list);

        mSearchClient = new GooglePlacesClient();
        mElevationClient = new GoogleElevationClient();

        mPlacesList = new ArrayList<PlaceResult>();
        mArrayAdapter = new SearchResultsAdapter(getApplicationContext(), R.id.list, mPlacesList);
        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(placeChoseListener);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSlot = extras.getInt("slot", 0);
        }
    }

    TextView.OnEditorActionListener textListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.getText().toString());
                return true;
            }
            return false;
        }
    };

    AdapterView.OnItemClickListener placeChoseListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mChosenPlace = mPlacesList.get(position);
            updateUiForElevationFetch();
        }
    };

    private void performSearch(String query) {
        mSearchClient.search(query);
    }

    private void updateUiForElevationFetch() {
        // show progress bar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching elevation, please wait.");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        // actually fetch the elevation
        fetchElevationForPlace();
    }

    private void fetchElevationForPlace() {
        mElevationRequestId = UuidUtil.generate();
        mElevationClient.fetchElevation(mElevationRequestId, mChosenPlace.getLat(), mChosenPlace.getLng());
    }

    @Subscribe
    public void onElevationEvent(ElevateEvent event) {
        if (mElevationRequestId == null) {
            return;
        }
        if (mElevationRequestId.equals(event.requestId)) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mChosenPlace.elevation = event.getPrimaryEntry().elevation;
            Intent returnIntent = new Intent();
            returnIntent.putExtra("place", mChosenPlace);
            returnIntent.putExtra("slot", mSlot);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    @Subscribe
    public void onSearchResultsEvent(PlaceEvent event) {
        mPlacesList.clear();
        mPlacesList.addAll(event.results.results);
        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
