package vivatexai.rasp_pi_android_bt.common;

import android.os.Handler;

import vivatexai.rasp_pi_android_bt.service.BluetoothService;

public class StatusInfoContainer {

    // Member fields
    private String mConnectionState;
    private String mConnectedDeviceName;
    private String mConnectedDeviceAddress;
    private String mConnectionDuration;
    private String mConnectedSignalStrength;
    private Handler timerHandler;
    private Runnable timerRunnable;

    /**
     * TODO: Possibly move to another constants file
     */
    // Not Connected String Constants
    public static final String NO_CONNECTION = "No device connected";
    public static final String CONNECTING = "Connecting...";
    public static final String CONNECTED = "Connected";
    public static final String NIL = "---";
    public static final String ZERO = "0:00";

    public StatusInfoContainer(){
        this.mConnectionState = NO_CONNECTION;
        this.mConnectedDeviceName = NIL;
        this.mConnectedDeviceAddress = NIL;
        this.mConnectionDuration = ZERO;
        this.mConnectedSignalStrength = NIL;
    }

    public StatusInfoContainer(String mConnectedDeviceName) {
        this.mConnectionState = CONNECTED;
        this.mConnectedDeviceName = mConnectedDeviceName;
        startTimer();

    }

    public StatusInfoContainer(String mConnectedDeviceName, String mConnectedDeviceAddress) {
        this(mConnectedDeviceName);
        this.mConnectedDeviceAddress = mConnectedDeviceAddress;
    }

    public String getmConnectionState() {
        return mConnectionState;
    }

    public void setmConnectionState(String mConnectionState) {
        this.mConnectionState = mConnectionState;
    }

    public String getmConnectedDeviceName() {
        return mConnectedDeviceName;
    }

    public void setmConnectedDeviceName(String mConnectedDeviceName) {
        this.mConnectedDeviceName = mConnectedDeviceName;
    }

    public String getmConnectedDeviceAddress() {
        return mConnectedDeviceAddress;
    }

    public void setmConnectedDeviceAddress(String mConnectedDeviceAddress) {
        this.mConnectedDeviceAddress = mConnectedDeviceAddress;
    }

    public String getmConnectionDuration() {
        return mConnectionDuration;
    }

    public void setmConnectionDuration(String mConnectionDuration) {
        this.mConnectionDuration = mConnectionDuration;
    }

    public String getmConnectedSignalStrength() {
        return mConnectedSignalStrength;
    }

    public void setmConnectedSignalStrength(String mConnectedSignalStrength) {
        this.mConnectedSignalStrength = mConnectedSignalStrength;
    }

    public void startTimer(){
        final long startTime = System.currentTimeMillis();

        this.timerHandler = new Handler();
        this.timerRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                mConnectionDuration = String.format("%d:%02d", minutes, seconds);
                timerHandler.postDelayed(this, 500);
            }
        };

        this.timerRunnable.run();
    }

    public void copy(StatusInfoContainer status_info) {
        this.setmConnectionState(status_info.getmConnectionState());
        this.setmConnectedDeviceName(status_info.getmConnectedDeviceName());
        this.setmConnectedDeviceAddress(status_info.getmConnectedDeviceAddress());
        this.setmConnectionDuration(status_info.getmConnectionDuration());
        this.setmConnectedSignalStrength(status_info.getmConnectedSignalStrength());
    }

    public void clear() {
        this.mConnectionState = NO_CONNECTION;
        this.mConnectedDeviceName = NIL;
        this.mConnectedDeviceAddress = NIL;
        this.mConnectionDuration = ZERO;
        this.mConnectedSignalStrength = NIL;
        this.timerHandler = null;
        this.timerRunnable = null;
    }
}
