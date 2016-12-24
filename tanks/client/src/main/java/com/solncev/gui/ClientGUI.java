package com.solncev.gui;

import com.solncev.helpers.Protocol;
import com.solncev.models.Client;
import com.solncev.models.Tank;

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
    private Client client;
    private Tank clientTank;
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

        client = Client.getGameClient();

        clientTank = new Tank();
        boardPanel = new GameBoardPanel(clientTank, false);

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

            try {
                client.register(ipaddressText.getText(), Integer.parseInt(portText.getText()), clientTank.getXposition(), clientTank.getYposition());
                boardPanel.setGameStatus(true);
                boardPanel.repaint();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                new ClientReceivingThread(client.getSocket()).start();
                registerButton.setFocusable(false);
                boardPanel.setFocusable(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "The Server is not running, try again later!", "Tanks", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("The Server is not running!");
                registerButton.setEnabled(true);
            }
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        Client.getGameClient().sendToServer(new Protocol().ExitMessagePacket(clientTank.getTankID()));
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

    public class ClientReceivingThread extends Thread {
        Socket clientSocket;
        DataInputStream reader;

        public ClientReceivingThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                reader = new DataInputStream(clientSocket.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            while (isRunning) {
                String sentence = "";
                try {
                    sentence = reader.readUTF();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (sentence.startsWith("ID")) {
                    int id = Integer.parseInt(sentence.substring(2));
                    clientTank.setTankID(id);
                } else if (sentence.startsWith("NewClient")) {
                    int pos1 = sentence.indexOf(',');
                    int pos2 = sentence.indexOf('-');
                    int pos3 = sentence.indexOf('|');
                    int x = Integer.parseInt(sentence.substring(9, pos1));
                    int y = Integer.parseInt(sentence.substring(pos1 + 1, pos2));
                    int dir = Integer.parseInt(sentence.substring(pos2 + 1, pos3));
                    int id = Integer.parseInt(sentence.substring(pos3 + 1, sentence.length()));
                    if (id != clientTank.getTankID())
                        boardPanel.registerNewTank(new Tank(x, y, dir, id));
                } else if (sentence.startsWith("Update")) {
                    int pos1 = sentence.indexOf(',');
                    int pos2 = sentence.indexOf('-');
                    int pos3 = sentence.indexOf('|');
                    int x = Integer.parseInt(sentence.substring(6, pos1));
                    int y = Integer.parseInt(sentence.substring(pos1 + 1, pos2));
                    int dir = Integer.parseInt(sentence.substring(pos2 + 1, pos3));
                    int id = Integer.parseInt(sentence.substring(pos3 + 1, sentence.length()));

                    if (id != clientTank.getTankID()) {
                        boardPanel.getTank(id).setXpoistion(x);
                        boardPanel.getTank(id).setYposition(y);
                        boardPanel.getTank(id).setDirection(dir);
                        boardPanel.repaint();
                    }

                } else if (sentence.startsWith("Shot")) {
                    int id = Integer.parseInt(sentence.substring(4));

                    if (id != clientTank.getTankID()) {
                        boardPanel.getTank(id).shot();
                    }

                } else if (sentence.startsWith("Remove")) {
                    int id = Integer.parseInt(sentence.substring(6));

                    if (id == clientTank.getTankID()) {
                        int response = JOptionPane.showConfirmDialog(null, "Sorry, You are loss. Do you want to try again ?", "Tanks 2D Multiplayer Game", JOptionPane.OK_CANCEL_OPTION);
                        if (response == JOptionPane.OK_OPTION) {
                            setVisible(false);
                            dispose();
                            new ClientGUI();
                        } else {
                            System.exit(0);
                        }
                    } else {
                        boardPanel.removeTank(id);
                    }
                } else if (sentence.startsWith("Exit")) {
                    int id = Integer.parseInt(sentence.substring(4));
                    if (id != clientTank.getTankID()) {
                        boardPanel.removeTank(id);
                    }
                }
            }
            try {
                reader.close();
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
