import javax.swing.JPanel;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class gamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 50;

    private Timer timer;
    private int delay = 10;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;

    private int ballDirX = -1;
    private int ballDirY = -2;
    private MapGenerator map;

    gamePlay() {
        map = new MapGenerator(5, 10);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // backGround
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        // Drawing Map
        map.draw((Graphics2D) g);

        // Score
        g.setColor(Color.MAGENTA);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString("" + score, 592, 30);

        // Border
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Padal
        g.setColor(Color.cyan);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.ORANGE);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // Game Over
        if (ballPosY > 570) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 50));
            g.drawString("GAME OVER , Score: " + score, 60, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter to Restart", 190, 350);
        }

        // Game Wining Condition

        if (totalBricks <= 0) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 50));
            g.drawString("You Won , Score: " + score, 170, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter to Restart", 190, 350);
        }

        g.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballDirY = -ballDirY;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rectBrickBox = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle rectBall = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle rectBrick = rectBrickBox;

                        if (rectBall.intersects(rectBrick)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= rectBrick.x || ballPosX + 1 >= rectBrick.x + rectBrick.width) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }
                            break A;
                        }

                    }
                }
            }

            ballPosX += ballDirX;
            ballPosY += ballDirY;

            if (ballPosX < 0) {
                ballDirX = -ballDirX;
            }
            if (ballPosY < 0) {
                ballDirY = -ballDirY;
            }
            if (ballPosX > 670) {
                ballDirX = -ballDirX;
            }
        }

        repaint();
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (!play) {
            play = true;
            ballPosX = 120;
            ballPosY = 350;
            ballDirX = -1;
            ballDirY = -2;
            playerX = 310;

            score = 0;
            totalBricks = 50;
            map = new MapGenerator(5, 10);

            repaint();
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

}
