package android.app;

import org.icedrobot.app.SimpleAppManager;

import android.bluetooth.BluetoothAdapter;

public class BluetoothDiscoverable {
    
    static {
        SimpleAppManager.init();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        
        if (!adapter.isEnabled()) {
            System.err.println("The adapter is disabled...");
            return;
        }
        
        String address = adapter.getAddress();
        String name = adapter.getName();
        
        String state = null;
        int stateID = adapter.getState();
        switch (stateID) {
        case BluetoothAdapter.STATE_ON:
            state = "ON";
            break;
        
        case BluetoothAdapter.STATE_OFF:
            state = "OFF";
            break;
        
        case BluetoothAdapter.STATE_TURNING_ON:
            state = "TURNING ON";
            break;
        
        case BluetoothAdapter.STATE_TURNING_OFF:
            state = "TURNING OFF";
            break;
            
        default:
            state = "UNKNOWN";
            break;
        }
        System.out.println("adapter name: " + name + " on address: " + address +
                           " has state: " + state);
        
        if (!adapter.isDiscovering()) {
            System.out.println("Start discovering remote Bluetooth devices...");
            adapter.startDiscovery();
        }
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        SimpleAppManager.shutdown();
    }

}
