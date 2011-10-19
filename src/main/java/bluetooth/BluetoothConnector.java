package bluetooth;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.icedrobot.app.SimpleAppManager;

import com.intel.bluetooth.BluetoothServerConnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothConnector extends JPanel {

    private UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
    
    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    
    private JButton startDiscovery;
    private JLabel statusLabel;
    
    void initComponent() {
        
        setLayout(new GridLayout(2, 1));
        
        startDiscovery = new JButton("Discovery Devices");
        startDiscovery.addActionListener(new BluetoothDiscovery());
        add(startDiscovery);
        
        statusLabel = new JLabel("");
        add(statusLabel);
    }
    
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                SimpleAppManager.init();
                
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setMinimumSize(new Dimension(600, 300));
                frame.addWindowListener(new AppManagerCloser());
                
                BluetoothConnector panel = new BluetoothConnector();
                panel.initComponent();
                frame.add(panel);
                frame.pack();
                
                frame.setVisible(true);
            }
        });
    }
    
    private class BluetoothDiscovery implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (adapter.isEnabled()) {
                
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    protected Void doInBackground() throws Exception {
                        
                        Context context = SimpleAppManager.getDefaultSystemContext();
                        context.registerReceiver(new BluetoothReceiver(),
                                new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
                        context.registerReceiver(new BluetoothReceiver(),
                                new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
                        
                        context.registerReceiver(new BluetoothDeviceLoader(),
                                new IntentFilter(BluetoothDevice.ACTION_FOUND));
                                                
                        if (!adapter.isDiscovering()) {
                            adapter.startDiscovery();
                        }
                        
                        return null;
                    };
                    
                };
                worker.execute();
                
            } else {
                statusLabel.setText("Device disabled, please try again later...");
            }
        }
    }
    
    private class BluetoothDeviceLoader extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            statusLabel.setText("device found: " + name);
            
            BluetoothDevice device =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceName = device.getName();
            System.out.println("device name: " + deviceName);
            
            try {
                BluetoothSocket clientSocket =
                        device.createInsecureRfcommSocketToServiceRecord(uuid);
                clientSocket.connect();
                
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    
    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    
                    if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                       statusLabel.setText("start discovering devices...."); 
                    
                    } else {
                        statusLabel.setText("discovery finished");                        
                    }
                    
                }
            });
        }
    }
    
    private static class AppManagerCloser extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            SimpleAppManager.shutdown();
        }
    }
}
