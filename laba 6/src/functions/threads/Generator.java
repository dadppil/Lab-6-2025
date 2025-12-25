package functions.threads;
import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private Random random = new Random();

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasks(); i++) {
                if (Thread.interrupted()) {
                    System.out.println("Generator interrupted");
                    return;
                }

                double base = 1 + random.nextDouble() * 9;
                double leftBorder = random.nextDouble() * 100;
                double rightBorder = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                semaphore.acquire();
                try {
                    task.setF(new Log(base));
                    task.setLeft(leftBorder);
                    task.setRight(rightBorder);
                    task.setStep(step);
                } finally {
                    semaphore.release();
                }

                System.out.printf("Source %.4f %.4f %.4f%n", leftBorder, rightBorder, step);

                Thread.sleep(15);
            }
        } catch (InterruptedException e) {
            System.out.println("Generator was interrupted");
        }
    }
}