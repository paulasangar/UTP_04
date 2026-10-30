
import java.awt.*;
import java.util.concurrent.locks.*;

public abstract class VThreadBall extends VBall {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private volatile boolean paused = false;

    public VThreadBall(int x, int y) {
        super(x, y);
        vExecutor.submit(this);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        super.draw(g);
    }

    public void checkPaused() throws InterruptedException {
        lock.lock();

        try {
            while (paused) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void pause() {
        lock.lock();

        try {
            paused = true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void wakeUp() {
        lock.lock();

        try {
            paused = false;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                checkPaused();

                if (collidingX()) {
                    vx *= -1;
                    x += vx;
                    pause();
                }

                if (collidingY()) {
                    vy *= -1;
                    y += vy;
                    pause();
                }

                x += vx;
                y += vy;

                Thread.sleep(speed);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
