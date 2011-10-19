package android.app.broadcast;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.icedrobot.app.SimpleAppManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BroadCastReceiver extends JPanel {
    
    private static final String ID = "android.app.broadcast.BroadCastReceiver";
    
    private JButton startDiscovery;
    private JLabel statusLabel;
    
    void initComponent() {
        
        setLayout(new GridLayout(2, 1));
        
        startDiscovery = new JButton("Send Message");
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
                
                BroadCastReceiver panel = new BroadCastReceiver();
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
            Context context = SimpleAppManager.getDefaultSystemContext();
            context.registerReceiver(new TestReceiver(), new IntentFilter(ID));
         
            context.sendBroadcast(new Intent(ID));
            statusLabel.setText("message sent!");
        }
    }
    
    private class TestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    statusLabel.setText("message received!");
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
