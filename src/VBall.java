
import java.util.concurrent.*;

public abstract class VBall extends Ball {

    protected static ExecutorService vExecutor =
            Executors.newVirtualThreadPerTaskExecutor();

    public VBall(int x, int y) {
        super(x, y);
    }
}
