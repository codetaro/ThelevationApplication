package au.edu.sydney.comp5216.thelevationapplication.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import au.edu.sydney.comp5216.thelevationapplication.R;
import au.edu.sydney.comp5216.thelevationapplication.SearchPlaceActivity;
import au.edu.sydney.comp5216.thelevationapplication.model.PlaceResult;

/**
 * Created by gyua0818 on 2016/10/16.
 */
public class TwoPointsFragment extends Fragment {

    public static final int CHOOSE_PLACE = 921;

    private Button mChooseSourceLocation;
    private Button mChooseDestinationLocation;
    private PlaceResult mSourcePlace;
    private PlaceResult mDestinationPlace;
    private TextView mGainLossLabel;
    private TextView mSummaryLabel;
    private TextView mHeaderLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_two_points, container, false);
        mChooseSourceLocation = (Button) rootView.findViewById(R.id.choose_source_location);
        mChooseSourceLocation.setOnClickListener(sourceClickListener);
        mChooseDestinationLocation = (Button) rootView.findViewById(R.id.choose_destination_location);
        mChooseDestinationLocation.setOnClickListener(destinationClickListener);

        mSummaryLabel = (TextView) rootView.findViewById(R.id.summary_label);
        mGainLossLabel = (TextView) rootView.findViewById(R.id.gain_loss);
        mHeaderLabel = (TextView) rootView.findViewById(R.id.header_label);

        if (savedInstanceState != null) {
            mSourcePlace = savedInstanceState.getParcelable("sourcePlace");
            mDestinationPlace = savedInstanceState.getParcelable("destinationPlace");
            updateUi();
        }

        return rootView;
    }

    private View.OnClickListener sourceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), SearchPlaceActivity.class);
            intent.putExtra("slot", 0);
            startActivityForResult(intent, CHOOSE_PLACE);
        }
    };

    private View.OnClickListener destinationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), SearchPlaceActivity.class);
            intent.putExtra("slot", 1);
            startActivityForResult(intent, CHOOSE_PLACE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras.containsKey("place") && extras.containsKey("slot")) {
                PlaceResult place = extras.getParcelable("place");
                int slot = extras.getInt("slot");
                if (slot == 0) {
                    mSourcePlace = place;
                } else if (slot == 1) {
                    mDestinationPlace = place;
                }
            }
            updateUi();
        }
    }

    private void updateUi() {
        if (mSourcePlace != null) {
            mChooseSourceLocation.setText("Source: " + mSourcePlace.name + " - " + mSourcePlace.formatted_address);
        }
        if (mDestinationPlace != null) {
            mChooseDestinationLocation.setText("Destination: " + mDestinationPlace.name + " - " + mDestinationPlace.formatted_address);
        }
        if (mSourcePlace != null && mDestinationPlace != null) {
            double finalElevation = mDestinationPlace.elevation - mSourcePlace.elevation;
            String labelInMeter = String.format("%.2f m.", finalElevation);
            StringBuilder buf = new StringBuilder();
            buf.append(mSourcePlace.name);

            String adjective = "";
            if (mDestinationPlace.elevation > mSourcePlace.elevation) {
                adjective = " is lower than ";
                buf.append(adjective);
            } else if (mDestinationPlace.elevation < mSourcePlace.elevation) {
                adjective = " is higher than ";
                buf.append(adjective);
            }
            buf.append(mDestinationPlace.name);
            mHeaderLabel.setVisibility(View.VISIBLE);
            mGainLossLabel.setText(labelInMeter);
            mGainLossLabel.setVisibility(View.VISIBLE);

            // Style the text
            String finalText = buf.toString();
            Spannable styledText = new SpannableString(finalText);
            int startIndex = finalText.indexOf(adjective);
            styledText.setSpan(new StyleSpan(
                    Typeface.BOLD),
                    startIndex,
                    startIndex + adjective.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSummaryLabel.setText(styledText);
            mSummaryLabel.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("sourcePlace", mSourcePlace);
        savedInstanceState.putParcelable("destinationPlace", mDestinationPlace);
        super.onSaveInstanceState(savedInstanceState);
    }
}
