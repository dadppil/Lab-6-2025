package functions.threads;

import functions.Function;
import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;
    private Random random = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasks(); i++) {
                double base = 1 + random.nextDouble() * 9;
                double leftBorder = random.nextDouble() * 100;
                double rightBorder = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                synchronized (task) {
                    task.setF(new Log(base));
                    task.setLeft(leftBorder);
                    task.setRight(rightBorder);
                    task.setStep(step);
                }

                System.out.printf("Source %.4f %.4f %.4f%n", leftBorder, rightBorder, step);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Generator was interrupted");
        }
    }
}
