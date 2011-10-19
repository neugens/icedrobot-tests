package com.ladybug.javaone11.bluetooth;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SendMessage extends JFrame {

    private FrameLister lister;
    SendMessage(FrameLister lister) {
        this.lister = lister;
    }
    
    public void initComponents() {
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        
        final JTextField textField = new JTextField();
        textField.setMinimumSize(new Dimension(200, 50));
        
        JButton send = new JButton("Send!");
        send.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                lister.sendMessage(textField.getText());
            }
        });
        pane.add(textField);
        pane.add(send);
        
        setMinimumSize(new Dimension(250, 50));
        add(pane);
    }
    
//    public static void main(String[] args) throws Exception {
//        SwingUtilities.invokeAndWait(new Runnable() {
//            
//            @Override
//            public void run() {
//                SendMessage message = new SendMessage();
//                message.initComponents();
//                message.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                message.setVisible(true);                
//            }
//        });
//    }
}
