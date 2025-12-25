package functions.threads;
import functions.Function;
import java.util.concurrent.Semaphore;
import static functions.Functions.integrate;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        int processed = 0;

        while (processed < task.getTasks() && !isInterrupted()) {
            Function function = null;
            double left = 0;
            double right = 0;
            double step = 0;

            try {
                semaphore.acquire();
                try {
                    function = task.getF();
                    left = task.getLeft();
                    right = task.getRight();
                    step = task.getStep();
                    task.setF(null);
                } finally {
                    semaphore.release();
                }
            } catch (InterruptedException e) {
                System.out.println("Integrator: прерван при ожидании семафора");
                Thread.currentThread().interrupt();
                return;
            }

            if (function != null) {
                processed++;
                try {
                    double result = integrate(function, left, right, step);
                    System.out.printf("Result %.4f %.4f %.4f %.8f%n", left, right, step, result);
                } catch (Exception e) {
                    System.out.println("Integrator error: " + e.getMessage());
                }
            }
            else {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    System.out.println("Integrator прерван во время сна");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
