package vivatexai.rasp_pi_android_bt.service;

public interface Constants {
    // Message types sent from the BluetootService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_STATUS_CHANGE = 6;

    // Key names received from the BluetoothtService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static final String STATUS_STATE = "status_state";
    public static final String STATUS_NAME = "status_name";
    public static final String STATUS_ADDRESS = "status_address";
    public static final String STATUS_DURATION = "status_duration";
    public static final String STATUS_SIGNAL = "status_signal";
}
