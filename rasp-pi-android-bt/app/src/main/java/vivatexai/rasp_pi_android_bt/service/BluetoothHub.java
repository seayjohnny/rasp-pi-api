package vivatexai.rasp_pi_android_bt.service;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import vivatexai.rasp_pi_android_bt.R;
import vivatexai.rasp_pi_android_bt.common.FragmentBase;
import vivatexai.rasp_pi_android_bt.common.StatusInfoContainer;

public class BluetoothHub extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private BluetoothService btService;
    private String mConnectedDeviceName;
    private DeviceListFragment mDeviceListFragment;
    private LogFragment mLogFragment;
    private SettingsFragment mSettingsFragment;
    private StatusFragment mStatusFragment;
    private StatusInfoContainer btStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_home);

        // Initialize BluetoothService
        btService = new BluetoothService(mHandler);

        // Initialize StatusInfoContainer
        btStatus = new StatusInfoContainer();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(10);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            // Set the third page as DeviceListFragment
            if (position == 0) {
                mSettingsFragment = SettingsFragment.newInstance(position + 1, btService);
                return mSettingsFragment;
            } else if (position == 1) {
                mStatusFragment = StatusFragment.newInstance(position + 1, btStatus);
                return mStatusFragment;
            } else if (position == 2) {
                mDeviceListFragment = DeviceListFragment.newInstance(position + 1, btService);
                return mDeviceListFragment;
            } else if (position == 3){
                mLogFragment = LogFragment.newInstance(position + 1);
                return mLogFragment;
            } else {
                return FragmentBase.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Settings";
                case 1:
                    return "Status";
                case 2:
                    return "Devices";
                case 3:
                    return "Log";
            }
            return null;
        }
    }

    /**
     * Handler to handle incoming messages from BluetoothService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = BluetoothHub.this;
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            final Toast toast = Toast.makeText(activity,
                                    getString(R.string.connecting),
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 750);
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mLogFragment.write("Device:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mLogFragment.write(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_STATUS_CHANGE:
                    switch(msg.getData().getString(Constants.STATUS_STATE)) {
                        case StatusInfoContainer.NO_CONNECTION:
                            btStatus.clear();
                            mStatusFragment.updateStatusInfo(btStatus);
                            break;
                        case StatusInfoContainer.CONNECTING:
                            btStatus.setmConnectionState(StatusInfoContainer.CONNECTING);
                            mStatusFragment.updateStatusInfo(btStatus);
                            break;
                        case StatusInfoContainer.CONNECTED:
                            btStatus.setmConnectionState(StatusInfoContainer.CONNECTED);
                            btStatus.setmConnectedDeviceName(
                                    msg.getData().getString(Constants.STATUS_NAME));
                            btStatus.setmConnectedDeviceAddress(
                                    msg.getData().getString(Constants.STATUS_ADDRESS));
                            btStatus.setmConnectedSignalStrength(
                                    msg.getData().getString(Constants.STATUS_SIGNAL));
                            btStatus.startTimer();
                            mStatusFragment.updateStatusInfo(btStatus);
                            break;
                    }
                    break;
            }
        }
    };

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class GetStatusInfo extends Thread {

        public GetStatusInfo() {
        }

        public void run() {
            setName("GetStatusInfo");

            while (true) {

            }
        }

    }
}
