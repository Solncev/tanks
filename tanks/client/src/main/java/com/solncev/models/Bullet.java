package com.solncev.models;


import com.solncev.gui.ClientGUI;
import com.solncev.gui.GameBoardPanel;
import com.solncev.helpers.Protocol;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bullet {
    private static final int SPEED = 10;
    public boolean stop = false;
    private Image bombImg;
    private BufferedImage bombBuffImage;
    private int posX;
    private int posY;
    private int direction;

    public Bullet(int x, int y, int direction) {
        posX = x;
        posY = y;
        this.direction = direction;
        stop = false;
        bombImg = new ImageIcon("C:\\qwer\\src\\client\\images/bomb.png").getImage();
        bombBuffImage = new BufferedImage(bombImg.getWidth(null), bombImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
        bombBuffImage.createGraphics().drawImage(bombImg, 0, 0, null);
    }

    public BufferedImage getBombBuffImage() {
        return bombBuffImage;
    }

    public int getPosiX() {
        return posX;
    }

    public int getPosiY() {
        return posY;
    }


    public boolean checkCollision() {
        ArrayList<Tank> clientTanks = GameBoardPanel.getClients();
        int x, y;
        for (int i = 1; i < clientTanks.size(); i++) {
            if (clientTanks.get(i) != null) {
                x = clientTanks.get(i).getXposition();
                y = clientTanks.get(i).getYposition();

                if ((posY >= y && posY <= y + 43) && (posX >= x && posX <= x + 43)) {
                    ClientGUI.setScore(50);
                    ClientGUI.gameStatusPanel.repaint();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    if (clientTanks.get(i) != null)
                        Client.getGameClient().sendToServer(new Protocol().RemoveClientPacket(clientTanks.get(i).getTankID()));
                    return true;
                }
            }
        }
        return false;
    }

    public void startBulletThread(boolean chekCollision) {
        new BulletShotThread(chekCollision).start();
    }

    private class BulletShotThread extends Thread {
        boolean checkCollision;

        public BulletShotThread(boolean chCollision) {
            checkCollision = chCollision;
        }

        public void run() {
            if (checkCollision) {
                switch (direction) {
                    case 1:
                        posX += 20;
                        while (posY > 50) {
                            posY -= SPEED;
                            if (checkCollision()) {
                                break;
                            }
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;

                    case 2:
                        posY += 20;
                        posX += 30;
                        while (posX < 564) {
                            posX += SPEED;
                            if (checkCollision()) {
                                break;
                            }
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                    case 3:
                        posY += 30;
                        posX += 20;
                        while (posY < 505) {
                            posY += SPEED;
                            if (checkCollision()) {
                                break;
                            }
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }

                        }
                        break;
                    case 4:
                        posY += 20;
                        while (posX > 70) {
                            posX -= SPEED;
                            if (checkCollision()) {
                                break;
                            }
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }

                        }
                        break;
                }
                stop = true;
            } else {
                switch (direction) {
                    case 1:
                        posX += 20;
                        while (posY > 50) {
                            posY -= SPEED;
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }

                        }
                        break;
                    case 2:
                        posY += 20;
                        posX += 30;
                        while (posX < 564) {
                            posX += SPEED;
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                    case 3:
                        posY += 30;
                        posX += 20;
                        while (posY < 505) {
                            posY += SPEED;

                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }

                        }
                        break;
                    case 4:
                        posY += 20;
                        while (posX > 70) {
                            posX -= SPEED;
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                }
                stop = true;
            }
        }
    }
}

