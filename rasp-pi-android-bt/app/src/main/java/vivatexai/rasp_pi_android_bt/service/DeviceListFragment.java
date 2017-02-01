package vivatexai.rasp_pi_android_bt.service;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import vivatexai.rasp_pi_android_bt.R;
import vivatexai.rasp_pi_android_bt.common.DeviceListAdapter;
import vivatexai.rasp_pi_android_bt.common.DeviceListItem;


public class DeviceListFragment extends Fragment {

    // Member fields
    private BluetoothAdapter btAdapter;
    private static BluetoothService btService;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private List<DeviceListItem> mDeviceItems;
    private String mConnectedDeviceName;

    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Empty constructor.
     */
    public DeviceListFragment() {}

    public static DeviceListFragment newInstance(int sectionNumber, BluetoothService service) {
        DeviceListFragment fragment = new DeviceListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        btService = service;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get view
        View rootView = inflater.inflate(R.layout.fragment_device_list, container, false);

        // Initialize array adapter.
        mPairedDevicesArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.device_list_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) rootView.findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Get the local Bluetooth adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        mDeviceItems = new ArrayList<>();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            rootView.findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mDeviceItems.add(
                        new DeviceListItem(device.getName(), device.getAddress()));
                mPairedDevicesArrayAdapter.add(device.getName());
                System.out.println("ADDED TO DEVICE LIST----------------------------------");
            }
        } else {
            mDeviceItems.add(
                    new DeviceListItem(getString(R.string.none_paired), ""));
            mPairedDevicesArrayAdapter.add(getString(R.string.none_paired));
        }

        return rootView;
    }

    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            btAdapter.cancelDiscovery();

            // Get the device name
            String deviceName = ((TextView) v).getText().toString();
            DeviceListItem deviceItem = null;
            // Get the address of item from DeviceItems
            for (DeviceListItem item : mDeviceItems) {
                if (item.getName().equals(deviceName)){
                    deviceItem = item;
                }
            }
            // Get a set of currently paired devices
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

            // If the device is still in the list of paired devices, connect to it
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice deviceBt : pairedDevices) {
                    if (deviceItem.getAddress().equals(deviceBt.getAddress())) {
                        btService.connect(deviceBt);
                        mConnectedDeviceName = deviceBt.getName();
                    }
                }
            } else {
                Toast.makeText(getContext(),
                        getString(R.string.connect_failed),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

}
