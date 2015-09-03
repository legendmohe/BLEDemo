package com.example.bledemo.view;

import java.util.HashSet;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bledemo.R;
import com.example.bledemo.ble.BLEManager;

public class MainActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final int REQUEST_ENABLE_BT = 1;

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    
    private int mFragmentIndex;
    
    private HashSet<MenuStub> mMenuStubs;

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setupBluetooth();
        mMenuStubs = new HashSet<>();
        mFragmentIndex = -1;
        
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, getString(R.string.ble_not_supported),
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupBluetooth() {
        if (BLEManager.getInstance().isBtEnable()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment fragment = null;

        if (position == 0) {
            fragment = DeviceListviewFragment.newInstance(position + 1);
            addMenuStub((DeviceListviewFragment)fragment);
        } else {
            fragment = PlaceholderFragment.newInstance(position + 1);
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        mFragmentIndex = number;
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            switch (mFragmentIndex) {
                case 1:
                    getMenuInflater().inflate(R.menu.device_list_fragment, menu);
                    break;
                case 2:
                    getMenuInflater().inflate(R.menu.main, menu);
                    break;
                case 3:
                    getMenuInflater().inflate(R.menu.main, menu);
                    break;

                default:
                    break;
            }
            injectMenuToStubs(menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        notifyMenuSelectionToStubs(item.getItemId());
        return true;
    }
    
    public void addMenuStub(MenuStub stub) {
        mMenuStubs.add(stub);
    }
    
    public void removeMenuStub(MenuStub stub) {
        mMenuStubs.remove(stub);
    }
    
    private void injectMenuToStubs(Menu menu) {
        for (MenuStub stub : mMenuStubs) {
            stub.injectMenu(menu);
        }
    }
    
    private void notifyMenuSelectionToStubs(int id) {
        for (MenuStub stub : mMenuStubs) {
            stub.onMenuSelected(id);
        }
    }
    
    public static interface MenuStub {
        void injectMenu(Menu menu);
        void onMenuSelected(int id);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(
                    ARG_SECTION_NUMBER));
        }
    }

}
