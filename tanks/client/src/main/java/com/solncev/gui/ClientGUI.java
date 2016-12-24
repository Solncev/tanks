

import client.helpers.Protocol;
import client.models.Client;
import client.models.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientGUI extends JFrame implements ActionListener, WindowListener {
    public static JPanel gameStatusPanel;
    private static JLabel scoreLabel;
    private static int score;
    int width = 790;
    int height = 580;
    boolean isRunning = true;
    private JLabel ipaddressLabel;
    private JLabel portLabel;
    private JTextField ipaddressText;
    private JTextField portText;
    private JButton registerButton;
    private JPanel registerPanel;
    private GameBoardPanel boardPanel;

    public ClientGUI() {
        score = 0;
        setTitle("Tanks");
        setSize(width, height);
        setLocation(60, 100);
        getContentPane().setBackground(Color.BLACK);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        addWindowListener(this);
        registerPanel = new JPanel();
        registerPanel.setBackground(Color.YELLOW);
        registerPanel.setSize(200, 140);
        registerPanel.setBounds(560, 50, 200, 140);
        registerPanel.setLayout(null);

        gameStatusPanel = new JPanel();
        gameStatusPanel.setBackground(Color.YELLOW);
        gameStatusPanel.setSize(200, 300);
        gameStatusPanel.setBounds(560, 210, 200, 311);
        gameStatusPanel.setLayout(null);

        ipaddressLabel = new JLabel("IP address: ");
        ipaddressLabel.setBounds(10, 25, 70, 25);

        portLabel = new JLabel("Port: ");
        portLabel.setBounds(10, 55, 50, 25);

        scoreLabel = new JLabel("Score : 0");
        scoreLabel.setBounds(10, 90, 100, 25);

        ipaddressText = new JTextField("localhost");
        ipaddressText.setBounds(90, 25, 100, 25);

        portText = new JTextField("3456");
        portText.setBounds(90, 55, 100, 25);

        registerButton = new JButton("Присоединиться");
        registerButton.setBounds(40, 100, 150, 25);
        registerButton.addActionListener(this);
        registerButton.setFocusable(true);


        registerPanel.add(ipaddressLabel);
        registerPanel.add(portLabel);
        registerPanel.add(ipaddressText);
        registerPanel.add(portText);
        registerPanel.add(registerButton);

        gameStatusPanel.add(scoreLabel);


        getContentPane().add(registerPanel);
        getContentPane().add(gameStatusPanel);
        getContentPane().add(boardPanel);
        setVisible(true);
    }


    public static void setScore(int scoreParametar) {
        score += scoreParametar;
        scoreLabel.setText("Score : " + score);
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == registerButton) {
            registerButton.setEnabled(false);
            //
        
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

}
