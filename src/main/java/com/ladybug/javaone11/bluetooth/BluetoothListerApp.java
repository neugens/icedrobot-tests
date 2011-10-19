package com.ladybug.javaone11.bluetooth;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.icedrobot.app.SimpleAppManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothListerApp extends JPanel {

    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    
    private FrameLister frame = new FrameLister();
    
    private JButton startDiscovery;
    void initComponent() {
        
        startDiscovery = new JButton("Start Discovery");
        startDiscovery.addActionListener(new BluetoothDiscovery());
        add(startDiscovery); 
    }
    
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                SimpleAppManager.init();
                
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setMinimumSize(new Dimension(100, 100));
                frame.addWindowListener(new AppManagerCloser());
                
                BluetoothListerApp panel = new BluetoothListerApp();
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
                
                frame.clear();
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
            }
        }
    }
    
    private class BluetoothDeviceLoader extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = (BluetoothDevice)
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            System.out.println("device found: " + device);
            frame.addToList(device);
        }
    }
    
    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    
                    if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                        startDiscovery.setText("Discovering...");
                        startDiscovery.setEnabled(false);
                    } else {
                        startDiscovery.setText("Start Discovery");
                        startDiscovery.setEnabled(true);
                        frame.initComponents();
                        frame.setVisible(true);
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
