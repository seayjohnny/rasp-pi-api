package vivatexai.rasp_pi_android_bt.service;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import vivatexai.rasp_pi_android_bt.R;

public class SettingsFragment extends Fragment {

    // Member fields
    private static BluetoothService btService = null;

    // Layout views
    private Button mButton;

    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Empty constructor.
     */
    public SettingsFragment() {}

    public static SettingsFragment newInstance(int sectionNumber, BluetoothService service) {
        SettingsFragment fragment = new SettingsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTestMessage();
            }
        });
    }

    /**
     * Sends a test message to remote device.
     */
    private void sendTestMessage() {
        // Check that we're actually connected before trying anything
        if (btService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        btService.write(getString(R.string.test_message).getBytes());
    }

}
