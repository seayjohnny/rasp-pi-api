package vivatexai.rasp_pi_android_bt.service;


import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import vivatexai.rasp_pi_android_bt.R;
import vivatexai.rasp_pi_android_bt.common.StatusInfoContainer;

public class StatusFragment extends Fragment {

    // Member fields
    private static StatusInfoContainer btStatus = null;
    private static RefreshStatusInfoThread mRefreshThread;
    private static Handler timerHandler;
    private static  Runnable timerRunnable;

    // Layout views
    TextView tState;
    TextView tName;
    TextView tAddress;
    TextView tDuration;
    TextView tSignal;

    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Empty constructor.
     */
    public StatusFragment() {}

    public static StatusFragment newInstance(int sectionNumber, StatusInfoContainer status_info) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        btStatus = status_info;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get view
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tState = (TextView) view.findViewById(R.id.status_state);
        tName = (TextView) view.findViewById(R.id.status_name);
        tAddress = (TextView) view.findViewById(R.id.status_address);
        tDuration = (TextView) view.findViewById(R.id.status_duration);
        tSignal = (TextView) view.findViewById(R.id.status_signal);

        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                tState.setText(btStatus.getmConnectionState());
                tName.setText(btStatus.getmConnectedDeviceName());
                tAddress.setText(btStatus.getmConnectedDeviceAddress());
                tDuration.setText(btStatus.getmConnectionDuration());
                tSignal.setText(btStatus.getmConnectedSignalStrength());

                timerHandler.postDelayed(this, 500);
            }
        };
        timerRunnable.run();

        //mRefreshThread = new RefreshStatusInfoThread();
        //mRefreshThread.start();
    }

    public synchronized void updateStatusInfo(StatusInfoContainer status_info) {
        btStatus.copy(status_info);
    }

    public class RefreshStatusInfoThread extends Thread {

        public RefreshStatusInfoThread() {
        }

        public void run() {
            setName("RefreshStatusInfoThread");

            while (true) {
                tState.setText(btStatus.getmConnectionState());
                tName.setText(btStatus.getmConnectedDeviceName());
                tAddress.setText(btStatus.getmConnectedDeviceAddress());
                tDuration.setText(btStatus.getmConnectionDuration());
                tSignal.setText(btStatus.getmConnectedSignalStrength());
            }
        }
    }

}
