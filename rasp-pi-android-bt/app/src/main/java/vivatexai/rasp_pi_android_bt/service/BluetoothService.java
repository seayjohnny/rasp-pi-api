package vivatexai.rasp_pi_android_bt.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import vivatexai.rasp_pi_android_bt.common.StatusInfoContainer;

public class BluetoothService {

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothServiceSecure";
    private static final String NAME_INSECURE = "BluetoothServiceInsecure";

    // Unique UUID, used for finding and establishing a connection to a BT Socket on the Pi.
    // The BT socket on the Pi must have the same UUID.
    private static final UUID PI_UUID =
            UUID.fromString("6e2121f5-103c-464e-aa0d-2bcc2f8a6cf9");

    // Member fields
    private final BluetoothAdapter btAdapter;
    private final Handler mHandler;
    private AcceptThread tAccept;
    private ConnectThread tConnect;
    private ConnectedThread tConnected;
    private int btState;
    private static StatusInfoContainer btStatus;

    // Constants indicating current connection state
    public static final int STATE_NONE = 0;         // doing nothing
    public static final int STATE_LISTEN = 1;       // listening for incoming connections
    public static final int STATE_CONNECTING = 2;   // initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;    // connected to a remote device

    /**
     * Constructer. Prepares a new BluetoothService session.
     * @param handler
     */
    public BluetoothService(Handler handler) {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btState = STATE_NONE;
        mHandler = handler;
        btStatus = new StatusInfoContainer();
    }



    /**
     * Set the current state of the Bluetooth connection.
     * @param state
     */
    private synchronized void setState(int state) {
        btState = state;
        switch (state) {
            case STATE_NONE:
            case STATE_LISTEN:
                btStatus.setmConnectionState(btStatus.NO_CONNECTION);
                break;
            case STATE_CONNECTING:
                btStatus.setmConnectionState(btStatus.CONNECTING);
                break;
            case STATE_CONNECTED:
                btStatus.setmConnectionState(btStatus.CONNECTED);
                break;

        }

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the currnt connection state.
     * @return
     */
    public synchronized int getState() { return btState;}

    /**
     * Start the Bluetooth service.
     * Start AcceptThread to begin session in listening (server) mode.
     */
    public synchronized void start() {
        // Cancel any thread attempting to make a connection
        if (tConnect != null) {
            tConnect.cancel();
            tConnect = null;
        }

        // Cancel any thread currently managing a connection
        if (tConnected != null) {
            tConnected.cancel();
            tConnected = null;
        }

        setState(STATE_LISTEN);

        // Start the thread to listen on a BT Server Socket
        if (tAccept == null) {
            tAccept = new AcceptThread();
            tAccept.start();
        }
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device
     */
    public synchronized void connect(BluetoothDevice device) {
        // Cancel any thread attempting to make a connection
        if (btState == STATE_CONNECTING) {
            if (tConnect != null) {
                tConnect.cancel();
                tConnect = null;
            }
        }

        // Cancel any thread currently running a connection
        if (tConnected != null) {
            tConnected.cancel();
            tConnected = null;
        }

        // Start the thread to connect with the given device
        tConnect = new ConnectThread(device);
        tConnect.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection.
     * @param socket
     * @param device
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {

        // Cancel the thread that completed the connection
        if (tConnect != null) {
            tConnect.cancel();
            tConnect = null;
        }

        // Cancel any thread currently running a connection
        if (tConnected != null) {
            tConnected.cancel();
            tConnected = null;
        }

        // Start the thread to manage the connection and perform transmissions
        tConnected = new ConnectedThread(socket);
        tConnected.start();

        // Update state
        setState(STATE_CONNECTED);

        // Send the info of the connected device back to the UI Activity
        Message msgInfo = mHandler.obtainMessage(Constants.MESSAGE_STATUS_CHANGE);
        Bundle bundleInfo = new Bundle();
        bundleInfo.putString(Constants.STATUS_STATE, btStatus.getmConnectionState());
        bundleInfo.putString(Constants.STATUS_NAME, device.getName());
        bundleInfo.putString(Constants.STATUS_ADDRESS, device.getAddress());
        bundleInfo.putString(Constants.STATUS_SIGNAL, btStatus.NIL);
        msgInfo.setData(bundleInfo);
        mHandler.sendMessage(msgInfo);

        // Send the connected device name back to the UI Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        //Log.d(TAG, "stop");

        if (tConnected != null) {
            tConnected.cancel();
            tConnected = null;
        }

        if (tConnected != null) {
            tConnected.cancel();
            tConnected = null;
        }

        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (btState != STATE_CONNECTED) return;
            r = tConnected;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity
     */
    private void connectionFailed() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        // Start the service over to restart listening mode
        BluetoothService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the Activity
     */
    private void connectionLost() {
        // Clear the status info
        btStatus.clear();

        // Send the info of the connected device back to the UI Activity
        Message msgInfo = mHandler.obtainMessage(Constants.MESSAGE_STATUS_CHANGE);
        Bundle bundleInfo = new Bundle();
        bundleInfo.putString(Constants.STATUS_STATE, btStatus.getmConnectionState());
        bundleInfo.putString(Constants.STATUS_NAME, btStatus.getmConnectedDeviceName());
        bundleInfo.putString(Constants.STATUS_ADDRESS, btStatus.getmConnectedDeviceAddress());
        bundleInfo.putString(Constants.STATUS_SIGNAL, btStatus.NIL);
        msgInfo.setData(bundleInfo);
        mHandler.sendMessage(msgInfo);

        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        // Start the service over to restart listening mode
        BluetoothService.this.start();
    }

    //==============================================================================================
    //==============================================================================================
    // Threads
    //==============================================================================================

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = btAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
                        PI_UUID);

            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            setName("AcceptThread");

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (btState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothService.this) {
                        switch (btState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(PI_UUID);
            } catch (IOException e) {
                //Log.e(TAG, "Socket create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            //Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Cancel discovery since we're already connected to the remote device
            btAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                mmSocket.connect();
            } catch (IOException e){
                // Close the socket upon connection failure
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    //Log.e(TAG, "unable to close() socket during connection failure", e2);
                }

                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                tConnect = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                //Log.e(TAG, "close() of connected socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handle all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            //Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                //Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            //Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (btState == STATE_CONNECTED) {
                System.out.println("Listening...................................................");
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    //Log.e(TAG, "disconnected", e);
                    connectionLost();
                    // Start the service over to restart listening mode
                    BluetoothService.this.start();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                //Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                //Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
