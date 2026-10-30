
import java.awt.*;
import java.awt.event.*;

public class MyFrame extends Frame {

    private BallPanel ballPanel;
    private Panel buttonPanel;

    private Button threadBall;
    private Button scheduledBall;
    private Button vThreadBall;
    private Button vsThreadBall;
    private Button shakePanel;

    public MyFrame() {

        ballPanel = new BallPanel();

        threadBall = new Button("Thread Ball");
        scheduledBall = new Button("Scheduled Ball");
        vThreadBall = new Button("Virtual Thread Ball");
        vsThreadBall = new Button("Semaphore Ball");
        shakePanel = new Button("Shake Balls");

        buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(threadBall);
        buttonPanel.add(scheduledBall);
        buttonPanel.add(vThreadBall);
        buttonPanel.add(vsThreadBall);
        buttonPanel.add(shakePanel);

        ballPanel.setBackground(Color.YELLOW);
        buttonPanel.setBackground(Color.GREEN);

        setLayout(new BorderLayout());

        add(ballPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        threadBall.setActionCommand("ThreadBall");
        scheduledBall.setActionCommand("ScheduledBall");
        vThreadBall.setActionCommand("VThreadBall");
        vsThreadBall.setActionCommand("VsThreadBall");

        threadBall.addActionListener(ballPanel);
        scheduledBall.addActionListener(ballPanel);
        vThreadBall.addActionListener(ballPanel);
        vsThreadBall.addActionListener(ballPanel);

        shakePanel.addActionListener(
                e -> ballPanel.shake()
        );

        setSize(700, 500);
        setTitle("Java Concurrency Models");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new MyFrame();
    }
}
