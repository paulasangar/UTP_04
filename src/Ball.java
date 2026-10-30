
import java.awt.*;
import java.util.Random;

public abstract class Ball implements Runnable {

    protected int x;
    protected int y;
    protected int vx;
    protected int vy;
    protected int speed;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;

        Random random = new Random();

        vx = random.nextInt(7) - 3;
        vy = random.nextInt(7) - 3;

        speed = random.nextInt(11) + 20;
    }

    public void draw(Graphics g) {
        g.fillOval(x - 5, y - 5, 10, 10);
    }

    public abstract boolean collidingX();

    public abstract boolean collidingY();

    public abstract void wakeUp();
}
