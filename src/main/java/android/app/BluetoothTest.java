package android.app;

import org.icedrobot.app.SimpleAppManager;

import android.bluetooth.BluetoothAdapter;

public class BluetoothTest {

    public static void main(String [] args) {
        SimpleAppManager.init();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        String toast;
        if (adapter.isEnabled()) {
            String address = adapter.getAddress();
            String name = adapter.getName();
            toast = name + " : " + address;
        } else {
            toast = "fluff not enabled";
        }
        System.err.println(toast);
    }
}
