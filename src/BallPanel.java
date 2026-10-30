
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BallPanel extends Panel implements Runnable, ActionListener {

    private final List<Ball> ballList;
    private final Thread thread;

    // Double buffering
    private Image offScreenImage;
    private Graphics offScreenGraphics;

    public BallPanel() {

        ballList = new ArrayList<>();

        // Start the repainting thread
        thread = new Thread(this);
        thread.start();
    }

    // Prevent AWT from clearing the screen before painting
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    // Double buffering implementation
    @Override
    public void paint(Graphics g) {

        // Create the off-screen image if necessary
        if (offScreenImage == null ||
                offScreenImage.getWidth(this) != getWidth() ||
                offScreenImage.getHeight(this) != getHeight()) {

            if (offScreenGraphics != null) {
                offScreenGraphics.dispose();
            }

            offScreenImage = createImage(getWidth(), getHeight());

            if (offScreenImage == null) {
                return;
            }

            offScreenGraphics = offScreenImage.getGraphics();
        }

        // Clear the off-screen image
        offScreenGraphics.setColor(getBackground());

        offScreenGraphics.fillRect(
                0, 0, getWidth(), getHeight()
        );

        // Draw all balls
        synchronized (ballList) {

            for (Ball ball : ballList) {
                ball.draw(offScreenGraphics);
            }
        }

        // Display the completed image
        g.drawImage(offScreenImage, 0, 0, this);
    }

    // Repainting thread
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

    // Create new balls
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

        // Add the new ball to the list
        if (ball != null) {

            synchronized (ballList) {
                ballList.add(ball);
            }

            repaint();
        }
    }

    // Wake up all balls
    public void shake() {

        synchronized (ballList) {

            for (Ball ball : ballList) {
                ball.wakeUp();
            }
        }
    }
}
