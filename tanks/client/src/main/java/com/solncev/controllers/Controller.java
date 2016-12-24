package com.solncev.controllers;


import com.solncev.helpers.Protocol;
import com.solncev.models.Client;
import com.solncev.models.Tank;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {
    private final int LEFT = 37;
    private final int RIGHT = 39;
    private final int UP = 38;
    private final int DOWN = 40;
    private Tank tank;
    private Client client;

    public Controller(Tank tank) {
        this.client = Client.getGameClient();
        this.tank = tank;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == LEFT) {
            if (tank.getDirection() == 1 | tank.getDirection() == 3) {
                tank.moveLeft();
                client.sendToServer(new Protocol().UpdatePacket(tank.getXposition(),
                        tank.getYposition(), tank.getTankID(), tank.getDirection()));
            } else if (tank.getDirection() == 4) {
                tank.moveLeft();
                client.sendToServer(new Protocol().UpdatePacket(tank.getXposition(),
                        tank.getYposition(), tank.getTankID(), tank.getDirection()));
            }
        } else if (e.getKeyCode() == RIGHT) {
            if (tank.getDirection() == 1 | tank.getDirection() == 3) {
                tank.moveRight();
                client.sendToServer(new Protocol().UpdatePacket(tank.getXposition(),
                        tank.getYposition(), tank.getTankID(), tank.getDirection()));

            } else if (tank.getDirection() == 2) {
                tank.moveRight();
                client.sendToServer(new Protocol().UpdatePacket(tank.getXposition(),
                        tank.getYposition(), tank.getTankID(), tank.getDirection()));
            }
        } else if (e.getKeyCode() == UP) {
            if (tank.getDirection() == 2 | tank.getDirection() == 4) {
                tank.moveForward();
                client.sendToServer(new Protocol().UpdatePacket(tank.getXposition(),
                        tank.getYposition(), tank.getTankID(), tank.getDirection()));

            } else if (tank.getDirection() == 1) {
                tank.moveForward();
                client.sendToServer(new Protocol().UpdatePacket(tank.getXposition(),
                        tank.getYposition(), tank.getTankID(), tank.getDirection()));

            }
        } else if (e.getKeyCode() == DOWN) {
            if (tank.getDirection() == 2 | tank.getDirection() == 4) {
                tank.moveBackward();

                client.sendToServer(new Protocol().UpdatePacket(tank.getXposition(),
                        tank.getYposition(), tank.getTankID(), tank.getDirection()));

            } else if (tank.getDirection() == 3) {
                tank.moveBackward();

                client.sendToServer(new Protocol().UpdatePacket(tank.getXposition(),

                        tank.getYposition(), tank.getTankID(), tank.getDirection()));

            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            client.sendToServer(new Protocol().ShotPacket(tank.getTankID()));
            tank.shot();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

}
