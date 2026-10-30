
import java.awt.*;

public abstract class ThreadBall extends Ball {

    public static final Object MONITOR = new Object();

    protected Thread thread;

    public ThreadBall(int x, int y) {
        super(x, y);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        super.draw(g);
    }

    @Override
    public void wakeUp() {
        synchronized (MONITOR) {
            MONITOR.notifyAll();
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            x += vx;
            y += vy;

            try {
                Thread.sleep(speed);

                if (collidingX()) {
                    vx *= -1;
                    x += vx;

                    synchronized (MONITOR) {
                        MONITOR.wait();
                    }
                }

                if (collidingY()) {
                    vy *= -1;
                    y += vy;

                    synchronized (MONITOR) {
                        MONITOR.wait();
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
