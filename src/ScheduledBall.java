
import java.awt.*;
import java.util.concurrent.*;

public abstract class ScheduledBall extends Ball {

    protected static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    protected ScheduledFuture<?> scheduledFuture;

    public ScheduledBall(int x, int y) {
        super(x, y);
        startScheduler();
    }

    private void startScheduler() {
        scheduledFuture = scheduler.scheduleAtFixedRate(
                this,
                0,
                speed,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        super.draw(g);
    }

    @Override
    public void run() {

        if (collidingX()) {
            vx *= -1;
            x += vx;
            scheduledFuture.cancel(false);
        }

        if (collidingY()) {
            vy *= -1;
            y += vy;
            scheduledFuture.cancel(false);
        }

        x += vx;
        y += vy;
    }

    @Override
    public void wakeUp() {
        if (scheduledFuture.isCancelled()) {
            startScheduler();
        }
    }
}
