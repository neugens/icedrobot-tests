package android.app;

import org.icedrobot.app.SimpleAppManager;

import android.bluetooth.BluetoothAdapter;

public class BluetoothTest {

    static {
        SimpleAppManager.init();
    }
    
    public static void main(String [] args) {
        
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        String toast;
        if (adapter.isEnabled()) {
            String address = adapter.getAddress();
            String name = adapter.getName();
            toast = "IcedRobot Bluetooth test#1\n" +
                    "adapter name: " + name + ", address: " + address;
        } else {
            toast = "fluff not enabled";
        }
        System.err.println(toast);
        
        SimpleAppManager.shutdown();
    }
}
