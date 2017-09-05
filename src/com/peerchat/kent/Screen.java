package com.peerchat.kent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
/**
 *
 * @author Faris McKay
 */
public class Screen extends JFrame {
 
    private JTextArea messagesArea;
    private JButton startServer;
    private JButton stopServer;
    private Server mServer;
 
    public Screen() {
        super("ICMetric Login Server");
        JPanel panelFields = new JPanel();
        panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.X_AXIS));
        JPanel panelFields2 = new JPanel();
        panelFields2.setLayout(new BoxLayout(panelFields2, BoxLayout.X_AXIS));
        messagesArea = new JTextArea();
        messagesArea.setColumns(30);
        messagesArea.setRows(10);
        messagesArea.setEditable(false);
        startServer = new JButton("Start");
        startServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mServer = new Server(new Server.ServerGUI() {
                    @Override
                    public void info(String message) {
                        messagesArea.append("\n " + message);
                    }
                });
                mServer.start();
                startServer.setEnabled(false);
                stopServer.setEnabled(true);
            }
        });
        stopServer = new JButton("Stop");
        stopServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mServer != null) {
                    mServer.close(true);
                }
                startServer.setEnabled(true);
                stopServer.setEnabled(false);
            }
        });
        panelFields.add(messagesArea);
        panelFields.add(startServer);
        panelFields.add(stopServer);
        getContentPane().add(panelFields);
        getContentPane().add(panelFields2);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(300, 170);
        setVisible(true);
    }
}