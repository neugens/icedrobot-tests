package com.ladybug.javaone11.bluetooth;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class FrameLister extends JFrame {
    
    private UUID uuid = UUID.fromString("27012f0c-68af-4fbf-8dbe-6bbaf7aa432a");
     
    private OutputStream socketStream;
    private List<String> entryList = new ArrayList<String>();
    private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
    
    private JButton connecTo = new JButton("Connect...");
    private JList list;
    
    void addToList(BluetoothDevice device) {
        entryList.add(device.getName());
        devices.add(device);
    }
    
    void initComponents() {
        
        connecTo.addActionListener(new DeviceConnector());
        setMinimumSize(new Dimension(200, 200));
        
        list = new JList(entryList.toArray());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(200, 100));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel("Devices:");
        
        label.setLabelFor(list);
        
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        listPane.add(connecTo);
        
        Container contentPane = getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
    }

    public void clear() {
        entryList.clear();
        devices.clear();
    }
    
    private class DeviceConnector implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (connecTo.getText().equalsIgnoreCase("Release...")) {
                connecTo.setText("Connect...");
                // TODO: release connection
                
            } else {                
                connecTo.setText("Connecting...");
                connecTo.setEnabled(false);
                
                int deviceID = list.getSelectedIndex();
                final BluetoothDevice device = devices.get(deviceID);
                System.err.println("connecting to device: " + device);
                
                SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
                    protected Boolean doInBackground() throws Exception {
                        BluetoothSocket clientSocket;
                        try {
                            clientSocket =
                                    device.createInsecureRfcommSocketToServiceRecord(uuid);
                            clientSocket.connect();
                            
                        } catch (Throwable e) {
                            e.printStackTrace();
                            return false;
                        }
                        
                        socketStream = clientSocket.getOutputStream();
                        
                        return true;
                    };
                    
                    @Override
                    protected void done() {
                        try {
                            boolean connected = get();
                            
                            connecTo.setEnabled(true);
                            if (connected) {
                                connecTo.setText("Release...");
                                SendMessage message =
                                        new SendMessage(FrameLister.this);
                                message.initComponents();
                                message.setVisible(true);
                            } else {
                                connecTo.setText("Connect...");
                            }
                            
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    
                };
                worker.execute();
            }
            
        }
    }

    public void sendMessage(String text) {
        System.err.println(text);
        if (socketStream != null) {
            try {
                socketStream.write(text.getBytes(Charset.forName("ISO-8859-1")));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
