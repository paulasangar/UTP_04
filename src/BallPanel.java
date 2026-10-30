
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BallPanel extends Panel implements Runnable, ActionListener {

    private final List<Ball> ballList;
    private final Thread thread;

    public BallPanel() {
        ballList = new ArrayList<>();

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        synchronized (ballList) {
            for (Ball ball : ballList) {
                ball.draw(g);
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            repaint();

            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        Ball ball = null;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        switch (command) {

            case "ThreadBall":
                ball = new ThreadBall(centerX, centerY) {
                    @Override
                    public boolean collidingX() {
                        return x + vx < 5 ||
                                x + vx > getWidth() - 5;
                    }

                    @Override
                    public boolean collidingY() {
                        return y + vy < 5 ||
                                y + vy > getHeight() - 5;
                    }
                };
                break;

            case "ScheduledBall":
                ball = new ScheduledBall(centerX, centerY) {
                    @Override
                    public boolean collidingX() {
                        return x + vx < 5 ||
                                x + vx > getWidth() - 5;
                    }

                    @Override
                    public boolean collidingY() {
                        return y + vy < 5 ||
                                y + vy > getHeight() - 5;
                    }
                };
                break;

            case "VThreadBall":
                ball = new VThreadBall(centerX, centerY) {
                    @Override
                    public boolean collidingX() {
                        return x + vx < 5 ||
                                x + vx > getWidth() - 5;
                    }

                    @Override
                    public boolean collidingY() {
                        return y + vy < 5 ||
                                y + vy > getHeight() - 5;
                    }
                };
                break;

            case "VsThreadBall":
                ball = new VsThreadBall(centerX, centerY) {
                    @Override
                    public boolean collidingX() {
                        return x + vx < 5 ||
                                x + vx > getWidth() - 5;
                    }

                    @Override
                    public boolean collidingY() {
                        return y + vy < 5 ||
                                y + vy > getHeight() - 5;
                    }
                };
                break;
        }

        if (ball != null) {
            synchronized (ballList) {
                ballList.add(ball);
            }
        }
    }

    public void shake() {

        synchronized (ballList) {
            for (Ball ball : ballList) {
                ball.wakeUp();
            }
        }
    }
}
