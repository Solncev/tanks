package com.solncev.models;


import com.solncev.gui.GameBoardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tank {
    private static final int SPEED = 5;
    private Image[] tankImg;
    private BufferedImage imageBuff;
    private Bullet bullet[] = new Bullet[1000];
    private int curBomb = 0;
    private int tankID;
    private int posX = 0;
    private int posY = 0;
    private int direction = 1;
    private int width = 560;
    private int height = 470;

    public Tank() {
        while (posX < 70 | posY < 50 | posY > height - 43 | posX > width - 43) {
            posX = (int) (Math.random() * width);
            posY = (int) (Math.random() * height);
        }
        loadImage(4);
    }

    public Tank(int x, int y, int dir, int id) {
        posX = x;
        posY = y;
        tankID = id;
        direction = dir;
        loadImage(0);
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int dir) {
        imageBuff = new BufferedImage(tankImg[dir - 1].getWidth(null), tankImg[dir - 1].getHeight(null), BufferedImage.TYPE_INT_RGB);
        imageBuff.createGraphics().drawImage(tankImg[dir - 1], 0, 0, null);
        direction = dir;
    }

    public void loadImage(int a) {
        tankImg = new Image[4];
        for (int i = a; i < tankImg.length + a; i++) {
            tankImg[i - a] = new ImageIcon("C:\\qwer\\src\\client\\images/" + i + ".png").getImage();
        }
        imageBuff = new BufferedImage(tankImg[direction - 1].getWidth(null), tankImg[direction].getHeight(null), BufferedImage.TYPE_INT_RGB);
        imageBuff.createGraphics().drawImage(tankImg[direction - 1], 0, 0, null);
    }

    public BufferedImage getBuffImage() {
        return imageBuff;
    }

    public int getXposition() {
        return posX;
    }

    public int getYposition() {
        return posY;
    }

    public void setYposition(int y) {
        posY = y;
    }

    public void setXpoistion(int x) {
        posX = x;
    }

    public void moveLeft() {
        if (direction == 1 | direction == 3) {
            imageBuff = new BufferedImage(tankImg[3].getWidth(null), tankImg[3].getHeight(null), BufferedImage.TYPE_INT_RGB);
            imageBuff.createGraphics().drawImage(tankImg[3], 0, 0, null);
            direction = 4;
        } else {
            int temp;
            temp = posX - SPEED;
            if (!checkCollision(temp, posY) && temp < 70) {
                posX = 70;
            } else if (!checkCollision(temp, posY)) {
                posX = temp;
            }
        }
    }

    public void moveRight() {
        if (direction == 1 | direction == 3) {
            imageBuff = new BufferedImage(tankImg[1].getWidth(null), tankImg[1].getHeight(null), BufferedImage.TYPE_INT_RGB);
            imageBuff.createGraphics().drawImage(tankImg[1], 0, 0, null);
            direction = 2;
        } else {
            int temp;
            temp = posX + SPEED;
            if (!checkCollision(temp, posY) && temp > width - 43 + 20) {

                posX = width - 43 + 20;
            } else if (!checkCollision(temp, posY)) {
                posX = temp;
            }
        }
    }

    public void moveForward() {
        if (direction == 2 | direction == 4) {
            imageBuff = new BufferedImage(tankImg[0].getWidth(null), tankImg[0].getHeight(null), BufferedImage.TYPE_INT_RGB);
            imageBuff.createGraphics().drawImage(tankImg[0], 0, 0, null);
            direction = 1;
        } else {
            int temp;
            temp = posY - SPEED;
            if (!checkCollision(posX, temp) && temp < 50) {
                posY = 50;
            } else if (!checkCollision(posX, temp)) {
                posY = temp;
            }
        }
    }

    public void moveBackward() {
        if (direction == 2 | direction == 4) {
            imageBuff = new BufferedImage(tankImg[2].getWidth(null), tankImg[2].getHeight(null), BufferedImage.TYPE_INT_RGB);
            imageBuff.createGraphics().drawImage(tankImg[2], 0, 0, null);
            direction = 3;
        } else {
            int temp;
            temp = posY + SPEED;
            if (!checkCollision(posX, temp) && temp > height - 43 + 50) {
                posY = height - 43 + 50;
            } else if (!checkCollision(posX, temp)) {
                posY = temp;
            }
        }
    }

    public void shot() {
        bullet[curBomb] = new Bullet(this.getXposition(), this.getYposition(), direction);
        bullet[curBomb].startBulletThread(true);
        curBomb++;
    }

    public Bullet[] getBullet() {
        return bullet;
    }

    public int getTankID() {
        return tankID;
    }

    public void setTankID(int id) {
        tankID = id;
    }

    public boolean checkCollision(int posX, int posY) {
        ArrayList<Tank> clientTanks = GameBoardPanel.getClients();
        int x, y;
        for (int i = 1; i < clientTanks.size(); i++) {
            if (clientTanks.get(i) != null) {
                x = clientTanks.get(i).getXposition();
                y = clientTanks.get(i).getYposition();
                switch (direction) {
                    case 1:
                        if (((posY <= y + 43) && posY >= y) && ((posX <= x + 43 && posX >= x) || (posX + 43 >= x && posX + 43 <= x + 43))) {
                            return true;
                        }
                        break;
                    case 2:
                        if (((posX + 43 >= x) && posX + 43 <= x + 43) && ((posY <= y + 43 & posY >= y) || (posY + 43 >= y && posY + 43 <= y + 43))) {
                            return true;
                        }
                        break;
                    case 3:
                        if (((posY + 43 >= y) && posY + 43 <= y + 43) && ((posX <= x + 43 && posX >= x) || (posX + 43 >= x && posX + 43 <= x + 43))) {
                            return true;
                        }
                        break;
                    case 4:
                        if (((posX <= x + 43) && posX >= x) && ((posY <= y + 43 && posY >= y) || (posY + 43 >= y && posY + 43 <= y + 43))) {
                            return true;
                        }
                        break;
                }
            }
        }
        return false;
    }
}
