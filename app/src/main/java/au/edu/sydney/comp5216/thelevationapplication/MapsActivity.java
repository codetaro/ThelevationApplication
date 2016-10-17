package au.edu.sydney.comp5216.thelevationapplication;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.multidex.MultiDex;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
//import android.app.FragmentManager;
import android.support.v4.app.FragmentManager;
//import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import au.edu.sydney.comp5216.thelevationapplication.fragments.TwoPointsFragment;
import au.edu.sydney.comp5216.thelevationapplication.fragments.YouOnMapFragment;

public class MapsActivity extends FragmentActivity implements ActionBar.TabListener {


    // The pager widget, which handles animation and allows swiping horizontally to access previous
    // and next page.
    private ViewPager mViewPager;
    // The pager adapter, which provides the pages to the view pager widget.
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        // Specify that the Home/Up button should not be enabled, since there is no hierarchical parent.
        actionBar.setHomeButtonEnabled(false);
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Instantiate a ViewPager and a PagerAdapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mPagerAdapter = new AppSectionsPagerAdapter(getFragmentManager());
        mPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        // Set up a listener for when the user swipes between sections.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        YouOnMapFragment youOnMapFragment = new YouOnMapFragment();
        TwoPointsFragment twoPointsFragment = new TwoPointsFragment();

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return youOnMapFragment;
                default:
                    return twoPointsFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "Your Location";
                    break;
                case 1:
                    title = "Two Points";
                    break;
            }
            return title;
        }
    }
}
